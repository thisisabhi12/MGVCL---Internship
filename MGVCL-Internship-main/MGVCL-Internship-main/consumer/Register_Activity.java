package com.example.consumer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_Activity extends AppCompatActivity {

    TextInputEditText cun_id, metr_no, name, ph, add, mail, pass;
    Button btn_sub;

    DatabaseReference database1;

    FirebaseAuth auth;

    boolean valid = false;

    AddUser user;

    ProgressDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        getSupportActionBar().setTitle("Register");

        initialise();

        btn_sub.setOnClickListener(view -> {
            dialog.setTitle("Process");
            dialog.setMessage("Please Wait......!");
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();
            if (valid()) {
                Adduser();
            }
            else{
                dialog.dismiss();
            }
        });

    }

    private void Adduser() {
        user.setCun_id(cun_id.getText().toString());
        user.setMetr_no(metr_no.getText().toString());
        user.setName(name.getText().toString());
        user.setPhone(ph.getText().toString());
        user.setAdd(add.getText().toString());
        user.setEmail(mail.getText().toString());
        user.setPass(pass.getText().toString());


        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPass())
                .addOnCompleteListener(task ->  {
                    if(!task.isSuccessful()){
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        database1.child(user.getCun_id()).setValue(user)
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this,Login_Activity.class));
                                        finish();
                                        dialog.dismiss();
                                    }
                                    else{
                                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }


    private boolean valid() {

        if (cun_id.getText().toString().isEmpty() && metr_no.getText().toString().isEmpty() && name.getText().toString().isEmpty() && ph.getText().toString().isEmpty() && add.getText().toString().isEmpty() && mail.getText().toString().isEmpty() && pass.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Please Fill All details");
            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            builder.setCancelable(false);
            builder.create().show();
            valid = false;
        }
        else{
            valid = true;
        }
        if (valid) {
            if (cun_id.getText().toString().isEmpty()) {
                cun_id.requestFocus();
                valid = false;
                Toast.makeText(this, "Please Enter Consumer ID", Toast.LENGTH_LONG).show();
            } else if (cun_id.getText().toString().length() != 11) {
                cun_id.requestFocus();
                valid = false;
                Toast.makeText(this, "Please Enter Consumer ID in 11 Digit", Toast.LENGTH_LONG).show();
            } else {
                valid = true;
            }
        }
        if (valid) {
            if (metr_no.getText().toString().isEmpty()) {
                metr_no.requestFocus();
                valid = false;
                Toast.makeText(this, "Please Enter Meter Number", Toast.LENGTH_LONG).show();
            }
            else{
                valid=true;
            }
        }
        if (valid) {
            if (name.getText().toString().isEmpty()) {
                name.requestFocus();
                valid = false;
                Toast.makeText(this, "Please Enter Meter Name", Toast.LENGTH_LONG).show();
            } else {
                Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                Matcher ms = ps.matcher(name.getText().toString());
                if (!ms.matches()) {
                    name.requestFocus();
                    valid = false;
                    Toast.makeText(this, "Please Enter Valid Name", Toast.LENGTH_SHORT).show();
                }
                else{
                    valid= true;
                }
            }
        }
        if (valid) {
            if (ph.getText().toString().isEmpty()) {
                ph.requestFocus();
                valid = false;
                Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
            }
            else {
                valid = true;
            }
        }
        if (valid) {
            if (add.getText().toString().isEmpty()) {
                add.requestFocus();
                valid = false;
                Toast.makeText(this, "PLease Enter Address", Toast.LENGTH_SHORT).show();
            } else {
                valid = true;
            }
        }
        if (valid) {
            if (mail.getText().toString().isEmpty()) {
                mail.requestFocus();
                valid = false;
                Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            } else {
                valid = true;
            }
        }
        if (valid) {
            if (pass.getText().toString().isEmpty()) {
                pass.requestFocus();
                valid = false;
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            } else {
                valid = true;
            }
        }
        return valid;
    }

    private void initialise() {

        cun_id = findViewById(R.id.RG_Consumer1);
        metr_no = findViewById(R.id.RG_Meter_Nu1);
        name = findViewById(R.id.RG_Name1);
        ph = findViewById(R.id.RG_Phone_NU1);
        add = findViewById(R.id.RG_Add1);
        mail = findViewById(R.id.RG_Email1);
        pass = findViewById(R.id.RG_Password1);

        btn_sub = findViewById(R.id.BTN_RG_SUB);

        user = new AddUser();

        database1 = FirebaseDatabase.getInstance().getReference("Customer");

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
    }
}
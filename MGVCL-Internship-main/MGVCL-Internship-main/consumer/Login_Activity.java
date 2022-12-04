package com.example.consumer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    boolean valid=false;

    //TextInputEdit
    TextInputEditText email,pass;

    //button
    Button btn_login;

    //textview
    TextView register;

    //firebasse auth
    FirebaseAuth auth;

    ProgressDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        initialise();

        btn_login.setOnClickListener(view -> {
            dialog.setTitle("Process");
            dialog.setMessage("Please Wait......!");
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();
            if(valid()){
                login();
            }else{
                dialog.dismiss();
            }
        });

        register.setOnClickListener(view -> {
            startActivity(new Intent(this,Register_Activity.class));
            finish();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!= null){
            startActivity(new Intent(this,Home_Activity.class));
            finish();
        }
    }

    private void login() {
        String mail,pas;
        mail = email.getText().toString();
        pas = pass.getText().toString();
        auth.signInWithEmailAndPassword(mail,pas).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
               startActivity(new Intent(this,Home_Activity.class));
               dialog.dismiss();
               finish();
            }else {
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private boolean valid() {

        if(email.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_LONG).show();
            email.requestFocus();
            valid=false;
        }
        else if(pass.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_LONG).show();
            pass.requestFocus();
            valid=false;
        }else if(pass.getText().toString().length()<8){
            pass.requestFocus();
            Toast.makeText(this, "Please Enter 8 Digit Password", Toast.LENGTH_SHORT).show();
            valid= false;
        }else{
            valid=true;
        }

        return valid;
    }

    private void initialise() {

        auth=FirebaseAuth.getInstance();

        email = findViewById(R.id.Email1);
        pass = findViewById(R.id.Password1);

        btn_login = findViewById(R.id.BTN_LOGIN);

        register = findViewById(R.id.TV_Register);

        dialog = new ProgressDialog(this);

    }
}
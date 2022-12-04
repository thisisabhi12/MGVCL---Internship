package com.example.consumer.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    private final String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SharedPreferences shrd;
    boolean valid = false;
    private boolean b;
    private TextView txt1;
    private EditText name, mtr_no, email, password, mobileNumber, Address, cons_id;
    private Button signUpbtn;
    private FirebaseFirestore db, db2, db3;
    private ProgressDialog progressDialog;
    private String tarrif, s1, s2, s3, s4, s5, s6, s7;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cons_id = findViewById(R.id.cons_id);
        mtr_no = findViewById(R.id.mter_no);
        name = findViewById(R.id.product_name);
        mobileNumber = findViewById(R.id.mobilenumber);
        Address = findViewById(R.id.Address);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);


        signUpbtn = findViewById(R.id.SignUpbtn);

        txt1 = findViewById(R.id.already_Create_An_account);

        db = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        db3 = FirebaseFirestore.getInstance();


        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your Account");
        shrd = getSharedPreferences("MGVCL", MODE_PRIVATE);

        cons_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    db3.collection("NewConsumer").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            b = false;
                            for (DocumentSnapshot d : queryDocumentSnapshots.getDocuments()) {
                                if (cons_id.getText().toString().equalsIgnoreCase(d.getString("ConsumerID"))) {
                                    Toast.makeText(SignUpActivity.this, "Already Register Consumer ID", Toast.LENGTH_LONG).show();
                                    b = false;
                                    signUpbtn.setEnabled(false);
                                    mtr_no.setEnabled(false);
                                    name.setEnabled(false);
                                    mobileNumber.setEnabled(false);
                                    Address.setEnabled(false);
                                    email.setEnabled(false);
                                    password.setEnabled(false);
                                    cons_id.requestFocus();
                                    break;
                                } else {
                                    b = true;
                                }
                            }
                            if (b) {
                                signUpbtn.setEnabled(true);
                                mtr_no.setEnabled(true);
                                name.setEnabled(true);
                                mobileNumber.setEnabled(true);
                                Address.setEnabled(true);
                                email.setEnabled(true);
                                password.setEnabled(true);
                                retrivedb();
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validfild()) {
                    StoreToDB();
                } else {

                    Toast.makeText(SignUpActivity.this, "Hi", Toast.LENGTH_SHORT).show();
                }
                //Chekemail(v);
            }
        });

        //        progressBar.setVisibility(View.GONE);
        txt1 = findViewById(R.id.already_Create_An_account);
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void StoreToDB() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("ConsumerID", s1);
        map.put("MeterNo", s2);
        map.put("Name", s3);
        map.put("PhoneNo", s4);
        map.put("Address", s5);
        map.put("Email", s6);
        map.put("Password", s7);
        map.put("Tarrif", tarrif);

        db2.collection("NewConsumer").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    SharedPreferences.Editor editor = shrd.edit();
                    editor.clear();
                    editor.apply();
                    editor.putString("CID", cons_id.getText().toString());
                    editor.apply();
                    editor.putString("CName", name.getText().toString());
                    editor.apply();
                    editor.putString("CAddress", Address.getText().toString());
                    editor.apply();
                    editor.putString("SDO_CODE", cons_id.getText().toString().substring(0, 3));
                    editor.apply();

                    Toast.makeText(SignUpActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validfild() {
        s1 = cons_id.getText().toString();
        s2 = mtr_no.getText().toString();
        s3 = name.getText().toString();
        s4 = mobileNumber.getText().toString();
        s5 = Address.getText().toString();
        s6 = email.getText().toString();
        s7 = password.getText().toString();

        if (s1.isEmpty() && s2.isEmpty() && s3.isEmpty() && s4.isEmpty() && s5.isEmpty() && s6.isEmpty() && s7.isEmpty()) {
            valid = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Please Fill All details");
            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            builder.setCancelable(false);
            builder.create().show();
        } else {
            valid = true;
        }
        if (valid) {
            if (s4.isEmpty()) {
                Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                mobileNumber.requestFocus();
                valid = false;
            } else {
                if (s4.length() < 10) {
                    Toast.makeText(this, "Please Enter 10 Digit Number", Toast.LENGTH_SHORT).show();
                    mobileNumber.requestFocus();
                    valid = false;
                } else {
                    valid = true;
                }
            }
        }
        if (valid) {
            if (s6.isEmpty()) {
                Toast.makeText(this, "Please Enter Mail", Toast.LENGTH_SHORT).show();
                email.requestFocus();
                valid = false;
            } else {
                if (!email.getText().toString().matches(emailpattern)) {
                    Toast.makeText(this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    valid = false;
                } else {
                    valid = true;
                }
            }
        }
        if (valid) {
            if (password.getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
                password.requestFocus();
                valid = false;
            } else {
                valid = true;
            }

        }

        return valid;
    }

    private void retrivedb() {


        db.collection("Consumer").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                b = false;
                for (DocumentSnapshot d : queryDocumentSnapshots.getDocuments()) {
                    if (cons_id.getText().toString().equalsIgnoreCase(d.getString("CONSUMER_ID"))) {
                        mtr_no.setText(d.getString("METER_NO"));
                        name.setText(d.getString("CONSUMER_NAME"));
                        Address.setText(d.getString("ADDRESS"));
                        tarrif = d.getString("TARRIF");
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    Toast.makeText(SignUpActivity.this, "Invalid Consumer ID", Toast.LENGTH_LONG).show();
                    cons_id.requestFocus();
                }
            }
        });
    }

    /*public void Chekemail(View v) {
        db = FirebaseFirestore.getInstance();
        b = true;

        db.collection(Module).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot d : queryDocumentSnapshots.getDocuments()) {
                    System.out.println("Email : " + d.getString("Email"));
                    if (email.getText().toString().equals(d.getString("Email"))) {
                        b = false;
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (b) {
                    Datainsert();
                } else
                    Toast.makeText(getApplicationContext(), "Email is Already Exist", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Datainsert() {
        db = FirebaseFirestore.getInstance();

        Map<String, String> map;

        map = new HashMap<String, String>();//Creating HashMap.
        SharedPreferences.Editor editor = shrd.edit();
        editor.remove("SID");
        editor.apply();
        editor.remove("CID");
        editor.apply();
        editor.remove("CName");
        editor.apply();
        editor.remove("SName");
        editor.apply();
        editor.remove("CEmail");
        editor.apply();
        editor.remove("SEmail");
        editor.apply();
        editor.clear();
        editor.apply();

        if (Module.equals("Customer")) {
            map.put("ConsumerID", cons_id.getText().toString());
            editor.apply();
            editor.putString("CID", cons_id.getText().toString());
            editor.apply();
            editor.putString("CEmail", email.getText().toString());
            editor.apply();
        } else {
            if (Module.equals("admin")) {
                editor.apply();
                editor.putString("AID", name.getText().toString());
                editor.apply();
                editor.putString("SEmail", email.getText().toString());
                editor.apply();
                map.put("DOB", cons_id.getText().toString());
            }
        }
        map.put("Name", name.getText().toString());
        map.put("PhoneNo", mobileNumber.getText().toString());
        map.put("Address1", Address.getText().toString());
        map.put("Email", email.getText().toString());
        map.put("Password", password.getText().toString());

        db.collection(Module).add(map);

        if (Module.equals("Customer")) {
            Intent intent = new Intent(this, Home_Activity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, Admin_Home_Activity.class);
            startActivity(intent);
            finish();
        }
    }


//    private void checkInput(){
//        if(!TextUtils.isEmpty(name.getText())){
//            if(!TextUtils.isEmpty(email.getText())){
//                if(!TextUtils.isEmpty(password.getText()) && password.length() >=4){
//                    if(!TextUtils.isEmpty(mobileNumber.getText()) && mobileNumber.length() >=10) {
//                        signUpbtn.setEnabled(true);
//                    }else{
//                        signUpbtn.setEnabled(false);
//                    }
//                }else {
//                    signUpbtn.setEnabled(false);
//                }
//            }else {
//                signUpbtn.setEnabled(false);
//            }
//        }else{
//            signUpbtn.setEnabled(false);
//        }
//    }

    private void checkEmailAndMobileNumber() {
        if (email.getText().toString().matches(emailpattern)) {

            progressBar.setVisibility(View.VISIBLE);
            signUpbtn.setEnabled(false);
            Home_Activity mainActivity = new Home_Activity();


        } else {
            password.setError("Too weak password, Use Strong One");
        }
    }*/
}

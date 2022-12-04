package com.example.consumer.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer.Admin_Home_Activity;
import com.example.consumer.Home_Activity;
import com.example.consumer.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SignInActivity extends AppCompatActivity {

    FirebaseFirestore database;
    SharedPreferences shrd;
    String AdminID, ConsumerID, ConsumerName, ConsumerAdd, SDO_Code;
    private TextView txtdontHaveAccount;
    private TextView txtSkipSign;
    private EditText email;
    private EditText password;
    private Button signinBtn;
    private FirebaseAuth firebaseAuth;
    private final String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private boolean b, a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        shrd = getSharedPreferences("MGVCL", MODE_PRIVATE);
        if (shrd.contains("CID")) {
            Intent intent = new Intent(SignInActivity.this, Home_Activity.class);
            startActivity(intent);
            finish();
        } else if (shrd.contains("SID")) {
            startActivity(new Intent(SignInActivity.this, Admin_Home_Activity.class));
        }
        setContentView(R.layout.activity_sign_in);

        txtdontHaveAccount = (TextView) findViewById(R.id.dontHaveAccount);
        signinBtn = (Button) findViewById(R.id.LoginButton);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
        txtSkipSign = (TextView) findViewById(R.id.skipSignIn);
        database = FirebaseFirestore.getInstance();


        txtdontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
            }
        });

        // email & password validation
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = shrd.edit();
                if (email.getText().length() == 3) {
                    database.collection("Admin").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            b = false;
                            for (DocumentSnapshot d : queryDocumentSnapshots.getDocuments()) {
                                if (email.getText().toString().equalsIgnoreCase(d.getString("SDO_CODE"))) {
                                    if (password.getText().toString().equalsIgnoreCase(d.getString("PASSWORD"))) {
                                        b = true;
                                        AdminID = d.getString("SDO_CODE");
                                        break;
                                    } else {
                                        Toast.makeText(SignInActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            if (!b) {
                                Toast.makeText(SignInActivity.this, "Incorrect Admin ID", Toast.LENGTH_SHORT).show();
                            } else {
                                editor.clear();
                                editor.apply();
                                editor.putString("SID", AdminID);
                                editor.apply();
                                startActivity(new Intent(SignInActivity.this, Admin_Home_Activity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    database.collection("NewConsumer").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            a = false;
                            for (DocumentSnapshot d : queryDocumentSnapshots.getDocuments()) {
                                if (email.getText().toString().equalsIgnoreCase(d.getString("PhoneNo"))) {
                                    if (password.getText().toString().equalsIgnoreCase(d.getString("Password"))) {
                                        ConsumerID = d.getString("ConsumerID");
                                        ConsumerAdd = d.getString("Address");
                                        ConsumerName = d.getString("Name");
                                        SDO_Code = ConsumerID.substring(0, 3);
                                        a = true;
                                        break;
                                    } else {
                                        Toast.makeText(SignInActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            if (a) {
                                editor.clear();
                                editor.apply();
                                editor.putString("CID", ConsumerID);
                                editor.apply();
                                editor.putString("CName", ConsumerName);
                                editor.apply();
                                editor.putString("CAdd", ConsumerAdd);
                                editor.apply();
                                editor.putString("SDO", SDO_Code);
                                editor.apply();
                                startActivity(new Intent(SignInActivity.this, Home_Activity.class));
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, "Invalid Consumer ID", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

               /*b=false;
                database.collection(Module).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot d: queryDocumentSnapshots.getDocuments()){
                            System.out.println("Email  : "+d.getString("Email")+email.getText().toString());
                            if(d.getString("Email")!=null)
                                if(d.getString("Email").equals(email.getText().toString()))
                                    if(d.getString("Password")!=null)
                                        if(d.getString("Password").equals(password.getText().toString()))
                                            b=true;
                                        else
                                            Toast.makeText(getApplicationContext(),"Password is Not Correct",Toast.LENGTH_LONG).show();
                        }
                        if(b==false)
                            Toast.makeText(getApplicationContext(),"Email is Not Correct",Toast.LENGTH_LONG).show();

                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(b==true) {
                            SharedPreferences.Editor editor =shrd.edit();
                            if(Module.equals("Customer")){
                                database.collection(Module).whereEqualTo("Email",email.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for(DocumentSnapshot d:queryDocumentSnapshots.getDocuments()){
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
                                            editor.putString("CID",d.getString("ConsumerID"));
                                            editor.apply();
                                            editor.putString("CName",d.getString("Name"));
                                            editor.apply();
                                            editor.putString("CEmail",d.getString("Email"));
                                            editor.apply();
                                        }
                                    }
                                });
                                Intent it = new Intent(SignInActivity.this, Home_Activity.class);
                                startActivity(it);
                            }
                            else {
                                database.collection(Module).whereEqualTo("Email",email.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for(DocumentSnapshot d:queryDocumentSnapshots.getDocuments()){
                                            editor.remove("SID");
                                            editor.apply();
                                            editor.remove("CID");
                                            editor.apply();
                                            editor.remove("SName");
                                            editor.apply();
                                            editor.remove("CName");
                                            editor.apply();
                                            editor.remove("CEmail");
                                            editor.apply();
                                            editor.remove("SEmail");
                                            editor.apply();
                                            editor.clear();
                                            editor.apply();
                                            editor.putString("SID","135186");
                                            editor.apply();
                                            editor.putString("SName",d.getString("Name"));
                                            editor.apply();
                                            editor.putString("SEmail",d.getString("Email"));
                                            editor.apply();

                                        }
                                    }
                                });
                                Intent it = new Intent(SignInActivity.this, Admin_Home_Activity.class);
                                startActivity(it);
                            }
                            finish();
                        }
                    }
                });*/
            }
        });


    }


    private void checkInput() {
        if (!TextUtils.isEmpty(email.getText())) {
            signinBtn.setEnabled(!TextUtils.isEmpty(password.getText()) && password.length() >= 3);
        } else {
            signinBtn.setEnabled(false);
        }
    }
}
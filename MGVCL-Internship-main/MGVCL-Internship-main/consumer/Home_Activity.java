package com.example.consumer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer.login.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home_Activity extends AppCompatActivity {

    Spinner sp1;

    RecyclerView.Adapter adapter1;
    RecyclerView.LayoutManager manager1;
    ArrayList<Complain> list1;
    RecyclerView raj;

    int Counter=0;

    TextInputEditText des;

    SharedPreferences shrd;

    TextView TV3,TV5;

    ArrayAdapter<String> adapter;

    Button Btn_submite;

    DatabaseReference db1, db2;

    FirebaseAuth auth;

    int Comp_Id = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        initialise();
        getSupportActionBar().setTitle("MGVCL");
        Btn_submite.setOnClickListener(v -> {
            if (sp1.getSelectedItem().toString() == "Select") {
                Toast.makeText(this, "Please Select Valid Reason", Toast.LENGTH_SHORT).show();
            } else {
                StoreToDB();
            }
        });

        db2 = FirebaseDatabase.getInstance().getReference("Complains").child(TV3.getText().toString().substring(0, 3));

        db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Counter=0;
                String  s2, s3, s4;
                adapter1 = new Consumer_RV_Adepter(list1,Home_Activity.this);
                list1.clear();
                for(DataSnapshot d: snapshot.getChildren()) {
                    if(d.getValue(Complain.class).getCon_Id().equalsIgnoreCase(TV3.getText().toString())){
                        Counter+=1;
                        s2 = d.getValue(Complain.class).getComplain();
                        s3 = d.getValue(Complain.class).getDisc();
                        s4 = d.getValue(Complain.class).getState();
                        list1.add(new Complain(String.valueOf(Counter), s2, s3, s4));
                    }else {
                        Toast.makeText(Home_Activity.this, "There is No complains For now", Toast.LENGTH_SHORT).show();
                    }
                    raj.setAdapter(adapter1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        getId();
    }

    private void StoreToDB() {
        String s1;
        Complain camp = new Complain();
        camp.setComplain(sp1.getSelectedItem().toString());

        if (!des.getText().toString().isEmpty()) {
            s1 = des.getText().toString();
        } else {
            s1 = "N/A";
        }
        camp.setAddress(shrd.getString("CAdd", "N/A"));
        camp.setName(shrd.getString("CName", "N/A"));
        camp.setSDO_Code(shrd.getString("SDO", "N/A"));
        camp.setCon_Id(TV3.getText().toString());
        camp.setComp_ID(String.valueOf(Comp_Id));
        camp.setState("InProcess");
        camp.setDisc(s1);
        db1 = FirebaseDatabase.getInstance().getReference("Complains");
        db1.child(shrd.getString("SDO", "N/A")).child(camp.getComp_ID()).setValue(camp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    getId();
                    Toast.makeText(Home_Activity.this, "Complain Register SuccessFully", Toast.LENGTH_SHORT).show();
                    des.setText("");
                }
            }
        });
    }

    private void getId() {
        shrd = getSharedPreferences("MGVCL", MODE_PRIVATE);
        TV3.setText(shrd.getString("CID", null));
        TV5.setText(shrd.getString("CName", null));

        db1 = FirebaseDatabase.getInstance().getReference("Complains");

        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int temp = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.hasChildren()) {
                        for (DataSnapshot ds1 : ds.getChildren()) {
                            if (ds1.exists()) {
                                if (ds1.getValue(Complain.class).getSDO_Code().equalsIgnoreCase(TV3.getText().toString().substring(0, 3))) {
                                    temp = Integer.valueOf(ds1.getValue(Complain.class).getComp_ID());
                                }
                            }
                        }
                    }
                }
                Comp_Id = temp + 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initialise() {

        sp1 = findViewById(R.id.HM_SP_Complain);

        raj = findViewById(R.id.Raj);
        manager1 = new LinearLayoutManager(this);
        list1 = new ArrayList<>();
        raj.setLayoutManager(manager1);
        raj.setHasFixedSize(true);

        des = findViewById(R.id.HM_Des1);
        Btn_submite = findViewById(R.id.HM_BTN_SUB);

        auth = FirebaseAuth.getInstance();


        TV3 = findViewById(R.id.TV_3);
        TV5 = findViewById(R.id.TV_5);

        getId();

        String[] List1 = {"Select", "Power Cut", "Billing Problems", "Street light issues", "Maintenance"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, List1);
        sp1.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.log_out_menu:
                AlertDialog.Builder builder = new AlertDialog.Builder(Home_Activity.this);
                builder.setMessage("Are you sure you want to Log out ?");
                builder.setTitle("Log out");
                builder.setCancelable(false);

                SharedPreferences.Editor editor = shrd.edit();
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Home_Activity.this, SignInActivity.class);
                        editor.clear();
                        editor.apply();
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }
}
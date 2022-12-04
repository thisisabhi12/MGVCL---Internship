package com.example.consumer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer.login.SignInActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Home_Activity extends AppCompatActivity {

    RecyclerView RV;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    SharedPreferences shrd;

    DatabaseReference db1;

    ArrayList<Complain> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_activity);

        initialise();

        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s1, s2, s3, s4, s5, s6, s7;
                boolean check = false;
                adapter = new ItemAdepter(list, Admin_Home_Activity.this);
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.hasChildren()) {
                        for (DataSnapshot ds1 : ds.getChildren()) {
                            if (ds1.getValue(Complain.class).getSDO_Code().equalsIgnoreCase(shrd.getString("SID", null))) {
                                if (ds1.getValue(Complain.class).getState().equalsIgnoreCase("InProcess")) {
                                    s1 = ds1.getValue(Complain.class).getCon_Id();
                                    s2 = ds1.getValue(Complain.class).getComplain();
                                    s3 = ds1.getValue(Complain.class).getDisc();
                                    s4 = ds1.getValue(Complain.class).getName();
                                    s5 = ds1.getValue(Complain.class).getAddress().toLowerCase();
                                    s6 = ds1.getValue(Complain.class).getState();
                                    s7 = ds1.getValue(Complain.class).getComp_ID();
                                    list.add(new Complain(s1, s2, s3, s4, s5, s6, s7));
                                    check = true;
                                }
                            }
                        }
                        RV.setAdapter(adapter);
                    }
                }
                if (!check) {
                    Toast.makeText(Admin_Home_Activity.this, "There is No complains For now", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initialise() {

        db1 = FirebaseDatabase.getInstance().getReference("Complains");

        RV = findViewById(R.id.AD_RV);
        RV.setHasFixedSize(true);

        manager = new LinearLayoutManager(this);
        list = new ArrayList<>();
        RV.setLayoutManager(manager);

        shrd = getSharedPreferences("MGVCL", MODE_PRIVATE);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Home_Activity.this);
                builder.setMessage("Are you sure you want to Log out ?");
                builder.setTitle("Log out");
                builder.setCancelable(false);

                SharedPreferences shrd = getSharedPreferences("MGVCL", MODE_PRIVATE);
                SharedPreferences.Editor editor = shrd.edit();
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Admin_Home_Activity.this, SignInActivity.class);
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
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}

/*
 for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getValue(Complain.class).getSDO_Code().equalsIgnoreCase(shrd.getString("SID", null))) {
                        s1 = ds.getValue(Complain.class).getCon_Id();
                        s2 = ds.getValue(Complain.class).getComplain();
                        s3 = ds.getValue(Complain.class).getDisc();
//                            s4 = ds.getValue(Complain.class).getName();
//                            s5 = ds.getValue(Complain.class).getAddress();
                        list.add(new Complain(s1, s2, s3));
                        break;
                    } else {
                        Toast.makeText(Admin_Home_Activity.this, "There is No complains For now", Toast.LENGTH_SHORT).show();
                    }
                }
                adapter = new ItemAdepter(list);
                RV.setAdapter(adapter);
* */
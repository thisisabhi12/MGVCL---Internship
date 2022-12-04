package com.example.consumer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemAdepter extends RecyclerView.Adapter<ItemAdepter.itemHolder> {

    ArrayList<Complain> list;

    Context ct;

    ArrayAdapter<String> adapter;


    DatabaseReference db,db2;


    public static class itemHolder extends RecyclerView.ViewHolder{

        TextView con_id,comp_sp,comp_des,Consumer_Name,Consumer_Add,Com_id;
        Spinner Com_stats;

        public itemHolder(@NonNull View itemView) {
            super(itemView);
            con_id = itemView.findViewById(R.id.CON_ID);
            comp_sp = itemView.findViewById(R.id.COMP_SP);
            comp_des = itemView.findViewById(R.id.COMP_DES);
            Consumer_Name= itemView.findViewById(R.id.CS_RV_Consumer_Name);
            Consumer_Add= itemView.findViewById(R.id.CS_RV_Consumer_ADD);
            Com_stats= itemView.findViewById(R.id.CS_RV_Stats);
            Com_id =itemView.findViewById(R.id.COM_ID);

        }

    }

    public  ItemAdepter(ArrayList<Complain> list,Context ct){
        this.list=list;
        this.ct=ct;
    }

    @NonNull
    @Override
    public itemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_rv,parent,false);
        itemHolder holder = new itemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull itemHolder holder, int position) {
        Complain cp = list.get(position);

        db= FirebaseDatabase.getInstance().getReference("Complains").child(cp.getCon_Id().substring(0,3));

        holder.Com_id.setText(cp.getComp_ID());
        holder.con_id.setText(cp.getCon_Id());
        holder.comp_sp.setText(cp.getComplain());
        holder.comp_des.setText(cp.getDisc());
        holder.Consumer_Name.setText(cp.getName());
        holder.Consumer_Add.setText(cp.getAddress());
        String[] List1 = {"InProcess", "Resolve"};
        adapter = new ArrayAdapter<String>(ct, android.R.layout.simple_spinner_dropdown_item, List1);
        holder.Com_stats.setAdapter(adapter);
        if(cp.getState().equalsIgnoreCase("InProcess")){
            holder.Com_stats.setSelection(0);
        }else{
            holder.Com_stats.setSelection(1);
        }

        holder.Com_stats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(holder.Com_id.getText().toString().equalsIgnoreCase(ds.getValue(Complain.class).getComp_ID())) {
                                db2 = db.getRef();
                                cp.setSDO_Code(holder.con_id.getText().toString().substring(0,3));
                                cp.setState(holder.Com_stats.getSelectedItem().toString());
                                db2.child(ds.getValue(Complain.class).getComp_ID()).setValue(cp);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

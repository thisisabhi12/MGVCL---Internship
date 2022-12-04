package com.example.consumer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Consumer_RV_Adepter extends RecyclerView.Adapter<Consumer_RV_Adepter.Con_RV_Holder> {

    ArrayList<Complain> list;

    Context ct;

    public Consumer_RV_Adepter(ArrayList<Complain> list, Context ct) {
        this.list = list;
        this.ct = ct;
    }


    @NonNull
    @Override
    public Con_RV_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumer_rv,parent,false);
        Consumer_RV_Adepter.Con_RV_Holder holder = new Con_RV_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Con_RV_Holder holder, int position) {
        Complain cp = list.get(position);


        holder.Cs_Con_Com_Id.setText(cp.getComp_ID());
        holder.Cs_Con_Comp_Sp.setText(cp.getComplain());
        holder.Cs_Con_Comp_Des.setText(cp.getDisc());
        holder.Cs_Con_Comp_Sats.setText(cp.getState());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Con_RV_Holder extends RecyclerView.ViewHolder{

        TextView  Cs_Con_Com_Id,Cs_Con_Comp_Sp,Cs_Con_Comp_Des,Cs_Con_Comp_Sats;

        public Con_RV_Holder(@NonNull View itemView) {
            super(itemView);

            Cs_Con_Com_Id=itemView.findViewById(R.id.CS_CON_COM_ID);
            Cs_Con_Comp_Sp=itemView.findViewById(R.id.CS_CON_COMP_SP);
            Cs_Con_Comp_Des=itemView.findViewById(R.id.CS_CON_COMP_DES);
            Cs_Con_Comp_Sats=itemView.findViewById(R.id.CS_CON_COMP_SATS);

        }
    }

}

package com.example.consumer;

import android.widget.ArrayAdapter;

public class Complain {

    String Con_Id,complain,disc,SDO_Code,Name,Address,state,Comp_ID;

    public  Complain (String Comp_ID,String complain,String disc,String state){
        this.Comp_ID=Comp_ID;
        this.complain=complain;
        this.disc=disc;
        this.state=state;
    }

    public Complain(String con_Id, String complain, String disc,  String name, String address,String state,String Comp_ID) {
        Con_Id = con_Id;
        this.complain = complain;
        this.disc = disc;
        Name = name;
        Address = address;
        this.state=state;
        this.Comp_ID=Comp_ID;
    }

    public Complain() {
    }

    public Complain(String con_Id,String complain,String disc) {
        this.Con_Id = con_Id;
        this.complain = complain;
        this.disc = disc;
    }

    public String getSDO_Code() {
        return SDO_Code;
    }

    public void setSDO_Code(String SDO_Code) {
        this.SDO_Code = SDO_Code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComp_ID() {
        return Comp_ID;
    }

    public void setComp_ID(String comp_ID) {
        Comp_ID = comp_ID;
    }

    public String getComplain() {
        return complain;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getCon_Id() {
        return Con_Id;
    }

    public void setCon_Id(String con_Id) {
        Con_Id = con_Id;
    }
}

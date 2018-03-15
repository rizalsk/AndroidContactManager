package com.example.rizmas.cobasatu;

import android.net.Uri;

/**
 * Created by rizmas on 8/17/2017.
 */

public class Contact {
    private String _name, _phone, _email, _address;
    private Uri _imgUri;
    private int _id;

    public Contact(int id, String name, String phone, String email, String address, Uri ImageUri){
        _id = id;
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _imgUri= ImageUri;
    }


    public int get_id(){ return _id; }
    public String get_name(){
        return _name;
    }
    public String get_phone(){return _phone;}
    public String get_email(){
        return _email;
    }
    public String get_address(){ return _address;}
    public Uri get_imgUri(){return _imgUri;}
}

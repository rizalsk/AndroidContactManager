package com.example.rizmas.cobasatu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rizmas on 9/2/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db_contact",
    TABLE_NAME = "ContactManager",
    KEY_ID = "id",
    KEY_NAME = "name",
    KEY_PHONE = "phone",
    KEY_EMAIL = "email",
    KEY_ADDRESS = "address",
    KEY_IMGURI = "imgUri";

    public DatabaseHandler(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_NAME+" TEXT ,"
                +KEY_PHONE+" TEXT,"+KEY_EMAIL+" TEXT,"+KEY_ADDRESS+" TEXT,"+ KEY_IMGURI+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void createContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.put(KEY_ID, contact.get_id());
        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_PHONE, contact.get_phone());
        values.put(KEY_EMAIL, contact.get_email());
        values.put(KEY_ADDRESS, contact.get_address());
        values.put(KEY_IMGURI, contact.get_imgUri().toString());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Contact getContact(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_ADDRESS, KEY_IMGURI}, KEY_ID+"=?", new String[] {String.valueOf(id)} ,null ,null ,null ,null );
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5)));
        return contact;
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID+"=?", new String[] {String.valueOf(contact.get_id())});
        db.close();
    }

    public int updateContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues  values = new ContentValues();

        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_PHONE, contact.get_phone());
        values.put(KEY_EMAIL, contact.get_email());
        values.put(KEY_ADDRESS, contact.get_address());
        values.put(KEY_IMGURI, contact.get_imgUri().toString());
        int rowAffected = db.update(TABLE_NAME, values, KEY_ID+"=?", new String[] {String.valueOf(contact.get_id())} );
        return rowAffected;
    }

    public int getContactsCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME , null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        return count+1;
    }

    public List<Contact> getAllContacts(){
        List<Contact> contacts = new ArrayList<Contact>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);

        if (cursor.moveToFirst()){
            //Contact contact;
            do{
                contacts.add(new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5))));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }
}

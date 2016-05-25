package com.example.minky.bigmeet;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ComponentInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minky on 23/05/2016.
 */
public class DatabaseContactHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_CONTACTS = " contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email_address";

    public DatabaseContactHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONTACTS);
        onCreate(db);
    }

    /* ALL CRUD OPERATION */
    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME,contact.getName());
        values.put(KEY_EMAIL,contact.getEmailAddress());

        db.insert(TABLE_CONTACTS,null,values);
        db.close();
    }

    public Contact getContact(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS,new String[]{KEY_ID,KEY_NAME,KEY_EMAIL},KEY_ID + "=?",new String[]{String.valueOf(id)},null,null,null,null);
        Contact contact = null;
        if(cursor != null && cursor.moveToFirst()){
            contact = new Contact(cursor.getString(1),Integer.parseInt(cursor.getString(0)),cursor.getString(2));
        }
        cursor.close();
        db.close();
        return contact;
    }

    public Contact getContact(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS,new String[]{KEY_ID,KEY_NAME,KEY_EMAIL},KEY_NAME + "=?",new String[]{name},null,null,null,null);
        Contact contact = null;
        if(cursor != null && cursor.moveToFirst()){
            contact = new Contact(cursor.getString(1),Integer.parseInt(cursor.getString(0)),cursor.getString(2));
        }
        cursor.close();
        db.close();
        return contact;
    }

    public List<Contact> getAllContacts(){
        List<Contact> contactList = new ArrayList<Contact>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setEmailAddress(cursor.getString(2));

                contactList.add(contact);
            }while (cursor.moveToNext());
        }
        db.close();
        return  contactList;
    }
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_EMAIL, contact.getEmailAddress());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return count
        return count;
    }

}

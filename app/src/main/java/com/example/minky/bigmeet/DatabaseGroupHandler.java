package com.example.minky.bigmeet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minky on 23/05/2016.
 */
public class DatabaseGroupHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "groupsManager";
    private static final String TABLE_GROUPS = "groups";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_MEMBERS = "members";
    private static final String KEY_IMAGE = "image";

    public DatabaseGroupHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROUPS_TABLE = "CREATE TABLE " + TABLE_GROUPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_MEMBERS + " TEXT," + KEY_IMAGE + " BLOB"+");";
        db.execSQL(CREATE_GROUPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_GROUPS);
        onCreate(db);
    }

    /* ALL CRUD OPERATION */
    public void addGroup(Group group){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME,group.getName());
        values.put(KEY_MEMBERS,convertObject2String(group));
        values.put(KEY_IMAGE,group.getGroupPhoto());
        db.insert(TABLE_GROUPS,null,values);
        db.close();
    }

    public String convertObject2String(Group group){
        String membersInString = new String();
        int count = group.getMembers().size();
        ArrayList<Contact> groupMembers = group.getMembers();
        for(int i=0;i<count;i++){
            membersInString = groupMembers.get(i).getName() + "/";
        }
        return membersInString;
    }

    public ArrayList<Contact> convertString2Object(String objectInString, ArrayList<Contact> dictionary){
        String[] splitted = objectInString.split("/");
        ArrayList<Contact> contactArrayList = new ArrayList<>();
        for(int i=0;i<splitted.length;i++){
            for(int j=0;j<dictionary.size();j++){
                if(dictionary.get(j).getName().equals(splitted[i])){
                    Contact contact = new Contact(splitted[i],dictionary.get(j).getId(),dictionary.get(j).getEmailAddress());
                    contactArrayList.add(contact);
                }
            }
        }
        return contactArrayList;
    }

    public Group getGroup(int id, ArrayList<Contact> contactList){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GROUPS,new String[]{KEY_ID,KEY_NAME,KEY_MEMBERS,KEY_IMAGE},KEY_ID + "=?",new String[]{String.valueOf(id)},null,null,null,null);
        Group group = null;
        if(cursor != null && cursor.moveToFirst()){
            group = new Group(cursor.getString(1),Integer.parseInt(cursor.getString(0)),convertString2Object(cursor.getString(2),contactList),cursor.getBlob(3));
        }
        cursor.close();
        db.close();
        return group;
    }

    public Group getGroup(String name, ArrayList<Contact> contactList ){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GROUPS,new String[]{KEY_ID,KEY_NAME,KEY_MEMBERS,KEY_IMAGE},KEY_NAME + "=?",new String[]{name},null,null,null,null);
        Group group = null;
        if(cursor != null && cursor.moveToFirst()){
            group = new Group(cursor.getString(1),Integer.parseInt(cursor.getString(0)),convertString2Object(cursor.getString(2),contactList),cursor.getBlob(3));
        }
        cursor.close();
        db.close();
        return group;
    }

    public List<Group> getAllGroups(ArrayList<Contact> contactList){
        List<Group> groupList = new ArrayList<Group>();
        String selectQuery = "SELECT * FROM " + TABLE_GROUPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Group group = new Group();
                group.setId(Integer.parseInt(cursor.getString(0)));
                group.setName(cursor.getString(1));
                group.setMembers(convertString2Object(cursor.getString(2),contactList));
                group.setGroupPhoto(cursor.getBlob(3));
                groupList.add(group);
            }while (cursor.moveToNext());
        }
        db.close();
        return groupList;
    }
    public int updateGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, group.getName());
        values.put(KEY_MEMBERS, convertObject2String(group));
        values.put(KEY_IMAGE,group.getGroupPhoto());
        // updating row
        return db.update(TABLE_GROUPS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(group.getId()) });
    }

    // Deleting single contact
    public void deleteGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROUPS, KEY_ID + " = ?",
                new String[] { String.valueOf(group.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getGroupsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_GROUPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return count
        return count;
    }
}

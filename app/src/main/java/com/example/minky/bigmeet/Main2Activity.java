package com.example.minky.bigmeet;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {

    ListView lv;
    ArrayAdapter<String> ladapter;
    ArrayList<Long> idList;
    String info;
    net.fortuna.ical4j.model.Calendar eventCal;
    FirebaseDatabase firebaseDatabse;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initialise();
        accessCal();
        checkCalendar();
        eventCal = retrieveEvent();
        if(eventCal!=null){
            System.out.println(eventCal.getComponents(Property.DESCRIPTION).toString());
        }
        pushEventDetails("Mink Y Ang",info);
        eListener();

        try {
            VFreeBusySample.requestFree("0900","1800","0130",this,eventCal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void initialise() {
        lv = (ListView) findViewById(R.id.listView);
        ladapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(ladapter);
        idList = new ArrayList<>();
        info = getIntent().getStringExtra("eventDetails");
        firebaseDatabse = FirebaseDatabase.getInstance();
        myRef = firebaseDatabse.getReference("event");

    }

    public void checkCalendar() {
        if (ladapter.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please add calendar!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void pushEventDetails(String location, String message){
        myRef = firebaseDatabse.getReference("event");
        DatabaseReference progressPath = myRef.child(location).child("progress");
        DatabaseReference requestPath = myRef.child(location).child("Request");
        DatabaseReference datePath = requestPath.child("Date");
        DatabaseReference timePath = requestPath.child("Time");
        DatabaseReference durationPath = requestPath.child("Duration");
        DatabaseReference organiserPath = requestPath.child("Organiser");
        DatabaseReference recipientPath = requestPath.child("Recipients");

        progressPath.setValue("In Progress");
        datePath.child("Start").setValue(extractInfo(message, "startDate"));
        datePath.child("End").setValue(extractInfo(message, "endDate"));
        timePath.child("Start").setValue(extractInfo(message, "startTime"));
        timePath.child("End").setValue(extractInfo(message, "endTime"));
        durationPath.setValue(extractInfo(message, "eventDuration"));
        organiserPath.setValue("");
        recipientPath.setValue("");
    }

    public void eListener() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", firebaseError.toException());
            }
        });
    }


    public String extractInfo(String message, String identity) {
        String[] extracted = message.split("\\.");
        String returnedInfo = new String();

        switch (identity) {
            case "eventName":
                returnedInfo = extracted[0];
                break;
            case "eventDuration":
                returnedInfo = extracted[1];
                break;
            case "startTime":
                returnedInfo = extracted[2];
                break;
            case "endTime":
                returnedInfo = extracted[3];
                break;
            case "startDate":
                returnedInfo = extracted[4];
                break;
            case "endDate":
                returnedInfo = extracted[5];
                break;
        }
        return returnedInfo;
    }

    public void accessCal() {
        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        ContentResolver cr = getContentResolver();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Please enable READ CALENDAR permission",Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor calCursor = cr.query(CalendarContract.Calendars.CONTENT_URI,
                projection,
                CalendarContract.Calendars.VISIBLE + " = 1",
                null,
                CalendarContract.Calendars._ID + " ASC");

        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                if(displayName != null) {
                    ladapter.add(displayName);
                    idList.add(id);
                }
            } while (calCursor.moveToNext());
        }
    }

    public net.fortuna.ical4j.model.Calendar retrieveEvent(){
        Toast.makeText(getApplicationContext(),"Retrieving events...",Toast.LENGTH_SHORT).show();
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2001, 9, 23, 8, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2016, 10, 24, 8, 0);

        String[] proj = new String[]{
                CalendarContract.Instances._ID,
                CalendarContract.Instances.TITLE,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.END,
                CalendarContract.Instances.EVENT_ID};
        Cursor cursor = CalendarContract.Instances.query(getContentResolver(),proj,beginTime.getTimeInMillis(),endTime.getTimeInMillis());

        net.fortuna.ical4j.model.Calendar tempCal = null;
        if (cursor.moveToFirst()) {
            do {
                tempCal = new net.fortuna.ical4j.model.Calendar();
                tempCal.getComponents().add(createEvent(cursor.getString(2),cursor.getString(3),cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        if (cursor.getCount() > 0) {
            Toast.makeText(getApplicationContext(),"Events found",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"No events found",Toast.LENGTH_SHORT).show();
        }
        return tempCal;
    }

    public VEvent createEvent(String begin, String end, String eventName){
        VEvent event;

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(Long.parseLong(begin));

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(Long.parseLong(end));
        System.out.println(startCalendar.getTime());
        DateTime dtBegin = new DateTime(startCalendar.getTime());
        DateTime dtEnd = new DateTime(endCalendar.getTime());
        event = new VEvent(dtBegin,dtEnd,eventName);
        return event;
    }
}

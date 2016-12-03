package com.example.minky.bigmeet;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.minky.bigmeet.FirebaseClient.*;
import static com.example.minky.bigmeet.FirebaseClient.firebaseDatabse;

public class SendEventActivity extends AppCompatActivity {

    ListView lv;
    ArrayAdapter<String> ladapter;
    ArrayList<Long> idList;
    String info;
    net.fortuna.ical4j.model.Calendar eventCal;
    String refID;
    HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initialise();
        accessCal();
        eListener();
        checkCalendar();
        eventCal = retrieveEvent();
        if (eventCal != null) {
            System.out.println(eventCal.getComponents(Property.DESCRIPTION).toString());
        }
        refID = pushEventDetails();
        //Toast.makeText(getApplicationContext(), "Token : " + refID, Toast.LENGTH_SHORT).show();
    }

    public void initialise() {
        lv = (ListView) findViewById(R.id.listView);
        ladapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(ladapter);
        idList = new ArrayList<>();
        //info is the event details from previous activity.
        //info = getIntent().getStringExtra("eventDetails");
        hashMap = (HashMap<String, String>)getIntent().getSerializableExtra("eventDetails");
    }

    public void checkCalendar() {
        //lAdapter stores all the email address synced with user's calendar.
        if (ladapter.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please add calendar!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public String pushEventDetails(){
        myRef = firebaseDatabse.getReference("Events");
        DatabaseReference pushRef = myRef.push();

        DatabaseReference keyPath = myRef.child(pushRef.getKey());

        Event event = new Event(hashMap);
        List<Attendees> attendees = new ArrayList<>();
        List<String> tokens = new ArrayList<>();
        tokens.add("");
        tokens.add("");
        attendees.add(new Attendees("7pOnpCRH2iaXlRaxNyRIva5JFax1",tokens));
        attendees.add(new Attendees("rRL9vD3k9dgKjMYFJ7KikBzUk0a2",tokens));
        event.setAttendees(attendees);
        keyPath.setValue(event);
        return pushRef.getKey();
    }

    public void eListener() {
        //Any update to Firebase is reflected here
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

    /**
     * Called at the start of activity to find out how many email addresses that are synced to user's Calendar
     * These email addresses and their ids are then stored in lAdapter
     */
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

    /**
     * Retrieve a list of events from user's calendar
     * @return Events in a calendar form
     */
    public net.fortuna.ical4j.model.Calendar retrieveEvent(){
        Toast.makeText(getApplicationContext(),"Retrieving events...",Toast.LENGTH_SHORT).show();
        Calendar beginTime = Calendar.getInstance();

        //Need to change this to specify start date
        //Better to parse in as input
        beginTime.set(2001, 9, 23, 8, 0);
        Calendar endTime = Calendar.getInstance();

        //Need to change this to specify end date
        //Better to parse in as input
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

    /**
     * Create event
     * Note: This only creates an event with no tie to the user calendar
     * Tbis is not an insert event method
     * @param begin  being cStartTime and date
     * @param end end cStartTime and date
     * @param eventName event name
     * @return
     */
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

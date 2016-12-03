package com.example.minky.bigmeet;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class AddEventActivity extends AppCompatActivity {

    Button startTime;
    Button endTime;
    Button startDate;
    Button endDate;
    Button checkLocation;
    Button send;
    Button group;

    EditText eventName;
    EditText eventDuration;
    EditText venue;
    static TextView showSTime;
    static TextView showETime;
    static TextView showSDate;
    static TextView showEDate;
    static TextView groupSelected;
    static GregorianCalendar cStartDate;
    static GregorianCalendar cEndDate;
    static GregorianCalendar cStartTime;
    static GregorianCalendar cEndTime;
    static Context main;
    static final int CHANGE_STARTTIME = 1;
    static final int CHANGE_ENDTIME = 2;
    static final int CHANGE_STARTDATE = 3;
    static final int CHANGE_ENDDATE = 4;
    public static final int GROUPSELECTED = 5;

    static Long EStartTime;
    static Long ESendTime;
    static Long EStartDate;
    static Long ESendDate;

    HashMap<String,String> eventDetails;

    static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case CHANGE_STARTTIME:
                    cStartTime = (GregorianCalendar)msg.obj;
                    EStartTime = cStartTime.getTimeInMillis();
                    if(cEndTime == null) {
                        String timer = String.format("%02d:%02d", cStartTime.get(Calendar.HOUR), cStartTime.get(Calendar.MINUTE));
                        showSTime.setText(timer);
                    }else if(cEndTime.compareTo(cStartTime)>=0) {
                        String timer = String.format("%02d:%02d", cStartTime.get(Calendar.HOUR), cStartTime.get(Calendar.MINUTE));
                        showSTime.setText(timer);
                    }else{
                        Toast.makeText(main, "Invalid time selected", Toast.LENGTH_SHORT).show();
                        cStartTime = null;
                        cEndTime = null;
                        showSTime.setText("");
                        showETime.setText("");
                    }
                    break;
                case CHANGE_ENDTIME:
                    cEndTime = (GregorianCalendar)msg.obj;
                    ESendTime = cEndTime.getTimeInMillis();
                    if(cStartTime!=null) {
                        if (cEndTime.compareTo(cStartTime) >= 0) {
                            String timeEntered = String.format("%02d:%02d", cEndTime.get(Calendar.HOUR), cEndTime.get(Calendar.MINUTE));
                            showETime.setText(timeEntered);
                        } else {
                            Toast.makeText(main, "Invalid time selected", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        String timeEntered = String.format("%02d:%02d", cEndTime.get(Calendar.HOUR), cEndTime.get(Calendar.MINUTE));
                        showETime.setText(timeEntered);
                    }
                    break;
                case CHANGE_STARTDATE:
                    cStartDate = (GregorianCalendar)msg.obj;
                    EStartDate = cStartDate.getTimeInMillis();
                    if(cEndDate == null) {
                        String date = String.valueOf(cStartDate.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cStartDate.get(Calendar.MONTH)) + "/" + String.valueOf(cStartDate.get(Calendar.YEAR));
                        showSDate.setText(date);
                    }else if(cEndDate.compareTo(cStartDate)>0){
                        String date = String.valueOf(cStartDate.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cStartDate.get(Calendar.MONTH)) + "/" + String.valueOf(cStartDate.get(Calendar.YEAR));
                        showSDate.setText(date);
                    }else{
                        Toast.makeText(main, "Invalid date selected", Toast.LENGTH_SHORT).show();
                        cStartDate = null;
                        cEndDate = null;
                        showSDate.setText("");
                        showEDate.setText("");
                    }
                    break;
                case CHANGE_ENDDATE:
                    cEndDate = (GregorianCalendar)msg.obj;
                    ESendDate = cEndDate.getTimeInMillis();
                    if(cStartDate!=null) {
                        if (cEndDate.compareTo(cStartDate) >= 0) {
                            String date1 = String.valueOf(cEndDate.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cEndDate.get(Calendar.MONTH)) + "/" + String.valueOf(cEndDate.get(Calendar.YEAR));
                            showEDate.setText(date1);
                        } else {
                            Toast.makeText(main, "Invalid date selected", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        String date1 = String.valueOf(cEndDate.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cEndDate.get(Calendar.MONTH)) + "/" + String.valueOf(cEndDate.get(Calendar.YEAR));
                        showEDate.setText(date1);
                    }
                    break;
                case GROUPSELECTED:
                    int position = (int)msg.obj;
                    groupSelected.setText(MainPageActivity.groupList.get(position).getName());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);
        getSupportActionBar().hide();
        main = this;
        initialise();
        listeners();
    }

    public void initialise(){
        startTime = (Button)findViewById(R.id.button2);
        endTime = (Button)findViewById(R.id.button3);
        startDate = (Button)findViewById(R.id.button4);
        endDate = (Button)findViewById(R.id.button5);
        send = (Button)findViewById(R.id.button7);
        group = (Button)findViewById(R.id.button18);
        checkLocation = (Button)findViewById(R.id.button17);
        eventName = (EditText)findViewById(R.id.editText);
        eventDuration = (EditText)findViewById(R.id.editText2);
        venue = (EditText)findViewById(R.id.editText7);
        eventDetails = new HashMap<>();
        showSTime = (TextView)findViewById(R.id.textView);
        showETime = (TextView)findViewById(R.id.textView2);
        showSDate = (TextView)findViewById(R.id.textView3);
        showEDate = (TextView)findViewById(R.id.textView4);
        groupSelected = (TextView)findViewById(R.id.textView9);
    }

    public void listeners(){
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog alert = new TimePickerDialog();
                alert.showDialog(AddEventActivity.this,CHANGE_STARTTIME);
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog alert = new TimePickerDialog();
                alert.showDialog(AddEventActivity.this,CHANGE_ENDTIME);
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog alert = new DateDialog();
                alert.showDialog(AddEventActivity.this,CHANGE_STARTDATE);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog alert = new DateDialog();
                alert.showDialog(AddEventActivity.this,CHANGE_ENDDATE);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDetails.put("eventName",eventName.getText().toString());
                eventDetails.put("eventDuration",eventDuration.getText().toString());
                eventDetails.put("startTime",showSTime.getText().toString());
                eventDetails.put("endTime",showETime.getText().toString());
                eventDetails.put("startDate",showSDate.getText().toString());
                eventDetails.put("endDate",showEDate.getText().toString());
                eventDetails.put("venue",venue.getText().toString());
                eventDetails.put("groupGKey",groupSelected.getText().toString());
                Intent intent = new Intent(getApplicationContext(),SendEventActivity.class);
                intent.putExtra("eventDetails",eventDetails);
                startActivity(intent);
            }
        });
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupDialog alert = new GroupDialog();
                alert.showDialog(AddEventActivity.this);
            }
        });
    }
}

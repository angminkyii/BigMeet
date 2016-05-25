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

public class MainActivity extends AppCompatActivity {

    Button startTime;
    Button endTime;
    Button startDate;
    Button endDate;
    Button send;
    EditText eventName;
    EditText eventDuration;

    static TextView showTime;
    static TextView showETime;
    static TextView showSDate;
    static TextView showEDate;
    static GregorianCalendar obj;
    static GregorianCalendar obj1;
    static GregorianCalendar time;
    static GregorianCalendar time1;
    static Context main;
    static final int CHANGE_STARTTIME = 1;
    static final int CHANGE_ENDTIME = 2;
    static final int CHANGE_STARTDATE = 3;
    static final int CHANGE_ENDDATE = 4;

    static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case CHANGE_STARTTIME:
                    time = (GregorianCalendar)msg.obj;
                    String timer = String.valueOf(time.get(Calendar.HOUR))+":"+String.valueOf(time.get(Calendar.MINUTE));
                    showTime.setText(timer);
                    break;
                case CHANGE_ENDTIME:
                    time1 = (GregorianCalendar)msg.obj;
                    if(compareGregorian(time1,time,"Time")==true){
                        String timeEntered = String.valueOf(time1.get(Calendar.HOUR))+":"+String.valueOf(time1.get(Calendar.MINUTE));
                        showETime.setText(timeEntered);
                    }else{
                        Toast.makeText(main, "Invalid time selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CHANGE_STARTDATE:
                    obj = (GregorianCalendar)msg.obj;
                    String date = String.valueOf(obj.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(obj.get(Calendar.MONTH))+"/"+String.valueOf(obj.get(Calendar.YEAR));
                    showSDate.setText(date);
                    break;
                case CHANGE_ENDDATE:
                    obj1 = (GregorianCalendar)msg.obj;
                    if(compareGregorian(obj1,obj,"Date")==true) {
                        String date1 = String.valueOf(obj1.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(obj1.get(Calendar.MONTH)) + "/" + String.valueOf(obj1.get(Calendar.YEAR));
                        showEDate.setText(date1);
                    }else{
                        Toast.makeText(main,"Invalid date selected",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        main = this;
        startTime = (Button)findViewById(R.id.button2);
        endTime = (Button)findViewById(R.id.button3);
        startDate = (Button)findViewById(R.id.button4);
        endDate = (Button)findViewById(R.id.button5);
        send = (Button)findViewById(R.id.button7);
        eventName = (EditText)findViewById(R.id.editText);
        eventDuration = (EditText)findViewById(R.id.editText2);
        showTime = (TextView)findViewById(R.id.textView);
        showETime = (TextView)findViewById(R.id.textView2);
        showSDate = (TextView)findViewById(R.id.textView3);
        showEDate = (TextView)findViewById(R.id.textView4);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog1 alert = new ViewDialog1();
                alert.showDialog(MainActivity.this);
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog2 alert = new ViewDialog2();
                alert.showDialog(MainActivity.this);
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog alert = new DateDialog();
                alert.showDialog(MainActivity.this,CHANGE_STARTDATE);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog alert = new DateDialog();
                alert.showDialog(MainActivity.this,CHANGE_ENDDATE);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataOver = eventName.getText()+"."+eventDuration.getText()+"."+showTime.getText()+"."+showETime.getText()+"."+showSDate.getText()+"."+showEDate.getText();
                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("eventDetails",dataOver);
                startActivity(intent);
            }
        });
    }

    public static boolean compareGregorian(GregorianCalendar cal1, GregorianCalendar cal2, String object){

        switch(object){
            case "Date":
                if(cal1!=null && cal2!=null) {
                    if (cal1.get(Calendar.YEAR) >= cal2.get(Calendar.YEAR)) {
                        if (cal1.get(Calendar.MONTH) >= cal2.get(Calendar.MONTH)) {
                            if (cal1.get(Calendar.DAY_OF_MONTH) >= cal2.get(Calendar.DAY_OF_MONTH)) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case "Time":
                if(cal1!=null && cal2!=null) {
                    if (cal1.get(Calendar.HOUR) >= cal2.get(Calendar.HOUR)) {
                        if (cal1.get(Calendar.MINUTE) >= cal2.get(Calendar.MINUTE)) {
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }
}

package com.example.minky.bigmeet;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.GregorianCalendar;

public class DateDialog {

    DatePicker dp;
    Button dialogButton;
    EditText et;
    int month;
    int day;
    int year;
    GregorianCalendar date;
    public void showDialog(Activity activity, final int id){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_date);


        dp = (DatePicker)dialog.findViewById(R.id.datePicker);
        dialogButton = (Button)dialog.findViewById(R.id.button6);


        dp.setSpinnersShown(false);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = dp.getDayOfMonth();
                month = dp.getMonth()+1;
                year = dp.getYear();
                date = new GregorianCalendar(year,month,day);
                AddEventActivity.mHandler.obtainMessage(id,date).sendToTarget();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

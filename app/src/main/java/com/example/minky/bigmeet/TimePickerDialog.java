package com.example.minky.bigmeet;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by minky on 21/04/2016.
 */
public class TimePickerDialog {

    int timeHour;
    int timeMinute;

    public void showDialog(Activity activity, final int id){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_signin);


        final TimePicker text = (TimePicker) dialog.findViewById(R.id.timePicker2);

        Button dialogButton = (Button) dialog.findViewById(R.id.button);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getCurrentHour() != null) {
                    timeHour = text.getCurrentHour();
                    timeMinute = text.getCurrentMinute();
                    //endTime = String.valueOf(endHour) + ":" + String.valueOf(endMinute);
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.set(Calendar.HOUR,timeHour);
                    gc.set(Calendar.MINUTE,timeMinute);
                    AddEventActivity.mHandler.obtainMessage(id, gc).sendToTarget();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

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
public class ViewDialog2 {

    int endHour;
    int endMinute;
    String endTime;

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_signin);
        dialog.onBackPressed();

        final TimePicker text = (TimePicker) dialog.findViewById(R.id.timePicker2);

        Button dialogButton = (Button) dialog.findViewById(R.id.button);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getCurrentHour() != null) {
                    endHour = text.getCurrentHour();
                    endMinute = text.getCurrentMinute();
                    //endTime = String.valueOf(endHour) + ":" + String.valueOf(endMinute);
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.set(Calendar.HOUR,endHour);
                    gc.set(Calendar.MINUTE,endMinute);
                    MainActivity.mHandler.obtainMessage(MainActivity.CHANGE_ENDTIME, gc).sendToTarget();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

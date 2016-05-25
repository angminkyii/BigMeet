package com.example.minky.bigmeet;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.GregorianCalendar;

/**
 * Created by minky on 21/04/2016.
 */
public class ViewDialog1 {

    int startHour;
    int startMinute;
    int endHour;
    int endMinute;
    String startTime;
    String endTime;

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_signin);

        final TimePicker text = (TimePicker) dialog.findViewById(R.id.timePicker2);

        Button dialogButton = (Button) dialog.findViewById(R.id.button);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getCurrentHour()!=null) {
                    startHour = text.getCurrentHour();
                    startMinute = text.getCurrentMinute();
                    GregorianCalendar gc = new GregorianCalendar(0,0,0,startHour,startMinute);
                    MainActivity.mHandler.obtainMessage(MainActivity.CHANGE_STARTTIME,gc).sendToTarget();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

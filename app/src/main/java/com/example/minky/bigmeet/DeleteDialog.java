package com.example.minky.bigmeet;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.GregorianCalendar;

/**
 * Created by minky on 23/05/2016.
 */
public class DeleteDialog {

    Button okayButton;
    Button cancelButton;
    TextView textView;

    public void showDialog(Activity activity, final ImageView imageView){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_delete);

        okayButton = (Button) dialog.findViewById(R.id.button14);
        cancelButton = (Button)dialog.findViewById(R.id.button15);
        textView = (TextView)dialog.findViewById(R.id.textView6);
        textView.setText("Are you sure you want to delete this group?");
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPageActivity.toBeDeleted = true;
                imageView.performLongClick();
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

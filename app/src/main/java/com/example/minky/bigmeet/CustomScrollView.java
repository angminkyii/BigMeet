package com.example.minky.bigmeet;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;

/**
 * Created by minky on 19/05/2016.
 */
public class CustomScrollView extends HorizontalScrollView{
    public CustomScrollView(Context context) {
        super(context);
    }

    @Override
    public void fling(int velocityX) {
        System.out.println(String.valueOf(velocityX));
        if(velocityX > 4000){
            velocityX = 2000;
        }else if(velocityX > 1000) {
            velocityX = 1000;
        }else if(velocityX < -1000){
            velocityX = -1000;
        }else if(velocityX < -4000){
            velocityX = -2000;
        }
        super.fling(velocityX);
    }
}

package com.example.minky.bigmeet;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import static com.example.minky.bigmeet.FirebaseClient.*;

/**
 * Created by minky on 27/06/2016.
 */
public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    public String TAG = "MOVE";
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        printLog(TAG,"Tap registered");
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX()>e2.getX()) {
            printLog(TAG,"Right to left");
        }
        if(e2.getX()>e1.getX()) {
            printLog(TAG,"Left to right");
        }
        if(e1.getY()>e2.getY()) {
            printLog(TAG,"Down to up");
            return true;
        }
        if(e2.getY()>e1.getY()) {
            printLog(TAG,"Up to down");
            return true;
        }
        return false;
    }


}

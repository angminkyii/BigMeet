package com.example.minky.bigmeet;

/**
 * Created by minky on 28/06/2016.
 */
public class Header {
    public static final int STATUS_NEW = 100;
    public static final int STATUS_INCOMPLETE = 101;
    public static final int STATUS_COMPLETE = 102;
    public static final int SHADOW_ENABLED = 200;
    public static final int SHADOW_DISABLED = 201;
    public static final int DEFAULT_DURATION = 50000;
    public static int duration;

    public static int getDuration(){
        return duration;
    }

    public static void setDuration(int duration1){
        duration = duration1;
    }

}


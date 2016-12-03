package com.example.minky.bigmeet;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;

public class NotificationCentre extends Activity {

    ImageButton notifiCentre;
    GestureListener gestureListener;
    GestureDetector gestureDetector;
    RecyclerView recyclerView;
    List<Nottification> notificationList;
    NotificationAdapter adapter;
    SwipeToAction swipeToAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_notification_centre);
        getActionBar().hide();
        initialise();
        listeners();
        populate();
    }

    public void initialise() {
        notifiCentre = (ImageButton) findViewById(R.id.button19);
        gestureListener = new GestureListener();
        gestureDetector = new GestureDetector(this,gestureListener);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        notificationList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);
        swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<Nottification>() {
            @Override
            public boolean swipeLeft(Nottification itemData) {
                return false;
            }

            @Override
            public boolean swipeRight(Nottification itemData) {
                return false;
            }

            @Override
            public void onClick(Nottification itemData) {

            }

            @Override
            public void onLongClick(Nottification itemData) {

            }
        });
    }

    public void listeners(){
        notifiCentre.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(gestureDetector.onTouchEvent(event)==true){
                    notifiCentre.performClick();
                };
                return false;
            }
        });
        notifiCentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stay,R.anim.slide_in_down);
            }
        });
    }

    private int removeNotification(Nottification notification) {
        int pos = notificationList.indexOf(notification);
        notificationList.remove(notification);
        adapter.notifyItemRemoved(pos);
        return pos;
    }

    private void addNotification(int pos, Nottification notification) {
        notificationList.add(pos, notification);
        adapter.notifyItemInserted(pos);
    }

    private void populate() {
        this.notificationList.add(new Nottification("Invitation","You've got a new invitation"));
        this.notificationList.add(new Nottification("Friend Request","You've got a new request"));
        this.notificationList.add(new Nottification("Friend Request","You've got a new request"));
        this.notificationList.add(new Nottification("Friend Request","You've got a new request"));
        this.notificationList.add(new Nottification("Invitation","You've got a new invitation"));
    }
    @Override
    public void onBackPressed() {
        notifiCentre.performClick();
    }
}

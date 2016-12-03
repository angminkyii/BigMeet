package com.example.minky.bigmeet;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.minky.bigmeet.Auth.EmailPasswordActivity;
import com.example.minky.bigmeet.Notification.GestureListener2;
import com.mikhaellopez.circularimageview.CircularImageView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.minky.bigmeet.Header.*;


public class MainPageActivity extends AppCompatActivity {

    ImageButton notifiCentre;
    CustomScrollView horizontalScrollView;
    LinearLayout imageLinear;
    LinearLayout linearLayout;
    ArrayList<ImageView> imageList;
    Button addGroup;
    Button createEvent;
    ObjectAnimator objectAnimator;
    Button contactBook;
    Runnable runnable;
    Boolean check = false;
    Map<String, ImageView> details;
    static DatabaseGroupHandler groupDB;
    static DatabaseContactHandler contactDB;
    List<Contact> contactList;
    static List<Group> groupList;
    static Boolean toBeDeleted = false;
    static final int ADD_GROUP_ACTIVITY = 10;
    GestureListener gestureListener;
    GestureListener2 swipeLeft;
    GestureDetector swipeLeffDetector;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //Hide action bar
        getSupportActionBar().hide();
        //Important, for database
        groupDB = new DatabaseGroupHandler(this);
        contactDB = new DatabaseContactHandler(this);
        initialise();
        listeners();
    }

    /**
     * Add space between two circular images, needed because the layout is set up programmatically.
     * Layout is not defined by default when used this way.
     * @param x width of space
     * @param y height of space
     * @return
     */
    public Space addSpace(int x, int y){
        Space spaceView = new Space(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(x,y);
        spaceView.setLayoutParams(layoutParams);
        return spaceView;
    }

    public CircularImageView generateCircImageView(int type, int status){
        CircularImageView circleImageView = new CircularImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(240, 240);
        circleImageView.setLayoutParams(layoutParams);
        circleImageView.setBorderWidth(4);
        if(status == STATUS_NEW) {
            circleImageView.setBorderColor(Color.WHITE);
        }else if(status == STATUS_INCOMPLETE){
            circleImageView.setBorderColor(Color.YELLOW);
        }else if(status == STATUS_COMPLETE){
            circleImageView.setBorderColor(Color.GREEN);
        }else {
            circleImageView.setBorderColor(Color.RED);
        }
        if(type == SHADOW_ENABLED) {
            circleImageView.setShadowColor(Color.BLACK);
            circleImageView.setShadowRadius(8);
        }
        return circleImageView;
    }
    /**
     * To add view (ImageView or SpaceView dynamically) i.e not via the xml files.
     * A hashmap is needed to store all the views added, able to track them later.
     * @param name Name of the group to be added
     * @param groupPhoto   Photo of group to be added (Selected from gallery before this function)
     */
    public void dynamicAdd(final String name, byte[] groupPhoto, CircularImageView circularImageView){
        details.put(name,circularImageView);
        Drawable image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(groupPhoto, 0, groupPhoto.length));
        details.get(name).setImageDrawable(image);
        imageLinear.addView(details.get(name));
        imageLinear.addView(addSpace(30,240));
        refreshView();

        details.get(name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change function here to do something else (Check group info or create an event with this group)
                Intent intent = new Intent(getApplicationContext(),GroupInfoActivity.class);
                intent.putExtra("groupInfo",name);
                startActivity(intent);
            }
        });

        //Long click is to delete a specific group
        details.get(name).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //this flag here is to prompt a confirmation dialog first before delete
                if(toBeDeleted == true) {
                    //imageLinear is a linearlayout which contains all the imageviews and spaceviews.
                    //Do not get confused with linearlayout
                    imageLinear.removeAllViews();
                    //Database for group (need to be removed from the database as well)
                    groupDB.deleteGroup(groupDB.getGroup(name, (ArrayList<Contact>) contactList));
                    //remove all groups from array
                    groupList.removeAll(groupList);
                    //Retrieve again from database
                    retrieveFromDatabase();
                    //Add groups into array again
                    initiateGroup(groupList);
                    //Refresh the horizontal Scroll View again
                    refreshView();
                    toBeDeleted = false;
                }else{
                    DeleteDialog alert = new DeleteDialog();
                    alert.showDialog(MainPageActivity.this,details.get(name));
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"Sign out");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                FirebaseClient.appLogout();
                Intent intent = new Intent(getApplicationContext(), EmailPasswordActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void refreshView(){
        horizontalScrollView.removeAllViews();;
        horizontalScrollView.addView(imageLinear);
    }

    @Override
    protected void onPause() {
        notifiCentre.setVisibility(View.INVISIBLE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        notifiCentre.setVisibility(View.VISIBLE);
        super.onResume();
    }

    public void initialise(){
        notifiCentre = (ImageButton)findViewById(R.id.dots);
        gestureListener = new GestureListener();
        swipeLeft = new GestureListener2();
        swipeLeffDetector = new GestureDetector(this,swipeLeft);
        gestureDetector = new GestureDetector(this,gestureListener);
        addGroup = (Button)findViewById(R.id.button10);
        contactBook = (Button)findViewById(R.id.button13);
        createEvent = (Button)findViewById(R.id.button20);
        details = new HashMap<>();
        horizontalScrollView = new CustomScrollView(this);

        //linearlayout is a LinearLayout containing the horizontalScrollView
        linearLayout = (LinearLayout)findViewById(R.id.layer);
        horizontalScrollView.setSmoothScrollingEnabled(true);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        horizontalScrollView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        linearLayout.addView(horizontalScrollView);

        //This LinearLayout contains all imageViews and SpaceViews. This is to be put into Horizontal Scroll View
        imageLinear = new LinearLayout(this);
        imageLinear.setOrientation(LinearLayout.HORIZONTAL);
        imageList = new ArrayList<>();
        contactList = new ArrayList<>();
        groupList = new ArrayList<>();
        retrieveFromDatabase();
        initiateGroup(groupList);
        //Starts animation of slow scrolling
        setDuration(DEFAULT_DURATION);
        slowScroll(getDuration());
    }

    public void retrieveFromDatabase(){
        contactList = contactDB.getAllContacts();
        groupList = groupDB.getAllGroups((ArrayList<Contact>) contactList);
    }

    public void initiateGroup(List<Group> groupsList){
        imageLinear.addView(addSpace(30,240));
        for(int i=0;i<groupsList.size();i++){
            dynamicAdd(groupsList.get(i).getName(),groupsList.get(i).getGroupPhoto(),generateCircImageView(SHADOW_ENABLED,STATUS_COMPLETE));
        }
    }

    public void listeners(){
        notifiCentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NotificationCentre.class);
                startActivity(intent);
                notifiCentre.setVisibility(View.INVISIBLE);
                overridePendingTransition(R.anim.slide_in_up,R.anim.stay);
            }
        });

        notifiCentre.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(gestureDetector.onTouchEvent(event)){
                    notifiCentre.performClick();
                }
                return false;
            }
        });
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddGroupActivity.class);
                startActivityForResult(intent,ADD_GROUP_ACTIVITY);
            }
        });

        contactBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ContactActivity.class);
                startActivity(intent);
            }
        });

        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(objectAnimator.isRunning()) {
                    objectAnimator.cancel();
                }
                return false;
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddEventActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This method is scroll the horizontal Scroll View to the end of the view when a new group is added
     * To focus on the group just added
     */
    public void scrollToEnd(){
        horizontalScrollView.postDelayed(new Runnable() {
            public void run() {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);
    }

    /**
     * Animation of slow scrolling. Change duration to change the speed of scrolling
     * @param duration Speed of scrolling
     */
    public void slowScroll(final int duration){
        int destination = 0;
        if(check == false){
            //This destination is set this way because there is no way to tell the end of View.
            //Estimate by the size of each photo added and the space in between
            destination = (groupList.size()-1)*260;
            check = true;
        }else{
            check = false;
        }
        objectAnimator = ObjectAnimator.ofInt(horizontalScrollView, "scrollX",destination);
        objectAnimator.setDuration(duration).start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                slowScroll(duration);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //This is the only way to stop animation by removing all listeners.
                //Don't remove this.
                objectAnimator.removeAllListeners();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        new CountDownTimer(3000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                if(check == true){
                                    check = false;
                                }else{
                                    check = true;
                                }
                                slowScroll(duration);
                            }
                        }.start();
                    }
                };
                runnable.run();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(swipeLeffDetector.onTouchEvent(event)){
            Intent intent = new Intent(getApplicationContext(),ViewPageActivity.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK ){
            if(requestCode == ADD_GROUP_ACTIVITY && data != null){
                Bundle b = data.getExtras();
                byte[] byteArray = b.getByteArray("overlay");
                String groupName = b.getString("groupName");
                //Create a new group when returning from creating group activity
                Group group = new Group(groupName,(ArrayList<Contact>)contactList, byteArray);
                groupDB.addGroup(group);
                groupList.add(group);
                //Add to horizontal Scroll View
                dynamicAdd(group.getName(),group.getGroupPhoto(),generateCircImageView(SHADOW_ENABLED,STATUS_NEW));
                //Cancel animation
                objectAnimator.cancel();
                scrollToEnd();
            }
        }
    }
}

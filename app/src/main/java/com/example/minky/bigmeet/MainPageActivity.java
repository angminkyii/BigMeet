package com.example.minky.bigmeet;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.widget.Space;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainPageActivity extends AppCompatActivity {

    //Need a static method to store group and get called every time this activity starts
    ImageButton createEvent;
    CustomScrollView horizontalScrollView;
    LinearLayout imageLinear;
    LinearLayout linearLayout;
    ArrayList<ImageView> imageList;
    Button addGroup;
    Button contactBook;
    Map<String, ImageView> details;
    static DatabaseGroupHandler groupDB;
    static DatabaseContactHandler contactDB;
    List<Contact> contactList;
    List<Group> groupList;
    static Boolean toBeDeleted = false;
    static final int ADD_GROUP_ACTIVITY = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getSupportActionBar().hide();
        groupDB = new DatabaseGroupHandler(this);
        contactDB = new DatabaseContactHandler(this);
        initialise();
        listeners();
    }

    public void dynamicAdd(String groupName, byte[] groupPhoto){
        createVariable(groupName, groupPhoto);
    }

    public Space addSpace(int x, int y){
        Space spaceView = new Space(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(x,y);
        spaceView.setLayoutParams(layoutParams);
        return spaceView;
    }
    public void createVariable(final String name, byte[] groupPhoto){
        details.put(name,new ImageView(this));
        Drawable image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(groupPhoto, 0, groupPhoto.length));
        details.get(name).setImageDrawable(image);
        imageLinear.addView(details.get(name));
        imageLinear.addView(addSpace(30,240));
        refreshView();

        details.get(name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent.performClick();
            }
        });

        details.get(name).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(toBeDeleted == true) {
                    imageLinear.removeView(details.get(name));
                    refreshView();
                    groupDB.deleteGroup(groupDB.getGroup(name, (ArrayList<Contact>) contactList));
                    toBeDeleted = false;
                }else{
                    DeleteDialog alert = new DeleteDialog();
                    alert.showDialog(MainPageActivity.this,details.get(name));
                }
                return true;
            }
        });
    }

    public void refreshView(){
        horizontalScrollView.removeAllViews();;
        horizontalScrollView.addView(imageLinear);
    }
    public void initialise(){
        createEvent = (ImageButton)findViewById(R.id.imageButton2);
        addGroup = (Button)findViewById(R.id.button10);
        contactBook = (Button)findViewById(R.id.button13);
        details = new HashMap<>();
        horizontalScrollView = new CustomScrollView(this);
        linearLayout = (LinearLayout)findViewById(R.id.layer);
        horizontalScrollView.setSmoothScrollingEnabled(true);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        horizontalScrollView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        linearLayout.addView(horizontalScrollView);
        imageLinear = new LinearLayout(this);
        imageLinear.setOrientation(LinearLayout.HORIZONTAL);
        imageList = new ArrayList<>();
        contactList = new ArrayList<>();
        groupList = new ArrayList<>();
        contactList = contactDB.getAllContacts();
        groupList = groupDB.getAllGroups((ArrayList<Contact>) contactList);
        initiateGroup(groupList);
        slowScroll(900);
    }

    public void initiateGroup(List<Group> groupsList){
        for(int i=0;i<groupsList.size();i++){
            dynamicAdd(groupsList.get(i).getName(),groupsList.get(i).getGroupPhoto());
        }
    }

    public void listeners(){
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
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
    }

    public void scrollToEnd(){
        horizontalScrollView.postDelayed(new Runnable() {
            public void run() {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);
    }

    public void slowScroll(int duration){
        ObjectAnimator.ofInt(horizontalScrollView, "scrollX",  horizontalScrollView.getBottom()).setDuration(duration).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK ){
            if(requestCode == ADD_GROUP_ACTIVITY && data != null){
                byte[] byteArray = data.getByteArrayExtra("overlay");
                Group group = new Group("Group"+new Random().nextInt(1000),(ArrayList<Contact>)contactList, byteArray);
                groupDB.addGroup(group);
                dynamicAdd(group.getName(),group.getGroupPhoto());
                scrollToEnd();
            }
        }
    }
}

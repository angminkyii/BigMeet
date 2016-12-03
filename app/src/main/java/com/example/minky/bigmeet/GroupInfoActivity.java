package com.example.minky.bigmeet;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.example.minky.bigmeet.MainPageActivity.*;

public class GroupInfoActivity extends AppCompatActivity {

    String groupInfo;
    ImageView groupPhoto;
    TextView groupName;
    Group thisGroup;
    ListView members;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        getSupportActionBar().hide();
        initialise();
    }

    public void initialise(){
        groupInfo = getIntent().getStringExtra("groupInfo");
        groupPhoto = (ImageView)findViewById(R.id.groupPhoto);
        groupName = (TextView)findViewById(R.id.groupName);
        members = (ListView)findViewById(R.id.listView4);
        members.setAdapter(mAdapter);
        groupName.setText(groupInfo);
        thisGroup = getGroup(groupInfo);
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,thisGroup.getMembersInString());
        Drawable image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(thisGroup.getGroupPhoto(), 0, thisGroup.getGroupPhoto().length));
        groupPhoto.setImageDrawable(image);
    }

    public Group getGroup(String name){
        int count=0;
        for(int i=0;i<groupList.size();i++){
            if(groupList.get(i).getName().equals(name)){
                count = i;           }
        }
        return groupList.get(count);
    }
}

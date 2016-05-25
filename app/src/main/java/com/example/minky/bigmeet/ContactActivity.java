package com.example.minky.bigmeet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class ContactActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> lAdapter;
    DatabaseContactHandler db;
    Button addFriend;
    Button search;
    EditText friendQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        db = new DatabaseContactHandler(this);
        initialise();
        listeners();
    }

    public void initialise(){
        addFriend = (Button)findViewById(R.id.button11);
        search = (Button)findViewById(R.id.button12);
        friendQuery = (EditText)findViewById(R.id.editText5);
        listView = (ListView)findViewById(R.id.listView2);
        lAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(lAdapter);
        List<Contact> contactList = db.getAllContacts();

        for(Contact cn : contactList){
            lAdapter.add(cn.getName());
        }
    }

    public void listeners(){
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "Friend"+ new Random().nextInt(10000);
                String emailAddress = new Random().nextInt() + "@gmail.com";
                Contact contact = new Contact(name,emailAddress);
                db.addContact(contact);
                lAdapter.add(contact.getName());
                lAdapter.notifyDataSetChanged();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friendQuery.getText()!=null){
                    Contact contact = db.getContact(friendQuery.getText().toString());
                    if(contact != null){
                        Toast.makeText(getApplicationContext(),contact.getName() + " found",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Contact not found",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

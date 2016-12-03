package com.example.minky.bigmeet;

import android.content.Intent;
import android.util.Log;

import com.example.minky.bigmeet.Auth.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

/**
 * Created by minky on 23/06/2016.
 */
public class FirebaseClient {
    public static FirebaseDatabase firebaseDatabse;
    public static DatabaseReference myRef;
    public static FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static boolean loggedIn = false;

    public static void appLogout() {
        loggedIn = false;
        FirebaseUser userProfile = mAuth.getCurrentUser();
        myRef = firebaseDatabse.getReference("Users").child(userProfile.getUid());
        myRef.child("notificationToken").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser userProfile = mAuth.getCurrentUser();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getValue().equals(FirebaseInstanceId.getInstance().getToken())){
                        firebaseDatabse.getReference("Users")
                                .child(userProfile.getUid())
                                .child("notificationToken")
                                .child(String.valueOf(child.getKey()))
                                .removeValue();
                    }
                }
                mAuth.signOut();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                loggedIn = true;
            }
        });
    }

    public static void appLogin() {
        FirebaseUser userProfile = mAuth.getCurrentUser();
        //Log.d(TAG,"Uid: " + userProfile.getUid());
        //Log.d(TAG,"Email: " + userProfile.getEmail());
        //Log.d(TAG,"token: " + FirebaseInstanceId.getInstance().getToken());
        firebaseDatabse = FirebaseDatabase.getInstance();
        myRef = firebaseDatabse.getReference("Users");



        HashMap<String, String> notificationTokenMap=new HashMap<String, String>();
        notificationTokenMap.put("1",FirebaseInstanceId.getInstance().getToken());

        UserProfile user = new UserProfile(userProfile.getEmail(), notificationTokenMap);
        myRef.child("TEST").setValue(user);

        HashMap<String, String> userMap=new HashMap<String, String>();
        userMap.put("email",userProfile.getEmail());

        myRef.child(userProfile.getUid()).setValue(userMap);
        myRef.child(userProfile.getUid()).child("notificationToken").setValue(notificationTokenMap);
        //Log.d(TAG,"User profile written successfully");
        loggedIn = true;
    }



                /*if (numberOfChildren > 1) {
                    HashMap meMap = (HashMap) dataSnapshot.getValue();  //convert it to HashMap
                    Iterator myVeryOwnIterator = meMap.keySet().iterator();     //Iterate through the contents of the HashMap
                    while(myVeryOwnIterator.hasNext()) {
                        String key=(String)myVeryOwnIterator.next();
                        String value=(String)meMap.get(key);
                        Log.d(TAG, "Key: "+key+" Value: "+value);
                        if (value.equals(FirebaseInstanceId.getInstance().getToken())){
                            Log.d(TAG, "Need to delete Key: " + key);
                            database.getReference("Users")
                                    .child(userProfile.getUid())
                                    .child("notificationToken")
                                    .child(key)
                                    .removeValue();
                        }
                    }
                } else {

                }*/
    public static void printLog(String tag, String message){
        Log.d(tag,message);
    }
}

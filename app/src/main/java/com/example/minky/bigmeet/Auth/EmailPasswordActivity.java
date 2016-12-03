package com.example.minky.bigmeet.Auth;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minky.bigmeet.MainPageActivity;
import com.example.minky.bigmeet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import static com.example.minky.bigmeet.FirebaseClient.*;
import static com.example.minky.bigmeet.FirebaseClient.firebaseDatabse;


/*
public class EmailPasswordActivity extends BaseActivity implements
        View.OnClickListener {
 */
public class EmailPasswordActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private static final int TRUE = 1;
    private static final int FALSE = 2;
    private static final int CANCEL = 3;

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private int firstClickedCreateAcc;
    private int firstClickedSignIn;
    private Button createAcc;
    private Button logIn;
    private int buttonWidth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);
        getSupportActionBar().hide();
        initialise();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && loggedIn == true) {
            // User is signed in
            Intent intent = new Intent(getApplicationContext(),MainPageActivity.class);
            startActivity(intent);
            finish();
        } else {
            // No user is signed in
            //For development purposes
            autoSignIn();
        }

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    autoSignIn();
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
    }

    public void autoSignIn(){
        mPasswordField.setText("qwer!@#$");
        mEmailField.setText("benjbroomfield@gmail.com");
        logIn.performClick();
        logIn.performClick();
    }
    public void initialise(){
        // Views
        firstClickedCreateAcc = TRUE;
        firstClickedSignIn = TRUE;
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mEmailField.setVisibility(View.INVISIBLE);
        mPasswordField.setVisibility(View.INVISIBLE);
        // Buttons
        logIn = (Button)findViewById(R.id.email_sign_in_button);
        logIn.setOnClickListener(this);
        createAcc = (Button)findViewById(R.id.email_create_account_button);
        createAcc.setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabse = FirebaseDatabase.getInstance();

        //Use this to get the width of button after layout has been laid
        mEmailField.post(new Runnable() {
            @Override
            public void run() {
                buttonWidth = mEmailField.getWidth();
            }
        });
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    public AnimationSet getAnimation(String inOrOut, double duration){
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setInterpolator(new AccelerateInterpolator());
        in.setDuration((long)duration*1000);
        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setInterpolator(new AccelerateInterpolator());
        out.setDuration((long)duration*1000);
        AnimationSet animationSet = new AnimationSet(false);
        switch (inOrOut){
            case "in":
                animationSet.addAnimation(in);
                break;
            case "out":
                animationSet.addAnimation(out);
                break;
        }
        return animationSet;
    }
    private void createAccount(final String email) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String password = Long.toHexString(Double.doubleToLongBits(Math.random()));
        System.out.println(password);
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mPasswordField.setEnabled(true);
                            firstClickedCreateAcc = TRUE;
                            firstClickedSignIn = TRUE;
                        } else if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"An email has been sent fo verification",Toast.LENGTH_SHORT).show();
                            //appLogin();
                            mAuth.sendPasswordResetEmail(email);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            firstClickedSignIn = TRUE;
                            firstClickedCreateAcc = TRUE;
                        } else if (task.isSuccessful()) {
                            appLogin();
                            Intent intent = new Intent(getApplicationContext(),MainPageActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        appLogout();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        if(mPasswordField.isEnabled()) {
            String password = mPasswordField.getText().toString();
            if (TextUtils.isEmpty(password)) {
                mPasswordField.setError("Required.");
                valid = false;
            } else {
                mPasswordField.setError(null);
            }
        }
        return valid;
    }

/*    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_create_account_button:
                if(firstClickedCreateAcc == TRUE){
                    mEmailField.setVisibility(View.INVISIBLE);
                    mPasswordField.setVisibility(View.INVISIBLE);
                    mEmailField.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mEmailField.getHeight()));
                    mEmailField.startAnimation(getAnimation("in",1.5));
                    mEmailField.setVisibility(View.VISIBLE);
                    logIn.setText("CANCEL");
                    createAcc.setVisibility(View.INVISIBLE);
                    logIn.setVisibility(View.INVISIBLE);
                    createAcc.startAnimation(getAnimation("in",1.5));
                    logIn.startAnimation(getAnimation("in",1.5));
                    logIn.setVisibility(View.VISIBLE);
                    createAcc.setVisibility(View.VISIBLE);
                    firstClickedCreateAcc = FALSE;
                    firstClickedSignIn = CANCEL;
                }else if(firstClickedCreateAcc == FALSE){
                    mPasswordField.setEnabled(false);
                    createAccount(mEmailField.getText().toString());
                }else{
                    createAcc.setText("CREATE ACCOUNT");
                    createAcc.setVisibility(View.INVISIBLE);
                    logIn.setVisibility(View.INVISIBLE);
                    mEmailField.setVisibility(View.INVISIBLE);
                    mPasswordField.setVisibility(View.INVISIBLE);
                    createAcc.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth,createAcc.getHeight()));
                    createAcc.setAnimation(getAnimation("in",1.5));
                    logIn.setAnimation(getAnimation("in",1.5));
                    createAcc.setVisibility(View.VISIBLE);
                    logIn.setVisibility(View.VISIBLE);
                    firstClickedCreateAcc = TRUE;
                    firstClickedSignIn = TRUE;
                }
                break;
            case R.id.email_sign_in_button:
                if(firstClickedSignIn == TRUE) {
                    mEmailField.setVisibility(View.INVISIBLE);
                    mPasswordField.setVisibility(View.INVISIBLE);
                    mEmailField.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth,mEmailField.getHeight()));
                    mEmailField.startAnimation(getAnimation("in",1.5));
                    mPasswordField.startAnimation(getAnimation("in",1.5));
                    mEmailField.setVisibility(View.VISIBLE);
                    mPasswordField.setVisibility(View.VISIBLE);
                    createAcc.setText("Cancel");
                    createAcc.setVisibility(View.INVISIBLE);
                    logIn.setVisibility(View.INVISIBLE);
                    createAcc.startAnimation(getAnimation("in",1.5));
                    logIn.startAnimation(getAnimation("in",1.5));
                    logIn.setVisibility(View.VISIBLE);
                    createAcc.setVisibility(View.VISIBLE);
                    firstClickedSignIn = FALSE;
                    firstClickedCreateAcc = CANCEL;
                }else if(firstClickedSignIn == FALSE){
                    signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                }else{
                    logIn.setText("SIGN IN");
                    logIn.setVisibility(View.INVISIBLE);
                    createAcc.setVisibility(View.INVISIBLE);
                    mEmailField.setVisibility(View.INVISIBLE);
                    mPasswordField.setVisibility(View.INVISIBLE);
                    logIn.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth,createAcc.getHeight()));
                    logIn.setAnimation(getAnimation("in",1.5));
                    createAcc.setAnimation(getAnimation("in",1.5));
                    createAcc.setVisibility(View.VISIBLE);
                    logIn.setVisibility(View.VISIBLE);
                    firstClickedCreateAcc = TRUE;
                    firstClickedSignIn = TRUE;
                }
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    @Override
    public void onDestroy() {
        firstClickedCreateAcc = TRUE;
        firstClickedSignIn = TRUE;
        super.onDestroy();
    }
}
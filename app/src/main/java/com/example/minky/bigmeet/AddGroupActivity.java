package com.example.minky.bigmeet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularimageview.CircularImageView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.minky.bigmeet.FirebaseClient.*;
import static com.example.minky.bigmeet.MainPageActivity.*;

public class AddGroupActivity extends AppCompatActivity {

    CircularImageView groupPhoto;
    Button done;
    EditText groupName;
    Bitmap groupPhotoBm;
    Bitmap photo2BSent;
    ArrayAdapter<String> dataAdapter;
    Spinner friendsList;
    List<Contact> contactList;
    static final int RESULT_LOAD_IMG = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        getSupportActionBar().hide();
        initialise();
        listeners();
    }

    public void initialise(){
        groupPhoto = (CircularImageView) findViewById(R.id.imageView3);
        groupPhoto.setShadowRadius(3);
        groupPhoto.setShadowColor(Color.BLACK);
        groupPhoto.setBorderColor(Color.WHITE);
        groupPhoto.setBorderWidth(3);
        groupPhoto.requestLayout();
        groupPhoto.getLayoutParams().height = 300;
        groupPhoto.getLayoutParams().width = 300;
        groupPhoto.setImageResource(R.drawable.quickmeet);
        done = (Button)findViewById(R.id.button16);
        contactList = new ArrayList<>(contactDB.getAllContacts());
        friendsList = (Spinner)findViewById(R.id.spinner);
        dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, convertContact2String(contactList));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friendsList.setAdapter(dataAdapter);
        groupName = (EditText)findViewById(R.id.editText6);
    }

    public String[] convertContact2String(List<Contact> contactList){
        String[] contacts = new String[contactList.size()];

        for(int i=0;i<contactList.size();i++){
            contacts[i] = contactList.get(i).getName();
        }
        return contacts;
    }

    public String pushGroupDetails(){
        myRef = firebaseDatabse.getReference("Groups");
        DatabaseReference pushRef = myRef.push();
        DatabaseReference keyPath = myRef.child(pushRef.getKey());
        //Insert group info here to be uploaded onto Firebase

        keyPath.setValue("");
        return pushRef.getKey();
    }

    public void listeners(){
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupPhotoBm != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    groupPhotoBm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Bundle bundle = new Bundle();
                    byte[] byteArray = stream.toByteArray();
                    Intent intent = new Intent();
                    bundle.putByteArray("overlay",byteArray);
                    bundle.putString("groupName",groupName.getText().toString());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Please pick an image :)",Toast.LENGTH_SHORT).show();
                }
            }
        });

        groupPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMG);
            }
        });

        friendsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),contactList.get(position).getName()+ " selected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Method to get circular bitmap
     * @param bitmap
     * @return
     */
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        paint.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        //canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 240, 240, false);
        //return _bmp;
        return output;
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            int rotate = getCameraPhotoOrientation(this,selectedImage,selectedImage.getPath());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bitmap != null) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                bitmap = getCroppedBitmap(bitmap);
                groupPhotoBm = Bitmap.createScaledBitmap(bitmap,240,240,false);
                groupPhoto.setImageBitmap(bitmap);
            }
        }
    }
}

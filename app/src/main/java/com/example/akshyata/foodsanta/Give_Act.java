package com.example.akshyata.foodsanta;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Give_Act extends AppCompatActivity {
    FloatingActionButton f_cam;
    //Uri selected_image;
    private StorageReference mStorageRef;
    ProgressBar pb;
    ObjectAnimator animation;
    private static final int PERMISSION_REQUEST_CODE = 200;
    static final int REQUEST_TAKE_PHOTO = 1;


    String mCurrentPhotoPath;
    Uri photoURI;
    Uri uri;

    private File createImageFile() throws IOException {
// Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = getExternalFilesDir("Pictures");
        if(!storageDir.exists())
        {
            if (!storageDir.mkdirs()){
                Log.e("noDir","No directory created ");
            }
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

// Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
// Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                /* photoURI = FileProvider.getUriForFile(this,
                        "com.example.akshyata.foodsanta.android.fileprovider",
                        photoFile);*/
                 photoURI = Uri.fromFile(photoFile);    //no fil proviers added
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_);

        if (checkPermission()) {
            //main logic or main code

            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            requestPermission();
        }

        mStorageRef = FirebaseStorage.getInstance().getReference().child("give");
        //firebase database

        //progress
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);
        animation = ObjectAnimator.ofInt(R.id.progressBar, "progress", 0, 100);
        // see this max value coming back here, we animate towards that value
        animation.setDuration(5000); // in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());

        //progress end

        f_cam = (FloatingActionButton) findViewById(R.id.camera);
        f_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
               /* Intent int_cam = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //int_cam.setType("image/*");
                File file = new File(Environment.DIRECTORY_DCIM);
                if(!file.exists())
                {
                    file.mkdirs();
                }

                startActivityForResult(int_cam,100);
                */
            }
        });
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, AccessibilityService.CAMERA_SERVICE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(Give_Act.this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE},
                PERMISSION_REQUEST_CODE);
//        onActivityResult(100,RESULT_OK,getIntent());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    //onActivityResult(100,RESULT_OK,getIntent());
                    // main logic
                } else {
                   // Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                       // Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();

                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            new AlertDialog.Builder(this)
                                    .setMessage("You need to allow access permissions")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                                //onActivityResult(100,RESULT_OK,getIntent());
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    })
                                    .create()
                                    .show();
                            /*showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                           // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                requestPermission();
                                            //}
                                        }
                                    });*/
                        }
                    }
                }
                break;
        }
    }


   /* private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }*/



    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            pb.setVisibility(View.VISIBLE);
            animation.start();

         // Bundle extras = getIntent().getExtras();
             uri= photoURI;


            StorageReference particular_image = mStorageRef.child(uri.getLastPathSegment()); //name of the paticular image
            //eg. //content//local_images//foo/4
            particular_image.putFile(photoURI).addOnSuccessListener
                    (this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri download_uri = taskSnapshot.getDownloadUrl(); //getting uri of the image stored
                          // Photo_link p_link = new Photo_link(download_uri.toString());
                           // databasereference.push().setValue(p_link);

                            pb.clearAnimation();
                            animation.end();
                            Intent i = new Intent(getApplicationContext(),Giver_Edit.class);
                            i.setData(download_uri);
                            startActivity(i);


                            Log.d("giver_image_success","eroooooor_on_success");
                            //Toast.makeText(getApplicationContext(),"Image added",Toast.LENGTH_LONG).show();
                        }
                    });


            //  final Uri selectedImgUri = getIntent().getData();


            particular_image.putFile(uri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("giver_image_failure","eroooooor_on_ffailure");
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }



    }


}

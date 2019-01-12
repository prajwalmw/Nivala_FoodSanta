package com.example.akshyata.foodsanta;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener statelistener;
    public static final int RC_SIGN_IN = 1;

    private ViewFlipper simpleViewFlipper;
    Button btnNext,btnPrevious;
    ImageView no1,img1;
   private StorageReference mStorageRef;
    private FirebaseDatabase firebasedatabase;
    private DatabaseReference databasereference;

    Give_Act ga = new Give_Act();
   // private int checkedItem;
    // String[] ngo_numbers;

    AlertDialog.Builder dialogBuilder;

    NavigationView navigationView;
    View headerview;
    TextView tt1,tt;
    WebView wv;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermission();    //requests permisiions....

       mStorageRef = FirebaseStorage.getInstance().getReference().child("give");

        //firebase database
        firebasedatabase = FirebaseDatabase.getInstance();  //1st time is imp.
//       firebasedatabase.setPersistenceEnabled(true);   //handling offline data in MainActivity only !!!!!
        databasereference = firebasedatabase.getReference().child("giver_data"); //firebase database reference
        //databasereference.keepSynced(true);     //keeps data fresh i.e sync the data
        if(firebasedatabase == null)
        {
            firebasedatabase = FirebaseDatabase.getInstance();      //2nd time also imp
            firebasedatabase.setPersistenceEnabled(true);   //handling offline data in MainActivity only !!!!!
            databasereference = firebasedatabase.getReference().child("giver_data"); //firebase database reference
            databasereference.keepSynced(true);     //keeps data fresh i.e sync the data

        }

        wv = (WebView) findViewById(R.id.web_view);
        wv.setInitialScale(1);      //webview page matches the screen size.
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://prajwalwaingankar.wixsite.com/nivala");

        btnNext = (Button) findViewById(R.id.button_next);
        btnPrevious = (Button) findViewById(R.id.button_previous);
        simpleViewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);


        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show the next view of ViewFlipper
                simpleViewFlipper.showNext();

            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show the next view of ViewFlipper
                simpleViewFlipper.showPrevious();

            }
        });

        firebaseauth = FirebaseAuth.getInstance();
        //auth state listener
        statelistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); //firebaseAuth takes sign_in or sign_out params.

                if(user != null)
                {
                    tt1.setText(firebaseauth.getCurrentUser().getDisplayName());//username of logged in user.


                    tt.setText(firebaseauth.getCurrentUser().getEmail());//email id of logged in user.


                    Glide.with(getApplicationContext())
                            .load(firebaseauth.getCurrentUser().getPhotoUrl()).asBitmap().error(R.drawable.ic_selfie_point_icon)   //asbitmap after load always.
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    img1.setImageBitmap(resource);
                                }
                            });


                    Log.d("text","Nav : "+tt);
                    //sign-in
                    // Toast.makeText(getApplicationContext(), "You're now signed in. Rent It !", Toast.LENGTH_LONG).show();

                }
                else
                {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)

                {
                    displaySingleSelectionDialog();    //function call
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

  navigationView = (NavigationView) findViewById(R.id.nav_view);   //displays text of header of nav drawer.
         headerview = navigationView.getHeaderView(0);
         tt1 = (TextView) headerview.findViewById(R.id.textview_username);
         tt = (TextView) headerview.findViewById(R.id.textView_emailid);
         img1 = (ImageView) headerview.findViewById(R.id.imageView_userimage);

        navigationView.setNavigationItemSelectedListener(this);

    }

   private void displaySingleSelectionDialog() {

       final String[] ngo_numbers = {"RotiBank", "Robinhood Army"};
       // ngo_numbers = getResources().getStringArray(R.array.ngo_numbers);
       dialogBuilder = new AlertDialog.Builder(MainActivity.this);
       dialogBuilder.setTitle("Donate Instantly!");
       dialogBuilder.setIcon(R.drawable.ic_call_black_24dp);
       dialogBuilder.setItems(ngo_numbers, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               // the user clicked on colors[which]
               final String a = "tel:"+"9769025715";
               final String b = "tel:"+"7304154312";

               if("RotiBank".equals(ngo_numbers[which]))
               {
                   Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(a));
                   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   getApplicationContext().startActivity(i);
               }

                else if("Robinhood Army".equals(ngo_numbers[which]))
               {
                   Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(b));
                   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   getApplicationContext().startActivity(i);
               }
           }
       });
       dialogBuilder.show();

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

                        }
                    }
                }
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {


                tt1.setText(firebaseauth.getCurrentUser().getDisplayName());//username of logged in user.


                tt.setText(firebaseauth.getCurrentUser().getEmail());//email id of logged in user.


                Glide.with(getApplicationContext())
                        .load(firebaseauth.getCurrentUser().getPhotoUrl()).asBitmap().error(R.drawable.ic_selfie_point_icon)   //asbitmap after load always.
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                img1.setImageBitmap(resource);
                            }
                        });


                Log.d("text","Nav : "+tt);

                Toast.makeText(this, "You're now signed in", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RESULT_CANCELED) {
                onBackPressed();
               // Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }

    //Permission requests....
    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE},
                PERMISSION_REQUEST_CODE);
//        onActivityResult(100,RESULT_OK,getIntent());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        }
        if(!drawer.isDrawerOpen(GravityCompat.START))
        {

            moveTaskToBack(true);
            finish();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AuthUI.getInstance().signOut(this);
            Toast.makeText(this, "Visit Again", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_giver) {
            Intent int_give = new Intent(this, Give_Act.class);
            startActivity(int_give);
        }

        else if (id == R.id.nav_taker) {
            Intent int_take = new Intent(this, Take_Act.class);
            startActivity(int_take);

        }

        else if(id == R.id.nav_selfie_point)
        {
           Intent int_selfie = new Intent(this,Selfie_Act.class);
           startActivity(int_selfie);

        }

        else if (id == R.id.nav_login) {
            Intent int_login = new Intent(this, Give_Act.class);
            startActivity(int_login);

        } else if (id == R.id.nav_logout) {
            AuthUI.getInstance().signOut(this);
            Toast.makeText(this, "Visit Again", Toast.LENGTH_LONG).show();
            return true;

        } else if(id == R.id.nav_share){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String sAux = "\nAn initiative to SAVE food\nFeed the Hungry !\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Help Needy !!!"));

            //what to do ???

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        firebaseauth.addAuthStateListener(statelistener);


    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseauth.removeAuthStateListener(statelistener);
    }
}

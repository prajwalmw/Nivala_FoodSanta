package com.example.akshyata.foodsanta;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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

    NavigationView navigationView;
    View headerview;
    TextView tt1,tt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("take");

        //firebase database
        firebasedatabase = FirebaseDatabase.getInstance();  //1st time is imp.
       //firebasedatabase.setPersistenceEnabled(true);   //handling offline data
        databasereference = firebasedatabase.getReference().child("taker_data"); //firebase database reference
        //databasereference.keepSynced(true);     //keeps data fresh i.e sync the data
        if(firebasedatabase == null)
        {
            firebasedatabase = FirebaseDatabase.getInstance();      //2nd time also imp
            firebasedatabase.setPersistenceEnabled(true);   //handling offline data
            databasereference = firebasedatabase.getReference().child("taker_data"); //firebase database reference
            databasereference.keepSynced(true);     //keeps data fresh i.e sync the data

        }


        no1 = (ImageView) findViewById(R.id.help1);
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(mStorageRef)
                .into(no1);

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
                            .load(firebaseauth.getCurrentUser().getPhotoUrl()).asBitmap().atMost().error(R.drawable.ic_selfie_point_icon)   //asbitmap after load always.
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
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"9769025715"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    // return;
                    //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+pl.getMobileData())));
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


       /* NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);   //displays text of header of nav drawer.
        View headerview = navigationView.getHeaderView(0);
        TextView tt1 = (TextView) headerview.findViewById(R.id.textview_username);
        tt1.setText(firebaseauth.getCurrentUser().getDisplayName());//username of logged in user.

        TextView tt = (TextView) headerview.findViewById(R.id.textView_emailid);
        tt.setText(firebaseauth.getCurrentUser().getEmail());//email id of logged in user.

        final ImageView img1 = (ImageView) headerview.findViewById(R.id.imageView_userimage);
        Glide.with(getApplicationContext())
                .load(firebaseauth.getCurrentUser().getPhotoUrl()).asBitmap().atMost().error(R.drawable.ic_selfie_point_icon)   //asbitmap after load always.
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        img1.setImageBitmap(resource);
                    }
                });


        Log.d("text","Nav : "+tt);*/




         navigationView = (NavigationView) findViewById(R.id.nav_view);   //displays text of header of nav drawer.
         headerview = navigationView.getHeaderView(0);
         tt1 = (TextView) headerview.findViewById(R.id.textview_username);
         tt = (TextView) headerview.findViewById(R.id.textView_emailid);
         img1 = (ImageView) headerview.findViewById(R.id.imageView_userimage);

        navigationView.setNavigationItemSelectedListener(this);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {


                tt1.setText(firebaseauth.getCurrentUser().getDisplayName());//username of logged in user.


                tt.setText(firebaseauth.getCurrentUser().getEmail());//email id of logged in user.


                Glide.with(getApplicationContext())
                        .load(firebaseauth.getCurrentUser().getPhotoUrl()).asBitmap().atMost().error(R.drawable.ic_selfie_point_icon)   //asbitmap after load always.
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
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        }
        if(!drawer.isDrawerOpen(GravityCompat.START))
        {
            new AlertDialog.Builder(this)
                    //  .setIcon(android.R.drawable.)
                    .setTitle("FoodSanta")
                    .setMessage("Are you sure you want to EXIT ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                                finish();
                                //return;

                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
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

        } else if (id == R.id.nav_login) {
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
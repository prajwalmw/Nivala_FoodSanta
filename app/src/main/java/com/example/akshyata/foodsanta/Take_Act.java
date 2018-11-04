package com.example.akshyata.foodsanta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Take_Act extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef ;

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<Photo_link> PhotoList;
    Uri selectedImgUri;
    Bundle bundle;
    String title,address,phone;
    Long expiry;
    Photo_link pl;

    FloatingActionButton show_data;
    Date dateObj;
    Calendar cc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_);

        selectedImgUri = getIntent().getData();
        bundle = getIntent().getExtras();

         //cc = Calendar.getInstance();


        if(selectedImgUri != null && bundle != null)
        {
            title = bundle.getString("Title");
             dateObj = new Date(getIntent().getLongExtra("Expiry", -1));
             Log.d("EXP ","Expiry Date : "+dateObj);
            //expiry = bundle.getLong("Expiry");
            address = bundle.getString("Address");
            phone = bundle.getString("Phone");
        }


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //show_data = (FloatingActionButton) findViewById(R.id.show);



        //mycode
        database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        myRef = database.getReference().child("giver_data");
       // myRef.keepSynced(true);

        myRef.orderByValue().limitToLast(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                PhotoList = new ArrayList<Photo_link>();

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){


                    Photo_link value = dataSnapshot1.getValue(Photo_link.class);


                    if(selectedImgUri != null && bundle != null) {

                       /* if(dateObj.equals(System.currentTimeMillis()))
                        {
                            dataSnapshot1.getRef().removeValue();       //Delete the data from Firebase nd App.
                        }*/

                        pl = new Photo_link(selectedImgUri.toString(), title, dateObj.toString(), address, phone);
                        Log.d("data ","DAta in : "+pl);
                        String img = value.getPhotoUrl();
                        String tit = value.getTitleData();
                        String exp = value.getExpiryData();

                        String add = value.getAddressData();
                        String pho = value.getMobileData();

                        pl.setPhotoUrl(img.toString());
                        pl.setTitleData(tit);
                        pl.setExpiryData(exp);
                        pl.setAddressData(add);
                        pl.setMobileData(pho);
                    }

                    adapter = new RecyclerAdapter(getApplicationContext(), PhotoList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(mLayoutManager);
                    // recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    //post();


                    if(selectedImgUri != null && bundle != null)
                    {
                        PhotoList.add(pl);
                        //Log.d("EXP_funct ","Expiry Date : "+pl.getExpiryData());
                        /*SimpleDateFormat sdf1234 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        String abs12 = pl.getExpiryData();

                        Date todayDate = new Date();

                        try {
                            Date testDate1 = sdf1234.parse(abs12);

                            if(sdf1234.format(testDate1).equals(sdf1234.format(todayDate).toString()))
                            {
                                dataSnapshot1.getRef().removeValue();
                                Toast.makeText(getApplicationContext(),"Database Updated",Toast.LENGTH_LONG).show();
                            }
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }*/



                    }
                    else
                    {
                        PhotoList.add(value);
                        //Log.d("EXP_funct ","Expiry Date : "+pl.getExpiryData());
                        //Log.d("EXP_funct ","Expiry Date : "+pl.getExpiryData());
                        SimpleDateFormat sdf1234 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        String abs12 = value.getExpiryData();

                        Date todayDate = new Date();

                        try {
                            Date testDate1 = sdf1234.parse(abs12);

                            if(testDate1.compareTo(todayDate) <0){//   Date expired
                                dataSnapshot1.getRef().removeValue();
                            }
                            else if(testDate1.compareTo(todayDate)==0){// both date are same
                                if(testDate1.getTime() == todayDate.getTime() || testDate1.getTime() < todayDate.getTime())
                                {//  Time expired
                                    dataSnapshot1.getRef().removeValue();
                                }
                                else
                                    {//expired
                                    //Toast.makeText(getApplicationContext(),"Successful praju ",Toast.LENGTH_SHORT).show();
                                }
                            }else{//expired
                               // Toast.makeText(getApplicationContext(),"Successful praju ",Toast.LENGTH_SHORT).show();
                            }

                            /*if(sdf1234.format(testDate1).equals(sdf1234.format(todayDate).toString()))
                            {
                                dataSnapshot1.getRef().removeValue();
                                Toast.makeText(getApplicationContext(),"Successful praju ",Toast.LENGTH_SHORT).show();
                            }*/
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }
                    }

                    adapter.notifyDataSetChanged();


                    recyclerView.setAdapter(adapter);



                }

                Collections.reverse(PhotoList);     //displays recently added data first

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
// Failed to read value
                Log.w("Hello", "Failed to read value.", databaseError.toException());
            }
        });



       /* show_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter = new RecyclerAdapter(getApplicationContext(), PhotoList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(mLayoutManager);
                // recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                //post();

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            }
        });*/



    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        //super.onBackPressed();

       // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       // drawer.openDrawer(GravityCompat.START);
    }

    /* public void post()
    {
        pl = new Photo_link(selectedImgUri.toString(),title,expiry,address,phone);
        PhotoList.add(pl);
        Log.d("photolist","PhotoList "+title);
       // adapter.notifyDataSetChanged();
    }*/

}

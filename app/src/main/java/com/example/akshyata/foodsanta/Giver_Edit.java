package com.example.akshyata.foodsanta;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Giver_Edit extends AppCompatActivity {
ImageView view;
EditText e_title;
EditText e_expiry;
EditText e_address;
EditText e_phone;
Photo_link p_l;
    Uri selectedImgUri;
    FloatingActionButton d;
String t,e,a,p;
    Date testDate1;

    private FirebaseDatabase firebasedatabase;
    private DatabaseReference databasereference;

    private int year;
    private int month;
    private int day;
    int hour,minute;
    static final int DATE_PICKER_ID = 1111;
    static final int TIME_PICKER_ID = 2222;

    RadioGroup r_grp;
    RadioButton r_btn_pack,r_btn_home;
    Calendar c;
    //DateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giver_edit);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        firebasedatabase = FirebaseDatabase.getInstance();
       // firebasedatabase.setPersistenceEnabled(true);   //handling offline data
        databasereference = firebasedatabase.getReference().child("giver_data"); //firebase database reference
       // databasereference.keepSynced(true);     //keeps data fresh i.e sync the data

        view = (ImageView) findViewById(R.id.img_edit);
        e_title = (EditText) findViewById(R.id.title_edit);
        e_title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        e_expiry = (EditText) findViewById(R.id.expiry_edit);

        //calendar
         c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);          //Date
        day = c.get(Calendar.DAY_OF_MONTH);
        c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        //Time
         hour = c.get(Calendar.HOUR_OF_DAY);
         minute = c.get(Calendar.MINUTE);
         //Time end


        r_btn_pack = (RadioButton) findViewById(R.id.pack);
        r_btn_home = (RadioButton) findViewById(R.id.home);
        r_grp = (RadioGroup) findViewById(R.id.radio_group);

        // Show selected date
       /* StringBuilder dateValue1 = new StringBuilder().append(day).append("-")
                .append(month + 1).append("-").append(year).append(" ");

        // for Converting Correct Date format Save into Database
        SimpleDateFormat sdf123 = new SimpleDateFormat("dd-MM-yyyy");
        String abs1 = dateValue1.toString();
         testDate1 = null;

        try {
            testDate1 = sdf123.parse(abs1);
        } catch (ParseException e) {

            e.printStackTrace();
        }

        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
        String DateFormat = formatter1.format(testDate1);

        e_expiry.setText(DateFormat);*/

        e_expiry.setFocusable(false);
        e_expiry.setInputType(InputType.TYPE_NULL);
        e_expiry.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {

                int selectedId = r_grp.getCheckedRadioButtonId();
                // find which radioButton is checked by id
                if(selectedId == r_btn_pack.getId())
                {
                    showDialog(DATE_PICKER_ID);

                    //displays date picker in edit expiry text
                }
                else if(selectedId == r_btn_home.getId())
                {

                    showDialog(TIME_PICKER_ID);
                }

            }
        });


        //end calendar


        //e_expiry.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        e_address = (EditText) findViewById(R.id.address_edit);
        e_address.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        e_phone = (EditText) findViewById(R.id.phone_edit);
       // e_phone.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        selectedImgUri = getIntent().getData();
        p_l = new Photo_link(selectedImgUri.toString(),t,e,a,p);
        if (selectedImgUri != null) {

            // img.setVisibility(View.VISIBLE); //main
           // Toast.makeText(getApplicationContext(),"Got your Cycle !",Toast.LENGTH_LONG).show();
            Glide.with(getApplicationContext())
                    .load(p_l.getPhotoUrl()).asBitmap().centerCrop().error(R.drawable.ic_selfie_point_icon)   //asbitmap after load always.
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            view.setImageBitmap(resource);
                        }
                    });

        }


        d = (FloatingActionButton) findViewById(R.id.save);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                // return new DatePickerDialog(this, pickerListener, year, month,
                // day);

                // ///Only Show till Date Not More than That.
                final DatePickerDialog dialog = new DatePickerDialog(this,
                        pickerListener, year, month, day);
                dialog.getDatePicker();
               // testDate1=null;
                return dialog;


            case TIME_PICKER_ID:

                final TimePickerDialog dia = new TimePickerDialog(this,timeSetListener,hour,minute,false);
                dia.show();
               // testDate1=null;
                return dia;

        }
        return null;
    }



    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute_new) {
            hour = hourOfDay;
            minute = minute_new;

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);          //Date
            day = c.get(Calendar.DAY_OF_MONTH);


            String am_pm;
            if (hour > 12)
            {
                hour = hour - 12;
                am_pm = "PM";
            } else {
                am_pm = "AM";
            }


            StringBuilder dateValue_1 = new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" ").append(hour).append(":").append(minute).append(" ").append(am_pm);

            //Date date = new Date();
            SimpleDateFormat sdf1234 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            String abs12 = dateValue_1.toString();

           // testDate1 = null;
            try {
                testDate1 = sdf1234.parse(abs12);
            } catch (ParseException e) {

                e.printStackTrace();
            }
            //String strDateFormat = "hh:mm:ss a";

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            String formattedDate= dateFormat.format(testDate1);

            e_expiry.setText(formattedDate);
        }
    };
    //return timePickerDialog;


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            StringBuilder dateValue = new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" ");

            // for Converting Correct Date format Save into Database
            SimpleDateFormat sdf123 = new SimpleDateFormat("dd-MM-yyyy");
            String abs1 = dateValue.toString();
            // testDate1 = null;
            try {
                testDate1 = sdf123.parse(abs1);
            } catch (ParseException e) {

                e.printStackTrace();
            }
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
            String DateFormat = formatter1.format(testDate1);

            e_expiry.setText(DateFormat);

        }
    };


    public void save(){
        t = e_title.getText().toString();
        e= e_expiry.getText().toString();
        a=e_address.getText().toString();
        p=e_phone.getText().toString();


        p_l = new Photo_link(selectedImgUri.toString(),t,e,a,p);
        Log.d("ttt","Title "+t);

        databasereference.push().setValue(p_l);

        Intent i_give = new Intent(getApplicationContext(), Take_Act.class);
        i_give.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i_give.setData(selectedImgUri);
        i_give.putExtra("Title",t);
        i_give.putExtra("Expiry",testDate1.getTime());  //sends date as intent.
        i_give.putExtra("Address",a);
        i_give.putExtra("Phone",p);
        startActivity(i_give);

        Toast.makeText(this,"Thank you for the Food !",Toast.LENGTH_LONG).show();
        //finish();

    }

   /* // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.give_check, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            databasereference.push().setValue(p_l);
            finish();
          //  Log.d("text","Title : "+e_title.getText().toString());
            // do something here
        }
        return super.onOptionsItemSelected(item);
    }*/
}

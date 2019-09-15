package com.nivala.akshyata.foodsanta;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<Photo_link> photoList = new ArrayList<Photo_link>(); //initialization was imp. which caused the error 28th sep. 18

   // String sAux;

    public RecyclerAdapter(Context mContext, List<Photo_link> photoList) {
        this.mContext = mContext;
        this.photoList = photoList;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // photoList= new ArrayList<Photo_link>();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_give, parent, false);

        MyViewHolder myHoder = new MyViewHolder(itemView);

        return myHoder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Photo_link pl = photoList.get(position);
        holder.title_txt.setText(pl.getTitleData());
        holder.exp_txt.setText(pl.getExpiryData());
        holder.address_txt.setText(pl.getAddressData());


        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ii = new Intent(Intent.ACTION_SEND);
                ii.setType("text/plain");
                ii.putExtra(Intent.EXTRA_SUBJECT, "My application name");

                //    i.putExtra(Intent.EXTRA_STREAM, uri);

                //      shareimg = getIntent().getData();
                //       void g = image_giver.setImageURI(shareimg);

                String sAux = "\nFood : " + pl.getTitleData() + "\n\nConsume Within/Before : " +
                        pl.getExpiryData() + "\n\nAddress of Giver : "
                        + pl.getAddressData() + "\n\nGiver Number : " + pl.getMobileData();

                //   sAux = sAux + "\n\nFood Image : "+getIntent().getData()+"\n\n";
                ii.putExtra(Intent.EXTRA_TEXT, sAux);
                //mContext.startActivity(ii);
                //mContext.startActivity(Intent.createChooser(ii, "Help Needy !!!"));
             //   return;
                Intent.createChooser(ii,"Help the Needy !!!");
                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(ii);


            }
        });



        holder.phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)

                {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+pl.getMobileData()));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                   // return;
                    //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+pl.getMobileData())));
                }

            }
        });


        // loading album cover using Glide library
        Glide.with(mContext).load(pl.getPhotoUrl()).into(holder.food_img);

       /* holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/
    }

    @Override
    public int getItemCount() {
        return  photoList == null ? 0 : photoList.size();
        //return photoList.size();
        /*//your problem is here because you are printing size of list which is null,
        //so instead of that check the null first, then use the list object
        if (photoList == null) {

        Log.d("null", "null list");}
        else{
            Log.d("not null", "getItemCount: list DATA-----" + photoList.size());}

            //**Here I'm getting Null Pointer exception**
            return photoList == null ? 0 : photoList.size();*/




    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView food_img;
        TextView title_txt, exp_txt, address_txt;
        Button share_btn, phone_btn;

        public MyViewHolder(View view) {
            super(view);
            food_img = (ImageView) view.findViewById(R.id.giv_id);
            title_txt = (TextView) view.findViewById(R.id.title_text);
            exp_txt = (TextView) view.findViewById(R.id.textDate);
            address_txt = (TextView) view.findViewById(R.id.editAddress);
            share_btn = (Button) view.findViewById(R.id.share);
            phone_btn = (Button) view.findViewById(R.id.take);



        }
    }


}

package com.example.vayo.Recycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vayo.Model.Movies;
import com.example.vayo.R;
import com.example.vayo.ShowingsDetailActivity;

import java.util.List;

/**
 * Created by Aws on 28/01/2018.
 */

public class RecyclerAdapterFavourite extends RecyclerView.Adapter<RecyclerAdapterFavourite.MyViewHolderFavourite> {

    private Context mContext ;
    private List<Movies> mData ;


    public RecyclerAdapterFavourite(Context mContext, List<Movies> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolderFavourite onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.favourite_item_layout,parent,false);
        return new MyViewHolderFavourite(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderFavourite holder, final int position) {

        holder.tv_movie_title.setText(mData.get(position).getName());
        holder.tv_movie_price.setText(mData.get(position).getPRICE());
        byte[] movieImage = mData.get(position).getImage();
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(movieImage, 0, movieImage.length);
        }
        catch (Exception e)
        {
            Toast.makeText(mContext, String.valueOf(movieImage),Toast.LENGTH_SHORT).show();
        }
        holder.img.setImageBitmap(bitmap);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ShowingsDetailActivity.class);

                // passing data to the movie activity
                intent.putExtra("Id",mData.get(position).getId());
                // start the activity
                mContext.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderFavourite extends RecyclerView.ViewHolder {

        TextView tv_movie_title, tv_movie_price;
        ImageView img;
        CardView cardView ;

        public MyViewHolderFavourite(View itemView) {
            super(itemView);

            tv_movie_title = itemView.findViewById(R.id.movie_name_details) ;
            tv_movie_price =  itemView.findViewById(R.id.movie_price_details) ;
            img = itemView.findViewById(R.id.movie_image_favourite);
            cardView =  itemView.findViewById(R.id.card_id);


        }
    }


}

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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Movies> mData ;


    public RecyclerViewAdapter(Context mContext, List<Movies> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_showing,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_movie_title.setText(mData.get(position).getName());
        byte[] movieImage = mData.get(position).getImage();
        Bitmap bitmap = null;
        try {
             bitmap = BitmapFactory.decodeByteArray(movieImage, 0, movieImage.length);
        }
        catch (Exception e)
        {
        }
        holder.img_book_thumbnail.setImageBitmap(bitmap);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ShowingsDetailActivity.class);

                // passing data to the book activity
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_movie_title;
        ImageView img_book_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_movie_title =  itemView.findViewById(R.id.showing_movie_name) ;
            img_book_thumbnail =  itemView.findViewById(R.id.showing_img_id);
            cardView = itemView.findViewById(R.id.cardview_id);


        }
    }


}

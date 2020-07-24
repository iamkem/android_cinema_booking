package com.example.cinemabooking.Recycler;

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

import com.example.cinemabooking.Model.Movies;
import com.example.cinemabooking.R;
import com.example.cinemabooking.ChiTietPhimActivity;

import java.util.List;

public class RecyclerViewFiltered extends RecyclerView.Adapter<RecyclerViewFiltered.MyViewHolderFiltered>
{
    private Context mContext ;
    private List<Movies> mData ;


    public RecyclerViewFiltered(Context mContext, List<Movies> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolderFiltered onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.filtered_item_layout,parent,false);
        return new MyViewHolderFiltered(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderFiltered holder, final int position) {

        holder.tv_movie_title.setText(mData.get(position).getName());
        byte[] movieImage = mData.get(position).getImage();
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(movieImage, 0, movieImage.length);
        }
        catch (Exception e)
        {
            Toast.makeText(mContext, String.valueOf(movieImage),Toast.LENGTH_SHORT).show();
        }
        holder.img_book_thumbnail_filtered.setImageBitmap(bitmap);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ChiTietPhimActivity.class);

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

    public static class MyViewHolderFiltered extends RecyclerView.ViewHolder {

        TextView tv_movie_title;
        ImageView img_book_thumbnail_filtered;
        CardView cardView ;

        public MyViewHolderFiltered(View itemView) {
            super(itemView);

            tv_movie_title =  itemView.findViewById(R.id.filter_movie_name) ;
            img_book_thumbnail_filtered =  itemView.findViewById(R.id.filter_img_id);
            cardView = itemView.findViewById(R.id.cardview_filtered_id);


        }
    }

}

package com.example.cinemabooking.Recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabooking.Model.Showings;
import com.example.cinemabooking.R;

import java.util.List;

public class RecyclerViewShowing  extends RecyclerView.Adapter<RecyclerViewShowing.MyViewHolderShowing>{
    private Context mContext ;
    private List<Showings> mData ;


    public RecyclerViewShowing(Context mContext, List<Showings> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolderShowing onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.view_showing_item,parent,false);
        return new MyViewHolderShowing(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderShowing holder, final int position) {

        holder.showing_id.setText(mData.get(position).getShowing_ID());
        holder.movie_id.setText( mData.get(position).getMovie_ID());
        holder.cinema_id.setText( mData.get(position).getCinema_ID());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderShowing extends RecyclerView.ViewHolder {

        TextView showing_id, cinema_id, movie_id;
        CardView cardView ;

        public MyViewHolderShowing(View itemView) {
            super(itemView);

            showing_id =  itemView.findViewById(R.id.showing_showing_id) ;
            cinema_id =  itemView.findViewById(R.id.showing_cinema_name);
            movie_id = itemView.findViewById(R.id.showing_movie_name);
            cardView = itemView.findViewById(R.id.card_showing_id);


        }
    }

}

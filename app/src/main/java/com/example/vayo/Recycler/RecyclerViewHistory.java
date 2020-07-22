package com.example.vayo.Recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vayo.Model.Booking;
import com.example.vayo.R;

import java.util.List;

public class RecyclerViewHistory extends RecyclerView.Adapter<RecyclerViewHistory.MyViewHolderHistory>
{

    private Context mContext ;
    private List<Booking> mData ;


    public RecyclerViewHistory(Context mContext, List<Booking> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolderHistory onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.booking_history_item_,parent,false);
        return new MyViewHolderHistory(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolderHistory holder, final int position) {

        holder.booking_id.setText("#" + mData.get(position).getID());
        holder.movie_name.setText("Tên phim: " + mData.get(position).getMOVIE_NAME());
        holder.movie_location.setText("Tên rạp: " + mData.get(position).getCINEMA_NAME());
        holder.movie_price.setText("Giá mỗi ghế: " + mData.get(position).getPRICE());
        holder.movie_showing_date.setText("Ngày chiếu: " + mData.get(position).getDATE());
        holder.movie_status.setText("Đã xác nhận: " + mData.get(position).getSTATUS());
        holder.seats.setText("Số ghế đặt: " + mData.get(position).getSEATS());




    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderHistory extends RecyclerView.ViewHolder {

        TextView booking_id, movie_name,movie_location, movie_price, movie_showing_date,movie_status, seats;
        CardView cardView ;

        public MyViewHolderHistory(View itemView) {
            super(itemView);

            booking_id =  itemView.findViewById(R.id.booking_id) ;
            movie_name =  itemView.findViewById(R.id.history_name);
            movie_location =  itemView.findViewById(R.id.history_location);
            movie_price =  itemView.findViewById(R.id.history_paid);
            movie_showing_date =  itemView.findViewById(R.id.history_date);
            movie_status =  itemView.findViewById(R.id.history_confirmation);
            seats =  itemView.findViewById(R.id.history_seats);



            cardView =  itemView.findViewById(R.id.card_history_id);


        }
    }


}

package com.example.cinemabooking.Recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabooking.Model.BookingUser;
import com.example.cinemabooking.R;

import java.util.List;

public class RecyclerViewUser extends RecyclerView.Adapter<RecyclerViewUser.MyViewHolderUser>
{
    private Context mContext ;
    private List<BookingUser> mData ;


    public RecyclerViewUser(Context mContext, List<BookingUser> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolderUser onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.view_users_item,parent,false);
        return new MyViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderUser holder, final int position) {

        holder.booking_id.setText(mData.get(position).getBookingId());
        holder.username.setText( mData.get(position).getUserName());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderUser extends RecyclerView.ViewHolder {

        TextView booking_id, username;
        CardView cardView ;

        public MyViewHolderUser(View itemView) {
            super(itemView);

            booking_id =  itemView.findViewById(R.id.booking_user_id) ;
            username =  itemView.findViewById(R.id.user_username);
            cardView = itemView.findViewById(R.id.card_user_id);


        }
    }

}

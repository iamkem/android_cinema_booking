package com.example.cinemabooking.Recycler;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabooking.Model.Booking;
import com.example.cinemabooking.Database.DatabaseHelper;
import com.example.cinemabooking.R;

import java.util.List;

public class RecyclerViewCheck extends RecyclerView.Adapter<RecyclerViewCheck.MyViewHolderCheck>
{
    private Context mContext ;
    private List<Booking> mData ;
    private DatabaseHelper myDb;



    public RecyclerViewCheck(Context mContext, List<Booking> mData) {
        this.mContext = mContext;
        this.mData = mData;
        myDb = new DatabaseHelper(mContext);

    }

    @Override
    public MyViewHolderCheck onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.check_booking_item,parent,false);
        return new MyViewHolderCheck(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolderCheck holder, final int position) {

        holder.booking_id.setText("#" + mData.get(position).getID());
        holder.movie_name.setText("Tên phim: " + mData.get(position).getMOVIE_NAME());
        holder.movie_location.setText("Tên rạp: " + mData.get(position).getCINEMA_NAME());
        holder.movie_showing_date.setText("Ngày chiếu: " + mData.get(position).getDATE());
        holder.movie_status.setText("Đã xác nhận: " + mData.get(position).getSTATUS());
        holder.seats.setText("Số ghế đặt: " + mData.get(position).getSEATS());
        holder.userName.setText("Tài khoản: " + mData.get(position).getUSERNAME());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(mContext)
                        .setTitle("Xác nhận đơn đặt")
                        .setMessage("Bạn có muốn xác nhận đơn này không?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                try {
                                    myDb.updateBookings(mData.get(position).getID(),"yes");
                                    holder.movie_status.setText("Trạng thái xác nhận: yes");
                                    Toast.makeText(mContext,"Xác nhận đơn thành công",Toast.LENGTH_SHORT).show();

                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            }
        });





    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderCheck extends RecyclerView.ViewHolder {

        TextView booking_id, movie_name,movie_location, movie_showing_date,movie_status, seats, userName;
        CardView cardView ;

        public MyViewHolderCheck(View itemView) {
            super(itemView);

            booking_id =  itemView.findViewById(R.id.check_booking_id) ;
            movie_name = itemView.findViewById(R.id.check_booking_name);
            movie_location =  itemView.findViewById(R.id.check_booking_location);
            movie_showing_date =  itemView.findViewById(R.id.check_booking_date);
            movie_status =  itemView.findViewById(R.id.check_booking_confirmation);
            seats = itemView.findViewById(R.id.check_booking_seats);
            userName =  itemView.findViewById(R.id.check_booking_user);
            cardView = itemView.findViewById(R.id.card_check_id);


        }
    }

}

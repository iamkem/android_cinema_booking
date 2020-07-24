package com.example.cinemabooking.Recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabooking.Model.MovieReview;
import com.example.cinemabooking.R;

import java.util.List;

public class RecyclerViewReview extends RecyclerView.Adapter<RecyclerViewReview.MyViewHolderReview>
{
    private Context mContext ;
    private List<MovieReview> mData ;


    public RecyclerViewReview(Context mContext, List<MovieReview> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolderReview onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.review_item_layout,parent,false);
        return new MyViewHolderReview(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderReview holder, final int position) {

        holder.review_description.setText(mData.get(position).getReview());
        holder.review_mark.setText(mData.get(position).getMark());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderReview extends RecyclerView.ViewHolder {

        TextView review_description, review_mark;
        CardView cardView ;

        public MyViewHolderReview(View itemView) {
            super(itemView);

            review_description =  itemView.findViewById(R.id.review_description) ;
            review_mark =  itemView.findViewById(R.id.review_mark);

            cardView =  itemView.findViewById(R.id.card_review_id);


        }
    }

}

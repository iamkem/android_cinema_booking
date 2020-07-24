package com.example.cinemabooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.cinemabooking.Database.DatabaseHelper;
import com.example.cinemabooking.Model.Showings;
import com.example.cinemabooking.Recycler.RecyclerViewShowing;

import java.util.ArrayList;
import java.util.List;

public class DangChieuActivity extends AppCompatActivity {

    List<Showings> lstShowing ;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_chieu);

        myDb = new DatabaseHelper(this);


        RecyclerView myrv =  findViewById(R.id.view_showing_rv);


        //Cập nhật dữ liệu
        UpdateScreen();


        RecyclerViewShowing myAdapter = new RecyclerViewShowing(this,lstShowing);
        myrv.setLayoutManager(new GridLayoutManager(this,1));
        myrv.setAdapter(myAdapter);

    }

    private void UpdateScreen()
    {
        //Trả về thông tin với tất cả user có đơn hàng
        Cursor res = myDb.getAllShowwithoutBookings();
        //Khởi tạo danh sách
        lstShowing = new ArrayList<>();


        for(int i=0;i<res.getCount();i++)
        {
            //Thêm thông tin vào danh sách
            res.moveToPosition(i);
            String id = String.valueOf(res.getString(0));
            String movie_id = get_movie_name(String.valueOf(res.getString(1)));
            String cinema_id = get_cinema_name(String.valueOf(res.getString(2)));

            lstShowing.add(new Showings(id, movie_id, cinema_id));

        }

    }

    String get_movie_name(String id)
    {
        String name= "";

        Cursor res = myDb.getAllMovies();


        for(int i=0;i<res.getCount();i++)
        {
            //Kiểm tra xem tên đã nhập có tương ứng với tên phim khác trong cơ sở dữ liệu không
            res.moveToPosition(i);
            if(id.equals(String.valueOf(res.getString(0))))
            {
                name = String.valueOf(res.getString(1));
            }
        }

        res.close();

        return name;
    }


    String get_cinema_name(String id)
    {
        String name= "";
        Cursor res = myDb.getAllCinemas();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            if(id.equals(String.valueOf(res.getString(0))))
            {
                name = String.valueOf(res.getString(1));
            }
        }

        res.close();

        return name;
    }

}

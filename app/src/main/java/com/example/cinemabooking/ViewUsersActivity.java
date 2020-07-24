package com.example.cinemabooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.cinemabooking.Model.BookingUser;
import com.example.cinemabooking.Recycler.RecyclerViewUser;
import com.example.cinemabooking.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {

    List<BookingUser> lstUsers ;
    private DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);


        myDb = new DatabaseHelper(this);


        RecyclerView myrv =  findViewById(R.id.view_users_rv);



        UpdateScreen();


        RecyclerViewUser myAdapter = new RecyclerViewUser(this,lstUsers);
        myrv.setLayoutManager(new GridLayoutManager(this,1));
        myrv.setAdapter(myAdapter);


    }

    private void UpdateScreen()
    {

        Cursor res = myDb.getAllUserWithBookings();

        lstUsers = new ArrayList<>();


        for(int i=0;i<res.getCount();i++)
        {

            res.moveToPosition(i);
            String userName = String.valueOf(res.getString(0));
            String bookingId = String.valueOf(res.getString(1));
            lstUsers.add(new BookingUser(userName, bookingId));

        }

    }

}

package com.example.vayo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.vayo.Model.BookingUser;
import com.example.vayo.Recycler.RecyclerViewUser;
import com.example.vayo.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {

    List<BookingUser> lstUsers ;
    private DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        //initializare BD
        myDb = new DatabaseHelper(this);

        //conectare recycler view
        RecyclerView myrv =  findViewById(R.id.view_users_rv);


        //actualizare informatii cu useri
        UpdateScreen();

        //punere lista cu useri ce au comenzi in recycler view
        RecyclerViewUser myAdapter = new RecyclerViewUser(this,lstUsers);
        myrv.setLayoutManager(new GridLayoutManager(this,1));
        myrv.setAdapter(myAdapter);


    }

    private void UpdateScreen()
    {
        //primire informatii cu toti userii ce au comenzi
        Cursor res = myDb.getAllUserWithBookings();
        //initializez lista
        lstUsers = new ArrayList<>();


        for(int i=0;i<res.getCount();i++)
        {
            //adaugare informatii in lista
            res.moveToPosition(i);
            String userName = String.valueOf(res.getString(0));
            String bookingId = String.valueOf(res.getString(1));
            lstUsers.add(new BookingUser(userName, bookingId));

        }

    }

}

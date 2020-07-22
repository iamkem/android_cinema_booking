package com.example.vayo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.Model.BookingUser;
import com.example.vayo.Model.Showings;
import com.example.vayo.Recycler.RecyclerViewShowing;

import java.util.ArrayList;
import java.util.List;

public class ViewShowingActivityActivity extends AppCompatActivity {

    List<Showings> lstShowing ;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_showing_activity);

        myDb = new DatabaseHelper(this);

        //conectare recycler view
        RecyclerView myrv =  findViewById(R.id.view_showing_rv);


        //actualizare informatii cu useri
        UpdateScreen();

        //punere lista cu useri ce au comenzi in recycler view
        RecyclerViewShowing myAdapter = new RecyclerViewShowing(this,lstShowing);
        myrv.setLayoutManager(new GridLayoutManager(this,1));
        myrv.setAdapter(myAdapter);

    }

    private void UpdateScreen()
    {
        //primire informatii cu toti userii ce au comenzi
        Cursor res = myDb.getAllShowwithoutBookings();
        //initializez lista
        lstShowing = new ArrayList<>();


        for(int i=0;i<res.getCount();i++)
        {
            //adaugare informatii in lista
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
        //Accesare tabel cu toate filmele
        Cursor res = myDb.getAllMovies();


        for(int i=0;i<res.getCount();i++)
        {
            //verific daca numele introdus corespunde cu alt nume de film din baza de date
            res.moveToPosition(i);
            if(id.equals(String.valueOf(res.getString(0))))
            {
                name = String.valueOf(res.getString(1));
            }
        }

        res.close();

        return name;
    }

    //primire id cinema in functie de numele cinema-ului
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

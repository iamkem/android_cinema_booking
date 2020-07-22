package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vayo.Model.Booking;
import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.R;
import com.example.vayo.Recycler.RecyclerViewCheck;
import com.example.vayo.ViewShowingActivityActivity;
import com.example.vayo.ViewUsersActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AdminCheckReservationsActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private List<Booking> lstBook;

    private String Booking_Id,CinemaName,MovieName,Paid,ShowingDate,SeatsNumber,Status;
    private String ShowingID, MovieID, CinemaID;
    private String UserID,UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_reservations);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Initializare baza de date
        myDb = new DatabaseHelper(this);

        //Corelarea cu interfata vizuala
        RecyclerView myrv =  findViewById(R.id.check_booking_rv);
        Button viewUsers = findViewById(R.id.view_user_button);
        Button viewS = findViewById(R.id.view_showing_without_booking);

        lstBook = new ArrayList<>();

        //Actualizare lista
        UpdateScreen();

        //Button pentru accesarea altei ferestre
        viewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCheckReservationsActivity.this, ViewUsersActivity.class);
                startActivity(intent);
            }
        });

        viewS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCheckReservationsActivity.this, ViewShowingActivityActivity.class);
                startActivity(intent);
            }
        });


        //Punerea informatiilor pe ecran din lista creata anterior mai sus
        RecyclerViewCheck myAdapter = new RecyclerViewCheck(this,lstBook);
        myrv.setLayoutManager(new GridLayoutManager(this,1));
        myrv.setAdapter(myAdapter);


    }

    private void UpdateScreen()
    {
        //Se citesc toate rezervarile facute
        Cursor res = null;
        try{
            res = myDb.getAllBookings();
            lstBook = new ArrayList<>();

            for(int i=0;i<res.getCount();i++)
            {
                //Adaugarea de informatii in lista
                res.moveToPosition(i);
                Booking_Id = String.valueOf(res.getString(0));
                UserID = String.valueOf(res.getString(1));
                UserName = getUserName(UserID);
                ShowingID = String.valueOf(res.getString(2));
                SeatsNumber = String.valueOf(res.getString(6));
                getMovieandCinemaId(ShowingID);
                MovieName = getMovieName(MovieID);
                CinemaName = getCinemaName(CinemaID);
                Paid = getMoviePrice(MovieID);
                ShowingDate = String.valueOf(res.getString(3));
                Status = String.valueOf(res.getString(7));
                lstBook.add(new Booking(Booking_Id,MovieName,CinemaName,Paid,ShowingDate,Status,SeatsNumber,UserName));

            }
        }
        catch (Exception e)
        {
            Toast.makeText(AdminCheckReservationsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally
        {
            if(res !=null)
            {
                res.close();
            }
        }

    }

    //functiie pentru intoarcerea username-ului primind ca paramentru id-ul user-ului
    private String getUserName(String id)
    {
        //Preluare informatii despre user
        Cursor res = myDb.getAllData();
        String name = "";

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (id.equals(String.valueOf(res.getString(0)))) {
                name = String.valueOf(res.getString(1));
            }
        }

        return name;
    }


    //functiie pentru intoarcerea cautarii sortate dupa film si cinema
    private void getMovieandCinemaId(String show_id)
    {
        Cursor res = null;
        try {
            res = myDb.getAllShowings();

            for (int i = 0; i < res.getCount(); i++) {
                res.moveToPosition(i);
                if (show_id.equals(String.valueOf(res.getString(0)))) {
                    MovieID = String.valueOf(res.getString(1));
                    CinemaID = String.valueOf(res.getString(2));
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(AdminCheckReservationsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally
        {
            if(res !=null)
            {
                res.close();
            }
        }


    }


    //functie pentru intoarcerea numelui filmului primind ca paramentru id ul filmului
    private String getMovieName(String movie_id)
    {
        Cursor res = myDb.getAllMovies();
        String name = "";

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (movie_id.equals(String.valueOf(res.getString(0)))) {
                name = String.valueOf(res.getString(1));
            }
        }
        res.close();

        return name;

    }

    //functie ce returneaza numele cinema ului primind id ul cinemaului
    private String getCinemaName(String cinema_id)
    {
        Cursor res = myDb.getAllCinemas();
        String name = "";

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (cinema_id.equals(String.valueOf(res.getString(0)))) {
                name = String.valueOf(res.getString(1));
            }
        }
        res.close();

        return name;

    }

    //functie ce returneaza pretul filmului  primind id ul filmului
    private String getMoviePrice(String movie_id)
    {
        Cursor res = myDb.getAllMovies();
        String price = "";

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (movie_id.equals(String.valueOf(res.getString(0)))) {
                price = String.valueOf(res.getString(4));
            }
        }
        res.close();

        return price;

    }





}

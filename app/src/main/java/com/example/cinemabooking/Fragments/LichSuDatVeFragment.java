package com.example.cinemabooking.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemabooking.Model.Booking;
import com.example.cinemabooking.Database.DatabaseHelper;
import com.example.cinemabooking.Prevalent.Prevalent;
import com.example.cinemabooking.R;
import com.example.cinemabooking.Recycler.RecyclerViewHistory;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class LichSuDatVeFragment extends Fragment {

    private DatabaseHelper myDb;
    private List<Booking> lstBook;

    private String USER_ID;
    private String Booking_Id,CinemaName,MovieName,Paid,ShowingDate,SeatsNumber,Status;
    private String ShowingID, MovieID, CinemaID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View productView = inflater.inflate(R.layout.fragment_lich_su_dat_ve, container, false);
        myDb = new DatabaseHelper(getContext());

        RecyclerView myrv = productView.findViewById(R.id.history_rv);

        UpdateScreen();


        RecyclerViewHistory myAdapter = new RecyclerViewHistory(getContext(),lstBook);
        myrv.setLayoutManager(new GridLayoutManager(getContext(),1));
        myrv.setAdapter(myAdapter);





        return productView;
    }

    private void UpdateScreen()
    {
        Cursor res = null;
        try{
            res = myDb.getAllBookings();
            String UserEmailKey = Paper.book().read(Prevalent.UserEmailKey);
            USER_ID = getUserId(UserEmailKey);
            lstBook = new ArrayList<>();


            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                if(USER_ID.equals(String.valueOf(res.getString(1))))
                {
                    Booking_Id = String.valueOf(res.getString(0));
                    ShowingID = String.valueOf(res.getString(2));
                    SeatsNumber = String.valueOf(res.getString(6));
                    getMovieandCinemaId(ShowingID);
                    MovieName = getMovieName(MovieID);
                    CinemaName = getCinemaName(CinemaID);
                    Paid = getMoviePrice(MovieID);
                    ShowingDate = String.valueOf(res.getString(3));
                    Status = String.valueOf(res.getString(7));
                    lstBook.add(new Booking(Booking_Id,MovieName,CinemaName,Paid,ShowingDate,Status,SeatsNumber,UserEmailKey));
                }

            }
        } catch (Exception e)
        {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally
        {
            if(res !=null)
            {
                res.close();
            }
        }



    }

    private void getMovieandCinemaId(String show_id)
    {
        Cursor res = null;
        try{
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
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally
        {
            if(res !=null)
            {
                res.close();
            }
        }


    }



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

        return name;

    }

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

        return name;

    }

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

        return price;

    }


    private String getUserId(String email) {
        String id = "";
        //accesez toti userii
        Cursor res = myDb.getAllData();

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            //verific daca am gasit email ul in BD
            if (email.equals(String.valueOf(res.getString(1))))
                id = String.valueOf(res.getString(0));

        }


        return id;
    }



}


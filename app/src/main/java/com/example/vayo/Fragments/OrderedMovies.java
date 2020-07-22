package com.example.vayo.Fragments;

import android.database.Cursor;
import android.database.CursorWindow;
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

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.Model.Movies;
import com.example.vayo.R;
import com.example.vayo.Recycler.RecyclerViewAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class OrderedMovies extends Fragment {

    private List<Movies> lstBook ;
    private DatabaseHelper myDb;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View productView = inflater.inflate(R.layout.fragment_ordered_movies, container, false);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
                e.printStackTrace();
        }


        //initializare baza de date
        myDb = new DatabaseHelper(getContext());
        RecyclerView myrv = productView.findViewById(R.id.recyclerview_id);

        //preluare date din fragmentul parinte
        assert getArguments() != null;
        String strtext=getArguments().getString("message");

        //daca string ul primit este "Least" afisare doar filme care se difuzeaza in cel putin 2 cinemauri
        //altfel afiseaza in ordinea ceruta
        assert strtext != null;
        if(strtext.equals("Least"))
        {
            UpdateLeast();
        }
        else if(strtext.equals("Most"))
        {
           UpdateMost();
        }
        else
        {
            UpdateScreen(strtext);
        }

        //afiseaza lista in RecylerView
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getContext(),lstBook);
        myrv.setLayoutManager(new GridLayoutManager(getContext(),3));
        myrv.setAdapter(myAdapter);



        return productView;
    }

    private void UpdateMost()
    {
        Cursor res = null;
        try {
            res = myDb.getAllMoviesifromShowingsMostPopular();
            lstBook = new ArrayList<>();



            for (int i = 0; i < res.getCount(); i++) {
                res.moveToPosition(i);
                String id = String.valueOf(res.getString(0));
                String name = getMovieName(id);
                byte[] image = getMovieImage(id);
                lstBook.add(new Movies(id, name, image));

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

    //afisare doar filme care se difuzeaza in cel putin 2 cinemauri
    private void UpdateLeast()
    {
        Cursor res = null;
        try {
            res = myDb.getMovieswithShowings();
            lstBook = new ArrayList<>();


            for (int i = 0; i < res.getCount(); i++) {
                res.moveToPosition(i);
                String id = String.valueOf(res.getString(0));
                String name = String.valueOf(res.getString(1));
                byte[] image = getMovieImage(id);
                lstBook.add(new Movies(id, name, image));

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

    private void UpdateScreen(String str)
    {
        //afisare filme in oridinea criteriului cerut
        Cursor res = myDb.getOrderedMovies(str);
        lstBook = new ArrayList<>();


        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            String id = String.valueOf(res.getString(0));
            String name = String.valueOf(res.getString(1));
            byte[] image = getMovieImage(id);
            lstBook.add(new Movies(id,name,image));

        }

    }

    //functie ce returneaza numele filmului in functie de id
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


    private byte[] getMovieImage(String id)
    {
        byte [] im = null;
        Cursor res = myDb.getAllMovies();


        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (id.equals(String.valueOf(res.getString(0))))
                im = res.getBlob(6);

        }



        return im;
    }




}

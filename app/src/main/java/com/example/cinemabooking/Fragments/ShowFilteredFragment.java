package com.example.cinemabooking.Fragments;

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

import com.example.cinemabooking.Database.DatabaseHelper;
import com.example.cinemabooking.Model.Movies;
import com.example.cinemabooking.R;
import com.example.cinemabooking.Recycler.RecyclerViewFiltered;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ShowFilteredFragment extends Fragment
{

    private List<Movies> lstBook ;
    private DatabaseHelper myDb;
    private String MovieId, MovieName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show_filtered_layout, container, false);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        myDb = new DatabaseHelper(getContext());
        RecyclerView myrv = v.findViewById(R.id.recyclerview_filtered_id);
        lstBook = new ArrayList<>();

        assert getArguments() != null;
        String cinemaName =getArguments().getString("cinema");
        String category =getArguments().getString("category");
        String max =getArguments().getString("max");


        if(!cinemaName.equals("Chọn rạp chiếu") && category.equals("Chọn thể loại") && max.isEmpty())
        {
            UpdateScreenbyCinema(cinemaName);
        }
        else if(cinemaName.equals("Chọn rạp chiếu") && !category.equals("Chọn thể loại") && max.isEmpty())
        {
            UpdateScreenbyCategory(category);
        }
        else if(cinemaName.equals("Chọn rạp chiếu") && category.equals("Chọn thể loại") && !max.isEmpty())
        {
            UpdateScreenbyPrice(max);
        }
        else if(!cinemaName.equals("Chọn rạp chiếu") && category.equals("Chọn thể loại") && !max.isEmpty())
        {
            UpdateScreenbyPriceandCinema(cinemaName, max);
        }
        else if(cinemaName.equals("Chọn rạp chiếu") && !category.equals("Chọn thể loại") && !max.isEmpty())
        {
            UpdateScreenbyCategoryandPrice(category, max);
        }
        else if(!cinemaName.equals("Chọn rạp chiếu") && !category.equals("Chọn thể loại") && max.isEmpty())
        {
            UpdateScreenbyCategoryandCinema(category, cinemaName);
        }
        else if(!cinemaName.equals("Chọn rạp chiếu") && !category.equals("Chọn thể loại") && !max.isEmpty())
        {
            UpdateScreenbyAll(cinemaName,category,max);
        }
        else
        {
            ShowAllMovies();
        }



        RecyclerViewFiltered myAdapter = new RecyclerViewFiltered(getContext(),lstBook);
        myrv.setLayoutManager(new GridLayoutManager(getContext(),3));
        myrv.setAdapter(myAdapter);



        return v;
    }

    private void ShowAllMovies()
    {
        Cursor res = null;

        try {
            res = myDb.getAllMovies();
            lstBook = new ArrayList<>();


            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                MovieId = String.valueOf(res.getString(0));
                MovieName = String.valueOf(res.getString(1));
                byte [] image = getMovieImage(MovieId);
                lstBook.add(new Movies(MovieId,MovieName,image));

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

    private void UpdateScreenbyAll(String cinemaName, String category, String max)
    {
        String cinema_id = getCinemaId(cinemaName);
        Cursor res = null;
        try {
             res = myDb.getAllFilteredMovies(cinema_id,category,max);
            lstBook = new ArrayList<>();


            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                MovieId = String.valueOf(res.getString(0));
                MovieName = getMovieName(MovieId);
                byte [] image = getMovieImage(MovieId);
                lstBook.add(new Movies(MovieId,MovieName,image));

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

    private void UpdateScreenbyCategoryandCinema(String cat, String cin_name)
    {
        Cursor res = null;
        String cinema_id = getCinemaId(cin_name);

        try {
            res = myDb.getAllMoviesbyCategoryandCinema(cat,cinema_id);
            lstBook = new ArrayList<>();


            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                MovieId = String.valueOf(res.getString(0));
                MovieName = getMovieName(MovieId);
                byte [] image = getMovieImage(MovieId);
                lstBook.add(new Movies(MovieId,MovieName,image));

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

    private void UpdateScreenbyCategoryandPrice(String c, String p)
    {
        Cursor res = null;

        lstBook = new ArrayList<>();

        try {
            res = myDb.getAllMoviesbyCategoryPrice(c,p);
            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                MovieId = String.valueOf(res.getString(0));
                MovieName = String.valueOf(res.getString(1));
                byte [] image = getMovieImage(MovieId);
                lstBook.add(new Movies(MovieId,MovieName,image));
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

    private void UpdateScreenbyPriceandCinema(String cinema_name, String price)
    {

        Cursor res = null;
        String cinema_id = getCinemaId(cinema_name);
        lstBook = new ArrayList<>();

        try {
            res = myDb.getAllMoviesbyCinemaandPrice(cinema_id, price);
            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                MovieId = String.valueOf(res.getString(0));
                MovieName = getMovieName(MovieId);
                byte [] image = getMovieImage(MovieId);
                lstBook.add(new Movies(MovieId,MovieName,image));

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

    private void UpdateScreenbyPrice(String price)
    {
        Cursor res = null;
        lstBook = new ArrayList<>();

        try {
            res = myDb.getAllMoviesunderPrice(price);
            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                MovieId = String.valueOf(res.getString(0));
                MovieName = getMovieName(MovieId);
                byte [] image = getMovieImage(MovieId);
                lstBook.add(new Movies(MovieId,MovieName,image));

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

    private void UpdateScreenbyCategory(String category)
    {
        Cursor res = null;
        lstBook = new ArrayList<>();

        try {
            res = myDb.getAllMoviesbyCategory(category);
            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                MovieId = String.valueOf(res.getString(0));
                MovieName = String.valueOf(res.getString(1));
                byte [] image = getMovieImage(MovieId);
                lstBook.add(new Movies(MovieId,MovieName,image));

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

    private void UpdateScreenbyCinema(String cinemaName)
    {
        Cursor res = null;
        String CinemaId = getCinemaId(cinemaName);


        try {
            res = myDb.getAllShowingsInCinema(CinemaId);
            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                MovieId = String.valueOf(res.getString(0));
                MovieName = getMovieName(MovieId);
                byte [] image = getMovieImage(MovieId);
                lstBook.add(new Movies(MovieId,MovieName,image));

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

    private String getCinemaId(String name)
    {
        Cursor res = myDb.getAllCinemas();
        String id = "";

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (name.equals(String.valueOf(res.getString(1)))) {
                id = String.valueOf(res.getString(0));
            }
        }



        return id;
    }


}

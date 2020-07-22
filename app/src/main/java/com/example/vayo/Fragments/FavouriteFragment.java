package com.example.vayo.Fragments;


import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.Model.Movies;
import com.example.vayo.Prevalent.Prevalent;
import com.example.vayo.R;
import com.example.vayo.Recycler.RecyclerAdapterFavourite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class FavouriteFragment extends Fragment {
    List<Movies> lstFavourite ;
    private DatabaseHelper myDb;

    private String USER_ID;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View productView = inflater.inflate(R.layout.fragment_favourite, container, false);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initializez baza de date
        myDb = new DatabaseHelper(getContext());


        //initializez jurnalul
        Paper.init(getContext());

        //fac legatura dintre interfata si cod
        RecyclerView myrv =  productView.findViewById(R.id.favourite_rv);

        UpdateScreen();

        //pun lista in Recycler
        RecyclerAdapterFavourite myAdapter = new RecyclerAdapterFavourite(getContext(),lstFavourite);
        myrv.setLayoutManager(new GridLayoutManager(getContext(),1));
        myrv.setAdapter(myAdapter);






        return productView;
    }




    //functie ce mi returneaza id ul user-ului
    private String getUserId(String email) {
        String id = "";
        //citesc toate datele despre useri
        Cursor res = myDb.getAllData();


        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            //verific daca email ul exista in BD
            if (email.equals(String.valueOf(res.getString(1))))
                id = String.valueOf(res.getString(0));

        }


        return id;
    }




    private void UpdateScreen()
    {

        Cursor res = null;
        try{
            res = myDb.getAllFavourite();
            //preiau email-ul user ului conectat din jurnal
            String UserEmailKey = Paper.book().read(Prevalent.UserEmailKey);
            USER_ID = getUserId(UserEmailKey);
            lstFavourite = new ArrayList<>();


            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                //verific daca gasesc user id ul in BD
                if(USER_ID.equals(String.valueOf(res.getString(1))))
                {
                    String id = String.valueOf(res.getString(0));
                    String name = getMovieName(String.valueOf(res.getString(0)));
                    String price = getMoviePrice(String.valueOf(res.getString(0)));
                    byte [] image = getMovieImage(String.valueOf(res.getString(0)));
                    lstFavourite.add(new Movies(id,name,"","",price,"", image));

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

    //primesc numele filmului
    private String getMovieName(String id) {
        String name = "";
        Cursor res = myDb.getAllMovies();


        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (id.equals(String.valueOf(res.getString(0))))
                name = String.valueOf(res.getString(1));

        }


        return name;
    }

    //primesc pretul fimului
    private String getMoviePrice(String id) {
        String price = "";
        Cursor res = myDb.getAllMovies();


        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (id.equals(String.valueOf(res.getString(0))))
                price = String.valueOf(res.getString(4));

        }


        return price;
    }






}

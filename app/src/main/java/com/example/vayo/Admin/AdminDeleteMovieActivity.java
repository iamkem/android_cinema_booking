package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AdminDeleteMovieActivity extends AppCompatActivity {

    private Spinner deleteMovieSpinner;
    private DatabaseHelper myDb;

    private String MovieName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_movie);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
        //initializez baza de date
        myDb = new DatabaseHelper(this);
        //fac legatura dintre cod si interfata vizuala
        Button deleteMovieButton = findViewById(R.id.delete_movie_button);
        deleteMovieSpinner = findViewById(R.id.delete_movie_spinner);

        //setez lista cu filme
        SetSpinnerList();

        deleteMovieSpinner.setVisibility(View.VISIBLE);
        //preiau item-ul selectat
        deleteMovieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                //stochez valoarea intr o variabila globala
                MovieName = parent.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        //actionare buton stergere
        deleteMovieButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ConfirmdeleteMovie(MovieName);


            }
        });


    }

    //Functie Stergere film
    private void ConfirmdeleteMovie(String movieName)
    {
        //Accesare toate filmele
        Cursor res = null;
        try {
            res = myDb.getAllMovies();
            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                String name = String.valueOf(res.getString(1));

                //verific daca l am gasit in baza de date
                if(movieName.equals(name))
                {
                    //sterg filmul gasit din baza de date
                    Integer deletedRows = myDb.deleteMovie(res.getString(0));
                    if(deletedRows > 0) {
                        Toast.makeText(AdminDeleteMovieActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminDeleteMovieActivity.this, AdminDeleteMovieActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(AdminDeleteMovieActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(AdminDeleteMovieActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally
        {
            if(res !=null)
            {
                res.close();
            }
        }



    }

    //setez lista cu filme
    private void SetSpinnerList()
    {
        //accesez baza de date
        Cursor res = myDb.getAllMovies();
        //initializez lista
        ArrayList<String> arrayList1 = new ArrayList<>();

        //pun in lista fiecare film gasit
        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(1)));
        }
        //in caz ca lista este goala specific utilizatorului ca nu exista niciun film
        if(arrayList1.isEmpty())
        {
            arrayList1.add("No movie in database");
        }

        res.close();


        //pun lista in spinner
        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        deleteMovieSpinner.setAdapter(adp);

    }


}

package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.R;


import java.lang.reflect.Field;
import java.util.ArrayList;

public class AdminDeleteShowingsActivity extends AppCompatActivity {

    private Spinner spinner;
    private DatabaseHelper myDb;
    private TextView ShowMovie,ShowCinema,ShowFrom,ShowTo,ShowTime;

    private String showID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_showings);

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
        spinner = findViewById(R.id.delete_showings_spinner);
        ShowMovie = findViewById(R.id.delete_showings_movie);
        ShowCinema = findViewById(R.id.delete_showings_cinema);
        ShowFrom = findViewById(R.id.delete_showings_from_date);
        ShowTo = findViewById(R.id.delete_showings_to_date);
        ShowTime = findViewById(R.id.delete_showings_time_start);
        Button deleteShowings = findViewById(R.id.delete_showing_button);


        //Setez lista cu show-uri
        SetSpinnerList();
        spinner.setVisibility(View.VISIBLE);

        //preiau item ul selectat
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                showID = parent.getItemAtPosition(position).toString();
                updateScreen(showID);

            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });


        //actionez buton stergere show
        deleteShowings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ConfirmDeleteShowings(showID);

            }
        });



    }

    private void ConfirmDeleteShowings(String showID)
    {
        //accesez lista cu toate show-urile
        Cursor res = null;

        try {
            res = myDb.getAllShowings();

            for(int i=0;i<res.getCount();i++)
            {
                //in caz ca l am gasit il sterg
                res.moveToPosition(i);
                if(showID.equals(String.valueOf(res.getString(0))))
                {
                    Integer deletedRows = myDb.deleteShowings(res.getString(0));
                    if(deletedRows > 0) {
                        Toast.makeText(AdminDeleteShowingsActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminDeleteShowingsActivity.this, AdminDeleteShowingsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(AdminDeleteShowingsActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(AdminDeleteShowingsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally
        {
            if(res !=null)
            {
                res.close();
            }
        }

    }

    //dupa o eventuala stergere a unui show update la ecran cu noile informatii din baza de date
    @SuppressLint("SetTextI18n")
    private void updateScreen(String id)
    {
        String movieId ="";
        String cinemaId ="";
        //Accesez show-urile filmele si cinema-urile
        Cursor res = myDb.getAllShowings();
        Cursor res_movie = myDb.getAllMovies();
        Cursor res_cinema = myDb.getAllCinemas();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            if(id.equals(String.valueOf(res.getString(0))))
            {
                movieId = String.valueOf(res.getString(1));
                cinemaId = String.valueOf(res.getString(2));
                ShowFrom.setText("From: " + res.getString(3));
                ShowTo.setText("To: " + res.getString(4));
                ShowTime.setText("Time start: " + res.getString(5));

            }
        }

        for(int i=0;i<res_movie.getCount();i++)
        {
            res_movie.moveToPosition(i);
            if(movieId.equals(String.valueOf(res_movie.getString(0))))
            {
               ShowMovie.setText("Movie Name: " + res_movie.getString(1));
            }
        }

        for(int i=0;i<res_cinema.getCount();i++)
        {
            res_cinema.moveToPosition(i);
            if(cinemaId.equals(String.valueOf(res_cinema.getString(0))))
            {
                ShowCinema.setText("Cinema Name: " + res_cinema.getString(1));
            }
        }

        res.close();
        res_cinema.close();
        res_movie.close();




    }

    //setez lista spinner ului cu toate show urile
    private void SetSpinnerList()
    {
        Cursor res = myDb.getAllShowings();
        ArrayList<String> arrayList1 = new ArrayList<>();

        //trec in lista toate show-urile
        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(0)));
        }
        if(arrayList1.isEmpty())
        {
            arrayList1.add("No showings in database");
        }

        //pun in spinner lista
        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        spinner.setAdapter(adp);
    }


}

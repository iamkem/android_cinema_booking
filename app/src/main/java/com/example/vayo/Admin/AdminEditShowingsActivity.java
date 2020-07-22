package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

public class AdminEditShowingsActivity extends AppCompatActivity {
    private Spinner spinner, MovieSpinner, CinemaSpinner;
    private Button editButton;
    private TextView ShowFrom,ShowTo,ShowTime;
    private TextView t2,t3;

    DatePickerDialog picker;
    TimePickerDialog timePicker;
    private DatabaseHelper myDb;

    private String idShow, MovieName,CinemaName, From,To,Time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_showings);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initializare baza de date
        myDb = new DatabaseHelper(this);

        //legatura dintre cod si inerfata vizuala
        ShowFrom = findViewById(R.id.edit_showings_from_date);
        ShowTo = findViewById(R.id.edit_showings_to_date);
        ShowTime = findViewById(R.id.edit_showings_time_start);
        editButton = findViewById(R.id.edit_showings_button);
        spinner = findViewById(R.id.edit_showings_spinner);
        MovieSpinner = findViewById(R.id.edit_showings_movie_spinner);
        CinemaSpinner = findViewById(R.id.edit_showings_cinema_spinner);
        t2 = findViewById(R.id.simpletext2);
        t3 = findViewById(R.id.simpletext3);


        //setez lista pentru filme si cinemauri
        SetSpinnerList();
        SetMovieSpinnerList();
        SetCinemaSpinnerList();


        spinner.setVisibility(View.VISIBLE);
        MovieSpinner.setVisibility(View.VISIBLE);
        CinemaSpinner.setVisibility(View.VISIBLE);

        //setare data incepere
        ShowFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdminEditShowingsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                ShowFrom.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();

            }
        });


        //setare data sfarsit
        ShowTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdminEditShowingsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                ShowTo.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();

            }
        });

        //setare ora incepere
        ShowTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                timePicker = new TimePickerDialog(AdminEditShowingsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                ShowTime.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                timePicker.show();

            }
        });


        //preluare film
        MovieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                MovieName = parent.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        //preluare cinema
        CinemaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                CinemaName = parent.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                idShow = parent.getItemAtPosition(position).toString();
                updateScreen(idShow);

            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               SaveChanges();

            }
        });


    }

    //salvare modificari
    private void SaveChanges()
    {
        From = ShowFrom.getText().toString();
        To = ShowTo.getText().toString();
        Time = ShowTime.getText().toString();
        MovieName = MovieSpinner.getSelectedItem().toString();
        CinemaName = CinemaSpinner.getSelectedItem().toString();


        //verificare eventuale nereguli de introducere
        if (TextUtils.isEmpty(From))
        {
            Toast.makeText(this, "Please write from date...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(To))
        {
            Toast.makeText(this, "Please write to date...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Time))
        {
            Toast.makeText(this, "Please write time...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreShowings(From,To,Time);
        }
    }


    //functie de preluare id film
    String get_movie_id(String name)
    {
        String id= "";
        //Accesare tabel cu toate filmele
        Cursor res = myDb.getAllMovies();


        for(int i=0;i<res.getCount();i++)
        {
            //verific daca numele introdus corespunde cu alt nume de film din baza de date
            res.moveToPosition(i);
            if(name.equals(String.valueOf(res.getString(1))))
            {
                id = String.valueOf(res.getString(0));
            }
        }

        res.close();

        return id;
    }

    //primire id cinema in functie de numele cinema-ului
    String get_cinema_id(String name)
    {
        String id= "";
        Cursor res = myDb.getAllCinemas();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            if(name.equals(String.valueOf(res.getString(1))))
            {
                id = String.valueOf(res.getString(0));
            }
        }

        res.close();

        return id;
    }

    //stocare show
    private void StoreShowings(String from, String to, String time)
    {
        //preiau toate show urile din baza de date
        Cursor res = myDb.getAllShowings();
        String movie_id = get_movie_id(MovieName);
        String cinema_id = get_cinema_id(CinemaName);



        for(int i=0;i<res.getCount();i++)
        {
            //verific daca id ul show-ului corespunde cu alt id din baza de date
            res.moveToPosition(i);
            if(idShow.equals(String.valueOf(res.getString(0))))
            {
                try {
                    myDb.updateShowings(String.valueOf(res.getString(0)),movie_id,cinema_id,from,to,time);
                    Toast.makeText(AdminEditShowingsActivity.this, "Data Update", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminEditShowingsActivity.this,AdminEditShowingsActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e)
                {
                    Toast.makeText(AdminEditShowingsActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                }

            }
        }

        res.close();
    }

    //Actualizare informatii
    @SuppressLint("SetTextI18n")
    private void updateScreen(String idShow)
    {

        String id_movie = "";
        String id_cinema = "";

        if(spinner.getFirstVisiblePosition() == 0)
        {
           ShowTo.setText("Showing from date...");
           ShowFrom.setText("Showing to date...");
           ShowTime.setText("Showing time start...");

        }
        Cursor res = null;
        Cursor res_m = null;
        Cursor res_c = null;

        try{
            res = myDb.getAllShowings();
            res_m = myDb.getAllMovies();
            res_c = myDb.getAllCinemas();


            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                if(idShow.equals(String.valueOf(res.getString(0))))
                {
                    id_movie = String.valueOf(res.getString(1));
                    id_cinema = String.valueOf(res.getString(2));
                    ShowFrom.setText(String.valueOf(res.getString(3)));
                    ShowTo.setText(String.valueOf(res.getString(4)));
                    ShowTime.setText(String.valueOf(res.getString(5)));
                }
            }

            //setez numele filmului
            for(int i=0;i<res_m.getCount();i++)
            {
                res_m.moveToPosition(i);
                if(id_movie.equals(String.valueOf(res_m.getString(0))))
                {
                    MovieName = String.valueOf(res_m.getString(1));
                    t2.setText("Current Movie: " + res_m.getString(1));
                }
            }

            //setez numele cinema-ului
            for(int i=0;i<res_c.getCount();i++)
            {
                res_c.moveToPosition(i);
                if(id_cinema.equals(String.valueOf(res_c.getString(0))))
                {
                    CinemaName = String.valueOf(res_c.getString(1));
                    t3.setText("Current Cinema: " + res_c.getString(1));

                }
            }
        }catch (Exception e)
        {
            Toast.makeText(AdminEditShowingsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }

    //setez spinner ul cu filme
    private void SetMovieSpinnerList()
    {
        Cursor res = myDb.getAllMovies();
        ArrayList<String> arrayList2 = new ArrayList<>();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList2.add(String.valueOf(res.getString(1)));
        }
        if(arrayList2.isEmpty())
        {
            arrayList2.add("No movie in database");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList2);
        MovieSpinner.setAdapter(adp);
    }

    //setez spinner ul cu cinema-uri
    private void SetCinemaSpinnerList()
    {
        Cursor res = myDb.getAllCinemas();
        ArrayList<String> arrayList3 = new ArrayList<>();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList3.add(String.valueOf(res.getString(1)));
        }
        if(arrayList3.isEmpty())
        {
            arrayList3.add("No cinema in database");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList3);
        CinemaSpinner.setAdapter(adp);
    }

    //setez spinner ul cu show uri
    private void SetSpinnerList()
    {
        Cursor res = myDb.getAllShowings();
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Please choose a showing");

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(0)));
        }
        if(arrayList1.isEmpty())
        {
            arrayList1.add("No showings in database");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        spinner.setAdapter(adp);

    }

}

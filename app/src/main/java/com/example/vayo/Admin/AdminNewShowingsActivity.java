package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

public class AdminNewShowingsActivity extends AppCompatActivity {
    private Spinner addMovieSpinner;
    private Spinner addCinemaSpinner;
    private DatabaseHelper myDb;

    private TextView FromDate,ToDate, TimeStart;
    DatePickerDialog picker;
    TimePickerDialog timePicker;


    private String MovieName, CinemaName;
    private String fromDateInput,toDateInput, timeStartInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_showings);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initializare baza de date
        myDb = new DatabaseHelper(this);

        //stabilizarea legaturii dintre cod si interfata vizuala
        addCinemaSpinner = findViewById(R.id.add_showings_cinema_spinner);
        addMovieSpinner = findViewById(R.id.add_showings_movie_spinner);
        Button addShowingsButton = findViewById(R.id.add_showings_button);
        FromDate = findViewById(R.id.add_showings_from_date);
        ToDate = findViewById(R.id.add_showings_to_date);
        TimeStart = findViewById(R.id.add_showings_time_start);

        //setare spinner cu filme si spinner cu cinema-uri
        SetMovieSpinnerList();
        SetCinemaSpinnerList();

        addCinemaSpinner.setVisibility(View.VISIBLE);
        addMovieSpinner.setVisibility(View.VISIBLE);


        //preiau numele filmului din spinner
        addMovieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

        //preiau numele cinema-ului din spinner
        addCinemaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

        //preiau data de inceput
        FromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdminNewShowingsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                FromDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();

            }
        });

        //preiau data de sfarsit
        ToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdminNewShowingsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                ToDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        //preiau ora de start
        TimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                timePicker = new TimePickerDialog(AdminNewShowingsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                TimeStart.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                timePicker.show();

            }
        });

        addShowingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
              ValidateShowingData();
            }
        });

    }

    private void ValidateShowingData()
    {

        fromDateInput = FromDate.getText().toString();
        toDateInput = ToDate.getText().toString();
        timeStartInput = TimeStart.getText().toString();

        //verific corectitudinea datelor
        if (TextUtils.isEmpty(fromDateInput))
        {
            Toast.makeText(this, "Please write from date ...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(toDateInput))
        {
            Toast.makeText(this, "Please write to date...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(timeStartInput))
        {
            Toast.makeText(this, "Please write time start...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreShowings(fromDateInput,toDateInput,timeStartInput);
        }
    }


    //preiau id ul filmului prin intermediul numelui filmului
    String get_movie_id(String name)
    {
        String id= "";
        Cursor res = myDb.getAllMovies();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            if(name.equals(String.valueOf(res.getString(1))))
            {
              id = String.valueOf(res.getString(0));
            }
        }

        return id;
    }

    //preiau id ul cinemaului prin intermediul numelui cinemaului
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

        return id;
    }


    //Stochez Show-ul
    private void StoreShowings(String from, String to, String time)
    {

        String m_id = get_movie_id(MovieName);
        String c_id = get_cinema_id(CinemaName);


            try {
                //incerc introducerea showului
                myDb.insertShowings(m_id, c_id, from, to, time);
                Toast.makeText(AdminNewShowingsActivity.this, "Showing was added successfully", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(AdminNewShowingsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }

    }


    //setez lista cu filme
    private void SetMovieSpinnerList()
    {
        //citesc toate filmele din BD
        Cursor res = myDb.getAllMovies();
        ArrayList<String> arrayList1 = new ArrayList<>();

        for(int i=0;i<res.getCount();i++)
        {
            //adaug fiecare film in lista
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(1)));
        }
        if(arrayList1.isEmpty())
        {
            arrayList1.add("No movie in database");
        }

        //pun lista cu filme in spinner
        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        addMovieSpinner.setAdapter(adp);

    }

    //setez lista cu cinemauri
    private void SetCinemaSpinnerList()
    {
        //citesc toate cinemaurile din BD
        Cursor res = myDb.getAllCinemas();
        ArrayList<String> arrayList2 = new ArrayList<>();

        for(int i=0;i<res.getCount();i++)
        {
            //pun fiecare cinema in lista
            res.moveToPosition(i);
            arrayList2.add(String.valueOf(res.getString(1)));
        }
        if(arrayList2.isEmpty())
        {
            arrayList2.add("No cinema in database");
        }

        //pun in spinner lista cu cinemauri
        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList2);
        addCinemaSpinner.setAdapter(adp);

    }
}

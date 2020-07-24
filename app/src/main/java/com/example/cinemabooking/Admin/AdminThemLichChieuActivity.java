package com.example.cinemabooking.Admin;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cinemabooking.Database.DatabaseHelper;
import com.example.cinemabooking.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

public class AdminThemLichChieuActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_admin_them_lich_chieu);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        myDb = new DatabaseHelper(this);

        addCinemaSpinner = findViewById(R.id.add_showings_cinema_spinner);
        addMovieSpinner = findViewById(R.id.add_showings_movie_spinner);
        Button addShowingsButton = findViewById(R.id.add_showings_button);
        FromDate = findViewById(R.id.add_showings_from_date);
        ToDate = findViewById(R.id.add_showings_to_date);
        TimeStart = findViewById(R.id.add_showings_time_start);

        SetMovieSpinnerList();
        SetCinemaSpinnerList();

        addCinemaSpinner.setVisibility(View.VISIBLE);
        addMovieSpinner.setVisibility(View.VISIBLE);


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

        FromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdminThemLichChieuActivity.this,
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

        ToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdminThemLichChieuActivity.this,
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

        TimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);

                timePicker = new TimePickerDialog(AdminThemLichChieuActivity.this,
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

        //Validate
        if (TextUtils.isEmpty(fromDateInput))
        {
            Toast.makeText(this, "Vui lòng nhập ngày khởi chiếu ...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(toDateInput))
        {
            Toast.makeText(this, "Vui lòng nhập ngày kết thúc...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(timeStartInput))
        {
            Toast.makeText(this, "Vui lòng nhập suất chiếu...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreShowings(fromDateInput,toDateInput,timeStartInput);
        }
    }


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


    private void StoreShowings(String from, String to, String time)
    {

        String m_id = get_movie_id(MovieName);
        String c_id = get_cinema_id(CinemaName);


            try {
                myDb.insertShowings(m_id, c_id, from, to, time);
                Toast.makeText(AdminThemLichChieuActivity.this, "Thêm lịch chiếu thành công!", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(AdminThemLichChieuActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }

    }


    private void SetMovieSpinnerList()
    {
        Cursor res = myDb.getAllMovies();
        ArrayList<String> arrayList1 = new ArrayList<>();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(1)));
        }
        if(arrayList1.isEmpty())
        {
            arrayList1.add("Không có phim nào");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        addMovieSpinner.setAdapter(adp);

    }

    private void SetCinemaSpinnerList()
    {
        Cursor res = myDb.getAllCinemas();
        ArrayList<String> arrayList2 = new ArrayList<>();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList2.add(String.valueOf(res.getString(1)));
        }
        if(arrayList2.isEmpty())
        {
            arrayList2.add("Không có rạp nào");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList2);
        addCinemaSpinner.setAdapter(adp);

    }
}

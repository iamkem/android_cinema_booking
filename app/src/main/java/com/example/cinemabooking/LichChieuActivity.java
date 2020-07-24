package com.example.cinemabooking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinemabooking.Database.DatabaseHelper;
import com.example.cinemabooking.Prevalent.Prevalent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


import io.paperdb.Paper;

public class LichChieuActivity extends AppCompatActivity {
    private String movieName, cinemaName;
    private String BookForDate, SeatCount;


    private TextView MovieName, CinemaName;
    private Spinner spinner, spinner2;
    private DatabaseHelper myDb;

    private String ShowingId, FromDate,ToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_chieu);
        Paper.init(this);

        myDb = new DatabaseHelper(this);


        MovieName = findViewById(R.id.movie_name);
        CinemaName = findViewById(R.id.cinema_name);
        spinner = findViewById(R.id.booking_for_date_spinner);
        spinner2 = findViewById(R.id.seat_count_spinner);
        Button bookButton = findViewById(R.id.book_showing);



        GetBookingDetails();
        getShowingId();
        GetSpinnerList();
        GetSpinnerList2();
        spinner.setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.VISIBLE);


        bookButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BookShowing();
            }
        });
    }


    private String getUserId(String name)
    {
        String id = "";

        Cursor res = myDb.getAllData();

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (name.equals(String.valueOf(res.getString(1))))
                id = String.valueOf(res.getString(0));
        }


        return id;
    }

    private void BookShowing()
    {
        //Thêm ngày & giờ
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        String currentTime = date.format(currentLocalTime);

        String UserEmailKey = Paper.book().read(Prevalent.UserEmailKey);
        String UserId = getUserId(UserEmailKey);

        SeatCount = String.valueOf(spinner2.getSelectedItem());
        BookForDate = String.valueOf(spinner.getSelectedItem());



        try{
            myDb.insertBookings(
                    UserId,
                    ShowingId,BookForDate,currentDate,
                    currentTime,SeatCount,"no"
            );
            Toast.makeText(getApplicationContext(), "Thêm lịch chiếu thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LichChieuActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void GetSpinnerList2()
    {

        ArrayList<String> arrayList1 = new ArrayList<>();
            for(int i=1;i<=10;i++)
                arrayList1.add(String.valueOf(i));


        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        spinner2.setAdapter(adp);

    }


    //Trả về ID phim
    public String getMovieId(String name) {
        String n = "";
        Cursor res = myDb.getAllMovies();

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (name.equals(String.valueOf(res.getString(1))))
                n = String.valueOf(res.getString(0));
        }

        return n;
    }

    //Trả về ID rạp chiếu
    public String getCinemaId(String name) {
        String n = "";
        Cursor res = myDb.getAllCinemas();

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (name.equals(String.valueOf(res.getString(1))))
                n = String.valueOf(res.getString(0));
        }

        return n;
    }


    //Trả về ID lịch chiếu tùy thuộc vào ID phim và ID rạp
    private void getShowingId()
    {
        String movieId = getMovieId(movieName);
        String cinemaId = getCinemaId(cinemaName);

        Cursor res = myDb.getAllShowings();

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            if (movieId.equals(String.valueOf(res.getString(1))) && cinemaId.equals(String.valueOf(res.getString(2)))) {
                ShowingId = String.valueOf(res.getString(0));
                FromDate = String.valueOf(res.getString(3));
                ToDate = String.valueOf(res.getString(4));
            }
        }

    }

    private void GetSpinnerList()
    {

        ArrayList<Date> dates = getDates(String.valueOf(FromDate), String.valueOf(ToDate));
        ArrayList<String> arrayList1 = new ArrayList<>();
        for (Date date : dates) {
            String dateStr = dateToString(date);
            arrayList1.add(dateStr);
        }

        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        spinner.setAdapter(adp);
    }

    //Chuyển dữ liệu ngày & giờ thành một chuỗi
    public static String dateToString(Date date) {
        String convertedDate = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            convertedDate = dateFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dateToString_EX", e + "");
        }

        return convertedDate;
    }

    @SuppressLint("SetTextI18n")
    private void GetBookingDetails()
    {
        movieName = getIntent().getStringExtra("MOVIE_ID");
        cinemaName = getIntent().getStringExtra("CINEMA_ID");

        MovieName.setText("Movie Name: " + movieName);
        CinemaName.setText("Cinema Name: " + cinemaName);


    }


    private static ArrayList<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("dd/mm/yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        assert date1 != null;
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        assert date2 != null;
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }


}

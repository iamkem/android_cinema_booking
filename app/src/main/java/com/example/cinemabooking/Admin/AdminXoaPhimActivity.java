package com.example.cinemabooking.Admin;

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

import com.example.cinemabooking.Database.DatabaseHelper;
import com.example.cinemabooking.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AdminXoaPhimActivity extends AppCompatActivity {

    private Spinner deleteMovieSpinner;
    private DatabaseHelper myDb;

    private String MovieName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_xoa_phim);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        myDb = new DatabaseHelper(this);
        Button deleteMovieButton = findViewById(R.id.delete_movie_button);
        deleteMovieSpinner = findViewById(R.id.delete_movie_spinner);

        SetSpinnerList();

        deleteMovieSpinner.setVisibility(View.VISIBLE);
        deleteMovieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

        deleteMovieButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ConfirmdeleteMovie(MovieName);


            }
        });


    }

    private void ConfirmdeleteMovie(String movieName)
    {
        Cursor res = null;
        try {
            res = myDb.getAllMovies();
            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                String name = String.valueOf(res.getString(1));

                if(movieName.equals(name))
                {
                    Integer deletedRows = myDb.deleteMovie(res.getString(0));
                    if(deletedRows > 0) {
                        Toast.makeText(AdminXoaPhimActivity.this, "Đã xóa", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminXoaPhimActivity.this, AdminXoaPhimActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(AdminXoaPhimActivity.this,"Dữ liệu chưa được xóa",Toast.LENGTH_LONG).show();

                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(AdminXoaPhimActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally
        {
            if(res !=null)
            {
                res.close();
            }
        }



    }

    private void SetSpinnerList()
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

        res.close();


        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        deleteMovieSpinner.setAdapter(adp);

    }


}

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

public class AdminDeleteCinemasActivity extends AppCompatActivity {
    private Spinner deleteCinemaSpinner;
    private DatabaseHelper myDb;

    private String CinemaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_cinemas);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initilizez baza de date
        myDb = new DatabaseHelper(this);

        //legatura dintre cod si interfata vizuala
        Button deleteCinemaButton = findViewById(R.id.delete_cinema_button);
        deleteCinemaSpinner = findViewById(R.id.delete_cinema_spinner);

        //setarea listei spinner-ului cu cinemauri
        SetSpinnerList();


        deleteCinemaSpinner.setVisibility(View.VISIBLE);

        //preluarea item-ului selectat
        deleteCinemaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                //punerea informatiei preluate intr o variabila globala
                CinemaName = parent.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        //actionarea butonului delete
        deleteCinemaButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ConfirmdeleteCinema();


            }
        });


    }

    //functie pentru stergerea cinemaului
    private void ConfirmdeleteCinema()
    {
        //accesare baza de date cu toate cinema-urile
        Cursor res = null;
        try{
            res = myDb.getAllCinemas();

            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                String name = String.valueOf(res.getString(1));

                //verific daca cinema-ul selectat de spinner se regaseste in vaza de date
                if(CinemaName.equals(name))
                {
                    //daca se respecta conditia il sterg din baza de date
                    Integer deletedRows = myDb.deleteCinema(res.getString(0));
                    if(deletedRows > 0) {
                        Toast.makeText(AdminDeleteCinemasActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminDeleteCinemasActivity.this, AdminDeleteCinemasActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(AdminDeleteCinemasActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                }
            }
        }catch (Exception e)
        {
            Toast.makeText(AdminDeleteCinemasActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally
        {
            if(res !=null)
            {
                res.close();
            }
        }


    }

    //setez lista cu cinema-uri
    private void SetSpinnerList()
    {
        //accesez baza de date cu toate cinema-urile
        Cursor res = myDb.getAllCinemas();
        //initializez lista
        ArrayList<String> arrayList1 = new ArrayList<>();

        // fiecare cinema gasit in baza de date il trec in lista
        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(1)));
        }
        //in caz ca lista este goala arat utilizatorului ca nu exista niciun cinema
        if(arrayList1.isEmpty())
        {
            arrayList1.add("No cinemas in database");
        }

        res.close();

        //pun lista in spinner
        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        deleteCinemaSpinner.setAdapter(adp);

    }
}

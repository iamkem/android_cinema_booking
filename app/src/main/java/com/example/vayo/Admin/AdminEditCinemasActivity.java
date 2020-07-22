package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AdminEditCinemasActivity extends AppCompatActivity {

    private String Cname,Manager,Address,Capacity;
    private Spinner editCinemaSpinner;
    private Button editCinemaButton;
    private EditText InputCinemaName,InputCinemaManagerName, InputCinemaAddress, InputCinemaCapacity;
    private DatabaseHelper myDb;

    private String CinemaName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_cinemas);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initializez baza de date
        myDb = new DatabaseHelper(this);

        //fac legatura dintre cod si baza de date
        editCinemaButton = findViewById(R.id.edit_cinema_button);
        editCinemaSpinner = findViewById(R.id.edit_cinema_spinner);
        InputCinemaName = findViewById(R.id.admin_edit_cinema_name);
        InputCinemaManagerName = findViewById(R.id.admin_edit_cinema_manager);
        InputCinemaAddress = findViewById(R.id.admin_edit_cinema_address);
        InputCinemaCapacity = findViewById(R.id.admin_edit_cinema_capacity);

        //setez lista cu cinemauri
        SetSpinnerList();

        editCinemaSpinner.setVisibility(View.VISIBLE);
        editCinemaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                //update la ecran la fiecare alegere de nume a cinema-ului
                CinemaName = parent.getItemAtPosition(position).toString();
                updateScreen(CinemaName);

            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        //buton pentru salvarea modificarilor
        editCinemaButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveChanges();
            }
        });



    }


    //salvare modificari la apasarea butonului
    private void SaveChanges()
    {
        Cname = InputCinemaName.getText().toString();
        Manager = InputCinemaManagerName.getText().toString();
        Address = InputCinemaAddress.getText().toString();
        Capacity = InputCinemaCapacity.getText().toString();

        //verific posibile greseli de
        if (TextUtils.isEmpty(Cname))
        {
            Toast.makeText(this, "Please write cinema name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Manager))
        {
            Toast.makeText(this, "Please write cinema manager...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Address))
        {
            Toast.makeText(this, "Please write cinema address...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Capacity))
        {
            Toast.makeText(this, "Please write cinema capacity...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreCinema(Cname,Manager,Address,Capacity);
        }

    }

    //modific cinema-ul
    private void StoreCinema(String n, String m, String a, String c)
    {
        //accesez toate cinemaurile
        Cursor res = null;
        try {
            res = myDb.getAllCinemas();

            for(int i=0;i<res.getCount();i++)
            {
                //verific daca am gasit cinema ul selectat in baza de date
                res.moveToPosition(i);
                if(CinemaName.equals(String.valueOf(res.getString(1))))
                {
                    try
                    {
                        //incerc modificarea datelor
                        myDb.updateCinema(String.valueOf(res.getString(0)),n,m,a,c);
                        Toast.makeText(AdminEditCinemasActivity.this, "Data Update", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminEditCinemasActivity.this,AdminEditCinemasActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(AdminEditCinemasActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }

                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(AdminEditCinemasActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally
        {
            if(res !=null)
            {
                res.close();
            }
        }

    }


    //actualizare informatii pe ecran
    private void updateScreen(String name)
    {
        if(editCinemaSpinner.getFirstVisiblePosition() == 0)
        {
            InputCinemaName.setText("");
            InputCinemaManagerName.setText("");
            InputCinemaAddress.setText("");
            InputCinemaCapacity.setText("");
        }
        Cursor res = myDb.getAllCinemas();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            if(name.equals(String.valueOf(res.getString(1))))
            {
                InputCinemaName.setText(String.valueOf(res.getString(1)));
                InputCinemaManagerName.setText(String.valueOf(res.getString(2)));
                InputCinemaAddress.setText(String.valueOf(res.getString(3)));
                InputCinemaCapacity.setText(String.valueOf(res.getString(4)));
            }
        }

        res.close();


    }


    //Setez lista cu cinemauri
    private void SetSpinnerList()
    {
        //preiau toate cinemaurile
        Cursor res = myDb.getAllCinemas();
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Please choose a cinema");

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(1)));
        }
        if(arrayList1.isEmpty())
        {
            arrayList1.add("No cinema in database");
        }

        res.close();
        //pun lista in spinner
        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        editCinemaSpinner.setAdapter(adp);

    }
}

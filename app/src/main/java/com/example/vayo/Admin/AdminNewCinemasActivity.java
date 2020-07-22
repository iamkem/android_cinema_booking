package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.MainActivity;
import com.example.vayo.R;
import com.example.vayo.RegisterActivity;

import java.lang.reflect.Field;

public class AdminNewCinemasActivity extends AppCompatActivity {

    private String Name, ManagerName, Address, Capacity;
    private EditText InputCinemaName, InputCinemaManagerName, InputCinemaAddress, InputCinemaCapacity;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_cinemas);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initializare baza de date
        myDb = new DatabaseHelper(this);


        //legatura dintre cod si interfata vizuala
        Button addNewCinemaButton = findViewById(R.id.add_new_cinema);
        InputCinemaName = findViewById(R.id.admin_add_cinema_name);
        InputCinemaManagerName = findViewById(R.id.admin_add_cinema_manager);
        InputCinemaAddress = findViewById(R.id.admin_add_cinema_address);
        InputCinemaCapacity = findViewById(R.id.admin_add_cinema_capacity);

        //actionare buton adaugare cinema
        addNewCinemaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateCinema();
            }
        });
    }

    private void ValidateCinema() {
        Name = InputCinemaName.getText().toString();
        ManagerName = InputCinemaManagerName.getText().toString();
        Address = InputCinemaAddress.getText().toString();
        Capacity = InputCinemaCapacity.getText().toString();

        //prevenire eventuale greseli de introducere
        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "Please write cinema name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ManagerName)) {
            Toast.makeText(this, "Please write cinema manager name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Address)) {
            Toast.makeText(this, "Please write cinema address..", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Capacity)) {
            Toast.makeText(this, "Please write cinema capacity...", Toast.LENGTH_SHORT).show();
        } else {
            StoreCinema();
        }
    }

    private void StoreCinema() {

        //daca totul este in regula
            try
            {
                //incerc sa introduc datele in baza de date
                myDb.insertCinema(Name, ManagerName, Address, Capacity);
                Toast.makeText(AdminNewCinemasActivity.this, "Cinema was added successfully", Toast.LENGTH_LONG).show();
                InputCinemaManagerName.setText("");
                InputCinemaName.setText("");
                InputCinemaAddress.setText("");
                InputCinemaCapacity.setText("");
            }
            catch (Exception e)
            {
                Toast.makeText(AdminNewCinemasActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }


        }
    }




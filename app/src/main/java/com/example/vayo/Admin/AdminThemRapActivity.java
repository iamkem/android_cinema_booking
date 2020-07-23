package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.database.CursorWindow;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.R;

import java.lang.reflect.Field;

public class AdminThemRapActivity extends AppCompatActivity {

    private String Name, ManagerName, Address, Capacity;
    private EditText InputCinemaName, InputCinemaManagerName, InputCinemaAddress, InputCinemaCapacity;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_them_rap);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }


        myDb = new DatabaseHelper(this);



        Button addNewCinemaButton = findViewById(R.id.add_new_cinema);
        InputCinemaName = findViewById(R.id.admin_add_cinema_name);
        InputCinemaManagerName = findViewById(R.id.admin_add_cinema_manager);
        InputCinemaAddress = findViewById(R.id.admin_add_cinema_address);
        InputCinemaCapacity = findViewById(R.id.admin_add_cinema_capacity);


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

        //Validate
        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "Vui lòng nhập tên rạp...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ManagerName)) {
            Toast.makeText(this, "Vui lòng nhập tên người quản lý...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Address)) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ rạp..", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Capacity)) {
            Toast.makeText(this, "Vui lòng nhập sức chứa của rạp...", Toast.LENGTH_SHORT).show();
        } else {
            StoreCinema();
        }
    }

    private void StoreCinema() {


            try
            {

                myDb.insertCinema(Name, ManagerName, Address, Capacity);
                Toast.makeText(AdminThemRapActivity.this, "Thêm rạp chiếu thành công", Toast.LENGTH_LONG).show();
                InputCinemaManagerName.setText("");
                InputCinemaName.setText("");
                InputCinemaAddress.setText("");
                InputCinemaCapacity.setText("");
            }
            catch (Exception e)
            {
                Toast.makeText(AdminThemRapActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }


        }
    }




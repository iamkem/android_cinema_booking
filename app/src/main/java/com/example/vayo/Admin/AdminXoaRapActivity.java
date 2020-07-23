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

public class AdminXoaRapActivity extends AppCompatActivity {
    private Spinner deleteCinemaSpinner;
    private DatabaseHelper myDb;

    private String CinemaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_xoa_rap);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }


        myDb = new DatabaseHelper(this);


        Button deleteCinemaButton = findViewById(R.id.delete_cinema_button);
        deleteCinemaSpinner = findViewById(R.id.delete_cinema_spinner);


        SetSpinnerList();


        deleteCinemaSpinner.setVisibility(View.VISIBLE);

        //Lấy rạp đã chọn
        deleteCinemaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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


        deleteCinemaButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ConfirmdeleteCinema();


            }
        });


    }


    private void ConfirmdeleteCinema()
    {
        //Lấy data từ DB
        Cursor res = null;
        try{
            res = myDb.getAllCinemas();

            for(int i=0;i<res.getCount();i++)
            {
                res.moveToPosition(i);
                String name = String.valueOf(res.getString(1));

                if(CinemaName.equals(name))
                {

                    Integer deletedRows = myDb.deleteCinema(res.getString(0));
                    if(deletedRows > 0) {
                        Toast.makeText(AdminXoaRapActivity.this, "Đã xóa dữ liệu", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminXoaRapActivity.this, AdminXoaRapActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(AdminXoaRapActivity.this,"Không được xóa dữ liệu",Toast.LENGTH_LONG).show();

                }
            }
        }catch (Exception e)
        {
            Toast.makeText(AdminXoaRapActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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

        Cursor res = myDb.getAllCinemas();

        ArrayList<String> arrayList1 = new ArrayList<>();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(1)));
        }

        if(arrayList1.isEmpty())
        {
            arrayList1.add("Không có rạp nào");
        }

        res.close();

        //pun lista in spinner
        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        deleteCinemaSpinner.setAdapter(adp);

    }
}

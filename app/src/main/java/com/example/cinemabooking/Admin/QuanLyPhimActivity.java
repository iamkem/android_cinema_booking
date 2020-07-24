package com.example.cinemabooking.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cinemabooking.R;

public class QuanLyPhimActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_phim);

        CardView addCard = findViewById(R.id.admin_add_movie);
        CardView deleteCard = findViewById(R.id.admin_delete_movie);
        CardView editCard = findViewById(R.id.admin_edit_movie);
        CardView logoutCard = findViewById(R.id.admin_back_to_main_admin_panel);


        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyPhimActivity.this, AdminThemPhimActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyPhimActivity.this, AdminXoaPhimActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyPhimActivity.this, AdminChinhSuaPhimActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyPhimActivity.this, AdminMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }





}

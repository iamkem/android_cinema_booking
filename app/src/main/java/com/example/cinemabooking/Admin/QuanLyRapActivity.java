package com.example.cinemabooking.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cinemabooking.R;

public class QuanLyRapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_rap);


        CardView addCard = findViewById(R.id.admin_add_cinema);
        CardView deleteCard = findViewById(R.id.admin_delete_cinema);
        CardView editCard = findViewById(R.id.admin_edit_cinema);
        CardView logoutCard = findViewById(R.id.admin_back_to_main_admin_panel);


        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyRapActivity.this, AdminThemRapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyRapActivity.this, AdminXoaRapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyRapActivity.this, AdminChinhSuaRapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyRapActivity.this, AdminMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}

package com.example.cinemabooking.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cinemabooking.R;

public class QuanLyLichChieuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_lich_chieu);

        CardView addCard = findViewById(R.id.admin_add_showings);
        CardView deleteCard = findViewById(R.id.admin_delete_showings);
        CardView editCard = findViewById(R.id.admin_edit_showings);
        CardView logoutCard = findViewById(R.id.admin_back_to_main_admin_panel);


        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyLichChieuActivity.this, AdminThemLichChieuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyLichChieuActivity.this, AdminXoaLichChieuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyLichChieuActivity.this, AdminChinhSuaLichChieuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(QuanLyLichChieuActivity.this, AdminMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}

package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.vayo.R;

public class CinemaManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_manage);


        //legatura dintre fiecare cardView din interfata vizuala cu codul
        CardView addCard = findViewById(R.id.admin_add_cinema);
        CardView deleteCard = findViewById(R.id.admin_delete_cinema);
        CardView editCard = findViewById(R.id.admin_edit_cinema);
        CardView logoutCard = findViewById(R.id.admin_back_to_main_admin_panel);


        //pentru fiecare cardView specific in ce fereastra se va trece la actionarea sa
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(CinemaManageActivity.this, AdminNewCinemasActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(CinemaManageActivity.this, AdminDeleteCinemasActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(CinemaManageActivity.this, AdminEditCinemasActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(CinemaManageActivity.this, AdminMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}

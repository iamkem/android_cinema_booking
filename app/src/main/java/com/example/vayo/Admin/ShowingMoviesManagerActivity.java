package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.vayo.R;

public class ShowingMoviesManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_movies_manager);

        //legatura dintre fiecare cardView din interfata vizuala cu codul
        CardView addCard = findViewById(R.id.admin_add_showings);
        CardView deleteCard = findViewById(R.id.admin_delete_showings);
        CardView editCard = findViewById(R.id.admin_edit_showings);
        CardView logoutCard = findViewById(R.id.admin_back_to_main_admin_panel);

        //pentru fiecare cardView specific in ce fereastra se va trece la actionarea sa

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(ShowingMoviesManagerActivity.this, AdminNewShowingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(ShowingMoviesManagerActivity.this, AdminDeleteShowingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(ShowingMoviesManagerActivity.this, AdminEditShowingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(ShowingMoviesManagerActivity.this, AdminMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}

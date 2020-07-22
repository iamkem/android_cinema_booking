package com.example.vayo.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.vayo.R;

public class MovieManageActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_manage);

        //legatura dintre fiecare cardView din interfata vizuala cu codul
        CardView addCard = findViewById(R.id.admin_add_movie);
        CardView deleteCard = findViewById(R.id.admin_delete_movie);
        CardView editCard = findViewById(R.id.admin_edit_movie);
        CardView logoutCard = findViewById(R.id.admin_back_to_main_admin_panel);

        //pentru fiecare cardView specific in ce fereastra se va trece la actionarea sa

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(MovieManageActivity.this, AdminNewMoviesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(MovieManageActivity.this, AdminDeleteMovieActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(MovieManageActivity.this, AdminEditMovieActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { Intent intent = new Intent(MovieManageActivity.this, AdminMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }





}

package com.example.vayo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.Model.MovieReview;
import com.example.vayo.Prevalent.Prevalent;
import com.example.vayo.Recycler.RecyclerViewReview;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class ShowingsDetailActivity extends AppCompatActivity {

    private TextView tvtitle, tvdescription, tvcategory, txtLength, txtPrice, txtRating;
    private DatabaseHelper myDb;
    private Spinner spinner;
    private ImageView movieImage;
    private ToggleButton FavouriteButton;

    private List<MovieReview> lstMovieReview ;




    private String MOVIE_ID, MOVIE_NAME, USER_ID;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showings_detail);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }


        //initializez jurnal
        Paper.init(this);

        //initializez BD
        myDb = new DatabaseHelper(this);

        //fac legatura dintre cod si interfata vizuala
        tvtitle = findViewById(R.id.txttitle);
        tvdescription =  findViewById(R.id.txtDesc);
        tvcategory =  findViewById(R.id.txtCategory);
        txtLength = findViewById(R.id.txtLength);
        txtPrice = findViewById(R.id.txtPrice);
        txtRating = findViewById(R.id.txtrating);
        spinner = findViewById(R.id.available_cinemas);
        movieImage = findViewById(R.id.movie_details_image);
        FloatingActionButton fab = findViewById(R.id.fab_button);
        FavouriteButton = findViewById(R.id.favourite_image);



        //Primesc date despre film
        Intent intent = getIntent();
        MOVIE_ID = Objects.requireNonNull(intent.getExtras()).getString("Id");
        ReceiveMovieDetails();



        RecyclerView myrv =  findViewById(R.id.review_rv);
        lstMovieReview = new ArrayList<>();

        UpdateScreen();

        //pun lista in Recycler
        RecyclerViewReview myAdapter = new RecyclerViewReview(this, lstMovieReview);
        myrv.setLayoutManager(new GridLayoutManager(this,1));
        myrv.setAdapter(myAdapter);





        //setez lista cu cinemauri disponibile pentru filmul curent
        SetSpinnerList();

        //buton pentru efectuarea unei rezervari
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!String.valueOf(spinner.getSelectedItem()).equals("No cinema for this movie")) {
                    Intent intent = new Intent(ShowingsDetailActivity.this, BookShowingActivity.class);
                    intent.putExtra("MOVIE_ID", MOVIE_NAME);
                    intent.putExtra("CINEMA_ID", String.valueOf(spinner.getSelectedItem()));
                    startActivity(intent);
                }
                else if(!String.valueOf(spinner.getSelectedItem()).equals(""))
                {
                    Toast.makeText(ShowingsDetailActivity.this, "No cinema for this movie", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(ShowingsDetailActivity.this, "No cinema for this movie", Toast.LENGTH_SHORT).show();
                }
            }

        });

        //Actionare button film favorit
        FavouriteButton.setChecked(false);
        FavouriteButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_no));
        FavouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AddToFavourite();
                    FavouriteButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_yes));
                }else {
                    DeleteFromFavourite();
                    FavouriteButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_no));
                }
                }
        });


    }

    private void UpdateScreen()
    {
        Cursor res = myDb.getAllMovieeReviews();
         lstMovieReview = new ArrayList<>();


        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            if(MOVIE_ID.equals(String.valueOf(res.getString(1)))) {
                String id = String.valueOf(res.getString(0));
                String movie_id = String.valueOf(res.getString(1));
                String review_description = String.valueOf(res.getString(2));
                String mark = String.valueOf(res.getString(3));
                lstMovieReview.add(new MovieReview(id, movie_id, review_description, mark));
            }

        }

    }


    //returneaza id ul user-ului
    private String getUserId(String email) {
        String id = "";
        //citesc toate datele din tabelul de users
        Cursor res = myDb.getAllData();

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            //verific daca email-ul corespunde cu vreun email din BD
            if (email.equals(String.valueOf(res.getString(1))))
                id = String.valueOf(res.getString(0));

        }


        return id;
    }

    //stergere filmul din favoritele userului
    private void DeleteFromFavourite() {
        //citesc toate filmele favorite
        Cursor res = myDb.getAllFavourite();

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            //verific daca acestea corespund
            if (USER_ID.equals(String.valueOf(res.getString(1))) && MOVIE_ID.equals(String.valueOf(res.getString(0)))) {
                myDb.deleteFavourite(String.valueOf(res.getString(0)), String.valueOf(res.getString(1)));
                Toast.makeText(getApplicationContext(), "Movie was deleted successfully to favourite!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //adaug filmul la favoritele userului
    private void AddToFavourite() {
        //preiau user ul curent din jurnal
        String UserEmailKey = Paper.book().read(Prevalent.UserEmailKey);
        USER_ID = getUserId(UserEmailKey); //aflu id ul userului curent


            try {
                //adaug filmul in BD
                myDb.insertFavourite(
                        MOVIE_ID,
                        USER_ID
                );
                Toast.makeText(getApplicationContext(), "Movie was added successfully to favourite!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }

    }


    //setez lista cu showuri
    private void SetSpinnerList() {
        //citesc din BD toate show-urile
        Cursor res = myDb.getAllShowings();
        ArrayList<String> arrayList1 = new ArrayList<>();
        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            //verific daca am gasit id ul filmului in BD
            if(MOVIE_ID.equals(String.valueOf(res.getString(1))))
            {
                arrayList1.add(getCinemaName(String.valueOf(res.getString(2))));
            }
        }

        if(arrayList1.isEmpty())
        {
            arrayList1.add("No cinema for this movie");
        }


        //pun in spinner lista cu show uri
        ArrayAdapter<String> adp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayList1);
        spinner.setAdapter(adp);
    }

    //returneza numele cinemaului primind ca parametru id ul cinemaului
    private String getCinemaName(String id){
        String name = "";
        Cursor res = myDb.getAllCinemas();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            //verific daca id ul corespunde cu id ul cinemaurilor din baza de date din tabelul cinema
            if(id.equals(String.valueOf(res.getString(0))))
            {
                name = String.valueOf(res.getString(1));
            }
        }


        return name;
    }

    //preiau informatiile despre filmul selectat
    @SuppressLint("SetTextI18n")
    private void ReceiveMovieDetails() {
        Cursor res = myDb.getAllMovies();
        Cursor res_review = myDb.getMovieAverage(MOVIE_ID);

        for (int i = 0; i < res.getCount(); i++) {
            res.moveToPosition(i);
            //verific daca id ul filmului corespunde cu vreun id din tabelul de cinemauri
            //in caz afirmativ preiau informatiile din BD
            if (MOVIE_ID.equals(String.valueOf(res.getString(0)))) {
                MOVIE_NAME = String.valueOf(res.getString(1));
                tvtitle.setText(String.valueOf(res.getString(1)));
                tvcategory.setText(String.valueOf(res.getString(2)));
                tvdescription.setText(String.valueOf(res.getString(3)));
                txtPrice.setText("Price: " + res.getString(4) + " €");
                byte [] image = res.getBlob(6);
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                }
                catch (Exception e)
                {
                    Toast.makeText(ShowingsDetailActivity.this, String.valueOf(movieImage),Toast.LENGTH_SHORT).show();
                }
                movieImage.setImageBitmap(bitmap);
                txtLength.setText("Length: " + res.getString(5) + " min");


            }
        }


        for(int i=0; i <res_review.getCount(); i++)
        {
            res_review.moveToPosition(i);
            txtRating.setText("Rating: " + res_review.getString(0) + "/10");

        }


    }

}

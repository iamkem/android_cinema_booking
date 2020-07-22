package com.example.vayo.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AdminEditMovieActivity extends AppCompatActivity {
    private String Mname,CategoryName, Description, Price, Length;
    private Spinner editMovieSpinner, spinner1;
    private EditText InputMovieName,InputMovieDescription, InputMoviePrice, InputMovieLength;
    private DatabaseHelper myDb;
    private TextView currentCategoty;


    private String MovieName;
    private String checked = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_movie);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initializez baza de date
        myDb = new DatabaseHelper(this);

        //fac legatura dintre cod si interfata vizuala
        Button editMovieButton = findViewById(R.id.edit_movie_button);
        editMovieSpinner = findViewById(R.id.edit_movie_spinner);
        InputMovieName = findViewById(R.id.edit_movie_name);
        InputMovieDescription = findViewById(R.id.edit_movie_description);
        InputMoviePrice = findViewById(R.id.edit_movie_price);
        InputMovieLength = findViewById(R.id.edit_movie_length);
        currentCategoty = findViewById(R.id.current_category);
        spinner1 = findViewById(R.id.spinner1);


        //preiau item ul selectat
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String select = (String) parent.getItemAtPosition(position);
                if(select.equals("Choose movie category...") || spinner1.getFirstVisiblePosition() == 0)
                {
                    checked = "false";
                }else
                {
                    checked = "true";
                    CategoryName = select;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        SetSpinnerList();

        //preiau itemul selectat
        editMovieSpinner.setVisibility(View.VISIBLE);
        editMovieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                MovieName = parent.getItemAtPosition(position).toString();
                updateScreen(MovieName);

            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        editMovieButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveChanges();
            }
        });


    }

    private void SaveChanges()
    {
        Mname = InputMovieName.getText().toString();
        Description = InputMovieDescription.getText().toString();
        Price = InputMoviePrice.getText().toString();
        Length = InputMovieLength.getText().toString();

        //verific eventualele nereguli
        if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write movie description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write movie Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Mname))
        {
            Toast.makeText(this, "Please write movie name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Length))
        {
            Toast.makeText(this, "Please write length of the movie...", Toast.LENGTH_SHORT).show();
        }
        else if(checked.equals("false"))
        {
            Toast.makeText(this, "Please choose a category...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreMovie(Mname,Description,Price,Length);
        }

    }

    private void StoreMovie(String n,String d,String p,String l)
    {
        //preiau toate filmele
        Cursor res = null;
        try {
            res = myDb.getAllMovies();

            for(int i=0;i<res.getCount();i++)
            {
                //verific daca am gasit numele filmului
                res.moveToPosition(i);
                if(MovieName.equals(String.valueOf(res.getString(1))))
                {
                    try
                    {
                        //incerc sa modific filmul
                        myDb.updateMovie(String.valueOf(res.getString(0)),n,CategoryName,d,p,l);
                        Toast.makeText(AdminEditMovieActivity.this, "Data Update", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminEditMovieActivity.this,AdminEditMovieActivity.class);
                        startActivity(intent);
                        finish();
                    }catch (Exception e)
                    {
                        Toast.makeText(AdminEditMovieActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }

                }
            }

        }
        catch (Exception e)
        {
            Toast.makeText(AdminEditMovieActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally
        {
            if(res !=null)
            {
                res.close();
            }
        }

    }

    //actualizare informatii ecran
    private void updateScreen(String name)
    {
        if(editMovieSpinner.getFirstVisiblePosition() == 0)
        {
            InputMovieName.setText("");
            InputMovieDescription.setText("");
            InputMoviePrice.setText("");
            InputMovieLength.setText("");
            spinner1.setSelection(0);
            checked = "false";

        }
        Cursor res = myDb.getAllMovies();

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            if(name.equals(String.valueOf(res.getString(1))))
            {
                InputMovieName.setText(String.valueOf(res.getString(1)));
                InputMovieDescription.setText(String.valueOf(res.getString(3)));
                InputMoviePrice.setText(String.valueOf(res.getString(4)));
                InputMovieLength.setText(String.valueOf(res.getString(5)));
                currentCategoty.setText("Current category: " + String.valueOf(res.getString(2)));
            }
        }

        res.close();
    }


    private void SetSpinnerList()
    {
        Cursor res = myDb.getAllMovies();
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Please choose a movie");

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(1)));
        }
        if(arrayList1.isEmpty())
        {
            arrayList1.add("No movie in database");
        }

        res.close();
        ArrayAdapter<String> adp = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        editMovieSpinner.setAdapter(adp);

    }
}

package com.example.vayo.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;


public class AdminNewMoviesActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, Pname, Length;
    private ImageView InputMovieImage;
    private EditText InputMovieName, InputMovieDescription, InputMoviePrice, InputMovieLength;
    private DatabaseHelper myDb;


    final int REQUEST_CODE_GALLERY = 999;


    private String checked = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_movies);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initializez baza de date
        myDb = new DatabaseHelper(this);

        final Spinner spinner1 = findViewById(R.id.spinner1);

        //preiau categoria filmului din spinner
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String select = (String) parent.getItemAtPosition(position);
                if(select.equals("Choose a category...") || spinner1.getFirstVisiblePosition() == 0)
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



        //stabilesc legatura dintre cod si interfata vizuala
        Button addNewMovieButton = findViewById(R.id.add_new_movie);
        InputMovieImage =  findViewById(R.id.admin_add_product_image);
        InputMovieName =  findViewById(R.id.admin_add_product_name);
        InputMovieDescription =  findViewById(R.id.admin_add_product_description);
        InputMoviePrice =  findViewById(R.id.admin_add_product_price);
        InputMovieLength = findViewById(R.id.admin_add_product_length);



        //preiau imaginea
        InputMovieImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ActivityCompat.requestPermissions(
                        AdminNewMoviesActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );

            }
        });



        //actionez butonul pentru adaugarea filmului
        addNewMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
                spinner1.setSelection(0);
            }
        });
    }



    private void ValidateProductData()
    {
        Pname = InputMovieName.getText().toString();
        Description = InputMovieDescription.getText().toString();
        Price = InputMoviePrice.getText().toString();
        Length = InputMovieLength.getText().toString();

        //verific corectidudinea informatiilor adaugate

        if (TextUtils.isEmpty(Pname))
        {
        Toast.makeText(this, "Please write movie name...", Toast.LENGTH_SHORT).show();
        }
       else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write movie description...", Toast.LENGTH_SHORT).show();
        }
        else if(checked.equals("false"))
        {
            Toast.makeText(this, "Please choose a category...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write movie Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Length))
        {
            Toast.makeText(this, "Please write length of the movie...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreMovie();
        }
    }

    private void StoreMovie()
    {
            try{
                //incerc introducerea filmului in baza de date
                myDb.insertMovie(
                        Pname,
                        CategoryName,Description,Price,
                        Length,imageViewToByte(InputMovieImage)
                );
                Toast.makeText(getApplicationContext(), "Movie was added successfully!", Toast.LENGTH_SHORT).show();
                InputMovieName.setText("");
                InputMovieDescription.setText("");
                InputMoviePrice.setText("");
                InputMovieLength.setText("");
                InputMovieImage.setImageResource(R.drawable.add_image);
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
    }



    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                InputMovieImage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}

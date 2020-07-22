package com.example.vayo.Fragments;

import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;


public class FilterFragment extends Fragment
{

    private DatabaseHelper myDb;
    private Spinner CinemaSpinner, CategorySpinner;
    private EditText MaxInput;


    private String Cinema,Category;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View productView = inflater.inflate(R.layout.fragment_filter, container, false);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Initializare BD
        myDb = new DatabaseHelper(getContext());

        //Legatura dintre cod si interfata vizuala
        CinemaSpinner = productView.findViewById(R.id.select_cinema);
        CategorySpinner = productView.findViewById(R.id.select_category);
        MaxInput = productView.findViewById(R.id.range_maxim);
        Button applyButton = productView.findViewById(R.id.apply_filter);

        //Setez cele 2 spinnere pentru introducerea filtrelor
        SetSpinner1(); //Cinemauri
        SetSpinner2(); //Categorii

        CinemaSpinner.setVisibility(View.VISIBLE);
        CategorySpinner.setVisibility(View.VISIBLE);

        CinemaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                Cinema = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                Category = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                ApplyFilter();
            }
        });




        return productView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ApplyFilter()
    {
        //Transmit datele colectate catre urmatoarea fereastra
        Bundle bundle = new Bundle();
        bundle.putString("cinema",Cinema);
        bundle.putString("category",Category);
        bundle.putString("max",MaxInput.getText().toString());
        ShowFilteredFragment nextFrag= new ShowFilteredFragment();
        nextFrag.setArguments(bundle);
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, nextFrag).addToBackStack( "tag" );
        transaction.commit();

    }


    //setez lista cu cinemauri disponibile
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void SetSpinner1()
    {
        //accesez toate cinemaurile din BD
        Cursor res = myDb.getAllCinemas();
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Choose a cinema");

        for(int i=0;i<res.getCount();i++)
        {
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(1)));
        }
        if(arrayList1.isEmpty())
        {
            arrayList1.add("No cinemas in database");
        }

        //pun lista cu cinemauri in BD
        ArrayAdapter<String> adp = new ArrayAdapter<> (Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_dropdown_item,arrayList1);
        CinemaSpinner.setAdapter(adp);

    }

    //setez lista cu cateoory
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void SetSpinner2()
    {
        //accesez lista cu filme din BD
        Cursor res = myDb.getAllMovies();
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Choose a category");

        for(int i=0;i<res.getCount();i++)
        {
            //pun fiecare cateogirie gasita in spinner
            res.moveToPosition(i);
            arrayList1.add(String.valueOf(res.getString(2)));
        }
        if(arrayList1.isEmpty())
        {
            arrayList1.add("No movies in database");
        }

        //pun lista in spinner
        ArrayAdapter<String> adp = new ArrayAdapter<> (Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_dropdown_item,arrayList1);
        CategorySpinner.setAdapter(adp);

    }




}

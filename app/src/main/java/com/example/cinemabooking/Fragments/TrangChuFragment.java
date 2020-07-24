package com.example.cinemabooking.Fragments;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.cinemabooking.R;

import java.util.Objects;


public class TrangChuFragment extends Fragment {




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View productView = inflater.inflate(R.layout.fragment_trang_chu, container, false);


        CardView AscendingName = productView.findViewById(R.id.name_ascending);
        CardView DescenfingName =productView.findViewById(R.id.name_descending);
        CardView AscendingPrice = productView.findViewById(R.id.price_ascending);
        CardView DescendingPrice = productView.findViewById(R.id.price_descending);
        CardView News = productView.findViewById(R.id.news);
        CardView Least = productView.findViewById(R.id.least_2);
        CardView MostPopular = productView.findViewById(R.id.mostpopular);





        AscendingName.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("message","AscendingName");
                OrderedMovies nextFrag = new OrderedMovies();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag).addToBackStack("tag");
                transaction.commit();
            }
        });

        DescenfingName.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("message","DescendingName");
                OrderedMovies nextFrag = new OrderedMovies();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag).addToBackStack("tag");
                transaction.commit();
            }
        });

        AscendingPrice.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("message","AscendingPrice");
                OrderedMovies nextFrag = new OrderedMovies();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag).addToBackStack("tag");
                transaction.commit();
            }
        });

        DescendingPrice.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("message","DescendingPrice");
                OrderedMovies nextFrag = new OrderedMovies();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag).addToBackStack("tag");
                transaction.commit();
            }
        });

        News.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("message","News");
                OrderedMovies nextFrag = new OrderedMovies();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag).addToBackStack("tag");
                transaction.commit();
            }
        });

        Least.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("message","Least");
                OrderedMovies nextFrag = new OrderedMovies();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag).addToBackStack("tag");
                transaction.commit();
            }
        });

        MostPopular.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("message","Most");
                OrderedMovies nextFrag = new OrderedMovies();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag).addToBackStack("tag");
                transaction.commit();
            }
        });




        return productView;
    }





}

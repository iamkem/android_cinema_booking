package com.example.vayo.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.vayo.MainActivity;
import com.example.vayo.Prevalent.Prevalent;
import com.example.vayo.R;

import java.util.Objects;

import io.paperdb.Paper;

public class AccountFragment extends Fragment
{


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        //initializare jurnal
        Paper.init(Objects.requireNonNull(getContext()));
        String UserEmailKey = Paper.book().read(Prevalent.UserEmailKey);  //preluare date din jurnal



        //legarura dintre cod si interfata vizuala
        CardView orderHistoryCard = v.findViewById(R.id.account_order_history);
        CardView customerSupportCard = v.findViewById(R.id.account_customer_support);
        CardView settingsCard = v.findViewById(R.id.account_settings);
        CardView logoutCard = v.findViewById(R.id.account_logout);
        TextView accountEmail = v.findViewById(R.id.account_email_text);


        accountEmail.setText(UserEmailKey);


        //CardView uri ce vor fi apasate pentru trecerea la fereastra dorita
        orderHistoryCard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view)
            {
                BookingHistoryFragment nextFrag= new BookingHistoryFragment();
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag);
                transaction.commit();

            }
        });

        customerSupportCard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view)
            {
                CustomerSupportFragment nextFrag= new CustomerSupportFragment();
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag);
                transaction.commit();

            }
        });


        settingsCard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view)
            {
                SettingsFragment nextFrag= new SettingsFragment();
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag);
                transaction.commit();


            }
        });


        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new AlertDialog.Builder(getContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to Logout?")


                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                Paper.book().destroy();

            }
        });


        return v;
    }


}

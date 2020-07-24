package com.example.cinemabooking.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cinemabooking.R;

import java.util.Objects;

public class HoTroKhachHangFragment extends Fragment
{


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.ho_tro_khach_hang_layout, container, false);
        ImageView leftArrowImage = v.findViewById(R.id.left_arrow_image);
        Button emailButton = v.findViewById(R.id.email_button);
        Button phoneButton = v.findViewById(R.id.phone_button);

        leftArrowImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                AccountFragment nextFrag = new AccountFragment();
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nextFrag);
                transaction.commit();

            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new AlertDialog.Builder(getContext())
                        .setTitle("Email Hỗ Trợ")
                        .setMessage("hoangdinhthangloi@gmail.com")
                        .setNegativeButton("Close", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new AlertDialog.Builder(getContext())
                        .setTitle("Gọi hỗ trợ")
                        .setMessage("Bạn có muốn gọi không?")


                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialContactPhone();

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        return v;

    }

    private void dialContactPhone() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0796664119", null)));
    }
}

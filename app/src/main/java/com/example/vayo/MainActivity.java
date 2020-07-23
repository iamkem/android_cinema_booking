package com.example.vayo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.vayo.Prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Khởi tạo đăng nhập
        Paper.init(this);


        Button registerButton = findViewById(R.id.main_join_now_btn);
        Button loginButton = findViewById(R.id.main_login_btn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        //Nếu có dữ liệu, người dùng vẫn đăng nhập và có thể vào ứng dụng trực tiếp mà không cần đăng nhập lại.

        String UserEmailKey = Paper.book().read(Prevalent.UserEmailKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserEmailKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserEmailKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {
                Intent intent = new Intent(MainActivity.this, TrangChuActivity.class);
                startActivity(intent);
            }
        }
    }
}

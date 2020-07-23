package com.example.vayo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vayo.Admin.AdminMainActivity;
import com.example.vayo.Database.DatabaseHelper;
import com.example.vayo.Prevalent.Prevalent;

import io.paperdb.Paper;

public class DangNhapActivity extends AppCompatActivity {
    private DatabaseHelper myDb;
    private EditText InputEmail, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;
    private String parentDbName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Khởi tạo DB
        myDb = new DatabaseHelper(this);

        //Khởi tạo đăng nhập
        Paper.init(this);


        LoginButton = findViewById(R.id.login_btn);
        InputPassword =  findViewById(R.id.login_password_input);
        InputEmail = findViewById(R.id.login_email_input);
        AdminLink =  findViewById(R.id.admin_panel_link);
        NotAdminLink =  findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });


    }



    private void LoginUser()
    {

        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();

        //Validate
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Vui lòng nhập email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Vui lòng nhập mật khẩu...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();



            Paper.book().write(Prevalent.UserEmailKey, email);
            Paper.book().write(Prevalent.UserPasswordKey, password);


            //Nếu tài khoản là Admin/User thì:
            if (parentDbName.equals("Admins"))
            {
               AllowAccessToAdmin(email,password);
            }
            else if (parentDbName.equals("Users"))
            {
                AllowAccessToAccount(email, password);

            }

        }

    }

    //Check data DB
    private void AllowAccessToAdmin(final String email, final String password)
    {
        int ok = 0;
        //Đọc dữ liệu ĐB
        Cursor res = myDb.getAllData();
        String adminCheck = "yes";
        loadingBar.dismiss();

        //Nếu ko tìm thấy gì:
        if(res.getCount() == 0)
            Toast.makeText(DangNhapActivity.this,"Chưa có tài khoản trong DB", Toast.LENGTH_SHORT).show();

        for(int i=0;i<res.getCount();i++)
        {
            //Đọc dữ liệu từ table
            res.moveToPosition(i);
            String user = String.valueOf(res.getString(1));
            String pass = String.valueOf(res.getString(2));
            String admin = String.valueOf(res.getString(3));
            if(email.equals(user) && password.equals(pass) && adminCheck.equals(admin))
            {
                ok = 1;
                Toast.makeText(DangNhapActivity.this,"Welcome " + res.getString(1), Toast.LENGTH_SHORT).show();

            }
        }

        //Nếu tìm thấy dữ liệu
        if(ok == 1)
        {
            Intent intent = new Intent(DangNhapActivity.this, AdminMainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(DangNhapActivity.this,"Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();

        }

    }

    //Cho phép vào ứng dụng
    private void AllowAccessToAccount(final String email, final String password)
    {
        int ok = 0;
        //Allow all user
        Cursor res = myDb.getAllData();
        loadingBar.dismiss();

        //Kiểm tra nếu có user trong DB
        if(res.getCount() == 0)
            Toast.makeText(DangNhapActivity.this,"Chưa có tài khoản trong DB", Toast.LENGTH_SHORT).show();

      for(int i=0;i<res.getCount();i++)
      {
          res.moveToPosition(i);
          String user = String.valueOf(res.getString(1));
          String pass = String.valueOf(res.getString(2));
          //So sánh user & pass nếu khớp trong DB
          if(email.equals(user) && password.equals(pass))
          {
              ok = 1;
              Toast.makeText(DangNhapActivity.this,"Chào mừng " + res.getString(1), Toast.LENGTH_SHORT).show();

          }
      }

      //Check nếu tất cả ổn thì:
      if(ok == 1)
      {
          Intent intent = new Intent(DangNhapActivity.this, TrangChuActivity.class);
          startActivity(intent);
      }
      else
      {
          Toast.makeText(DangNhapActivity.this,"Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();

      }

    }

}

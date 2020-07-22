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

public class LoginActivity extends AppCompatActivity {
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
        //initializare BD
        myDb = new DatabaseHelper(this);

        //initializare jurnal
        Paper.init(this);


        //stabilire legatura dintre cod si interfata vizuala
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
        //preluare date
        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();

        //verific campurile goale
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


            //scriu datele in jurnal
            Paper.book().write(Prevalent.UserEmailKey, email);
            Paper.book().write(Prevalent.UserPasswordKey, password);


            //in functie de stare user ului (Admin/User normal) si de destinatia dorita execut:
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

    //accesez backend-ul aplicatiei
    private void AllowAccessToAdmin(final String email, final String password)
    {
        int ok = 0;
        //citesc toate datele despre useri
        Cursor res = myDb.getAllData();
        String adminCheck = "yes";
        loadingBar.dismiss();

        //in caz ca n am gasit nimic
        if(res.getCount() == 0)
            Toast.makeText(LoginActivity.this,"Chưa có tài khoản trong DB", Toast.LENGTH_SHORT).show();

        for(int i=0;i<res.getCount();i++)
        {
            //citesc datele din tabel
            res.moveToPosition(i);
            String user = String.valueOf(res.getString(1));
            String pass = String.valueOf(res.getString(2));
            String admin = String.valueOf(res.getString(3));
            if(email.equals(user) && password.equals(pass) && adminCheck.equals(admin))
            {
                ok = 1;
                Toast.makeText(LoginActivity.this,"Welcome " + res.getString(1), Toast.LENGTH_SHORT).show();

            }
        }

        //daca totul este in regula
        if(ok == 1)
        {
            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(LoginActivity.this,"Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();

        }

    }

    //Accesez aplicatia
    private void AllowAccessToAccount(final String email, final String password)
    {
        int ok = 0;
        //accesez toti userii
        Cursor res = myDb.getAllData();
        loadingBar.dismiss();

        //verific daca exista useri in BD
        if(res.getCount() == 0)
            Toast.makeText(LoginActivity.this,"Chưa có tài khoản trong DB", Toast.LENGTH_SHORT).show();

      for(int i=0;i<res.getCount();i++)
      {
          res.moveToPosition(i);
          String user = String.valueOf(res.getString(1));
          String pass = String.valueOf(res.getString(2));
          //verific daca user ul si parola corespund vreunei inregistrari din BD
          if(email.equals(user) && password.equals(pass))
          {
              ok = 1;
              Toast.makeText(LoginActivity.this,"Welcome " + res.getString(1), Toast.LENGTH_SHORT).show();

          }
      }

      //daca totul este in regula
      if(ok == 1)
      {
          Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
          startActivity(intent);
      }
      else
      {
          Toast.makeText(LoginActivity.this,"Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();

      }

    }

}

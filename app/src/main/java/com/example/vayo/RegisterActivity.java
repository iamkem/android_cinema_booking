package com.example.vayo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vayo.Database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseHelper myDb;
    private EditText InputEmail, InputPassword, InputConfirmPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializare baza de date
        myDb = new DatabaseHelper(this);


        //stabilire legaturi dintre interfaza vizuala si cod
        Button createAccountButton = findViewById(R.id.register_btn);
        InputEmail = findViewById(R.id.register_email_input);
        InputPassword = findViewById(R.id.register_password_input);
        InputConfirmPassword = findViewById(R.id.register_confirm_password_input);
        loadingBar = new ProgressDialog(this);

        //stabilire comanda pentru buton
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }
        });
    }


    private void CreateAccount()
    {
        //accesare toate datele
        Cursor res = myDb.getAllData();

        //colectarea datelor
        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();
        String confirm_password = InputConfirmPassword.getText().toString();

        //verificarea corectitudinii
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Vui lòng nhập email...", Toast.LENGTH_SHORT).show();
        }
        else if(!isValidEmail(email))
        {
            Toast.makeText(this, "Email không hợp lệ...", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Vui lòng nhập mật khẩu...", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirm_password))
        {
            Toast.makeText(this, "Mật khẩu không khớp...", Toast.LENGTH_SHORT).show();

        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            //daca este prima inregistrare acest user devine si admin
            if(res.getCount() > 0)
                ValidateAccount(email, password ,"no");
            else
                ValidateAccount(email,password,"yes");
        }
    }



    private void ValidateAccount(final String email, final String password, final String admin)
    {
        //citesc toate datele
        Cursor res = myDb.getAllData();


            try {
                //introduc datele in baza de date
                myDb.insertData(email, password,admin);
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            catch (Exception e)
            {
                //caz de eroare
                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }


        loadingBar.dismiss();
    }

    //functie pentru verificare daca email ul este valid
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}

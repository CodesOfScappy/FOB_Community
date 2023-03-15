package com.info.scappy.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //UI-Elements
    EditText edEmail, edPassword;
    TextView forgetPassword,needNewAccountLink;
    Button btnLogin;
    private final String TAG = "Password Recovery";

    //Firebase Elements
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));


        //Initialize UI-Elements
        InitializedFields();


        // User has already an account and want to login
        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        //User has forget his password
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(LoginActivity.this,RecoveryPasswordActivity.class));
                    finish();
            }
        });


    }


    private void InitializedFields() {

        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        forgetPassword = findViewById(R.id.forget_password);
        btnLogin = findViewById(R.id.btn_login);
        needNewAccountLink = findViewById(R.id.needNewAccountLink);
    }
}
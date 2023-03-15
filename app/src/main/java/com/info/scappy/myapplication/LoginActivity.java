package com.info.scappy.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText edUsername, edPassword;
    TextView forgetPassword,needNewAccountLink;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));


        //Initialize UI-Elements
        InitializedFields();


    }


    private void InitializedFields() {

        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        forgetPassword = findViewById(R.id.forget_password);
        btnLogin = findViewById(R.id.btn_login);
        needNewAccountLink = findViewById(R.id.needNewAccountLink);
    }
}
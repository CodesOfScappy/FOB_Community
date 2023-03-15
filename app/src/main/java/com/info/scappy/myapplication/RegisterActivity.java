package com.info.scappy.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RegisterActivity extends AppCompatActivity {

    //UI-Elements
    EditText edUsername,edEmail,edPassword;
    Button btnRegister,btnLogin;
    TextView alreadyHaveAccountLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));


        //Initialize UI-Elements
         InitializedFields();



    }


    //Methode to initialize UI-Elements
    private void InitializedFields() {

        edUsername = findViewById(R.id.edUsername);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);
        alreadyHaveAccountLink = findViewById(R.id.alreadyHaveAnAccountLink);
    }


}
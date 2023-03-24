package com.info.scappy.myapplication.Start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.info.scappy.myapplication.Activitys.MainActivity;
import com.info.scappy.myapplication.R;
import com.info.scappy.myapplication.Start.Authentication.LoginActivity;
import com.info.scappy.myapplication.Start.Authentication.RegisterActivity;


public class StartActivity extends AppCompatActivity {

    Button btn_RegisterStartActivity, btn_LoginStartActivity;
    FirebaseUser firebaseUser;




    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already logged in
        if (firebaseUser != null) {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Initialize fields here
        InitializedFields();

        // Check if user is already logged in
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        btn_LoginStartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                finish();
            }
        });

       btn_RegisterStartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                finish();
            }
        });


    }



    //Methode to initialize UI-Elements
    private void InitializedFields() {

            btn_LoginStartActivity = findViewById(R.id.btn_loginStartActivity);
            btn_RegisterStartActivity = findViewById(R.id.btn_registerActivity);
    }
}
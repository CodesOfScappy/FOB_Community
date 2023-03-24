package com.info.scappy.myapplication.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.info.scappy.myapplication.R;
import com.info.scappy.myapplication.Start.Authentication.LoginActivity;

public class OptionsActivity extends AppCompatActivity {

    TextView logout, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);


        logout = findViewById(R.id.logout);
        settings = findViewById(R.id.settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settings);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new android.content.Intent(OptionsActivity.this, LoginActivity.class).setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }

        });
    }
}
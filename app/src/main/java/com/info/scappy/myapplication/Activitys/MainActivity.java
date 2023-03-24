package com.info.scappy.myapplication.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.info.scappy.myapplication.Fragments.HomeFragment;
import com.info.scappy.myapplication.Fragments.NotificationFragment;
import com.info.scappy.myapplication.Fragments.ProfileFragment;
import com.info.scappy.myapplication.Fragments.SearchFragment;
import com.info.scappy.myapplication.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(navigationItemSelectedListener);

        // Get the data from the intent
        Bundle intent = getIntent().getExtras();

        // Check if the intent is not null
        if (intent != null)
        {
            String publisher = intent.getString("publisherid");
            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();

        }
        else
        {
            // Set the default fragment to HomeFragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }






        // Set the default fragment to HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


    }


    // BottomNavigation Listener for switching between fragments
    private BottomNavigationView.OnNavigationItemReselectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemReselectedListener() {
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem menuItem)
                {
                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;

                        case R.id.nav_add:
                            selectedFragment = null;
                            startActivity(new Intent(MainActivity.this, PostActivity.class));
                            break;

                        case R.id.nav_heart:
                            selectedFragment = new NotificationFragment();
                            break;

                        case R.id.nav_profile:
                            // Get the current user's ID and store it in SharedPreferences
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            selectedFragment = new ProfileFragment();
                            break;

                    }
                    if (selectedFragment != null)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }


                    return;
                }
            };
}
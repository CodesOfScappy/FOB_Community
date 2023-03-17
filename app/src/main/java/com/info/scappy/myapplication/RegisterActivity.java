package com.info.scappy.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    //UI-Elements
    EditText edFullName, edUsername, edEmail, edPassword;
    Button btnRegisterActivity;
    TextView alreadyHaveAccountLink;

    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference reference;

    //UserDialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        //Initialize UI-Elements
        InitializedFields();

        //Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        // Create a DatabaseReference root: --> "Users"
        reference = FirebaseDatabase.getInstance().getReference().child("Users");





        alreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create a ProgressDialog
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle(getString(R.string.account_creating));
                progressDialog.setMessage(getString(R.string.please_wait_creating_account));
                progressDialog.show();

                // Save the Data in Strings
                String username = edUsername.getText().toString();
                String fullname = edFullName.getText().toString();
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();

                //Check if the Fields are empty
                if (TextUtils.isEmpty(username)||
                        TextUtils.isEmpty(fullname)||
                        TextUtils.isEmpty(email)||
                        TextUtils.isEmpty(password))
                {
                    Toast.makeText(RegisterActivity.this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                }
                else if(password.length() <6)
                {
                    Toast.makeText(RegisterActivity.this, R.string.password_must_be_at_least_6_characters, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Create a User Account
                    RegisterUserAccount(username, fullname, email, password);
                }




            }


        });


    }

    //Methode to create a User Account
    private void RegisterUserAccount(String username, String fullname, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String userId = firebaseUser.getUid();
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                        //Create a Hashmap to store the User Data
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", userId);
                        hashMap.put("username", username.toLowerCase());
                        hashMap.put("fullname", fullname);
                        hashMap.put("bio", "");
                        hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/fob-demo-299b7.appspot.com/o/placeholder.jpg?alt=media&token=9c271ac1-eee6-43f0-8135-e85046d669a7");
                        hashMap.put("isPatreonLv1", "false");
                        hashMap.put("isPatreonLv2", "false");
                        hashMap.put("isPatreonLv3", "false");
                        hashMap.put("isUser", "true");


                        //Save the User Data in the Database
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    //Dismiss the ProgressDialog
                                    progressDialog.dismiss();
                                    //Start the MainActivity
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });


                    } else {   // If sign in fails, display a message to the user.
                        Toast.makeText(this, R.string.you_not_register, Toast.LENGTH_SHORT).show();
                    }


                });


    }


    //Methode to initialize UI-Elements
    private void InitializedFields() {

        edFullName = findViewById(R.id.edFullName);
        edUsername = findViewById(R.id.edUsername);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnRegisterActivity = findViewById(R.id.btn_registerActivity);
        alreadyHaveAccountLink = findViewById(R.id.alreadyHaveAnAccountLink);
    }


}
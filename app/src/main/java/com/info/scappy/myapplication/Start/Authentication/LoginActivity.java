package com.info.scappy.myapplication.Start.Authentication;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.info.scappy.myapplication.MainActivity;
import com.info.scappy.myapplication.R;
import com.info.scappy.myapplication.Start.Authentication.Recovery.RecoveryPasswordActivity;

public class LoginActivity extends AppCompatActivity {

    //UI-Elements
    EditText edEmail, edPassword;
    TextView forgetPassword,needNewAccountLink;
    Button btnLoginActivity;
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

        //Initialize Firebase Elements
        mAuth = FirebaseAuth.getInstance();


        // User has already an account and want to login
        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        //User has forget his password
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(LoginActivity.this, RecoveryPasswordActivity.class));
                    finish();
            }
        });

        //Sign in with email and password
        btnLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Show progress dialog --> is Set Final
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle(getString(R.string.login_progress));
                progressDialog.setMessage(getString(R.string.login_progress_message));
                progressDialog.show();

                //Get email and password from user
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Sign in with email and password
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                //Get user id from firebase database
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(mAuth.getCurrentUser().getUid());

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {
                                        //Hide progress dialog
                                        progressDialog.dismiss();
                                        // User is logged in successfully and go to main activity
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError)
                                    {
                                        //Hide progress dialog
                                        progressDialog.dismiss();
                                        //Show error message
                                        Toast.makeText(LoginActivity.this, R.string.authentication_fail, Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            else
                            {
                                //Hide progress dialog
                                progressDialog.dismiss();
                                //Show error message
                                Toast.makeText(LoginActivity.this, R.string.authentication_fail, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });


    }


    private void InitializedFields() {

        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.password);
        forgetPassword = findViewById(R.id.forget_password);
        btnLoginActivity = findViewById(R.id.btn_loginActivity);
        needNewAccountLink = findViewById(R.id.needNewAccountLink);
    }
}
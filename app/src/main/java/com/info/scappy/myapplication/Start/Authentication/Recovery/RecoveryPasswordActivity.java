package com.info.scappy.myapplication.Start.Authentication.Recovery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.info.scappy.myapplication.R;
import com.info.scappy.myapplication.Start.Authentication.LoginActivity;

public class RecoveryPasswordActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText edEmail;
    Button btn_RecoveryPassword,btn_Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);

        //Initialize UI-Elements
        InitializedFields();

        //Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        //Cancel Button
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecoveryPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });

        //Recovery Password Button
        btn_RecoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edEmail.getText().toString().isEmpty())
                {
                    edEmail.setError(getString(R.string.error_text));
                    edEmail.requestFocus();
                }
                else
                {
                    String recoveryEmail = edEmail.getText().toString();
                    mAuth.sendPasswordResetEmail(recoveryEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) 
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RecoveryPasswordActivity.this, R.string.send_email, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RecoveryPasswordActivity.this,LoginActivity.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(RecoveryPasswordActivity.this, R.string.email_send_fail, Toast.LENGTH_SHORT).show();
                            }   
                        }
                    });
                }
               
                  

            }
        });


    }
    





    // Methode to initialize UI-Elements
    private void InitializedFields() {
        edEmail = findViewById(R.id.edEmail);
        btn_RecoveryPassword = findViewById(R.id.btn_recovery_password);
        btn_Cancel = findViewById(R.id.btn_recovery_abort);
    }
}
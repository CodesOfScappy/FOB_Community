package com.info.scappy.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.canhub.cropper.CropImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class PostActivity extends AppCompatActivity {

    Uri imageUri;
    String imageUrl;
    StorageTask uploadTask;
    StorageReference storageReference;
    EditText description;
    ImageView close, image_added;

    TextView post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Initialize fields here
        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        description = findViewById(R.id.description);
        post = findViewById(R.id.post);

        // Initialize Firebase here
        // Create a storage reference Root: --> "posts"
        storageReference = FirebaseStorage.getInstance().getReference("posts");


        // Icon: X set to close the PostActivity
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(PostActivity.this, MainActivity.class));

                finish();
            }
        });

        // Text Icon: "POST" -> set to open the gallery
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Open gallery and upload a selected image
                uplaodImage();

            }
        });

        CropImage
    }
}
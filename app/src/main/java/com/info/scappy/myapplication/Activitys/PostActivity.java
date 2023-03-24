package com.info.scappy.myapplication.Activitys;

import static com.theartofdev.edmodo.cropper.CropImage.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.info.scappy.myapplication.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

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
                uploadImage();

            }




        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostActivity.this);

    }


    private String getFileExtentions(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }





    // Methode:--> Upload image to Firebase Storage and get the download URL
    private void uploadImage()
    {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting...");
        progressDialog.show();

        if (imageUri != null)
        {
            StorageReference reference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtentions(imageUri));

            uploadTask = reference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        // Get the download URL
                        Uri downloadUri = (Uri) task.getResult();
                        // Convert the download URL to a String
                        imageUrl = downloadUri.toString();

                        // Create a new post in the database
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
                        // Get the unique ID of the post
                        String postid = reference.push().getKey();

                        // Create a new HashMap and add the post data to it
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postimage", imageUrl);
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(postid).setValue(hashMap);
                        progressDialog.dismiss();

                        // Go back to the MainActivity
                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        finish();
                    }
                    else
                    {
                        // If something went wrong with the image upload process -> show a toast message
                        Toast.makeText(PostActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    // If something went wrong with the image upload process -> show a toast message
                    Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            // If no image was selected -> show a toast message
            Toast.makeText(this, R.string.no_image_selected, Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Get the image URI from the CropImage Activity and set it to the image_added ImageView
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            image_added.setImageURI(imageUri);
        }
        else
        {
            // If something went wrong with the image upload process -> show a toast message
            Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }

    }
}
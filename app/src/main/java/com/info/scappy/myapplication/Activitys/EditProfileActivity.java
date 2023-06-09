package com.info.scappy.myapplication.Activitys;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.info.scappy.myapplication.Models.User;
import com.info.scappy.myapplication.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    ImageView close,image_profile;
    MaterialEditText bio,fullname,username;
    TextView save,tv_change;

    private Uri mImageUri;
    private StorageTask uploadTask;
    FirebaseUser firebaseUser;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        // Initialize the Elements
        close = findViewById(R.id.close);
        image_profile = findViewById(R.id.image_profile);
        bio = findViewById(R.id.bio);
        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        save = findViewById(R.id.save);
        tv_change = findViewById(R.id.tv_change);

        // Firebase Init
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                fullname.setText(user.getFullname());
                username.setText(user.getUsername());
                bio.setText(user.getBio());

                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Close the Activity
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Change the Profile Image of the User
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CropImage.activity().setAspectRatio(1,1).setCropShape(CropImageView.CropShape.OVAL)
                        .start(EditProfileActivity.this);
            }
        });


        // get the new Image from the CropImage Activity and set it to the ImageView
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CropImage.activity().setAspectRatio(1,1).setCropShape(CropImageView.CropShape.OVAL)
                        .start(EditProfileActivity.this);
            }
        });



        // Save the Changes to the Database and Storage
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                updateProfile(fullname.getText().toString(),username.getText().toString(),bio.getText().toString());
                startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
                finish();
            }
        });


    }


    // Update the Profile
    private void updateProfile(String fullname, String username, String bio)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        // Create a HashMap
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("fullname",fullname);
        hashMap.put("username",username);
        hashMap.put("bio",bio);
        // Update the HashMap
        reference.updateChildren(hashMap);
    }


    // get The File Extension of the Image
    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    // Upload the Image to the Firebase Storage
    private void uploadImage()
    {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");
        pd.show();

        if(mImageUri != null)
        {
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    +"."+getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        String myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                                .child(firebaseUser.getUid());

                        // Create a HashMap
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("imageurl",""+myUrl);
                        // Update the HashMap
                        reference.updateChildren(hashMap);
                        pd.dismiss();


                    }
                    else
                    {
                        // Failed to Upload the Image to the Firebase Storage Database and Show a Toast
                        Toast.makeText(EditProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    // Failed to Upload the Image to the Firebase Storage Database and Show a Toast
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
        else
        {
            // Failed to Upload the Image to the Firebase Storage Database and Show a Toast
            Toast.makeText(this, R.string.no_image_selected, Toast.LENGTH_SHORT).show();
        }
    }


    // Get the Result of the Image Cropping Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            uploadImage();
        }
        else
        {
            // Failed to Upload the Image to the Firebase Storage Database and Show a Toast
            Toast.makeText(this, R.string.failed, Toast.LENGTH_SHORT).show();
        }
    }
}
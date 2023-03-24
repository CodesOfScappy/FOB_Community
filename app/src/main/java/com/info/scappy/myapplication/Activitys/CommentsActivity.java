package com.info.scappy.myapplication.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.info.scappy.myapplication.Adapter.CommentAdapter;
import com.info.scappy.myapplication.Models.Comment;
import com.info.scappy.myapplication.Models.User;
import com.info.scappy.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    EditText addComment;
    ImageView image_profile;
    TextView post;
    String postid;
    String publisherid;
    FirebaseUser firebaseUser;

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.comment);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Recycler View
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        recyclerView.setAdapter(commentAdapter);




        // Initialize
        addComment = findViewById(R.id.add_comment);
        post = findViewById(R.id.post);
        image_profile = findViewById(R.id.image_profile);

        // Get Current User
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get Post ID from Intent  (from PostAdapter)
        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        publisherid = intent.getStringExtra("publisherid");

        Log.d("publisherid: -> ", publisherid);

        // Post Button
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addComment.getText().toString().equals("")) {
                    Toast.makeText(CommentsActivity.this, R.string.empty_comments_error, Toast.LENGTH_SHORT).show();
                } else {
                    // Add Comment Method
                    addComment();
                }
            }
        });


        getImage();
        readComments();
    }


    private void addNotification()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(publisherid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", getString(R.string.commeded) + addComment.getText().toString());
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);

        reference.push().setValue(hashMap);
    }



    // Add Comment Method
    private void addComment() {
        // Create / Read Reference to Database (Comments) and (Post ID)
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        // Create HashMap
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", addComment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());

        // Add Comment to Database
        reference.push().setValue(hashMap);
        addNotification();
        addComment.setText("");
        //TODO: (work it) this works--> addNotification();


    }


    private void getImage() {
        // Create / Read Reference to Database (Users) and (User ID)
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        // Get Image from Database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get User Object
                User user = dataSnapshot.getValue(User.class);

                // Set Image
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }


    // Read Comments Method
    private void readComments() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
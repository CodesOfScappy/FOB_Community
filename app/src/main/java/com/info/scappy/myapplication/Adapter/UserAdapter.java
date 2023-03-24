package com.info.scappy.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.info.scappy.myapplication.Activitys.MainActivity;
import com.info.scappy.myapplication.Fragments.ProfileFragment;
import com.info.scappy.myapplication.Models.User;
import com.info.scappy.myapplication.R;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private Context mContext;   // Context of the fragment in which the recycler view is present
    private List<User> mUsers;  // List of users to be displayed in the recycler view of the fragment

    FirebaseUser firebaseUser;


    private Boolean isFragments;


    public UserAdapter(Context mContext, List<User> mUsers, boolean isFragments) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFragments = isFragments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the recycler view item and return the view holder object
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);


        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = mUsers.get(position);

        // Set the values of the views of the recycler view item
        holder.btn_follow.setVisibility(View.VISIBLE);
        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getFullname());

        // Load the image of the user using Glide
        Glide.with(mContext).load(user.getImageurl()).into(holder.image_profile);

        // Check if the user is already following the other user
        isFollowing(user.getId(), holder.btn_follow);

        if (user.getId().equals(firebaseUser.getUid()))
        {
            holder.btn_follow.setVisibility(View.GONE);
        }

        // Add a click listener to the follow item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isFragments) {

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getId());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();

                }
                else
                {
                    // Start the main activity and pass the user id of the user to be displayed
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("publisherid", user.getId());
                    mContext.startActivity(intent);
                }
            }
        });

        // Add a click listener to the follow button
        holder.btn_follow.setOnClickListener(v -> {
            if (holder.btn_follow.getText().toString().equals("follow"))
            {
                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                        .child("following").child(user.getId()).setValue(true);



                FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                        .child("followers").child(firebaseUser.getUid()).setValue(true);


                addNotification(user.getId());



            }
            else
            {
                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                        .child("following").child(user.getId()).removeValue();



                FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                        .child("followers").child(firebaseUser.getUid()).removeValue();
            }
        });


    }

    private void addNotification(String userid)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", mContext.getString(R.string.follow_you_now));
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    // View holder class for the recycler view item
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Declare the views of the recycler view item
        public TextView username;
        public TextView fullname;
        public CircleImageView image_profile;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views of the recycler view item
            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            image_profile = itemView.findViewById(R.id.image_profile);
            btn_follow = itemView.findViewById(R.id.btn_follow);
        }
    }


    // Method to check if the user is already following the other user
    private void isFollowing(final String userid, final Button button)
    {
        // Check if the user is already following the other user
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid())
                .child("following");


        // Add a value event listener to the reference
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(userid).exists())
                {
                    button.setText("following");
                }
                else
                {
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


}

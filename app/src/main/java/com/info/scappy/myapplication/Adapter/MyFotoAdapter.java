package com.info.scappy.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.info.scappy.myapplication.Fragments.PostDetailsFragment;
import com.info.scappy.myapplication.Models.Post;
import com.info.scappy.myapplication.R;

import java.util.List;

public class MyFotoAdapter extends RecyclerView.Adapter<MyFotoAdapter.ViewHolder> {


    private Context mContext;
    private List<Post> mPost;

    public MyFotoAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fotos_item, parent, false);

        return new MyFotoAdapter.ViewHolder(view);
    }





    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
       final Post post = mPost.get(position);
       Glide.with(mContext).load(post.getPostimage()).into(holder.post_image);


        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostDetailsFragment()).commit();

            }
        });
    }



    @Override
    public int getItemCount() {
        return mPost.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView post_image;

        public ViewHolder(View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);

        }
    }


}

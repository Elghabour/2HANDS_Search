package com.example.a2hands;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRecyclerViewAdapter extends FirestoreRecyclerAdapter<User,
        SearchRecyclerViewAdapter.SearchHolder> {


    public SearchRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchHolder holder, int position, @NonNull User model) {
        holder.userFullName.setText(model.user_name+" "+model.last_name);
        holder.userCountry.setText(model.country);
        holder.userRating.setText(model.rate + "");
        Picasso.get().load(model.profile_pic).into(holder.userProfilePic);

    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_item,
                parent, false);
        return new SearchHolder(v);
    }

    class SearchHolder extends RecyclerView.ViewHolder{

        TextView userFullName, userCountry, userRating;
        CircleImageView userProfilePic;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);

            userFullName = itemView.findViewById(R.id.search_userName);
            userCountry = itemView.findViewById(R.id.search_userCountry);
            userRating = itemView.findViewById(R.id.search_userRating);
            userProfilePic = itemView.findViewById(R.id.search_UserProfileImage);
        }
    }
}

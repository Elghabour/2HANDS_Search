package com.example.a2hands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.net.Uri;
import android.os.Bundle;
import android.widget.SearchView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity{

    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");

    private SearchRecyclerViewAdapter searchAdapter;

    CircleImageView profile_image ;
    SearchView searchView;

    RecyclerView searchRecyclerView;
    Query searchQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchQuery = usersRef.orderBy("first_name", Query.Direction.DESCENDING)
                .startAt("A")
                .endAt("A"+"\uf8ff");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(searchQuery, User.class)
                .build();

        searchAdapter = new SearchRecyclerViewAdapter(options);

        searchRecyclerView = findViewById(R.id.searchRecyclerView);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(searchRecyclerView.getContext()));
        searchRecyclerView.setAdapter(searchAdapter);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        searchView = findViewById(R.id.searchView);
        profile_image = findViewById(R.id.search_UserProfileImage);

        //load current user main pic in home top menu
        FirebaseFirestore.getInstance().collection("users/").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                FirebaseStorage.getInstance().getReference().child("Profile_Pics/"+uid+"/"+user.profile_pic).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).into(profile_image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Query searchQuery = usersRef.orderBy("first_name", Query.Direction.DESCENDING)
                        .startAt(searchView.getQuery().toString().trim())
                        .endAt(searchView.getQuery().toString().trim()+"\uf8ff");

                FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(searchQuery, User.class)
                        .build();

                searchAdapter = new SearchRecyclerViewAdapter(options);
                searchRecyclerView = findViewById(R.id.searchRecyclerView);
                searchRecyclerView.setHasFixedSize(true);
                searchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                searchRecyclerView.setAdapter(searchAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        searchAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchAdapter.stopListening();
    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        setUprecyclerView(newText);
//        return false;
//    }
}

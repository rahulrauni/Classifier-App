package com.example.quikrassign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class commentActivity extends AppCompatActivity {
    private  RecyclerView itemlist;
    private FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    private commentAdapter commentAdapter;
    String advkey;
   


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        itemlist = findViewById(R.id.itemlist);
        itemlist.setHasFixedSize(true);
        itemlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Advertisementcomment");
        advkey = getIntent().getStringExtra("advkey");
        Query query = collectionReference.whereEqualTo("advkey", advkey);
        FirestoreRecyclerOptions<comment> options = new FirestoreRecyclerOptions.Builder<comment>().setQuery(query, comment.class).build();
        commentAdapter = new commentAdapter(options);
        itemlist.setAdapter(commentAdapter);


    }
    @Override
    public void onStop() {
        super.onStop();
        commentAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        commentAdapter.startListening();
    }

}

package com.example.quikrassign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class productDetail extends AppCompatActivity {
    TextView titledetail, categorydetail, valuedetail,descriptiondetail,sellernamedetail,sellerdetail, editcomment,ratingdetail;
    ImageView imagedetail, sendimage;
    FirebaseFirestore fstore ;
    String advkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        titledetail = findViewById(R.id.titledetail);
        categorydetail = findViewById(R.id.categorydetail);
        valuedetail = findViewById(R.id.valuedetail);
        descriptiondetail = findViewById(R.id.descriptiondetail);
        sellernamedetail = findViewById(R.id.sellernamedetail);
        sellerdetail = findViewById(R.id.sellerdetail);
        imagedetail = findViewById(R.id.imagedetail);
        sendimage = findViewById(R.id.sendimage);
        editcomment =findViewById(R.id.editcomment);
        ratingdetail = findViewById(R.id.ratingdetail);
        fstore = FirebaseFirestore.getInstance();
        new NetworkCheck(getApplicationContext()).checkConnection();
        productmanager();
    }
    public void productmanager(){
        advkey = getIntent().getStringExtra("AdvKey");
        titledetail.setText(getIntent().getStringExtra("Title"));
        categorydetail.setText("#"+ getIntent().getStringExtra("Category"));
        valuedetail.setText("Rs."+ getIntent().getStringExtra("Value"));
        descriptiondetail.setText(getIntent().getStringExtra("Description"));
        sellernamedetail.setText(getIntent().getStringExtra("username"));

        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("link1")).into(imagedetail);
        sellerdetail.setText("Sold By");
        //sellernamedetail.setText("Rahul");

        imagedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sendimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcomments();
            }
        });
        ratingdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inetnt = new Intent(getApplicationContext(), commentActivity.class);
                inetnt.putExtra("advkey", advkey);
                startActivity(inetnt);
            }
        });
    }
    public void addcomments(){

        Log.i("advkey2", advkey);
        String comment = editcomment.getText().toString();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String commentKey = UUID.randomUUID().toString().substring(0,16);
        final DocumentReference documentReference = fstore.collection("Advertisementcomment").document(commentKey);
        final Map<String , Object> postmap =new HashMap<>();
        postmap.put("Comment",comment);
        postmap.put("userid", userid);
        postmap.put("advkey",advkey);
        postmap.put("commentkey", commentKey);
        documentReference.set(postmap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(productDetail.this, "Your Comment is successfully posted", Toast.LENGTH_SHORT).show();
                editcomment.setText(null);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(productDetail.this, "Something error is occured", Toast.LENGTH_SHORT).show();

            }
        });


    }
}

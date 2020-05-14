package com.example.quikrassign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class myadd extends AppCompatActivity {
    private RecyclerView itemlist;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myadd);
        itemlist = findViewById(R.id.itemlist);
        itemlist.setHasFixedSize(true);
        itemlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Advertisement");
        Query query = collectionReference.whereEqualTo("Userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirestoreRecyclerOptions<productlist> options = new FirestoreRecyclerOptions.Builder<productlist>().setQuery(query, productlist.class).build();

        adapter = new FirestoreRecyclerAdapter<productlist, productFragment.productViewHolder>(options) {
            @NonNull
            @Override
            public productFragment.productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
                return new productFragment.productViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull productFragment.productViewHolder productViewHolder, int i, @NonNull final productlist productlist) {
                productViewHolder.setTitle(productlist.getTitle());
                productViewHolder.setValue("Rs. " + productlist.getValue());
                productViewHolder.setLink1(getApplicationContext(), productlist.getLink1());
                productViewHolder.ordercard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productDetails = new Intent(getApplicationContext(), productDetail.class);
                        productDetails.putExtra("Title", productlist.getTitle());
                        productDetails.putExtra("Value", productlist.getValue());
                        productDetails.putExtra("Category", productlist.getCategory());
                        productDetails.putExtra("Description", productlist.getDescription());
                        productDetails.putExtra("city", productlist.getCity());
                        productDetails.putExtra("State", productlist.getState());
                        productDetails.putExtra("Userid", productlist.getUserid());
                        productDetails.putExtra("AdvKey",productlist.getAdvKey());
                        productDetails.putExtra("Mobile", productlist.getMobile());
                        productDetails.putExtra("link1", productlist.getLink1());
                        productDetails.putExtra("username", productlist.getUsername());
                        startActivity(productDetails);
                        Log.i("advkey", productlist.getAdvKey());



                    }
                });

            }
        } ;
        itemlist.setAdapter(adapter);
        
    }
    public static class productViewHolder extends RecyclerView.ViewHolder{
        View view;
        CardView ordercard;

        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ordercard = view.findViewById(R.id.ordercard);
        }
        public void setTitle(String title) {
            TextView titlefield = (TextView) view.findViewById(R.id.textTitle);
            titlefield.setText(title);

        }
        public void setValue(String value) {
            TextView valuefield = view.findViewById(R.id.textValue);
            valuefield.setText(value);
        }

        public void  setLink1(final Context ctx , final String link1){
            CircleImageView addproduct_image = view.findViewById(R.id.imageAdvertisement);
            Glide.with(ctx).load(link1).into(addproduct_image);

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

}

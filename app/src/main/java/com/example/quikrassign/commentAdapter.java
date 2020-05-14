package com.example.quikrassign;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class commentAdapter extends FirestoreRecyclerAdapter<comment, commentAdapter.commentHolder> {

    public commentAdapter(@NonNull FirestoreRecyclerOptions<comment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final commentHolder commentHolder, int i, @NonNull comment comment) {
        commentHolder.commentdetail.setText(comment.getComment());
        String advkey = comment.getComment();
        String userid= comment.getComment();

    }

    @NonNull
    @Override
    public commentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentcard,parent,false);
        return new commentHolder(v) ;
    }

    class commentHolder extends RecyclerView.ViewHolder {
        TextView commentdetail;
        public commentHolder(@NonNull View itemView) {
            super(itemView);
            commentdetail = itemView.findViewById(R.id.commentdetail);

        }
    }

}

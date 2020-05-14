package com.example.quikrassign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import static android.content.Context.MODE_PRIVATE;


public class profileFragment extends Fragment {
    TextView logout, profileRegister, nameuser,Myads,shareit;
    CircleImageView imageuser;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
     private ListenerRegistration notlistener;
     SharedPreferences detail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        logout =v.findViewById(R.id.logout);
        profileRegister =v.findViewById(R.id.profileRegister);
        imageuser = v.findViewById(R.id.imageuser);
        nameuser =v.findViewById(R.id.nameuser);
        Myads = v.findViewById(R.id.Myads);
        shareit = v.findViewById(R.id.shareit);
        firestore = FirebaseFirestore.getInstance();
        detail = this.getActivity().getSharedPreferences("com.example.quikrassign", MODE_PRIVATE);
        documentReference = firestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userretrive();
        profileRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegDetail.class));

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(getActivity(),MainActivity.class));
                                getActivity().finish();
                                oncleardata();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        Myads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), myadd.class));
            }
        });
        shareit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        notlistener = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e!= null){
                    Toast.makeText(getActivity(), "error While loading", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("name");
                    String  profilelink = documentSnapshot.getString("imageuser");
                    nameuser.setText(name);
                    Glide.with(getActivity()).load(profilelink).into(imageuser);

                }else{
                    Toast.makeText(getActivity(), "Document Doesn't Exist", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public  void userretrive(){
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("name");
                    String  profilelink = documentSnapshot.getString("imageuser");
                    if (getActivity() == null) {
                        return;
                    }
                    nameuser.setText(name);
                    Glide.with(getActivity()).load(profilelink).into(imageuser);

                }else{
                    Toast.makeText(getActivity(), "Document Doesn't Exist", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Document Doesn't Exist", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        notlistener.remove();
    }
    public void share(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now sell old item with quikr app check this app: https://in.linkedin.com/in/saurabh-bansal-575907a/");
        startActivity(Intent.createChooser(sharingIntent, "Share via Family And Friends"));

    }

    public void oncleardata(){
        detail.edit().clear().commit();
    }
}

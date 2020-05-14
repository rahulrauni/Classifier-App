package com.example.quikrassign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class RegDetail extends AppCompatActivity {
    EditText nameText;
    CircleImageView imageuser;
    Button uploadButton;
    FirebaseFirestore fstore ;
    private static  int GALLERY_REQUEST = 1;
    ProgressDialog progressDialog;
    private StorageReference mstorageref;
    Uri imageUri = null;
    SharedPreferences detail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_detail);
        nameText = findViewById(R.id.nameText);
        imageuser = findViewById(R.id.imageuser);
        uploadButton = findViewById(R.id.uploadButton);
        fstore = FirebaseFirestore.getInstance();
        mstorageref = FirebaseStorage.getInstance().getReference();
        new NetworkCheck(getApplicationContext()).checkConnection();
        detail = getSharedPreferences("com.example.quikrassign", MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering");
        imageuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    displayimage();

                }else{
                    ActivityCompat.requestPermissions(RegDetail.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 56);
                }

            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addpost();

            }
        });

    }

    public void addpost(){
        final String name = nameText.getText().toString();
        final DocumentReference documentReference = fstore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (!TextUtils.isEmpty(name) && imageUri!= null){
            progressDialog.show();
            final StorageReference filepath = mstorageref.child("user images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            final Map<String , Object> postmap =new HashMap<>();
                            postmap.put("name" , name);
                            postmap.put("imageuser", downloadUrl.toString());
                            documentReference.set(postmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    detail.edit().putBoolean("fillDetail", true).commit();
                                    Toast.makeText(getApplicationContext(), "successfully Registered", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();


                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });

        }else {
            Toast.makeText(getApplicationContext(), "All fields are Mandatory", Toast.LENGTH_SHORT).show();
        }

        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            imageUri = data.getData();
            imageuser.setImageURI(imageUri);

        }
        super.onActivityResult(requestCode, resultCode, data);


    }
    //open gallery when product image button is clicked
    public void displayimage(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);

    }
}

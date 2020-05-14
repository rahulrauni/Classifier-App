package com.example.quikrassign;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class sellFragment extends Fragment {
    private EditText fieldTitle,  fieldValue,fieldPhone, fieldCity;
    private ImageView image1;
    private Spinner fieldState, fieldCategory;
    MultiAutoCompleteTextView fieldDescription;
    Button postButton;
    FirebaseFirestore fstore ;
    private static  int GALLERY_REQUEST = 1;
    ProgressDialog progressDialog;
    private StorageReference mstorageref;
    DocumentReference documentReference2;
    String name;
    Uri imageUri = null;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sell, container, false);
        fieldTitle = v.findViewById(R.id.editTitle);
        fieldDescription = v.findViewById(R.id.editDescription);
        fieldValue = v.findViewById(R.id.editValue);
        fieldPhone = v.findViewById(R.id.editPhone);
        fieldState = v.findViewById(R.id.spinnerState);
        fieldCategory = v.findViewById(R.id.spinnerCategory);
        fieldCity = v.findViewById(R.id.editCity);
        image1 = v.findViewById(R.id.imageRegister1);
        postButton =v.findViewById(R.id.postButton);
        loadingDataSpinner();
        fstore = FirebaseFirestore.getInstance();
        mstorageref = FirebaseStorage.getInstance().getReference();
        new NetworkCheck(getContext()).checkConnection();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Posting Your Advertisement please wait");
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    displayimage();

                }else{
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 56);
                }



            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressed();
            }
        });
        return  v;
    }
    private void loadingDataSpinner(){

        //configurate spinner de state
        String[] estate = getResources().getStringArray(R.array.estate);
        ArrayAdapter<String> adapterState = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, estate);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldState.setAdapter(adapterState);

        //configurate spinner de category
        String[] category = getResources().getStringArray(R.array.category);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,
                category
        );
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldCategory.setAdapter(adapterCategory);

    }
    public void onPressed()
    {


        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(getActivity());

        builder.setMessage("Do you Sure you want To Post this Product");
        builder.setTitle("Alert !");
        builder.setCancelable(false);

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                addpost();




                            }
                        });

        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {


                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void addpost(){
        userretrive();
        final String state = fieldState.getSelectedItem().toString();
        final String category = fieldCategory.getSelectedItem().toString();
        final String city = fieldCity.getText().toString();
        final String title = fieldTitle.getText().toString();
        final String value = fieldValue.getText().toString();
        final String phone = fieldPhone.getText().toString();
        final String description = fieldDescription.getText().toString();
        final String advKey = UUID.randomUUID().toString().substring(0,16);
        final DocumentReference documentReference = fstore.collection("Advertisement").document(advKey);
        //
        if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(title)&& !TextUtils.isEmpty(value) && !TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(description) &&imageUri!= null && phone.length() == 10 ) {
            progressDialog.show();
            // adding data to firebase
            final StorageReference filepath = mstorageref.child("product images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            final Map<String , Object> postmap =new HashMap<>();
                            postmap.put("Title", title);
                            postmap.put("Category",category);
                            postmap.put("Value", value);
                            postmap.put("Description", description);
                            postmap.put("Mobile", phone);
                            postmap.put("city", city);
                            postmap.put("State", state);
                            postmap.put("Userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            postmap.put("Advkey", advKey);
                            postmap.put("link1", downloadUrl.toString());
                            postmap.put("username",name);
                            documentReference.set(postmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Your Advertisement is successfully posted", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    fieldCity.setText(null);
                                    fieldTitle.setText(null);
                                    fieldPhone.setText(null);
                                    fieldDescription.setText(null);
                                    fieldValue.setText(null);
                                    image1.setImageURI(null);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }

                    });


                }
            });

        }else{
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "All fields are Mandatory", Toast.LENGTH_SHORT).show();

        }
        //

    }
    public void userretrive(){
        documentReference2 = fstore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    name = documentSnapshot.getString("name");
                    String  profilelink = documentSnapshot.getString("imageuser");

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            imageUri = data.getData();
            image1.setImageURI(imageUri);

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

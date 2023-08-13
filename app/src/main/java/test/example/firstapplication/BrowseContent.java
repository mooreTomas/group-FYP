package test.example.firstapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import test.example.firstapplication.retrievePDF;


public class BrowseContent extends AppCompatActivity {

    EditText editText;
    Button btn;

    DatabaseReference databaseReference;
    StorageReference storageReference;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_content);


        editText = findViewById(R.id.editText);
        btn = findViewById(R.id.btn);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF");

        btn.setEnabled(false);

        // hide button unless admin is logged in
        // only admin can upload content
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.getEmail().toString().equals("admin.user@gmail.com")) {
            editText.setVisibility(View.VISIBLE);


            editText.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                selectPDF();
                                            }

                                        }
            );
        }
        ;

        if (user.getEmail().toString().equals("admin.user@gmail.com")) {
            btn.setVisibility(View.VISIBLE);
        }


    }


    @SuppressLint("NewApi")
    public void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 12);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            btn.setEnabled(true);
            editText.setText(data.getDataString()
                    .substring(data.getDataString().lastIndexOf("/") + 1));

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    uploadPDFFileFirebase(data.getData());
                }
            });
        }
    }

    private void uploadPDFFileFirebase(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is being uploaded...");
        progressDialog.show();

        StorageReference reference = storageReference.child("upload" + System.currentTimeMillis());

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();

                        Upload upload = new Upload(editText.getText().toString(), uri.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(upload);
                        Toast.makeText(BrowseContent.this, "File uploaded", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("File uploaded..." + (int) progress + "%");

                    }
                });


    }

    public void retrievePDF(View view) {

        startActivity(new Intent(getApplicationContext(), retrievePDF.class));
    }

    // if the phone back button pressed go to customer activity
    public void onBackPressed() {
        Intent intent = new Intent(this, CustomerActivity.class);
        finish();
        startActivity(intent);
    }
}







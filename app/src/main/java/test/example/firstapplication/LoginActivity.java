package test.example.firstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import test.example.firstapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText enteredEmail, enteredPassword;
    FirebaseAuth auth;
    Switch userType;
    DatabaseReference databaseReference;
    public String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textview);
        textView.setText(message);

        userType = findViewById(R.id.switch1);

        enteredEmail = findViewById(R.id.email);
        enteredPassword = findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance("https://database-tutorial-345d5-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

    }

    public void reset(View v) {
        Intent intent = new Intent(this, ResetActivity.class);
        finish();
        startActivity(intent);
    }

    public void clickRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    public void signIn(View v) {
        String email = enteredEmail.getText().toString().trim();
        String password = enteredPassword.getText().toString().trim();

        // Checking if user input is empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        // Checking if user input is empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Signing in with email and password
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "User login success", Toast.LENGTH_LONG).show();
                        FirebaseUser user = auth.getCurrentUser();
                        userID = user.getUid();
                        //Reading data from Firebase
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                showdata(snapshot);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        Toast.makeText(LoginActivity.this, "User login failed", Toast.LENGTH_LONG).show();
                    }

                });
    }

    //Show Data Method
    public void showdata(DataSnapshot dataSnapshot) {

        String custCheck = dataSnapshot.child("Customer").child(userID).child("firstname").getValue(String.class);
        String berCheck = dataSnapshot.child("Assessor").child(userID).child("firstname").getValue(String.class);
        //Login in if Customer
        if (custCheck != null) {
            // Here we will change activity
            Intent intent = new Intent(this, CustomerActivity.class);
            finish();
            startActivity(intent);
        }
        //Login in if Assessor
        else if (berCheck != null) {
            // Here we will change activity
            Intent intent = new Intent(this, AssessorActivity.class);
            finish();
            startActivity(intent);
        } else {
            Toast.makeText(this, "User not found",
                    Toast.LENGTH_LONG).show();
        }
    }

}
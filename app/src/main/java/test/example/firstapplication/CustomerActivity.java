package test.example.firstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import test.example.firstapplication.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import test.example.firstapplication.CustomerPropertyForm;

public class CustomerActivity extends AppCompatActivity {
    FirebaseAuth auth;
    String userID;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userID = Objects.requireNonNull(firebaseUser).getUid();
        databaseReference = FirebaseDatabase.getInstance("https://database-tutorial-345d5-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        databaseReference.child("Customer").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    // Get user values
                    String name = dataSnapshot.child("firstname").getValue(String.class) + " " + dataSnapshot.child("lastname").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);

                    FirebaseOptions options = new FirebaseOptions.Builder()
                            .setApplicationId("1:675485231071:android:4c9b72062dea9610d5866a")
                            .setApiKey("AIzaSyAPj5CXZyjXuxTLAzEtZCO2HydCnJGEp9k")
                            .setDatabaseUrl("https://formagent-y9bl.firebaseio.com/")
                            .build();

                    try {
                        FirebaseApp.initializeApp(CustomerActivity.this, options, "secondary");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Retrieve secondary database
                    FirebaseApp app = FirebaseApp.getInstance("secondary");
                    FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);


                    // If user has created a property form using Iot
                    DatabaseReference dbRef = secondaryDatabase.getReference();
                    dbRef.child("form").child(phone).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Change voice answers to correct option like in dropdown (manually entered)
                                // If any answers like I don't, it wasn't, etc Answer is negative otherwise the positive option
                                String propertyAge = snapshot.child("propertyAge").getValue(String.class);
                                if (propertyAge.contains("n")) {
                                    propertyAge = "Built After 2006";
                                } else {
                                    propertyAge = "Built Before 2006";
                                }

                                String atticInsulation = snapshot.child("atticInsulation").getValue(String.class);
                                if (atticInsulation.contains("n")) {
                                    atticInsulation = "No";
                                } else {
                                    atticInsulation = "Yes";
                                }

                                String wallInsulation = snapshot.child("wallInsulation").getValue(String.class);

                                String floorInsulation = snapshot.child("floorInsulation").getValue(String.class);
                                if (floorInsulation.contains("n")) {
                                    floorInsulation = "No";
                                } else {
                                    floorInsulation = "Yes";
                                }

                                String heatingType = snapshot.child("heatingType").getValue(String.class);
                                String heatingAge = snapshot.child("heatingAge").getValue(String.class);
                                String windowGlazing = snapshot.child("windowGlazing").getValue(String.class);

                                CustomerPropertyForm form = new CustomerPropertyForm(name, propertyAge, atticInsulation, wallInsulation, floorInsulation, heatingType, heatingAge, windowGlazing);

                                databaseReference = databaseReference.child("CustomerPropertyForm");
                                databaseReference.child(firebaseUser.getUid()).setValue(form).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(CustomerActivity.this, CustomerActivity.class);
                                        finish();
                                        startActivity(intent);
                                        dbRef.child("form").child(phone).removeValue();
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logout(View v) {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    public void browse(View v) {
        Intent intent = new Intent(this, BrowseContent.class);
        finish();
        startActivity(intent);
    }

    public void profile(View v) {
        Intent intent = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }

    public void initiate(View view) {
        Intent intent = new Intent(this, BerActivity.class);
        finish();
        startActivity(intent);
    }

    public void notifications(View view) {
        Intent intent = new Intent(this, NotificationActivity.class);
        finish();
        startActivity(intent);
    }
}
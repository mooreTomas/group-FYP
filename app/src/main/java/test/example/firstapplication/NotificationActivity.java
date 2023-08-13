package test.example.firstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import test.example.firstapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

import test.example.firstapplication.BerIntent;
import test.example.firstapplication.Notification;

public class NotificationActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance("https://database-tutorial-345d5-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        FirebaseUser user = auth.getCurrentUser();
        String userID = user.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("Notification").orderByChild("custId").equalTo(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Iterator<DataSnapshot> dataSnapshots = snapshot.getChildren().iterator();
                        HashMap<String, Notification> notificationHashMap = new HashMap<>();

                        // Add keys and values
                        while (dataSnapshots.hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshots.next();
                            Notification notice = dataSnapshotChild.getValue(Notification.class);
                            String uid = dataSnapshotChild.getKey();
                            notificationHashMap.put(uid, notice);
                        }
                        LinearLayout linearLayout = findViewById(R.id.layout);

                        if (notificationHashMap.size() != 0) {
                            // Display keys and values
                            for (String i : notificationHashMap.keySet()) {
                                final boolean[] clicked = {false};
                                TextView textView = new TextView(NotificationActivity.this);
                                Notification notification = notificationHashMap.get(i);
                                TextView textView2 = new TextView(NotificationActivity.this);

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        BerIntent form = snapshot.child("BerIntentForm").child(i).getValue(BerIntent.class);
                                        String assessorID = snapshot.child("BerIntentForm").child(i).child("chosenAssessorID").getValue(String.class);
                                        String assessor = snapshot.child("Assessor").child(assessorID).child("firstname").getValue(String.class) + " " + snapshot.child("Assessor").child(assessorID).child("lastname").getValue(String.class) + " " + snapshot.child("Assessor").child(assessorID).child("email").getValue(String.class) ;

                                        String text = assessor + " has responded to your intent form " + i + "\n";
                                        String response = form.toString() + "\nResponse: " + notification.getDetails() + "\n";
                                        textView.setText(text);

                                        textView2.setText(response);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                textView.setTextColor(ContextCompat.getColor(NotificationActivity.this, R.color.blue));
                                textView2.setTextColor(ContextCompat.getColor(NotificationActivity.this, R.color.black));
                                textView2.setVisibility(View.GONE);

                                textView.setOnClickListener(view -> {
                                    if (!clicked[0]) {
                                        textView2.setVisibility(View.VISIBLE);
                                        clicked[0] = true;
                                    } else {
                                        textView2.setVisibility(View.GONE);
                                        clicked[0] = false;
                                    }
                                });

                                linearLayout.addView(textView);
                                linearLayout.addView(textView2);
                            }

                        } else {
                            TextView text = findViewById(R.id.textview1);
                            String none = "No Notifications at present";
                            text.setText(none);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // add back button to customer activity at top of screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, CustomerActivity.class);
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    // if the phone back button pressed go to customer activity
    public void onBackPressed() {
        Intent intent = new Intent(this, CustomerActivity.class);
        finish();
        startActivity(intent);
    }
}
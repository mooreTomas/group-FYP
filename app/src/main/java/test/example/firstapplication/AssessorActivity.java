package test.example.firstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import test.example.firstapplication.BerIntent;
import test.example.firstapplication.Notification;

public class AssessorActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessor);

        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance("https://database-tutorial-345d5-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        FirebaseUser user = auth.getCurrentUser();
        String userID = user.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.child("BerIntentForm").orderByChild("chosenAssessorID").equalTo(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Iterator<DataSnapshot> dataSnapshots = snapshot.getChildren().iterator();
                        HashMap<String, BerIntent> forms = new HashMap<>();

                        // Add keys and values
                        while (dataSnapshots.hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshots.next();
                            BerIntent form = dataSnapshotChild.getValue(BerIntent.class);
                            String uid = dataSnapshotChild.getKey();
                            forms.put(uid, form);
                        }

                        LinearLayout linearLayout = findViewById(R.id.layout);
                        if (forms.size() != 0) {
                            // Display keys and values
                            for (String i : forms.keySet()) {
                                final boolean[] clicked = {false};
                                TextView textView = new TextView(AssessorActivity.this);
                                BerIntent berIntent = forms.get(i);
                                String text = berIntent.getOwnerName() + " sent Intent form: " + i + " on the " + berIntent.getDate() + "\n";
                                textView.setText(text);
                                textView.setTextColor(ContextCompat.getColor(AssessorActivity.this, R.color.blue));


                                String custForm = berIntent.toString();
                                TextView textView2 = new TextView(AssessorActivity.this);
                                textView2.setText(custForm);
                                textView2.setTextColor(ContextCompat.getColor(AssessorActivity.this, R.color.black));
                                textView2.setVisibility(View.GONE);

                                Button update = new Button(AssessorActivity.this);
                                // Check if form already has a notification
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String exists = snapshot.child("Notification").child(String.valueOf(i)).child("details").getValue(String.class);
                                        if (exists != null) {
                                            update.setText(exists);
                                        } else {
                                            String btnText = "Send Notification";
                                            update.setText(btnText);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                update.setVisibility(View.GONE);

                                textView.setOnClickListener(view -> {
                                    if (!clicked[0]) {
                                        textView2.setVisibility(View.VISIBLE);
                                        update.setVisibility(View.VISIBLE);
                                        clicked[0] = true;
                                    } else {
                                        textView2.setVisibility(View.GONE);
                                        update.setVisibility(View.GONE);
                                        clicked[0] = false;
                                    }
                                });

                                update.setOnClickListener(view -> {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AssessorActivity.this);
                                    // Add the buttons
                                    builder.setTitle("Send Notification");
                                    String[] items = {"Accept", "Reject"};
                                    int checkedItem = 1;

                                    final String[] dateSelected = new String[1];
                                    final String[] timeSelected = new String[1];
                                    final Boolean[] rejection = new Boolean[1];
                                    rejection[0] = true;

                                    builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
                                        switch (which) {
                                            case 0:
                                                // Get Current Date
                                                final Calendar c = Calendar.getInstance();
                                                int mYear = c.get(Calendar.YEAR);
                                                int mMonth = c.get(Calendar.MONTH);
                                                int mDay = c.get(Calendar.DAY_OF_MONTH);

                                                // Get Current Time
                                                int mHour = c.get(Calendar.HOUR_OF_DAY);
                                                int mMinute = c.get(Calendar.MINUTE);

                                                // pick time
                                                TimePickerDialog timePickerDialog = new TimePickerDialog(AssessorActivity.this,
                                                        (view12, hourOfDay, minute) -> timeSelected[0] = (hourOfDay + ":" + minute), mHour, mMinute, false);

                                                timePickerDialog.setTitle("Arrange Visit");
                                                timePickerDialog.show();

                                                // pick date
                                                DatePickerDialog datePickerDialog = new DatePickerDialog(AssessorActivity.this,
                                                        (view1, year, monthOfYear, dayOfMonth) ->
                                                                dateSelected[0] = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, mYear, mMonth, mDay);

                                                datePickerDialog.setTitle("Arrange Visit");
                                                datePickerDialog.show();

                                                rejection[0] = false;

                                                break;

                                            case 1:
                                                rejection[0] = true;
                                                break;
                                        }
                                    });

                                    Date today = new Date();

                                    // User clicked OK button
                                    builder.setPositiveButton("OK", (dialog, which) -> {
                                        dialog.dismiss();
                                        if (dateSelected[0] != null && timeSelected[0] != null) {
                                            String dateTime = "Visit Scheduled for " + dateSelected[0] + " " + timeSelected[0];
                                            String failed = "Notification failed to be sent";

                                            Notification notification = new Notification(berIntent.getCustId(), userID, dateTime, today.toString());

                                            databaseReference.child("Notification").child(String.valueOf(i)).setValue(notification).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    update.setText(dateTime);
                                                } else {
                                                    update.setText(failed);
                                                }
                                            });

                                        } else if (rejection[0]) {
                                            String notice = "Rejection Sent";
                                            String failed = "Notification failed to be sent";

                                            Notification notification = new Notification(berIntent.getCustId(), userID, notice, today.toString());

                                            databaseReference.child("Notification").child(String.valueOf(i)).setValue(notification).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    update.setText(notice);
                                                } else {
                                                    update.setText(failed);
                                                }
                                            });
                                        }
                                    });
                                    // User clicked No button
                                    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss()
                                    );

                                    // Create the AlertDialog
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                });

                                linearLayout.addView(textView);
                                linearLayout.addView(textView2);
                                linearLayout.addView(update);
                            }
                        } else {
                            TextView text = findViewById(R.id.textview1);
                            String noForms = "No Customer Forms at present";
                            text.setText(noForms);
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

    public void logout(View v) {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);
    }
}
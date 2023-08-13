package test.example.firstapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import test.example.firstapplication.BerIntent;

public class BerActivity extends AppCompatActivity {
    TextView owner, property_age, attic, wall, floor, heating, window, attic_suggestion, wall_suggestion, floor_suggestion, heat_suggestion, window_suggestion;
    CheckBox attic_check, wall_check, floor_check, heat_check, window_check;
    String not_app = "Not Applicable";
    MapsActivity mapsActivity = new MapsActivity();

    FirebaseAuth auth;
    String userID;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Button select;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ber);

        select = findViewById(R.id.select);

        select.setOnClickListener(view -> startActivity(new Intent(BerActivity.this, MapsActivity.class)));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userID = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance("https://database-tutorial-345d5-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        databaseReference.child("CustomerPropertyForm").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    // Get Current Values From Property Profile

                    // Owner Name
                    String name = dataSnapshot.child("ownerName").getValue(String.class);
                    owner = findViewById(R.id.owner);
                    owner.setText(name);

                    // Property Age
                    String age = dataSnapshot.child("propertyAge").getValue(String.class);
                    property_age = findViewById(R.id.property_age);
                    property_age.setText(age);

                    // Attic Insulation
                    String att = dataSnapshot.child("atticInsulation").getValue(String.class);
                    attic = findViewById(R.id.attic);
                    attic.setText(att);

                    // Wall Insulation
                    String wallInsulation = dataSnapshot.child("wallInsulation").getValue(String.class);
                    wall = findViewById(R.id.wall);
                    wall.setText(wallInsulation);

                    // Floor Insulation
                    String floorInsulation = dataSnapshot.child("floorInsulation").getValue(String.class);
                    floor = findViewById(R.id.floor);
                    floor.setText(floorInsulation);

                    // Heating System & Age
                    String h = dataSnapshot.child("heatingType").getValue(String.class);
                    String h_age = dataSnapshot.child("heatingAge").getValue(String.class);
                    heating = findViewById(R.id.heating);
                    String heating_suggestion = h + "\n" + h_age;
                    heating.setText(heating_suggestion);

                    // Window Glazing
                    String w_glazing = dataSnapshot.child("windowGlazing").getValue(String.class);
                    window = findViewById(R.id.window);
                    window.setText(w_glazing);

                    // Set Suggestions based on current

                    attic_suggestion = findViewById(R.id.attic_option);
                    attic_check = findViewById(R.id.atticCheckBox);
                    wall_suggestion = findViewById(R.id.wall_option);
                    wall_check = findViewById(R.id.wallCheckBox);
                    floor_suggestion = findViewById(R.id.floor_option);
                    floor_check = findViewById(R.id.floorCheckBox);
                    heat_suggestion = findViewById(R.id.heat_option);
                    heat_check = findViewById(R.id.heatCheckBox);
                    window_suggestion = findViewById(R.id.window_option);
                    window_check = findViewById(R.id.windowCheckBox);

                    // attic
                    if (attic.getText().toString().equalsIgnoreCase("yes")) {
                        attic_suggestion.setText(not_app);
                        attic_check.setEnabled(false);
                    } else {
                        attic_suggestion.setText("Insulate Attic");
                    }

                    // wall
                    ArrayList<String> wall_insulations = new ArrayList<>();
                    wall_insulations.add("Cavity");
                    wall_insulations.add("Internal");
                    wall_insulations.add("External");

                    // Leave only insulation options the user doesn't already have
                    if (wall.getText().toString().toUpperCase().contains("CAVITY"))
                        wall_insulations.remove("Cavity");
                    if (wall.getText().toString().toUpperCase().contains("INTERNAL"))
                        wall_insulations.remove("Internal");
                    if (wall.getText().toString().toUpperCase().contains("EXTERNAL"))
                        wall_insulations.remove("External");

                    if (wall_insulations.size() != 0) {
                        String wall = "";
                        for (String n : wall_insulations) {
                            wall = wall + " " + (n);
                        }
                        wall += " Insulation";
                        wall_suggestion.setText(wall);
                    } else {
                        wall_suggestion.setText(not_app);
                        wall_check.setEnabled(false);
                    }

                    // floor
                    if (floor.getText().toString().equalsIgnoreCase("Yes")) {
                        floor_suggestion.setText(not_app);
                        floor_check.setEnabled(false);
                    } else {
                        floor_suggestion.setText("Insulate Floor");
                    }

                    // heating
                    if (heating.getText().toString().contains("Less than 1 year") || heating.getText().toString().contains("1 to 2")) {
                        heat_suggestion.setText(not_app);
                        heat_check.setEnabled(false);
                    } else if (heating.getText().toString().contains("2 to 5")) {
                        heat_suggestion.setText("Consider Replacing in a few years time");
                        heat_check.setEnabled(false);
                    } else if (heating.getText().toString().contains("6 to 10")) {
                        heat_suggestion.setText("Consider Replacing");
                    } else if (heating.getText().toString().contains("More than 10 years") || heating.getText().toString().contains("10 +") || heating.getText().toString().toLowerCase().contains("10 plus")) {
                        heat_suggestion.setText("Replace");
                    }

                    // window
                    if (window.getText().toString().toUpperCase().contains("SINGLE")) {
                        window_suggestion.setText("Double Glazing");
                    } else if (window.getText().toString().toUpperCase().contains("DOUBLE")) {
                        window_suggestion.setText("Triple Glazing");
                    } else if (window.getText().toString().toUpperCase().contains("TRIPLE")) {
                        window_suggestion.setText("Quadruple Glazing");
                    } else {
                        window_suggestion.setText(not_app);
                        wall_check.setEnabled(false);
                    }
                } else {
                    // if user has not already created a property profile redirect them
                    Intent intent = new Intent(BerActivity.this, ProfileActivity.class);
                    finish();
                    Toast.makeText(BerActivity.this, "Please create Property Profile first", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    // Get chosen values and add intent form to database
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendAssessor(View v) {
        String name, age, attic_chosen, wall_chosen, floor_chosen, heat_chosen, window_chosen;

        if (attic_check.isChecked()) {
            attic_chosen = attic_suggestion.getText().toString();
        } else {
            attic_chosen = not_app;
        }
        if (wall_check.isChecked()) {
            wall_chosen = wall_suggestion.getText().toString();
        } else {
            wall_chosen = not_app;
        }
        if (floor_check.isChecked()) {
            floor_chosen = floor_suggestion.getText().toString();
        } else {
            floor_chosen = not_app;
        }
        if (heat_check.isChecked()) {
            heat_chosen = heat_suggestion.getText().toString();
        } else {
            heat_chosen = not_app;
        }
        if (window_check.isChecked()) {
            window_chosen = window_suggestion.getText().toString();
        } else {
            window_chosen = not_app;
        }

        name = owner.getText().toString();
        age = property_age.getText().toString();


        String assessor = mapsActivity.chosenAssessorID;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        String date = dateFormat.format(d);

        if (assessor != null) {
            BerIntent form = new BerIntent(name, age, attic_chosen, wall_chosen, floor_chosen, heat_chosen, window_chosen, assessor, userID, date);
            // unique id for each form instead of using customer id so that the customer can create multiple forms
            String uniqueId = String.valueOf(form.hashCode());
            databaseReference = databaseReference.child("BerIntentForm");
            databaseReference.child(uniqueId).setValue(form).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(this, CustomerActivity.class);
                    finish();
                    startActivity(intent);
                    Toast.makeText(BerActivity.this, "Intent form added to database", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(BerActivity.this, "Intent form failed to be stored in database", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(BerActivity.this, "Please select an assessor", Toast.LENGTH_LONG).show();
        }

    }

    // add back button to customer activity at top of screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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

    // if phone back button pressed
    public void onBackPressed() {
        Intent intent = new Intent(this, CustomerActivity.class);
        finish();
        startActivity(intent);
    }
}
package test.example.firstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Collections;

import test.example.firstapplication.CustomerPropertyForm;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String ownername;
    Spinner age_spinner, attic_spinner, floor_spinner, heating_spinner, heating_age_spinner, window_spinner;
    String age_result, attic_result, floor_result, heating_result, heating_age_result, window_result;
    TextView wall_insulation, owner;
    boolean[] selected;
    ArrayList<Integer> wallList = new ArrayList<>();
    String[] wallArray = {"Cavity", "Internal", "External"};
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Owner Name
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance("https://database-tutorial-345d5-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Customer").child(userID).child("firstname").getValue(String.class);
                String surname = dataSnapshot.child("Customer").child(userID).child("lastname").getValue(String.class);
                ownername = name + " " + surname;
                owner = findViewById(R.id.owner);
                owner.setText(ownername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Property Age
        age_spinner = findViewById(R.id.property_age_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.age_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age_spinner.setAdapter(adapter);

        // Attic Insulation
        attic_spinner = findViewById(R.id.attic_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.yes_no_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attic_spinner.setAdapter(adapter2);

        // Wall Insulation Multiple choice
        wall_insulation = findViewById(R.id.wall_text);
        // initialize selected array
        selected = new boolean[wallArray.length];

        wall_insulation.setOnClickListener(view -> {
            // Initialize alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            // set title
            builder.setTitle("Select your Property Wall Insulation types");
            // set dialog non cancelable
            builder.setCancelable(false);

            builder.setMultiChoiceItems(wallArray, selected, (dialogInterface, i, b) -> {
                // check condition
                if (b) {
                    // when checkbox selected add position in list
                    wallList.add(i);
                    // Sort array list
                    Collections.sort(wallList);
                } else {
                    // when checkbox unselected remove position from list
                    wallList.remove(Integer.valueOf(i));
                }
            });

            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                // Initialize string builder
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < wallList.size(); j++) {
                    // concat array value
                    stringBuilder.append(wallArray[wallList.get(j)]);
                    // check condition
                    if (j != wallList.size() - 1) {
                        // When j value  not equal to list size - 1 add comma
                        stringBuilder.append(", ");
                    }
                }
                // set text on textView
                wall_insulation.setText(stringBuilder.toString());
            });

            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                // dismiss dialog
                dialogInterface.dismiss();
            });

            builder.setNeutralButton("Clear All", (dialogInterface, i) -> {
                for (int j = 0; j < selected.length; j++) {
                    // remove all selection
                    selected[j] = false;
                    // clear language list
                    wallList.clear();
                    // clear text view value
                    wall_insulation.setText("");
                }
            });
            // show dialog
            builder.show();
        });


        // Floor Insulation
        floor_spinner = findViewById(R.id.floor_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.yes_no_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floor_spinner.setAdapter(adapter3);

        // Heating Type
        heating_spinner = findViewById(R.id.heating_spinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.heating_array, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heating_spinner.setAdapter(adapter4);

        // Heating System Age
        heating_age_spinner = findViewById(R.id.heating_age_spinner);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this, R.array.heating_age_array, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heating_age_spinner.setAdapter(adapter5);

        // Window Glazing
        window_spinner = findViewById(R.id.window_spinner);
        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this, R.array.window_array, android.R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        window_spinner.setAdapter(adapter6);

        // Start listening
        age_spinner.setOnItemSelectedListener(this);
        attic_spinner.setOnItemSelectedListener(this);
        floor_spinner.setOnItemSelectedListener(this);
        heating_spinner.setOnItemSelectedListener(this);
        heating_age_spinner.setOnItemSelectedListener(this);
        window_spinner.setOnItemSelectedListener(this);

    }

    public void getValues(View v) {
        String owner_name = owner.getText().toString();
        String wall_result = wall_insulation.getText().toString();
        if (wall_result.equals("")) wall_result = "none";

        CustomerPropertyForm form = new CustomerPropertyForm(owner_name, age_result, attic_result, wall_result, floor_result, heating_result, heating_age_result, window_result);

        databaseReference = databaseReference.child("CustomerPropertyForm");
        databaseReference.child(firebaseUser.getUid()).setValue(form).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(this, CustomerActivity.class);
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(ProfileActivity.this, "Values failed to be stored in database", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.property_age_spinner:
                age_result = age_spinner.getSelectedItem().toString();
                break;
            case R.id.attic_spinner:
                attic_result = attic_spinner.getSelectedItem().toString();
                break;
            case R.id.floor_spinner:
                floor_result = floor_spinner.getSelectedItem().toString();
                break;
            case R.id.heating_spinner:
                heating_result = heating_spinner.getSelectedItem().toString();
                break;
            case R.id.heating_age_spinner:
                heating_age_result = heating_age_spinner.getSelectedItem().toString();
                break;
            case R.id.window_spinner:
                window_result = window_spinner.getSelectedItem().toString();
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(ProfileActivity.this, "Nothing Selected", Toast.LENGTH_LONG).show();
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

    // if the phone back button pressed go to customer activity
    public void onBackPressed() {
        Intent intent = new Intent(this, CustomerActivity.class);
        finish();
        startActivity(intent);
    }
}
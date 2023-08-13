package test.example.firstapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import test.example.firstapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.example.firstapplication.Assessor;
import test.example.firstapplication.Customer;
import test.example.firstapplication.GpsTracker;

public class RegisterActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.firstapplication.MESSAGE";

    EditText firstname, lastname, email, password, phone;
    TextView longitude, latitude;
    Switch userType;
    Boolean switchState;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        userType = findViewById(R.id.switch1);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GpsTracker gpsTracker = new GpsTracker(RegisterActivity.this);
        if (gpsTracker.canGetLocation()) {
            double d_latitude = gpsTracker.getLatitude();
            double d_longitude = gpsTracker.getLongitude();
            latitude.setText(String.valueOf(d_latitude));
            longitude.setText(String.valueOf(d_longitude));
        } else {
            gpsTracker.showSettingsAlert();
        }


        auth = FirebaseAuth.getInstance();
    }

    public void registerUser(View v) {

        boolean firstnameBlank;
        boolean lastnameBlank;

        if(firstname.getText().toString().isEmpty()){
            firstname.setError("Please enter your firstname");
            firstnameBlank = true;
        }else{
            firstnameBlank = false;
        }

        if(lastname.getText().toString().isEmpty()){
            lastname.setError("Please enter your lastname");
            lastnameBlank = true;
        }else{
            lastnameBlank = false;
        }

        String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern e_pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher e_matcher = e_pattern.matcher(email.getText().toString());
        if (!e_matcher.matches()) {
            email.setError("Please enter a valid email. E.g., example@gmail.com. Must contain @ ");
        }

        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*%~$^+=<>]).{8,20}$";

        Pattern passpattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = passpattern.matcher(password.getText().toString());
        if (!matcher.matches()) {
            password.setError("Passwords must be minimum 8 in length and contain 1 digit, upper, lower and special character");
        }

        Pattern p = Pattern.compile("[0][8][3,5,6,7][0-9]{7}");
        Matcher m = p.matcher(phone.getText().toString());
        if (!m.matches()) {
            phone.setError("Phone Numbers must be of length 10 and start with 08(3/5/6/7)");
        }

        if (e_matcher.matches() && matcher.matches() && m.matches() && !firstnameBlank && !lastnameBlank) {
            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                    createDatabaseValues();

                } else {
                    Toast.makeText(RegisterActivity.this, "User could not be registered", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void clickLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        String message = "Login";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void createDatabaseValues() {
        firebaseUser = auth.getCurrentUser();

        String forename = firstname.getText().toString().trim();
        String surname = lastname.getText().toString().trim();
        String strEmail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String mobile = phone.getText().toString().trim();
        String lat = latitude.getText().toString().trim();
        String lon = longitude.getText().toString().trim();

        switchState = userType.isChecked();

        if (switchState) {
            // create customer user
            Toast.makeText(RegisterActivity.this, "Customer Values stored in database", Toast.LENGTH_LONG).show();
            databaseReference = FirebaseDatabase.getInstance("https://database-tutorial-345d5-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Customer");
            Customer user = new Customer(forename, surname, strEmail, pass, mobile, lat, lon);

            databaseReference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Customer Values stored in database", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Customer Values failed to be stored in database", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            // create assessor user
            databaseReference = FirebaseDatabase.getInstance("https://database-tutorial-345d5-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Assessor");
            Assessor user = new Assessor(forename, surname, strEmail, pass, mobile, lat, lon);

            databaseReference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Assessor Values stored in database", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Assessor Values failed to be stored in database", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}
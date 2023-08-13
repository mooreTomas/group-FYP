package test.example.firstapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import test.example.firstapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
    }

    public void send(View v) {
        EditText enteredEmail = findViewById(R.id.email);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = enteredEmail.getText().toString();

        String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern e_pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher e_matcher = e_pattern.matcher(emailAddress);
        if (!e_matcher.matches()) {
            enteredEmail.setError("Please enter a valid email. E.g., example@gmail.com. Must contain @ ");
        } else {

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            TextView success = findViewById(R.id.textView);
                            String text = "Email Sent";
                            success.setText(text);
                        }
                    });
        }
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);
    }
}
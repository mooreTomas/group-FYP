package test.example.firstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import test.example.firstapplication.R;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "test.example.firstapplication.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void loginScreen(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        String message = "Login";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void registerScreen(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        String message = "Register";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


}
package com.example.allaskereso_mobilalk.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allaskereso_mobilalk.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Optional;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String EMAIL_KEY = "email";
    private static final String PACKAGE_ERROR_MESSAGE = "Package name is null";

    EditText emailInput;
    EditText passwordInput;
    EditText passwordConfirmInput;

    private SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        passwordConfirmInput = findViewById(R.id.input_password_again);

        String packageName = Optional.ofNullable(RegisterActivity.class.getPackage()).map(Package::getName).orElseThrow(() -> new NullPointerException(PACKAGE_ERROR_MESSAGE));

        preferences = getSharedPreferences(packageName, MODE_PRIVATE);
        emailInput.setText(preferences.getString(EMAIL_KEY, ""));

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void register(View view) {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String passwordAgain = passwordConfirmInput.getText().toString();

        if (!isValidInput(email, password, passwordAgain)) {
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Successful registration");
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(loginSuccess -> {
                                Log.d(LOG_TAG, "Successful login");
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(loginFail -> {
                                Log.d(LOG_TAG, "Login failed");
                                Toast.makeText(RegisterActivity.this, "Login failed, please try again!", Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(fail -> {
                    Log.d(LOG_TAG, "Registration failed");
                    Toast.makeText(RegisterActivity.this, "Registration failed, please try again!", Toast.LENGTH_LONG).show();
                });
    }

    private boolean isValidInput(String email, String password, String passwordAgain) {
        return isValidEmail(email) && isValidPassword(password, passwordAgain);
    }

    private boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            Toast.makeText(this, "A mezők nem lehetnek üresek!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String passwordAgain) {
        if (password.isEmpty() || passwordAgain.isEmpty()) {
            Toast.makeText(this, "A mezők nem lehetnek üresek!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.equals(passwordAgain)) {
            Toast.makeText(this, "A két jelszó nem egyezik meg!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "A jelszónak legalább 6 karakter hosszúnak kell lennie!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL_KEY, emailInput.getText().toString());
        editor.apply();
    }
}

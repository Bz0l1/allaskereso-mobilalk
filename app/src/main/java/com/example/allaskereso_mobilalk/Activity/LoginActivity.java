package com.example.allaskereso_mobilalk.Activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allaskereso_mobilalk.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.Optional;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getName();
    private static final String EMAIL_KEY = "email_value";
    private static final String PASSWORD_KEY = "password_value";
    private static final String PACKAGE_ERROR_MESSAGE = "Package name is null";

    EditText emailInput;
    EditText passwordInput;

    private SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);

        String packageName = Optional.ofNullable(LoginActivity.class.getPackage())
                .map(Package::toString)
                .orElseThrow(() -> new NullPointerException(PACKAGE_ERROR_MESSAGE));

        preferences = getSharedPreferences(packageName, MODE_PRIVATE);

        emailInput.setText(preferences.getString(EMAIL_KEY, ""));
        passwordInput.setText(preferences.getString(PASSWORD_KEY, ""));

        firebaseAuth = FirebaseAuth.getInstance();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void login(View view)
    {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        showLoadingDialog();


        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(LoginActivity.this, "A mezők nem lehetnek üresek!", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(success ->{
                    hideLoadingDialog();
                    Log.d(LOG_TAG, "Successful login");
                    launchOffers();
                })
                .addOnFailureListener(fail -> {
                    hideLoadingDialog();
                    Log.d(LOG_TAG, "Login failed");
                    Toast.makeText(LoginActivity.this, "Sikertelen bejelentkezés. Próbáld újra!", Toast.LENGTH_LONG).show();
                    passwordInput.setText("");
                });
    }

    private void launchOffers()
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void register(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email_value", emailInput.getText().toString());
        editor.putString("password_value", passwordInput.getText().toString());
        editor.apply();
    }

    private void showLoadingDialog() {
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
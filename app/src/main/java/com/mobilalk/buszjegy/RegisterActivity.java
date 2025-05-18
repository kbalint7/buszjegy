package com.mobilalk.buszjegy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();

    EditText userNameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key != MainActivity.SECRET_KEY) {
            finish();
        }

        userNameET = findViewById(R.id.editTextUserName);
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        passwordAgainET = findViewById(R.id.editTextPasswordAgain);

        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view) {
        String userName = userNameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Az összes mezőt ki kell tölteni!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(passwordAgain)) {
            Toast.makeText(RegisterActivity.this, "A két jelszó nem egyezik!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "User created successfully");
                    startShopping();
                } else {
                    Log.d(LOG_TAG, "User creation error: " + task.getException().getMessage());
                    Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public void startShopping() {
        Intent intent = new Intent(this, TicketListActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.example.cybergpt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login_screen extends AppCompatActivity {
        TextView login_google;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);

        login_google = findViewById(R.id.login_google);


        login_google.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                });
                }
    }

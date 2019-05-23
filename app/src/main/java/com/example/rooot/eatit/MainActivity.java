package com.example.rooot.eatit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    Button btnSignIn , btnSignUp;
    TextView slogan;
    Typeface face;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitUi();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signIn = new Intent(MainActivity.this, SignIn.class);
                    startActivity(signIn); }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signUp = new Intent(MainActivity.this, SignUp.class);
                    startActivity(signUp);
                }
        });

    }

    private void InitUi() {
        btnSignIn = findViewById(R.id.signInbtn);
        btnSignUp = findViewById(R.id.signUpbtn);

        slogan = findViewById(R.id.txtSlogan);
        face = Typeface.createFromAsset(getAssets() , "fonts/NABILA.TTF");
        slogan.setTypeface(face);

    }

}

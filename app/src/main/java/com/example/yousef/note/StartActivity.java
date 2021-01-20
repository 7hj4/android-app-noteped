package com.example.yousef.note;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private Button Login , Regiser ;
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuth = FirebaseAuth.getInstance();

        updateUI();

        Login = (Button)findViewById(R.id.Login);
        Regiser = (Button)findViewById(R.id.Register);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        Regiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

    }


    private void Login(){
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void Register(){
        Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void updateUI(){
        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.i("StartActivity", "mAuth == null");
        }
    }
    }

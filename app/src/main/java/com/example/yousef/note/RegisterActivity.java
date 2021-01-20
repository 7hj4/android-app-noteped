package com.example.yousef.note;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText Name , Email , Password ;
    private Button Regsiter ;
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter);

        Name = (EditText)findViewById(R.id.Register_name);

        Email = (EditText)findViewById(R.id.Register_email);

        Password = (EditText)findViewById(R.id.Register_password);

        Regsiter = (Button)findViewById(R.id.btnRegsiter);

        mAuth = FirebaseAuth.getInstance();

        Regsiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = Name.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String pass = Password.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(RegisterActivity.this,"User Register",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this,"Filed",Toast.LENGTH_SHORT).show();

                        }

                    }
                });


            }
        });

    }
}

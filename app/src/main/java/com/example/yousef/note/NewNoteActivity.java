package com.example.yousef.note;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewNoteActivity extends AppCompatActivity {

    private EditText noteTitle, note;
    private Button create_Note;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private String noteID;
    private Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);


        noteID = getIntent().getStringExtra("noteId");

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        noteTitle = (EditText) findViewById(R.id.note_title);
        note = (EditText) findViewById(R.id.note);
        create_Note = (Button) findViewById(R.id.create_note);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Notes");


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(mAuth.getCurrentUser().getUid());

        create_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = noteTitle.getText().toString().trim();
                String notes = note.getText().toString().trim();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(notes)) {
                    createNote(title , notes);
                    Toast.makeText(NewNoteActivity.this, "Note added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NewNoteActivity.this, "check not Empty title or notes ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

        public void createNote(String title, String notes){
            if (mAuth.getCurrentUser() != null) {
                    // CREATE A NEW NOTE
                    final DatabaseReference newNoteRef = databaseReference.push();

                    final Map noteMap = new HashMap();
                    noteMap.put("title", title);
                    noteMap.put("notes", notes);
                    noteMap.put("timestamp", ServerValue.TIMESTAMP);

                    Thread mainThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(NewNoteActivity.this, "Note added", Toast.LENGTH_SHORT).show();
                                       // finish();
                                    } else {
                                      //  Toast.makeText(NewNoteActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    });
                    mainThread.start();

            } else {
                Toast.makeText(this, "USERS IS NOT SIGNED IN", Toast.LENGTH_SHORT).show();
            }

        }

}



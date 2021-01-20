package com.example.yousef.note;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class UpdateActivity extends AppCompatActivity {

    private Toolbar toolbar ;
    private EditText title_update , note_update ;
    private FirebaseAuth mAuth ;
    private DatabaseReference databaseReference ;

    private String noteId ;
    private boolean isExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        try {
            noteId = getIntent().getStringExtra("noteId");

            if (!noteId.trim().equals("")) {
                isExist = true;
            } else {
                isExist = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        title_update = (EditText) findViewById(R.id.title_update);
        note_update = (EditText) findViewById(R.id.note_update);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update");

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(mAuth.getCurrentUser().getUid());

        if (isExist) {
            databaseReference.child(noteId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("notes")) {
                        String title = dataSnapshot.child("title").getValue().toString();
                        String notes = dataSnapshot.child("notes").getValue().toString();

                        title_update.setText(title);
                        note_update.setText(notes);


                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    public void Update(){

        Map updateMap = new HashMap();
        updateMap.put("title", title_update.getText().toString().trim());
        updateMap.put("notes", note_update.getText().toString().trim());
        updateMap.put("timestamp", ServerValue.TIMESTAMP);

        databaseReference.child(noteId).updateChildren(updateMap);

        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();

    }

    public void deleteNotes(){

        databaseReference.child(noteId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UpdateActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                    noteId = "no";
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.update, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.update){
            Update();
            finish();
        }

        if(item.getItemId() == R.id.delete){
            deleteNotes();
            finish();
        }

        return true ;
    }
}

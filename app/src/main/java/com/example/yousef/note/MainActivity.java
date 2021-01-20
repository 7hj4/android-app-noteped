package com.example.yousef.note;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference ;
    private FirebaseAuth mAuth ;
    private RecyclerView recyclerView ;
    private Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notes");


        recyclerView = (RecyclerView) findViewById(R.id.notes_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(mAuth.getCurrentUser().getUid());
        }

        updateUI();
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<NoteModel, NoteView> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel, NoteView>(

                NoteModel.class,
                R.layout.notes_show,
                NoteView.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(final NoteView viewHolder, NoteModel model, int position) {

                final String noteId = getRef(position).getKey();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        databaseReference.child(noteId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("title")&& dataSnapshot.hasChild("timestamp")){

                                    String title = dataSnapshot.child("title").getValue().toString();
                                    String time = dataSnapshot.child("timestamp").getValue().toString();


                                    viewHolder.setNoteTitle(title);

                                    Time getTime = new Time();
                                    viewHolder.setNoteTime(getTime.Time(Long.parseLong(time), getApplicationContext()));
                                    viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                                            intent.putExtra("noteId", noteId);
                                            startActivity(intent);
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                thread.start();

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void updateUI(){

        if (mAuth.getCurrentUser() != null){
            Log.i("MainActivity", "fAuth != null");
        } else {
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
            Log.i("MainActivity", "fAuth == null");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.signOut){
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this,StartActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.Newnote){
            Intent intent = new Intent(MainActivity.this,NewNoteActivity.class);
            startActivity(intent);
        }

        return true ;
    }
}

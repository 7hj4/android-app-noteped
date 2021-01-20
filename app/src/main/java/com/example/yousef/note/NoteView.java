package com.example.yousef.note;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class NoteView extends RecyclerView.ViewHolder {

    View view ;

    private TextView note_title , note_time ;
     CardView cardview ;

    public NoteView(@NonNull View itemView) {
        super(itemView);

        view = itemView ;

        note_title = view.findViewById(R.id.note_title);
        note_time = view.findViewById(R.id.note_time);
        cardview = view.findViewById(R.id.cardView);

    }

    public void setNoteTitle(String title){
        note_title.setText(title);
    }
    public void setNoteTime(String Time){
        note_time.setText(Time);
    }
}

package com.example.vipul.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vipul.notes.data.NotesContract;
import com.example.vipul.notes.data.NotesDbHelper;


public class EditorActivity extends AppCompatActivity {

    EditText title, content ;
    NotesDbHelper notesDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        //Intent intent = getIntent();
        /*noteId = intent.getIntExtra("noteId",-1);
        if(noteId != -1)
        {
            title.setText(MainActivity.notes.get(noteId));
        }

        title.addTextChangedListener(this);*/

        FloatingActionButton fab = findViewById(R.id.fab_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNote();
                MainActivity m =new MainActivity();
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void insertNote(){

        notesDbHelper = new NotesDbHelper(this);
        SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        String titleText = title.getText().toString().trim();
        String contentText = content.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(NotesContract.NotesEntry.COLUMN_NAME_TITLE, titleText);
        values.put(NotesContract.NotesEntry.COLUMN_NAME_CONTENT, contentText);

        long newRowId = db.insert(NotesContract.NotesEntry.TABLE_NAME, null, values);
        Log.v("EditorActivity", String.valueOf(newRowId));

        if(newRowId == -1)
            Toast.makeText(this, "failed!",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"successfully saved with id: "+newRowId, Toast.LENGTH_LONG ).show();

    }

    /*@Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        *//*MainActivity.notes.set(noteId, String.valueOf(charSequence));
        MainActivity.arrayAdapter.notifyDataSetChanged();*//*
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }*/
}

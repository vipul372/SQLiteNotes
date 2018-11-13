package com.example.vipul.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vipul.notes.data.NotesContract;
import com.example.vipul.notes.data.NotesDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


   /* static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;*/
    NotesDbHelper notesDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /*notes.add("example note");

        ListView listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("noteId",position);
                startActivity(intent);
            }
        });*/
        FloatingActionButton fab = findViewById(R.id.fab_create);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

       notesDbHelper = new NotesDbHelper(this);
       SQLiteDatabase db = notesDbHelper.getReadableDatabase();

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        SQLiteDatabase db = notesDbHelper.getReadableDatabase();

        String[] projection={
                NotesContract.NotesEntry._ID,
                NotesContract.NotesEntry.COLUMN_NAME_TITLE
        };

        Cursor cursor = db.query(
                NotesContract.NotesEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        try {

            TextView displayView = findViewById(R.id.displayView);


            int titleIndex = cursor.getColumnIndex("title");
            int idIndex = cursor.getColumnIndex(NotesContract.NotesEntry._ID);

            String titleString, idString, resultString;

            while(cursor.moveToNext()) {

                    idString = cursor.getString(idIndex);
                    titleString = cursor.getString(titleIndex);

                    resultString = "\n"+ idString+ " " + titleString;
                    displayView.append(resultString);

            }
        } finally {

            cursor.close();
        }

    }

    private void insertNote() {
        // Gets the data repository in write mode
        SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotesContract.NotesEntry.COLUMN_NAME_TITLE, "hkdsjaeqw fs");
        values.put(NotesContract.NotesEntry.COLUMN_NAME_CONTENT, "lasue oueq e");

        long newRowId = db.insert(NotesContract.NotesEntry.TABLE_NAME, null, values);
        Log.v("CatalogActivity", String.valueOf(newRowId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_note) {
           // insertNote();
            //displayDatabaseInfo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

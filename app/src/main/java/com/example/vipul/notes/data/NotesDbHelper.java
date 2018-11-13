package com.example.vipul.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myNotes.db";

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_TABLE = "CREATE TABLE " + NotesContract.NotesEntry.TABLE_NAME + "("
                + NotesContract.NotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NotesContract.NotesEntry.COLUMN_NAME_TITLE + " TEXT, "
                + NotesContract.NotesEntry.COLUMN_NAME_CONTENT + " TEXT );";

        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.example.vipul.notes.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NoteProvider extends ContentProvider {

    private static final int NOTES = 100;
    private static final int NOTE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {

        sUriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTES, NOTES);
        sUriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTES + "/#", NOTE_ID);
    }


    private NotesDbHelper notesDbHelper;

    @Override
    public boolean onCreate() {
        notesDbHelper = new NotesDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = notesDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:

                cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case NOTE_ID:

                selection = NotesContract.NotesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        if (contentValues.containsKey(NotesContract.NotesEntry.COLUMN_NAME_TITLE)) {
            if (contentValues.getAsString(NotesContract.NotesEntry.COLUMN_NAME_TITLE) == null) {
                throw new IllegalArgumentException("Note requires a title");
            }
        }
        if (contentValues.containsKey(NotesContract.NotesEntry.COLUMN_NAME_CONTENT)) {
            if (contentValues.getAsString(NotesContract.NotesEntry.COLUMN_NAME_CONTENT) == null) {
                throw new IllegalArgumentException("Note requires some content");
            }
        }

        SQLiteDatabase db = notesDbHelper.getWritableDatabase();
        long id = db.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);

    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                // Delete all rows that match the selection and selection args
                return db.delete(NotesContract.NotesEntry.TABLE_NAME, selection, selectionArgs);
            case NOTE_ID:
                // Delete a single row given by the ID in the URI
                selection = NotesContract.NotesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int rowsDeleted = db.delete(NotesContract.NotesEntry.TABLE_NAME, selection, selectionArgs);

                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                if (rowsDeleted != 0)
                    getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return updateNote(uri, contentValues, selection, selectionArgs);

            case NOTE_ID:

                selection = NotesContract.NotesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNote(uri, contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private int updateNote(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if (contentValues.containsKey(NotesContract.NotesEntry.COLUMN_NAME_TITLE)) {
            if (contentValues.getAsString(NotesContract.NotesEntry.COLUMN_NAME_TITLE) == null) {
                throw new IllegalArgumentException("Note requires a title");
            }
        }
        if (contentValues.containsKey(NotesContract.NotesEntry.COLUMN_NAME_CONTENT)) {
            if (contentValues.getAsString(NotesContract.NotesEntry.COLUMN_NAME_CONTENT) == null) {
                throw new IllegalArgumentException("Note requires some content");
            }
        }
        if (contentValues.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = notesDbHelper.getWritableDatabase();
        int rowsUpdated = db.update(NotesContract.NotesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return NotesContract.NotesEntry.CONTENT_LIST_TYPE;
            case NOTE_ID:
                return NotesContract.NotesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}

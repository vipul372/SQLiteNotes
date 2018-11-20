package com.example.vipul.notes;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vipul.notes.data.NotesContract;

public class NotesCursorAdapter extends CursorAdapter {

    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = view.findViewById(R.id.title_display);
        TextView content = view.findViewById(R.id.content_display);

        int titleColumnIndex = cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_NAME_TITLE);
        int contentColumnIndex = cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_NAME_CONTENT);

        String noteTitle = cursor.getString(titleColumnIndex);
        String noteContent = cursor.getString(contentColumnIndex);

        if (TextUtils.isEmpty(noteContent)) {
            noteContent = "Empty Note";
        }

        title.setText(noteTitle);
        content.setText(noteContent);
    }
}

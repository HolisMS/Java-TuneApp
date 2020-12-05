package satella.app.servicemoto.catatan_page;

import android.database.Cursor;

import java.util.ArrayList;

import satella.app.servicemoto.catatan_page.db.DbNoteContract;

public class MappingHelper {

    public static ArrayList<Note> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<Note> notesList = new ArrayList<>();

        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DbNoteContract.NoteColumns._ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DbNoteContract.NoteColumns.TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DbNoteContract.NoteColumns.DESCRIPTION));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DbNoteContract.NoteColumns.DATE));
            notesList.add(new Note(id, title, description, date));
        }

        return notesList;
    }

}

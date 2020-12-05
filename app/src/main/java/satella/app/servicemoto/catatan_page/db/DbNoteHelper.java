package satella.app.servicemoto.catatan_page.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static satella.app.servicemoto.catatan_page.db.DbNoteContract.NoteColumns.TABLE_NAME;

public class DbNoteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db_note";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_NAME,
            DbNoteContract.NoteColumns._ID,
            DbNoteContract.NoteColumns.TITLE,
            DbNoteContract.NoteColumns.DESCRIPTION,
            DbNoteContract.NoteColumns.DATE
    );

    DbNoteHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

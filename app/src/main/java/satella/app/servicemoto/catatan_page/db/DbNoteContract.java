package satella.app.servicemoto.catatan_page.db;

import android.provider.BaseColumns;

public class DbNoteContract {

    public static final class NoteColumns implements BaseColumns {
        public static final String TABLE_NAME = "note";

        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String DATE = "date";

    }
}

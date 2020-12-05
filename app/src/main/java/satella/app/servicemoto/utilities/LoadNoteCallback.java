package satella.app.servicemoto.utilities;

import java.util.ArrayList;

import satella.app.servicemoto.catatan_page.Note;

public interface LoadNoteCallback {
    void preExecute();

    void postExecute(ArrayList<Note> notes);
}

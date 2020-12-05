package satella.app.servicemoto.reminder_page;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AllReminder {
    private static AllReminder mInstance;
    private ArrayList<Remind> list = new ArrayList<Remind>();

    public static AllReminder getInstance() {
        if (mInstance == null)
            mInstance = new AllReminder();
        return mInstance;
    }

    private AllReminder() {
        list = new ArrayList<Remind>();
    }

    public ArrayList<Remind> getArray() {
        return this.list;
    }

    public void addToArray(Remind value) {
        list.add(value);
    }

    public void override(ArrayList<Remind> temp){
        list = temp;
    }

    public void removeAllDeleteIcons(){
        for(int i = 0; i < list.size(); i++){
            list.get(i).setDeleteIconVisibility(View.GONE);
        }
    }

    public void saveToInternalStorage(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput("all", Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(list);
            of.flush();
            of.close();
            fos.close();
        } catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

}

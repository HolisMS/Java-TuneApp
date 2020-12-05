package satella.app.servicemoto.catatan_page;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import satella.app.servicemoto.R;
import satella.app.servicemoto.utilities.CustomOnItemClickListener;

import static satella.app.servicemoto.utilities.Utils.EXTRA_NOTE;
import static satella.app.servicemoto.utilities.Utils.EXTRA_POSITION;
import static satella.app.servicemoto.utilities.Utils.REQUEST_UPDATE;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final ArrayList<Note> mListNote = new ArrayList<>();
    private final Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Note> getmListNote() {
        return mListNote;
    }

    public void setmListNote(ArrayList<Note> listNotes) {

        if (listNotes.size() > 0) {
            this.mListNote.clear();
        }
        this.mListNote.addAll(listNotes);

        notifyDataSetChanged();
    }

    public void addItem(Note note) {
        this.mListNote.add(note);
        notifyItemInserted(mListNote.size() - 1);
    }

    public void updateItem(int position, Note note) {
        this.mListNote.set(position, note);
        notifyItemChanged(position, note);
    }

    public void removeItem(int position) {
        this.mListNote.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mListNote.size());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mListNote.get(position));

        holder.mCardNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, NoteAddUpdateActivity.class);
                intent.putExtra(EXTRA_POSITION, position);
                intent.putExtra(EXTRA_NOTE, mListNote.get(position));
                activity.startActivityForResult(intent, REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return mListNote.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle, mDesc, mDate;
        private CardView mCardNote;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_item_title_note);
            mDesc = itemView.findViewById(R.id.tv_item_description_note);
            mDate = itemView.findViewById(R.id.tv_item_date_note);
            mCardNote = itemView.findViewById(R.id.cv_note);

        }

        void bind(Note note) {
            mTitle.setText(note.getTitle());
            mDesc.setText(note.getDescription());
            mDate.setText(note.getDate());
        }
    }
}

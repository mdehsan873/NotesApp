package com.myjre.roomdatabase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myjre.roomdatabase.R;
import com.myjre.roomdatabase.notedb.Note;

import java.util.List;

public class NoteAdapters extends RecyclerView.Adapter<NoteAdapters.BeanHolder> {

    private List<Note> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnNoteItemClick onNoteItemClick;

    public NoteAdapters(List<Note> list, Context context) {
        layoutInflater=layoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.onNoteItemClick=(OnNoteItemClick) context;
    }

    public interface OnNoteItemClick{
        void onNoteClick(int pos);
    }


    @NonNull
    @Override
    public BeanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.note_list_item,parent, false);

        return new BeanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeanHolder holder, int position) {
        holder.textViewTitle.setText(list.get(position).getTitle());
        holder.textViewContent.setText(list.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //Bean holder Class (ViewHolder)

    public class BeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewContent;
        TextView textViewTitle;


        public BeanHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewContent=itemView.findViewById(R.id.item_text);
            textViewTitle=itemView.findViewById(R.id.tv_title);
        }

        @Override
        public void onClick(View v) {
        onNoteItemClick.onNoteClick(getAdapterPosition());
        }
    }
}

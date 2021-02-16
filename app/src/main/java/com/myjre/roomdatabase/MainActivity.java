package com.myjre.roomdatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myjre.roomdatabase.Adapter.NoteAdapters;
import com.myjre.roomdatabase.notedb.Note;
import com.myjre.roomdatabase.notedb.NoteDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapters.OnNoteItemClick {
    //varible and widgets

    private TextView textViewmsg;
    private RecyclerView recyclerView;
    NoteDatabase noteDatabase;
    List<Note> notes;
    NoteAdapters noteAdapters;
    int pos;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initializeViews();
        displayList();

    }


    @Override
    public void onNoteClick(int pos) {
    //alert dialog that will show to the user when fab is clicked
        //to  ask for update or deleete note
        new AlertDialog.Builder(MainActivity.this)
                .setItems(new String[]{"Delete","Update"},new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                noteDatabase.getNoteDao().deleteNote(notes.get(pos));
                                notes.remove(pos);
                                listVisibility();
                                break;

                            case 1:
                                MainActivity.this.pos = pos;
                                startActivityForResult(
                                        new Intent(MainActivity.this, AddNoteActivity.class).putExtra("Note", notes.get(pos)),
                                        100);
                                break;
                        }
                    }
                }).show();
    }


    private void displayList(){
        noteDatabase=NoteDatabase.getInstance(MainActivity.this);
        new RetrieveTask(this).execute();
    }

    private static class RetrieveTask extends AsyncTask<Void,Void,List<Note>> {
        private WeakReference<MainActivity> activityWeakReference;
        //the Only retain a weak refrence to the activity
        RetrieveTask(MainActivity context){
            activityWeakReference=new WeakReference<>(context);
        }
        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityWeakReference.get()!=null){
                return activityWeakReference.get().noteDatabase.getNoteDao().getNotes();
            }
            else
                return null;
        }


        @Override
        protected void onPostExecute(List<Note> notes) {
            if(notes!=null&&notes.size()>0 ){
                activityWeakReference.get().notes.clear();
                activityWeakReference.get().notes.addAll(notes);
                //hide the empty  text view
                activityWeakReference.get().textViewmsg.setVisibility(View.GONE);
                activityWeakReference.get().noteAdapters.notifyDataSetChanged();
            }
        }
    }
    private void initializeViews() {
        textViewmsg =findViewById(R.id.tv_empty);
        FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(listner);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        notes = new ArrayList<>();
        noteAdapters=new NoteAdapters(notes,MainActivity.this);
        recyclerView.setAdapter(noteAdapters);
    }
    private View.OnClickListener listner=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(MainActivity.this,AddNoteActivity.class),100);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==100&&resultCode>0){
            if(resultCode==1){
                notes.add((Note) data.getSerializableExtra("note"));
            }else if(requestCode==2){
                notes.set(pos,(Note) data.getSerializableExtra("note"));
            }
            listVisibility();
        }
    }

    private void listVisibility() {
        int emptyMsgVisibility=View.GONE;
        if(notes.size()==0){
            if(textViewmsg.getVisibility()==View.GONE)
                emptyMsgVisibility=View.VISIBLE;
        }
        textViewmsg.setVisibility(emptyMsgVisibility);
        noteAdapters.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        noteDatabase.cleanup();
        super.onDestroy();
    }
}
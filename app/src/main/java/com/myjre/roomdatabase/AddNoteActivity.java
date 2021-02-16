package com.myjre.roomdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.myjre.roomdatabase.notedb.Note;
import com.myjre.roomdatabase.notedb.NoteDatabase;

import java.lang.ref.WeakReference;

public class AddNoteActivity extends AppCompatActivity {

    //
    private TextInputEditText et_title,et_content;
    Button button;
    private boolean update;
    //object

    private NoteDatabase noteDatabase;

    private Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        et_content=findViewById(R.id.et_content);
        et_title=findViewById(R.id.et_title);
        button = findViewById(R.id.but_save);

      //object
      noteDatabase=NoteDatabase.getInstance(AddNoteActivity.this);

      //chech for correct object and date

        if((note=(Note)getIntent().getSerializableExtra("note"))!=null){
          getSupportActionBar().setTitle("Update Note");
          update=true;
          button.setText("Update");
          et_title.setText(note.getTitle());
          et_content.setText(note.getContent());

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if we need to update the note

                if(update){
                    note.setContent(et_content.getText().toString());
                    note.setTitle(et_title.getText().toString());
                    noteDatabase.getNoteDao().updateNote(note);
                    setResult(note,2);
                }else
                {
                    note=new Note(et_content.getText().toString(),et_title.getText().toString());
                    new InsertTask(AddNoteActivity.this,note).execute();
                }
            }
        });


    }

    //set result method:

    private void setResult(Note note,int flag){
        setResult(flag,new Intent().putExtra("note",note));
        finish();
    }

    private static class InsertTask extends AsyncTask<Void,Void,Boolean>{
        private WeakReference<AddNoteActivity> activityWeakReference;
        private Note note;

        //Only retain a weak refrence to the activity:
        InsertTask(AddNoteActivity context,Note note){
            activityWeakReference=new WeakReference<>(context);
            this.note=note;
        }

        //do in the background method on a worker thread


        @Override
        protected Boolean doInBackground(Void... voids) {
            //retriving auto increament data note id
            long j=activityWeakReference.get().noteDatabase.getNoteDao().insertNote(note);
            note.setNote_id(j);


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                activityWeakReference.get().setResult(note,1);
                activityWeakReference.get().finish();
            }
        }
    }

}
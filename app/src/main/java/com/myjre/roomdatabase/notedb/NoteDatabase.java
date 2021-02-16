package com.myjre.roomdatabase.notedb;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Note.class},version = 1,exportSchema = false)
@TypeConverters({DataRoomConverter.class})
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NotesDao getNoteDao();

    public static NoteDatabase noteDB;


    public static /*sync*/ NoteDatabase getInstance(Context context){
        if(null==noteDB){
            noteDB=buildDatabaseIntance(context);
        }
        return noteDB;
    }

    private static NoteDatabase buildDatabaseIntance(Context context) {
        return Room.databaseBuilder(context,NoteDatabase.class,Constants.DB_Name).allowMainThreadQueries().build();
    }

    public void cleanup(){
        noteDB=null;
    }

}

package com.myjre.roomdatabase.notedb;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM "+Constants.Table_Name_Note)
    List<Note> getNotes();



    //insert the data

    @Insert
    long insertNote(Note note);
    //update data
    @Update
    void updateNote(Note repons);
    //delete
    @Delete
    void deleteNote(Note note);

    //delete all notes from the database:

    @Delete
    void deleteNote(Note... note);


}

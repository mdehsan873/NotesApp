package com.myjre.roomdatabase.notedb;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(tableName = Constants.Table_Name_Note)
public class Note implements Serializable {


    //column
    @PrimaryKey(autoGenerate = true)
    private long note_id;

    @ColumnInfo(name = "note_content")
    private String content;

    private String title;
    private Date date;

    //Constructor


    public Note(String content, String title) {
        this.content = content;
        this.title = title;
        this.date=new Date(System.currentTimeMillis());
    }
    @Ignore
    public Note(){}

    public long getNote_id() {
        return note_id;
    }

    public void setNote_id(long id) {
        this.note_id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if(this==o)
            return true;
        if(!(o instanceof Note)) return false;

        Note note=(Note) o;
        if(note_id!=note.note_id) return false;

        return title!=null?title.equals(note.title):note.title==null;
    }



@Override
    public int hashCode(){
        int result=(int)note_id;
        result=31*result+(title!=null?title.hashCode():0);
        return result;
}

    @NonNull
    @Override
    public String toString() {
        return "Note{"+
                "note_id="+note_id+
                ", content='"+content+
                ", title='"+title+
                ", date='"+date+
                '}';

    }
}

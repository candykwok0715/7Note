package com.example.kwoksinman.comp437mobile.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KwokSinMan on 22/3/2016.
 */
public class NoteDAO {
    public static final String TABLE_NAME = "NOTES ";

    public static final String KEY_ID = "_notes_id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String USER_ID = "user_id";
    public static final String IS_DELETE = "is_deleted";
    public static final String SERVER_NOTE_ID = "server_note_id";
    public static final String LAST_UPDATE = "last_update";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ( " +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " TEXT, " +
                    CONTENT + " TEXT, " +
                    USER_ID + " TEXT, " +
                    IS_DELETE + " TEXT, " +
                    SERVER_NOTE_ID + " INTEGER, " +
                    LAST_UPDATE + " INTEGER )";

    private SQLiteDatabase db;

    public NoteDAO(Context context) {
        db = MyDBHelper.getDatabase(context);
    }

    public void close() {
        db.close();
    }
    public Note insert(Note note) {
        ContentValues cv = new ContentValues();
        cv.put(TITLE, note.getTitle());
        cv.put(CONTENT, note.getContent());
        cv.put(USER_ID, note.getUserId());
        cv.put(IS_DELETE, note.getIsDeleted());
        cv.put(SERVER_NOTE_ID, note.getServerNoteId());
        cv.put(LAST_UPDATE, note.getLastUpdate());
        long id = db.insert(TABLE_NAME, null, cv);
        note.setNoteId(id);
        return note;
    }

    public boolean update(Note note) {
        ContentValues cv = new ContentValues();
        cv.put(TITLE, note.getTitle());
        cv.put(CONTENT, note.getContent());
        cv.put(USER_ID, note.getUserId());
        cv.put(IS_DELETE, note.getIsDeleted());
        cv.put(SERVER_NOTE_ID, note.getServerNoteId());
        cv.put(LAST_UPDATE, note.getLastUpdate());
        String where = KEY_ID + "=" + note.getNoteId();
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    public boolean deleteAll(){
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    public List<Note> getAll() {
        List<Note> result = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    public Note get(long id) {
        Note note = null;
        String where = KEY_ID + "=" + id;
        Cursor result = db.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (result.moveToFirst()) {
            note = getRecord(result);
        }
        result.close();
        return note;
    }

    public Note getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Note result = new Note();
        result.setNoteId(cursor.getLong(0));
        result.setTitle(cursor.getString(1));
        result.setContent(cursor.getString(2));
        result.setUserId(cursor.getString(3));
        result.setIsDeleted(cursor.getString(4));
        result.setServerNoteId(cursor.getLong(5));
        result.setLastUpdate(cursor.getLong(6));
        // 回傳結果
        return result;
    }

    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }

    public void sample() {
        //Note note1 = new Note("筆記1","item1,item2,item3,item4","1","false");
        //Note note2 = new Note("筆記2","item5,item6,item7,item8","1","false");
        //Note note3 = new Note("筆記3","item2,item3,item4,item7","1","true");
        //insert(note1);
        //insert(note2);
        //insert(note3);
    }

}

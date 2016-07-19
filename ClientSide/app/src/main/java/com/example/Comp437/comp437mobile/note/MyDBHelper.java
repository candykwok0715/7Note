package com.example.kwoksinman.comp437mobile.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kwoksinman.comp437mobile.SystemConstant;

/**
 * Created by KwokSinMan on 23/3/2016.
 */
public class MyDBHelper extends SQLiteOpenHelper {


    private static SQLiteDatabase database;


    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new MyDBHelper(context, SystemConstant.DATABASE_NAME,
                    null, SystemConstant.DATABASE_VERSION).getWritableDatabase();
        }

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立應用程式需要的表格
        // 待會再回來完成它
        //System.out.println("Candy NoteDAO.CREATE_TABLE ="+NoteDAO.CREATE_TABLE);
        db.execSQL(NoteDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 刪除原有的表格
        db.execSQL("DROP TABLE IF EXISTS " + NoteDAO.TABLE_NAME);
        // 呼叫onCreate建立新版的表格
        onCreate(db);
    }

}

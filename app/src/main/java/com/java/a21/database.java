package com.java.a21;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

/**
 * Created by Kyle on 2017/9/13.
 */

public class database {
    private SQLiteDatabase db = null;

    public database() {

//        File file = new File("/data/data/com.java.a21/databases");
//        file.mkdir();

        db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.java.a21/databases/news.db", null);
        db.execSQL("DROP TABLE newstable");
        String stu_table = "create table newstable(id text primary key not null,path text,read INTEGER,collect INTEGER)";
        db.execSQL(stu_table);
        Log.d("db",db.getPath());
    }

    public void insert(String id){
        String stu_sql="insert into newstable values ('"+ id  + "','',0,0)";
        db.execSQL(stu_sql);
    }

    public Cursor querybyid(String id) {
        String whereClause = "id=?";
        String[] whereArgs={id};
        Log.d("db",db.getPath());
        Cursor cursor = db.query("newstable", null, whereClause, whereArgs, null, null, null);
        return cursor;
    }

    public void collect(String id) {
        ContentValues values = new ContentValues();
        values.put("collect",1);
        String whereClause = "id=?";
        String[] whereArgs={id};
        db.update("newstable",values,whereClause,whereArgs);
    }

    public void uncollect(String id) {
        ContentValues values = new ContentValues();
        values.put("collect",0);
        String whereClause = "id=?";
        String[] whereArgs={id};
        db.update("newstable",values,whereClause,whereArgs);
    }
}

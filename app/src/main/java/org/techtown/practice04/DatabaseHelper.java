package org.techtown.practice04;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private String TABLE_NAME = "employee";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);

        String CREATE_SQL = "create table " + TABLE_NAME + " (";
        CREATE_SQL += " id integer PRIMARY KEY autoincrement, ";
        CREATE_SQL += " title text, ";
        CREATE_SQL += " date text, ";
        CREATE_SQL += " content text, ";
        CREATE_SQL += " audiofile text, ";
        CREATE_SQL += " videofile text) ";

        try {
            db.execSQL(CREATE_SQL);
            println("테이블 생성 성공");
        } catch(Exception e) {
            println("테이블 생성 실패" + e);
        }

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        println("Data base open : 이미 생성되어 있음..");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void println(String msg) {
        Log.d("DatabaseHelper", "==============>" + msg);
    }
}

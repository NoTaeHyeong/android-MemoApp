package org.techtown.practice04;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TABLE_NAME ="employee";

    SQLiteDatabase db;
    StartView startView;
    WriteMemo writeMemo;
    UpdateDelete updateDelete;

    public void setDB(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startView = (StartView) getSupportFragmentManager().findFragmentById(R.id.startView);
        writeMemo = new WriteMemo();
        updateDelete = new UpdateDelete();

        setDB(startView.db);
    }

    public void insert(String title, String date, String content, String audiofile, String videofile, String photofile) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, startView).commit();

        db.execSQL("insert into " + TABLE_NAME + " " +
                "(title, date, content, audiofile, videofile, photofile) " +
                "values('"+title+"', '"+date+"', '"+content+"', '"+audiofile+"', '"+videofile+"', '"+photofile+"')");
    }

    public void update(int id, String title, String date, String content, String audiofile, String videofile, String photofile) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, startView).commit();

        db.execSQL("update " + TABLE_NAME + " " +
                "set title = '"+title+"', date = '"+date+"', content = '"+content+"', audiofile = '"+audiofile+"', videofile = '"+videofile+"', photofile = '"+photofile+"' " +
                "Where id = " + id);
    }

    public void delete(int id) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, startView).commit();

        db.execSQL("delete from " + TABLE_NAME + " Where id = " + id);
    }

    public void toStartView() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, startView).commit();
    }

    public void toWriteMemo() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, writeMemo).commit();
    }

    public void toUpdateDelete(int id) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, UpdateDelete.newInstance(id)).commit();
    }

    public void toUpdate(int id) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, WriteMemo.newInstance(id)).commit();
    }
}


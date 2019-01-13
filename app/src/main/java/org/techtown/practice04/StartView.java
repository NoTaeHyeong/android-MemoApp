package org.techtown.practice04;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.Date;

public class StartView extends Fragment {

    private String TABLE_NAME = "employee";
    private String DATABASE = "MemoF.db";

    MainActivity activity;

    ListView listView;
    MemoAdapter adapter;

    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    Button newMemo;
    String title, date;
    String AUDIO_FILE, VIDEO_FILE, PHOTO_FILE;

    int id;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(db == null) {
            openDatabase();
        }
        activity = (MainActivity) getActivity();

        View rootView = (View) inflater.inflate(R.layout.startview, container, false);

        listView = rootView.findViewById(R.id.listView);
        adapter = new MemoAdapter(getContext().getApplicationContext());

        listView.setAdapter(adapter);

        showListView(); // db에서 데이터 가져와서 리스트뷰에 추가해서 보여줌

        newMemo = (Button) rootView.findViewById(R.id.newMemo);
        newMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toWriteMemo();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemoItem item = (MemoItem) parent.getItemAtPosition(position);

                activity.toUpdateDelete(item.getId());
            }
        });

        return rootView;
    }

    public void showListView() { // db에서 데이터 가져와서 리스트뷰에 추가해서 보여줌
        Cursor c1 = db.rawQuery("select id, title, date, audiofile, videofile, photofile from " + TABLE_NAME, null);
        int rowCnt = c1.getCount();

        for(int i=0; i<rowCnt; i++) {
            c1.moveToNext();

            id = c1.getInt(0);
            title = c1.getString(1);
            date = c1.getString(2);
            AUDIO_FILE = c1.getString(3);
            VIDEO_FILE = c1.getString(4);
            PHOTO_FILE = c1.getString(5);

            adapter.addItem(new MemoItem(id, title, date, AUDIO_FILE, VIDEO_FILE, PHOTO_FILE));
        }

        c1.close();
    }

    public void openDatabase() {
        dbHelper = new DatabaseHelper(getContext().getApplicationContext(), DATABASE, null, 1);
        db = dbHelper.getWritableDatabase();
    }
}

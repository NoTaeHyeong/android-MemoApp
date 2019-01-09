package org.techtown.practice04;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WriteMemo extends Fragment {

    private static final String ARG_ID = "position";
    private String TABLE_NAME = "employee";
    int id = 0;

    MainActivity activity;

    SQLiteDatabase db;
    EditText title, content, recordName;
    Button restore, record, recordStop, recordPlay;

    String getTitle, getContent, getDate;
    String AUDIO_FILE;

    MediaPlayer player;
    MediaRecorder recorder;

    public void setDB(SQLiteDatabase db) {
        this.db = db;
    }

    public static WriteMemo newInstance(int id) {
        WriteMemo fragment = new WriteMemo();

        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.writememo, container, false);

        activity = (MainActivity) getActivity();
        setDB(activity.db);

        title = (EditText) rootView.findViewById(R.id.title);
        content = (EditText) rootView.findViewById(R.id.content);

        recordName = rootView.findViewById(R.id.recordName);

        if(id != 0) {
            updateView();
        }

        restore = (Button) rootView.findViewById(R.id.restore);
        restore.setOnClickListener(new RestoreEvent());

        record = rootView.findViewById(R.id.record);
        record.setOnClickListener(new RecordEvent());

        recordStop = rootView.findViewById(R.id.recordStop);
        recordStop.setOnClickListener(new RecordStopEvent());

        recordPlay = rootView.findViewById(R.id.recordPlay);
        recordPlay.setOnClickListener(new RecordPlayEvent());

        return rootView;
    }

    class RestoreEvent implements ViewGroup.OnClickListener {
        @Override
        public void onClick(View view) {
            getTitle = title.getText().toString();
            getContent = content.getText().toString();
            setDate();

            if(id == 0) { // 새 메모 작성 시
                activity.insert(getTitle, getDate, getContent, AUDIO_FILE);
            } else {
                activity.update(id, getTitle, getDate, getContent, AUDIO_FILE);
            }

            title.setText("");
            content.setText("");
            recordName.setText("");
        }
    }

    class RecordEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String record_Name = recordName.getText().toString();

            if(record_Name.equals("")) {
                Toast.makeText(getContext().getApplicationContext(), "녹음명을 입력하세요.", Toast.LENGTH_LONG).show();
            } else {
                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, record_Name + ".mp4");
                AUDIO_FILE = file.getAbsolutePath();

                if (recorder != null) {
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                }

                recorder = new MediaRecorder();

                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                recorder.setOutputFile(AUDIO_FILE);

                Toast.makeText(getContext().getApplicationContext(), "녹음 시작합니다.", Toast.LENGTH_SHORT).show();

                try {
                    recorder.prepare();
                    recorder.start();
                } catch (IOException e) {
                    Log.d("Audio", "오디오 저장중 오류 :"+ e.getMessage());
                }
            }

        }
    }

    class RecordStopEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
                recorder = null;

                Toast.makeText(getContext().getApplicationContext(), "녹음이 중지되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class RecordPlayEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (player != null) { // 재생중이면
                player.stop();
                player.release();
                player = null;
            }

            Toast.makeText(getContext().getApplicationContext(), "녹음파일을 재생합니다.", Toast.LENGTH_SHORT).show();
            player = new MediaPlayer();
            try {
                player.setDataSource(AUDIO_FILE);
                player.prepare(); // 가지고 와서
                player.start();
            } catch (IOException e) {
                Toast.makeText(getContext().getApplicationContext(), "에러 : 재생파일이 없습니다."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        id = 0; // id 값 초기화
    }

    public void setDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        getDate = dateFormat.format(date);
    }

    public void updateView() {
        Cursor c1 = db.rawQuery("select title, content from " + TABLE_NAME + " Where id = " + id, null);

        c1.moveToNext();
        title.setText(c1.getString(0));
        content.setText(c1.getString(1));

        c1.close();
    }

}

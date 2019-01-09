package org.techtown.practice04;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by F-06 on 2019-01-05.
 */

public class UpdateDelete extends Fragment {

    MainActivity activity;

    private static final String ARG_ID = "id";
    private String TABLE_NAME = "employee";

    String title, date, content;
    int id;

    MediaPlayer audioPlayer, videoPlayer;
    String AUDIO_FILE, VIDEO_FILE;

    SQLiteDatabase db;
    TextView titleView, dateView, contentView;
    Button update, delete, recordPlay, videoPlay;
    FrameLayout videoView;
    SurfaceView surface;
    SurfaceHolder holder; // 미리보기 클래스

    public void setDB(SQLiteDatabase db) {
        this.db = db;
    }

    public static UpdateDelete newInstance(int id) {
        UpdateDelete fragment = new UpdateDelete();

        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            id = getArguments().getInt(ARG_ID); // id는 1부터 시작 아이템 position 은 0부터 시작
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.updatedelete, container, false);

        activity = (MainActivity) getActivity();
        setDB(activity.db);

        titleView = rootView.findViewById(R.id.titleView);
        dateView = rootView.findViewById(R.id.dateView);
        contentView = rootView.findViewById(R.id.contentView);

        // 미리보기 클래스 추가
        surface = new SurfaceView(getContext().getApplicationContext());
        holder = surface.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // 레이아웃에 미리보기 view 추가
        videoView = rootView.findViewById(R.id.videoLayout);
        videoView.addView(surface);

        getItemView();

        update = rootView.findViewById(R.id.update);
        delete = rootView.findViewById(R.id.delete);
        recordPlay = rootView.findViewById(R.id.recordPlay);
        videoPlay = rootView.findViewById(R.id.videoPlay);

        update.setOnClickListener(new UpdateEvent());
        delete.setOnClickListener(new DeleteEvent());
        recordPlay.setOnClickListener(new RecordPlayEvent());
        videoPlay.setOnClickListener(new VideoPlayEvent());

        return rootView;
    }

    class UpdateEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            activity.toUpdate(id);
        }
    }

    class DeleteEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            activity.delete(id);
        }
    }

    class RecordPlayEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (audioPlayer != null) { // 재생중이면
                audioPlayer.stop();
                audioPlayer.release();
                audioPlayer = null;
            }

            Toast.makeText(getContext().getApplicationContext(), "녹음파일을 재생합니다.", Toast.LENGTH_SHORT).show();
            audioPlayer = new MediaPlayer();

            try {
                audioPlayer.setDataSource(AUDIO_FILE);
                audioPlayer.prepare(); // 가지고 와서
                audioPlayer.start();
            } catch (IOException e) {
                Toast.makeText(getContext().getApplicationContext(), "에러 : 재생파일이 없습니다."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class VideoPlayEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(videoPlayer == null) {
                videoPlayer = new MediaPlayer();

                try {
                    videoPlayer.setDataSource(VIDEO_FILE);
                    videoPlayer.setDisplay(holder);
                    videoPlayer.prepare();
                    videoPlayer.start();
                } catch (IOException e) {
                    Toast.makeText(getContext().getApplicationContext(), "재생할 파일 오류 발생:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void getItemView() {
        Cursor c1 = db.rawQuery("select title, date, content, audiofile, videofile from " + TABLE_NAME + " Where id = " + id, null);

        c1.moveToNext();

        title = c1.getString(0);
        date = c1.getString(1);
        content = c1.getString(2);
        AUDIO_FILE = c1.getString(3);
        VIDEO_FILE = c1.getString(4);

        titleView.setText(title);
        dateView.setText(date);
        contentView.setText(content);

        Log.d("UpdateDelete", "-=====>" + VIDEO_FILE);

        if(!VIDEO_FILE.equals("null")) {
            videoView.setVisibility(View.VISIBLE);
        }

        c1.close();
    }
}

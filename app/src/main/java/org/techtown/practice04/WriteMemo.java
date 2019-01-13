package org.techtown.practice04;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.techtown.util.SetDate;

public class WriteMemo extends Fragment {

    private static final String ARG_ID = "position";
    private String TABLE_NAME = "employee";
    int id = 0;

    MainActivity activity;
    Context context;
    SQLiteDatabase db;

    EditText title, content;

    Button restore, cancel;

    Button record, recordStop, recordPlay;
    Button video, videoStop, videoPlay;
    Button photo;
    ImageView getPhoto;
    File file; // 사진 file 경로

    String getTitle, getContent, getDate;
    String AUDIO_FILE, VIDEO_FILE, PHOTO_FILE;
    String video_path;

    MediaPlayer player;
    MediaRecorder recorder;

    FrameLayout frame;
    SurfaceView surface;
    SurfaceHolder holder; // 미리보기 클래스

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
        context = getContext().getApplicationContext();

        activity = (MainActivity) getActivity();
        setDB(activity.db);

        title = (EditText) rootView.findViewById(R.id.title);
        content = (EditText) rootView.findViewById(R.id.content);

        getPhoto = rootView.findViewById(R.id.getPhoto);

        if(id != 0) {
            updateView();
        }

        restore = (Button) rootView.findViewById(R.id.restore);
        restore.setOnClickListener(new RestoreEvent());

        cancel = (Button) rootView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new CancelEvent());

        record = rootView.findViewById(R.id.record);
        recordStop = rootView.findViewById(R.id.recordStop);
        recordPlay = rootView.findViewById(R.id.recordPlay);

        record.setOnClickListener(new RecordEvent());
        recordStop.setOnClickListener(new RecordStopEvent());
        recordPlay.setOnClickListener(new RecordPlayEvent());

        video = rootView.findViewById(R.id.video);
        videoStop = rootView.findViewById(R.id.videoStop);
        videoPlay = rootView.findViewById(R.id.videoPlay);

        video.setOnClickListener(new VideoEvent());
        videoStop.setOnClickListener(new VideoStopEvent());
        videoPlay.setOnClickListener(new VideoPlayEvent());

        photo = rootView.findViewById(R.id.photo);
        photo.setOnClickListener(new PhotoEvent());

        // 미리보기 클래스 추가
        surface = new SurfaceView(context);
        holder = surface.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // 레이아웃에 미리보기 view 추가
        frame = (FrameLayout) rootView.findViewById(R.id.videoLayout);
        frame.addView(surface);

        return rootView;
    }

    class RestoreEvent implements ViewGroup.OnClickListener {
        @Override
        public void onClick(View view) {
            getTitle = title.getText().toString();
            getContent = content.getText().toString();
            getDate = SetDate.setDate();

            if(id == 0) { // 새 메모 작성 시
                activity.insert(getTitle, getDate, getContent, AUDIO_FILE, VIDEO_FILE, PHOTO_FILE);
            } else {
                activity.update(id, getTitle, getDate, getContent, AUDIO_FILE, VIDEO_FILE, PHOTO_FILE);
            }

            textInit();
        }
    }

    class CancelEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(id != 0) {
                activity.toUpdateDelete(id);
            } else {
                activity.toStartView();
            }
        }
    }

    class RecordEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard, SetDate.setDateHour() + ".mp4");
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

            Log.d("12512512", AUDIO_FILE);

            Toast.makeText(context, "녹음 시작합니다.", Toast.LENGTH_SHORT).show();

            try {
                recorder.prepare();
                recorder.start();
            } catch (IOException e) {
                Log.d("Audio", "오디오 저장중 오류 :"+ e.getMessage());
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

                Toast.makeText(context, "녹음이 중지되었습니다.", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(context, "녹음파일을 재생합니다.", Toast.LENGTH_SHORT).show();
            player = new MediaPlayer();
            try {
                Log.d("WriteMemo", "====" + AUDIO_FILE);
                player.setDataSource(AUDIO_FILE);
                player.prepare(); // 가지고 와서
                player.start();
            } catch (IOException e) {
                Toast.makeText(context, "에러 : 재생파일이 없습니다."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class VideoEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            frame.setVisibility(View.VISIBLE);
            // 외부 저장 공간 ( 따로 추가한 메모리 )
            String state = Environment.getExternalStorageState();
            if(!state.equals(Environment.MEDIA_MOUNTED)) {
                Log.d("Activity", "메모리 마운트 불가");
            } else {
                video_path = Environment.getExternalStorageDirectory().getAbsolutePath();
            }

            // 동영상 녹화
            if(recorder == null) {
                recorder = new MediaRecorder();

                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

                String getVideoName = SetDate.setDateHour();

                if(video_path == null || video_path.equals("")) {
                    VIDEO_FILE = getVideoName + ".mp4";
                } else {
                    VIDEO_FILE = video_path + "/" + getVideoName + ".mp4";
                }

                recorder.setOutputFile(VIDEO_FILE); // 저장될 곳
                recorder.setPreviewDisplay(holder.getSurface()); // 미리보기
                try {
                    recorder.prepare(); // 준비
                    recorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "녹화 중 오류 발생:"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    recorder.release();
                    recorder = null;
                }

            } else {
                // 녹화중인 경우 ~
            }
        }
    }

    class VideoStopEvent implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            // 녹화파일 저장

            if(recorder != null) {
                recorder.stop();
                recorder.reset(); // 초기화
                recorder.release();
                recorder = null;

                // 컬렉션 hashmap 과 동일 (key, value)
                ContentValues c = new ContentValues();
                c.put(MediaStore.MediaColumns.TITLE, "RecordVideo");
                c.put(MediaStore.Audio.Media.ALBUM, "Video Album");
                c.put(MediaStore.Audio.Media.ARTIST, "PSE");
                c.put(MediaStore.Audio.Media.DISPLAY_NAME, "Sample Record");
                c.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis()/1000);
                c.put(MediaStore.Audio.Media.ARTIST, "videoicon/mp4");
                c.put(MediaStore.Audio.Media.DATA, VIDEO_FILE);

                // 녹화된 파일을 내용제공자를 이용해 동영상 목록으로 저장
                Uri videoUri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, c);
                if(videoUri == null) {
                    Toast.makeText(context, "저장 실패 오류 발생:", Toast.LENGTH_SHORT).show();
                }
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, videoUri));
            }
        }
    }

    class VideoPlayEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(player == null) {
                player = new MediaPlayer();

                try {
                    player.setDataSource(VIDEO_FILE);
                    player.setDisplay(holder);
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    Toast.makeText(context, "재생할 파일 오류 발생:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    class PhotoEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            String photo_Name = photoName.getText().toString();

            File storageDir = Environment.getExternalStorageDirectory();
            file = new File(storageDir, SetDate.setDateHour());

            PHOTO_FILE = file.getAbsolutePath();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

            startActivityForResult(intent, 100); // 실행 후 결과, 자동 onActivityResult() 호출됨
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        id = 0; // id 값 초기화
    }

    public void updateView() {
        Cursor c1 = db.rawQuery("select title, content, audiofile, videofile, photofile from " + TABLE_NAME + " Where id = " + id, null);
        c1.moveToNext();

        title.setText(c1.getString(0));
        content.setText(c1.getString(1));
        AUDIO_FILE = c1.getString(2);
        VIDEO_FILE = c1.getString(3);
        PHOTO_FILE = c1.getString(4);

        c1.close();
    }

    public void textInit() {
        title.setText("");
        content.setText("");

        AUDIO_FILE = "null";
        VIDEO_FILE = "null";
        PHOTO_FILE = "null";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == -1) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 0; // 대용량 사이즈 줄임

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            getPhoto.setImageBitmap(bitmap);
        }
    }
}

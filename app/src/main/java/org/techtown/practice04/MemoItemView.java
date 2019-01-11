package org.techtown.practice04;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemoItemView extends RelativeLayout {
    TextView titleView, dateView;
    ImageView record, video, photo;

    public MemoItemView(Context context) {
        super(context);

        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.memoitem, this, true);

        titleView = (TextView) findViewById(R.id.titleView);
        dateView = (TextView) findViewById(R.id.dateView);
        record = findViewById(R.id.record);
        video = findViewById(R.id.video);
        photo = findViewById(R.id.photo);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setDate(String date) {
        dateView.setText(date);
    }

    public void setRecordAlpha(float alpha) { record.setAlpha(alpha); }

    public void setVideoAlpha(float alpha) { video.setAlpha(alpha); }

    public void setPhoto(String PHOTO_FILE) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 0; // 대용량 사이즈 줄임

        Bitmap bitmap = BitmapFactory.decodeFile(PHOTO_FILE, options);
        photo.setImageBitmap(bitmap);
    }

}

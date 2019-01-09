package org.techtown.practice04;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WriteMemoItemView extends RelativeLayout {
    TextView titleView, dateView;
    ImageView record, video, picture;

    public WriteMemoItemView(Context context) {
        super(context);

        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.writememoitem, this, true);

        titleView = (TextView) findViewById(R.id.titleView);
        dateView = (TextView) findViewById(R.id.dateView);
        record = findViewById(R.id.record);
        video = findViewById(R.id.video);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setDate(String date) {
        dateView.setText(date);
    }

    public void setRecordAlpha(float alpha) { record.setAlpha(alpha); }

    public void setVideoAlpha(float alpha) { video.setAlpha(alpha); }

}

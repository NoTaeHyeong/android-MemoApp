package org.techtown.practice04;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class MemoAdapter extends BaseAdapter {
    ArrayList<MemoItem> items = new ArrayList<>();
    String title, date;
    String PHOTO_fILE;
    Context context;

    public MemoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemoItem item = items.get(position);
        MemoItemView view = new MemoItemView(context);

        view.setTitle(item.getTitle());
        view.setDate(item.getDate());

        if(!item.getAUDIO_FILE().equals("null")) {
            view.setRecordAlpha(1);
        }

        if(!item.getVIDEO_FILE().equals("null")) {
            view.setVideoAlpha(1);
        }

        PHOTO_fILE = item.getPHOTO_FILE();

        if(!PHOTO_fILE.equals("null")) {
            view.setPhoto(PHOTO_fILE);
        }

        return view;
    }

    public void addItem(MemoItem item) {
        items.add(item);
    }
}


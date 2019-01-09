package org.techtown.practice04;

public class WriteMemoItem {
    private int id;
    private String title;
    private String date;
    private String AUDIO_FILE;

    public WriteMemoItem(int id, String title, String date, String AUDIO_FILE) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.AUDIO_FILE = AUDIO_FILE;
    }

    public String getRECORD_FILE() {
        return AUDIO_FILE;
    }

    public void setRECORD_FILE(String RECORD_FILE) {
        this.AUDIO_FILE = RECORD_FILE;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

package org.techtown.practice04;

public class WriteMemoItem {
    private int id;
    private String title;
    private String date;
    private String AUDIO_FILE;
    private String VIDEO_FILE;

    public WriteMemoItem(int id, String title, String date, String AUDIO_FILE, String VIDEO_FILE) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.AUDIO_FILE = AUDIO_FILE;
        this.VIDEO_FILE = VIDEO_FILE;
    }

    public String getVIDEO_FILE() {
        return VIDEO_FILE;
    }

    public void setVIDEO_FILE(String VIDEO_FILE) {
        this.VIDEO_FILE = VIDEO_FILE;
    }

    public String getAUDIO_FILE() {
        return AUDIO_FILE;
    }

    public void setAUDIO_FILE(String RECORD_FILE) {
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

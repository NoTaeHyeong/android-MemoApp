package org.techtown.practice04;

public class MemoItem {
    private int id;
    private String title;
    private String date;
    private String AUDIO_FILE;
    private String VIDEO_FILE;
    private String PHOTO_FILE;

    public MemoItem(int id, String title, String date, String AUDIO_FILE, String VIDEO_FILE, String PHOTO_FILE) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.AUDIO_FILE = AUDIO_FILE;
        this.VIDEO_FILE = VIDEO_FILE;
        this.PHOTO_FILE = PHOTO_FILE;
    }

    public String getPHOTO_FILE() {
        return PHOTO_FILE;
    }

    public void setPHOTO_FILE(String PHOTO_FILE) {
        this.PHOTO_FILE = PHOTO_FILE;
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

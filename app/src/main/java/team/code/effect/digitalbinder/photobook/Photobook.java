package team.code.effect.digitalbinder.photobook;

/**
 * Created by student on 2016-11-25.
 */

public class Photobook {
    private int photobook_id;
    private String title;
    private String filename;
    private String icon;
    private String regdate;

    public int getPhotobook_id() {
        return photobook_id;
    }

    public void setPhotobook_id(int photobook_id) {
        this.photobook_id = photobook_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }
}

package team.code.effect.digitalbinder.photobook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by student on 2016-11-25.
 */

public class Photobook implements Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.photobook_id);
        dest.writeString(this.title);
        dest.writeString(this.filename);
        dest.writeString(this.icon);
        dest.writeString(this.regdate);
    }

    public Photobook() {
    }

    protected Photobook(Parcel in) {
        this.photobook_id = in.readInt();
        this.title = in.readString();
        this.filename = in.readString();
        this.icon = in.readString();
        this.regdate = in.readString();
    }

    public static final Creator<Photobook> CREATOR = new Creator<Photobook>() {
        @Override
        public Photobook createFromParcel(Parcel source) {
            return new Photobook(source);
        }

        @Override
        public Photobook[] newArray(int size) {
            return new Photobook[size];
        }
    };
}

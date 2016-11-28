package team.code.effect.digitalbinder.explorer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 재우 on 2016-11-27.
 */

public class Explorer implements Parcelable{
    private String title;
    private String filename;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.filename);
    }

    public Explorer() {
    }

    protected Explorer(Parcel in) {
        this.title = in.readString();
        this.filename = in.readString();
    }

    public static final Creator<Explorer> CREATOR = new Creator<Explorer>() {
        @Override
        public Explorer createFromParcel(Parcel source) {
            return new Explorer(source);
        }

        @Override
        public Explorer[] newArray(int size) {
            return new Explorer[size];
        }
    };
}

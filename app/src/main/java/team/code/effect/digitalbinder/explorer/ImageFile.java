package team.code.effect.digitalbinder.explorer;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageFile implements Parcelable{
    int bucket_id;
    String bucket_name;
    int image_id;
    Uri path;
    int orientation;
    String takenDate;

    public ImageFile(int bucket_id, String bucket_name, int image_id, Uri path, int orientation, String takenDate) {
        this.bucket_id = bucket_id;
        this.bucket_name = bucket_name;
        this.image_id = image_id;
        this.path = path;
        this.orientation = orientation;
        this.takenDate = takenDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bucket_id);
        dest.writeString(this.bucket_name);
        dest.writeInt(this.image_id);
        dest.writeParcelable(this.path, flags);
        dest.writeInt(this.orientation);
        dest.writeString(this.takenDate);
    }

    protected ImageFile(Parcel in) {
        this.bucket_id = in.readInt();
        this.bucket_name = in.readString();
        this.image_id = in.readInt();
        this.path = in.readParcelable(Uri.class.getClassLoader());
        this.orientation = in.readInt();
        this.takenDate = in.readString();
    }

    public static final Creator<ImageFile> CREATOR = new Creator<ImageFile>() {
        @Override
        public ImageFile createFromParcel(Parcel source) {
            return new ImageFile(source);
        }

        @Override
        public ImageFile[] newArray(int size) {
            return new ImageFile[size];
        }
    };
}

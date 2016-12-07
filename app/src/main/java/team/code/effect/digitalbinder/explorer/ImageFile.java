package team.code.effect.digitalbinder.explorer;

import android.net.Uri;

public class ImageFile {
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
}

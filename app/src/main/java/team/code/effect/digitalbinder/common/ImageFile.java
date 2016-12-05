package team.code.effect.digitalbinder.common;

import android.net.Uri;

public class ImageFile {
    Uri origin;
    Uri thumbnail;

    public ImageFile(Uri origin, Uri thumbnail) {
        this.origin = origin;
        this.thumbnail = thumbnail;
    }
}

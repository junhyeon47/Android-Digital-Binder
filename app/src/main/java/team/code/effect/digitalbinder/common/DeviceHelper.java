package team.code.effect.digitalbinder.common;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DeviceHelper {
    public static final int ORIENTATION_REVERSE_LANDSCAPE = 180;
    public static final int ORIENTATION_PORTRAIT = 90;
    public static final int ORIENTATION_LANDSCAPE = 0;
    public static final int ORIENTATION_REVERSE_PORTRAIT = 270;
    public static final int width;
    public static final int height;

    static {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
    }
}

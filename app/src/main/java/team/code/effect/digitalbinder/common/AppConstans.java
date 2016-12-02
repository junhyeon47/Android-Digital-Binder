package team.code.effect.digitalbinder.common;

import android.os.Environment;

/**
 * Created by student on 2016-11-30.
 */

public class AppConstans {
    public static final String DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String APP_PATH = DIR + "/DigitalBinder";
    public static final String DB_PATH = "/data/data/team.code.effect.digitalbinder/databases/digitalBinder.sqlite";
    public static final String BLUETOOTH = "블루투스";
    public static final String NFC = "NFC";
    public static final String GDRIVE = "드라이브";
    public static final String NDRIVE = "N Drive";
    public static final String DROPBOX = "드롭박스";
            ;
}

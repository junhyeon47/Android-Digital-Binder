package team.code.effect.digitalbinder.camera;

import android.os.AsyncTask;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import team.code.effect.digitalbinder.common.AppConstans;

public class StoreTempFileAsync extends AsyncTask<byte[], Void, Void> {
    CameraActivity cameraActivity;

    public StoreTempFileAsync(CameraActivity cameraActivity) {
        this.cameraActivity = cameraActivity;
    }

    @Override
    protected Void doInBackground(byte[]... params) {
        try {
            String fileName = Integer.toString(cameraActivity.list.size());
            FileOutputStream fos = new FileOutputStream(AppConstans.APP_PATH_TEMP+fileName+AppConstans.EXT_IMAGE);
            fos.write(params[0]);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

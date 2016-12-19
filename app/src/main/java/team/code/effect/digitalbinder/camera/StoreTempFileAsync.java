package team.code.effect.digitalbinder.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.graphics.BitmapCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.BitmapHelper;

public class StoreTempFileAsync extends AsyncTask<byte[], Void, Void> implements Parcelable{
    CameraActivity cameraActivity;
    int orientation;

    public StoreTempFileAsync(CameraActivity cameraActivity, int orientation) {
        this.cameraActivity = cameraActivity;
        this.orientation = orientation;
    }

    protected StoreTempFileAsync(Parcel in) {
        orientation = in.readInt();
    }

    @Override
    protected Void doInBackground(byte[]... params) {
        try {
            String fileName = Long.toString(System.currentTimeMillis());
            Bitmap bitmap = BitmapFactory.decodeByteArray(params[0], 0, params[0].length);
            FileOutputStream fos = new FileOutputStream(AppConstans.APP_PATH_TEMP+fileName+AppConstans.EXT_IMAGE);
            BitmapHelper.changeOrientation(bitmap, orientation).compress(Bitmap.CompressFormat.JPEG, BitmapHelper.QUALITY, fos);
            fos.write(params[0]);
            fos.close();
            File file = new File(AppConstans.APP_PATH_TEMP+fileName+AppConstans.EXT_IMAGE);
            cameraActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

    }

    public static final Creator<StoreTempFileAsync> CREATOR = new Creator<StoreTempFileAsync>() {
        @Override
        public StoreTempFileAsync createFromParcel(Parcel in) {
            return new StoreTempFileAsync(in);
        }

        @Override
        public StoreTempFileAsync[] newArray(int size) {
            return new StoreTempFileAsync[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(orientation);
    }
}

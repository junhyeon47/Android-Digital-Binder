package team.code.effect.digitalbinder.photobook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import team.code.effect.digitalbinder.common.BitmapHelper;
import team.code.effect.digitalbinder.common.DeviceHelper;
import uk.co.senab.photoview.PhotoView;

public class PhotobookAsync extends AsyncTask<String, Void, Bitmap> {
    PhotoView photo_view;

    public PhotobookAsync(PhotoView photo_view) {
        this.photo_view = photo_view;
    }

    @Override
    protected Bitmap doInBackground(String... parmas) {
        Log.d("ASYNC", parmas[0]);
        return BitmapHelper.decodeFile(parmas[0], DeviceHelper.width, DeviceHelper.height);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        photo_view.setImageBitmap(bitmap);
    }
}

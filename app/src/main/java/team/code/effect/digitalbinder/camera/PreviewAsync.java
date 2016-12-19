package team.code.effect.digitalbinder.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import team.code.effect.digitalbinder.common.BitmapHelper;
import team.code.effect.digitalbinder.common.DeviceHelper;
import uk.co.senab.photoview.PhotoView;

public class PreviewAsync extends AsyncTask<String, Void, Bitmap> {
    PhotoView photo_view;

    public PreviewAsync(PhotoView photo_view) {
        this.photo_view = photo_view;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return BitmapHelper.decodeFile(params[0], DeviceHelper.width, DeviceHelper.height);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        photo_view.setImageBitmap(bitmap);
    }
}

package team.code.effect.digitalbinder.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class PreviewAsync extends AsyncTask<Integer, Void, Bitmap> {
    private final WeakReference<ImageView> weakReference;
    private int data = 0;

    public PreviewAsync(ImageView imageView){
        weakReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }
}

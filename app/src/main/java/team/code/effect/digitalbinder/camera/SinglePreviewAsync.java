package team.code.effect.digitalbinder.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import team.code.effect.digitalbinder.common.DeviceHelper;
import uk.co.senab.photoview.PhotoView;

public class SinglePreviewAsync extends AsyncTask<Integer, Void, Bitmap> {
    private final String TAG = this.getClass().getName();
    private PhotoView iv_original;
    private ImageButton btn_close;

    public SinglePreviewAsync(PhotoView iv_original, ImageButton btn_close) {
        this.iv_original = iv_original;
        this.btn_close = btn_close;
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        int index = params[0];
        int size = DeviceHelper.width;
        Preview preview = CameraActivity.list.get(index);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[16 * 1024];
        Bitmap bitmap = BitmapFactory.decodeByteArray(preview.getBytes(), 0, preview.getBytes().length, options);
        float rotateRatio = 0f;
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int resizeWidth = (bitmapWidth*size)/bitmapHeight;

        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, size, true);
        switch (preview.getOrientation()){
            case DeviceHelper.ORIENTATION_REVERSE_LANDSCAPE:
                rotateRatio = 180f;
                break;
            case DeviceHelper.ORIENTATION_PORTRAIT:
                rotateRatio = 90f;
                break;
            case DeviceHelper.ORIENTATION_LANDSCAPE:
                rotateRatio = 0f;
                break;
            case DeviceHelper.ORIENTATION_REVERSE_PORTRAIT:
                rotateRatio = -90f;
                break;
        }
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.preRotate(rotateRatio);
        return Bitmap.createBitmap(resizeBitmap, 0, 0, resizeWidth, size, rotateMatrix, true);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        iv_original.setImageBitmap(bitmap);
        btn_close.setVisibility(View.VISIBLE);
    }
}

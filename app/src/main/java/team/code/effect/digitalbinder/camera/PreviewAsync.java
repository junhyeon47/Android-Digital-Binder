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

public class PreviewAsync extends AsyncTask<Integer, Void, Bitmap> {
    private ImageView iv_thumbnail;
    private ImageButton btn_remove;
    private TextView txt_index;

    public PreviewAsync(ImageView iv_thumbnail, ImageButton btn_remove, TextView txt_index) {
        this.iv_thumbnail = iv_thumbnail;
        this.btn_remove = btn_remove;
        this.txt_index = txt_index;
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
//        int index = params[0];
//        int size = DeviceHelper.width;
//        Preview preview = CameraActivity.list.get(index);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inTempStorage = new byte[16 * 1024];
//        Bitmap bitmap = BitmapFactory.decodeByteArray(preview.getBytes(), 0, preview.getBytes().length, options);
//        float rotateRatio = 0f;
//        int bitmapWidth = bitmap.getWidth();
//        int bitmapHeight = bitmap.getHeight();
//        int resizeWidth = (bitmapWidth*size)/bitmapHeight;
//
//        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, size, true);
//        Bitmap cropBitmap = Bitmap.createBitmap(resizeBitmap, (resizeWidth-size)/2, 0, size, size);
//        return BitmapHelper.changeOrientation(cropBitmap, preview.getOrientation());
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        iv_thumbnail.setImageBitmap(bitmap);
        btn_remove.setVisibility(View.VISIBLE);
        txt_index.setVisibility(View.VISIBLE);
    }
}

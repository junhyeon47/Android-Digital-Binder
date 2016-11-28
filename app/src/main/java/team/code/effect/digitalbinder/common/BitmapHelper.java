package team.code.effect.digitalbinder.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

public class BitmapHelper {
    final static int ORIENTATION_REVERSE_LANDSCAPE = 0;
    final static int ORIENTATION_PORTRAIT = 1;
    final static int ORIENTATION_LANDSCAPE = 2;
    final static int ORIENTATION_REVERSE_PORTRAIT = 3;

    public static Bitmap bytesToThumbnailBitmap(byte[] bytes, int size, int orientation){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[16 * 1024];
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        float rotateRatio = 0f;
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int resizeWidth = (bitmapWidth*size)/bitmapHeight;
        Log.d("BitmapHelper", "bitmapWidth: "+bitmapWidth+", bitmapHeight: "+bitmapHeight);
        Log.d("BitmapHelper", "resizeWidth: "+resizeWidth+", size: "+size);
        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, size, true);
        Bitmap cropBitmap = Bitmap.createBitmap(resizeBitmap, (resizeWidth-size)/2, 0, size, size);
        switch (orientation){
            case ORIENTATION_REVERSE_LANDSCAPE:
                rotateRatio = 180f;
                break;
            case ORIENTATION_PORTRAIT:
                rotateRatio = 90f;
                break;
            case ORIENTATION_LANDSCAPE:
                rotateRatio = 0f;
                break;
            case ORIENTATION_REVERSE_PORTRAIT:
                rotateRatio = -90f;
                break;
        }
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.preRotate(rotateRatio);
        return Bitmap.createBitmap(cropBitmap, 0, 0, size, size, rotateMatrix, true);
    }
}

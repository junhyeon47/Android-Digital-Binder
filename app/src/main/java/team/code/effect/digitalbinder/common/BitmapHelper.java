package team.code.effect.digitalbinder.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapHelper {
    public static int QUALITY = 80;
    public static BitmapFactory.Options getOptions(String filename){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);
        return options;
    }
    public static int getInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeFile(String filename, int width, int hegiht){
        BitmapFactory.Options options = getOptions(filename);
        options.inJustDecodeBounds = false;
        options.inSampleSize = getInSampleSize(options, width, hegiht);
        return BitmapFactory.decodeFile(filename, options);
    }
    public static Bitmap getThumbnail(Bitmap bitmap, int size){
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        Bitmap resizeBitmap;
        Bitmap cropBitmap;

        if(originWidth > size && originHeight > size){
            if(originWidth > originHeight){
                int resize = (originWidth*size)/originHeight;
                resizeBitmap = Bitmap.createScaledBitmap(bitmap, resize, size, true);
                cropBitmap = Bitmap.createBitmap(resizeBitmap, (resize-size)/2, 0, size, size);
            }else{
                int resize = (originHeight*size)/originWidth;
                resizeBitmap = Bitmap.createScaledBitmap(bitmap, size, resize, true);
                cropBitmap = Bitmap.createBitmap(resizeBitmap, 0, (resize-size)/2, size, size);
            }
        }else{
            if(originWidth > originHeight){
                int resize = (int)((size/(float)originHeight)*originWidth);
                resizeBitmap = Bitmap.createScaledBitmap(bitmap, resize, size, true);
                cropBitmap = Bitmap.createBitmap(resizeBitmap, (resize-size)/2, 0, size, size);
            }else{
                int resize = (int)((size/(float)originWidth)*originHeight);
                resizeBitmap = Bitmap.createScaledBitmap(bitmap, size, resize, true);
                cropBitmap = Bitmap.createBitmap(resizeBitmap, 0, (resize-size)/2, size, size);
            }
        }
        bitmap.recycle();
        resizeBitmap.recycle();
        return cropBitmap;
    }

    public static Bitmap changeOrientation(Bitmap bitmap, int orientation){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float rotateRatio = 0f;
        switch (orientation){
            case 0:
                rotateRatio = 0;
                break;
            case 90:
                rotateRatio = 90f;
                break;
            case 180:
                rotateRatio = 180f;
                break;
            case 270:
                rotateRatio = 270f;
                break;
        }
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.preRotate(rotateRatio);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, rotateMatrix, true);
    }
}

package team.code.effect.digitalbinder.camera;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.DeviceHelper;
import team.code.effect.digitalbinder.photobook.Photobook;

public class StoreFileAsync extends AsyncTask<String, String, Photobook>{
    private static final int QUALITY = 80;
    private Context context;
    private Dialog dialog;
    private ProgressDialog progressDialog;

    public StoreFileAsync(Context context, Dialog dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Photobook doInBackground(String... params) {
        String filename = params[0];
        Bitmap bitmap;
        FileOutputStream fos;
        File cacheDir = context.getExternalCacheDir();
        File cacheFile;
        Preview preview;
        ArrayList<String> cacheList = new ArrayList<String>();
        float rotateRatio = 0f;
        for(int i=0; i<CameraActivity.list.size(); ++i){
            try {
                preview = CameraActivity.list.get(i);
                bitmap = BitmapFactory.decodeByteArray(preview.getBytes(), 0, preview.getBytes().length);
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
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, true);
                cacheFile = File.createTempFile(Integer.toString(i+1), ".jpg", cacheDir);
                cacheList.add(cacheFile.getAbsolutePath());
                fos = new FileOutputStream(cacheFile.getName());
                bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, fos);
                fos.flush();
                fos.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
        fos = new FileOutputStream(AppConstans.APP_PATH+"/"+filename);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ZipOutputStream zos = new ZipOutputStream(bos);


            for(int i=0; i<cacheList.size(); ++i){
//                entry = new ZipEntry();
//                cacheList.get(i);
//                entry.setSize(cacheFile.length());
//                zos.putNextEntry(entry);
//                zos.wr;
//                zos.closeEntry();
            }
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //tempFile.deleteOnExit();
        Log.d("StoreFileAsync", filename);
        //Log.d("StoreFileAsync", cacheDir.toString());
        return null;
    }

    @Override
    protected void onPostExecute(Photobook photobook) {
        dialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(String... values) {
    }
}

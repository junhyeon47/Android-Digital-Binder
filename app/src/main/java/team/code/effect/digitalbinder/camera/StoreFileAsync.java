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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.DeviceHelper;
import team.code.effect.digitalbinder.main.MainActivity;
import team.code.effect.digitalbinder.photobook.Photobook;

public class StoreFileAsync extends AsyncTask<String, String, Photobook> {
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
        File tempFile = null;
        Preview preview;
        ArrayList<File> tempList = new ArrayList<File>();
        float rotateRatio = 0f;
        File tempDir = new File(AppConstans.APP_PATH + "/temp");
        if (tempDir.exists() == false)
            tempDir.mkdir();
        try {
            for (int i = 0; i < CameraActivity.list.size(); ++i) {

                preview = CameraActivity.list.get(i);
                bitmap = BitmapFactory.decodeByteArray(preview.getBytes(), 0, preview.getBytes().length);
                switch (preview.getOrientation()) {
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
                tempFile = new File(AppConstans.APP_PATH + "/temp/" + Integer.toString(i + 1) + ".jpg");
                tempList.add(tempFile);
                fos = new FileOutputStream(tempFile.getAbsolutePath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, fos);
                fos.flush();
                fos.close();

            }

            byte[] buff = new byte[16 * 1024];
            fos = new FileOutputStream(AppConstans.APP_PATH + "/" + filename + ".zip");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ZipOutputStream zos = new ZipOutputStream(bos);
            FileInputStream fis = null;
            for (int i = 0; i < tempList.size(); ++i) {
                Log.d("ASYNC", tempList.get(i).getAbsolutePath());
                fis = new FileInputStream(tempList.get(i).getAbsolutePath());
                zos.putNextEntry(new ZipEntry((i + 1) + ".jpg"));
                int length;
                while ((length = fis.read(buff, 0, 16 * 1024)) > 0) {
                    zos.write(buff, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photobook photobook = new Photobook();
        photobook.setFilename(filename + ".zip");
        photobook.setTitle(filename);
        photobook.setIcon("default.jpg");
        return photobook;
    }

    @Override
    protected void onPostExecute(Photobook photobook) {
        MainActivity.dao.insert(photobook);
        dialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(String... values) {
    }
}

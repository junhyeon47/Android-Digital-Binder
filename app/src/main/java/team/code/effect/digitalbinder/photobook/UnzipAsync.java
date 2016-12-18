package team.code.effect.digitalbinder.photobook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.ImageFile;
import team.code.effect.digitalbinder.main.MainActivity;

public class UnzipAsync extends AsyncTask<Integer, String, Void>{
    String TAG;
    PhotobookActivity photobookActivity;

    public UnzipAsync(PhotobookActivity photobookActivity) {
        TAG = this.getClass().getName();
        this.photobookActivity = photobookActivity;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        Photobook photobook = MainActivity.dao.select(params[0]);

        //디렉터리 존재 여부 확인 및 사진파일 삭제.
        photobookActivity.checkDirectory();

        //압축풀기 시작
        InputStream is = null;
        ZipInputStream zis = null;

        try {
            is = new FileInputStream(AppConstans.APP_PATH_DATA+photobook.getFilename());
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry entry;
            byte[] buffer = new byte[1024];
            int count;

            while((entry = zis.getNextEntry()) != null){
                Log.d(TAG, "압축을 해제한 파일: "+entry.getName());
                FileOutputStream fos = new FileOutputStream(AppConstans.APP_PATH+entry.getName());
                Bitmap bitmap = BitmapFactory.decodeStream(zis);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                while ((count = zis.read(buffer)) != -1){
//                    fos.write(buffer, 0, count);
//                }
                fos.close();
                zis.closeEntry();
//                File file = new File(AppConstans.APP_PATH+entry.getName());
//                Log.d(TAG, "path: "+Uri.fromFile(file.getAbsoluteFile()));
//                photobookActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(zis != null){
                try {
                    zis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //이미지 파일 불러오기
        String[] projection = {
                MediaStore.Images.Media.BUCKET_ID, //폴더 ID
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //폴더 명.
                MediaStore.Images.Media._ID, //이미지 ID
                MediaStore.Images.Media.DATA, //이미지 경로
                MediaStore.Images.Media.ORIENTATION, //이미지 회전 각도.
                MediaStore.Images.Media.DATE_TAKEN //이미지 촬영날짜.
        };

        String where = "bucket_display_name='DigitalBinder'";
        String orderBy = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC, ";
        orderBy += MediaStore.Images.Media.DATE_TAKEN + " DESC";

        Cursor imageCursor = photobookActivity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                where,
                null,
                orderBy
        );

        int bucketIdColumnIndex = imageCursor.getColumnIndex(projection[0]);
        int bucketDisplayNameColumnIndex = imageCursor.getColumnIndex(projection[1]);
        int idColumnIndex = imageCursor.getColumnIndex(projection[2]);
        int dataColumnIndex = imageCursor.getColumnIndex(projection[3]);
        int orientationColumnIndex = imageCursor.getColumnIndex(projection[4]);
        int dateTakenColumnIndex = imageCursor.getColumnIndex(projection[5]);

        if (imageCursor == null) {
            // Error 발생
            Log.d(TAG, "cursor is null");
        } else if (imageCursor.moveToFirst()) {
            do {
                String bucketId = imageCursor.getString(bucketIdColumnIndex);
                String bucketDisplayName = imageCursor.getString(bucketDisplayNameColumnIndex);
                String imageId = imageCursor.getString(idColumnIndex);
                String filePath = imageCursor.getString(dataColumnIndex);
                String orientation = imageCursor.getString(orientationColumnIndex);
                String dateTaken = imageCursor.getString(dateTakenColumnIndex);

                Log.d(TAG, "filePath: "+filePath);
                Log.d(TAG, "imageId: "+imageId);
                Log.d(TAG, "bucketName: "+bucketDisplayName);
                Log.d(TAG, "bucketId: "+bucketId);
                Log.d(TAG, "orientation: "+orientation); //null 값을 갖을 수 있다. 처리해야함.
                Log.d(TAG, "dateTaken: "+dateTaken);

                ImageFile imageFile = new ImageFile(
                        Integer.parseInt(bucketId),
                        bucketDisplayName,
                        Integer.parseInt(imageId),
                        Uri.parse(filePath),
                        (orientation == null) ? 0 : Integer.parseInt(orientation),
                        dateTaken
                );
                photobookActivity.list.add(imageFile);
            } while (imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        photobookActivity.photobookPagerAdapter.notifyDataSetChanged();
    }
}

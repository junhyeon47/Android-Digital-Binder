package team.code.effect.digitalbinder.common;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

public class MediaStorageHelper {
    public static String WHERE = "bucket_display_name='temp'";
    public static String ORDER_BY = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC, "+ MediaStore.Images.Media.DATE_TAKEN;
    public static String ASC = " ASC";
    public static String DESC = " DESC";

    public static ArrayList<ImageFile> getImageFiles(Context context, String where, String sort){
        //이미지 파일 불러오기
        String[] projection = {
                MediaStore.Images.Media.BUCKET_ID, //폴더 ID
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //폴더 명.
                MediaStore.Images.Media._ID, //이미지 ID
                MediaStore.Images.Media.DATA, //이미지 경로
                MediaStore.Images.Media.ORIENTATION, //이미지 회전 각도.
                MediaStore.Images.Media.DATE_TAKEN //이미지 촬영날짜.
        };

        Cursor imageCursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                where,
                null,
                ORDER_BY+sort
        );

        int bucketIdColumnIndex = imageCursor.getColumnIndex(projection[0]);
        int bucketDisplayNameColumnIndex = imageCursor.getColumnIndex(projection[1]);
        int idColumnIndex = imageCursor.getColumnIndex(projection[2]);
        int dataColumnIndex = imageCursor.getColumnIndex(projection[3]);
        int orientationColumnIndex = imageCursor.getColumnIndex(projection[4]);
        int dateTakenColumnIndex = imageCursor.getColumnIndex(projection[5]);

        ArrayList<ImageFile> list = new ArrayList<>();
        if (imageCursor == null) {
            // Error 발생
        } else if (imageCursor.moveToFirst()) {
            do {
                String bucketId = imageCursor.getString(bucketIdColumnIndex);
                String bucketDisplayName = imageCursor.getString(bucketDisplayNameColumnIndex);
                String imageId = imageCursor.getString(idColumnIndex);
                String filePath = imageCursor.getString(dataColumnIndex);
                String orientation = imageCursor.getString(orientationColumnIndex);
                String dateTaken = imageCursor.getString(dateTakenColumnIndex);

                Log.d("MediaStorageHelper", "orientation: "+orientation);

                ImageFile imageFile = new ImageFile(
                        Integer.parseInt(bucketId),
                        bucketDisplayName,
                        Integer.parseInt(imageId),
                        Uri.parse(filePath),
                        (orientation == null) ? 0 : Integer.parseInt(orientation),
                        dateTaken
                );
                list.add(imageFile);
            } while (imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();

        return list;
    }

    public static void delete(Context context, String where){
        context.getContentResolver().delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                where,
                null
        );
    }
}

package team.code.effect.digitalbinder.explorer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

import team.code.effect.digitalbinder.common.FileHelper;

public class FileAsync extends AsyncTask<Void, String, Void> {
    ExplorerActivity explorerActivity;

    public FileAsync(ExplorerActivity explorerActivity) {
        this.explorerActivity = explorerActivity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        fetchAllImages();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

    }

    @Override
    protected void onProgressUpdate(String... values) {
    }

    Uri uriToThumbnail(String imageId) {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = { MediaStore.Images.Thumbnails.DATA };
        ContentResolver contentResolver = explorerActivity.getApplicationContext().getContentResolver();

        // 원본 이미지의 _ID가 매개변수 imageId인 썸네일을 출력
        Cursor thumbnailCursor = contentResolver.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, // 썸네일 컨텐트 테이블
                projection, // DATA를 출력
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?", // IMAGE_ID는 원본 이미지의 _ID를 나타냅니다.
                new String[]{imageId},
                null);

        if (thumbnailCursor == null) {
            // Error 발생
            return null;
        } else if (thumbnailCursor.moveToFirst()) {
            int thumbnailColumnIndex = thumbnailCursor.getColumnIndex(projection[0]);

            String thumbnailPath = thumbnailCursor.getString(thumbnailColumnIndex);
            thumbnailCursor.close();
            return Uri.parse(thumbnailPath);
        } else {
            // thumbnailCursor가 비었습니다.
            // 이는 이미지 파일이 있더라도 썸네일이 존재하지 않을 수 있기 때문입니다.
            // 보통 이미지가 생성된 지 얼마 되지 않았을 때 그렇습니다.
            // 썸네일이 존재하지 않을 때에는 아래와 같이 썸네일을 생성하도록 요청합니다
            MediaStore.Images.Thumbnails.getThumbnail(contentResolver, Long.parseLong(imageId), MediaStore.Images.Thumbnails.MINI_KIND, null);
            thumbnailCursor.close();
            return uriToThumbnail(imageId);
        }
    }

    void fetchAllImages() {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATE_TAKEN
        };
        String orderBy = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC, ";
        orderBy += MediaStore.Images.Media.DATE_TAKEN +" DESC";
        Cursor imageCursor = explorerActivity.getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA, _ID를 출력
                null,       // 모든 개체 출력
                null,
                orderBy); //폴더별 정렬하고, 최신순으로 정렬.

        ArrayList<ImageFile> result = new ArrayList<>(imageCursor.getCount());
        int bucketIdColumnIndex = imageCursor.getColumnIndex(projection[0]);
        int bucketDisplayNameColumnIndex = imageCursor.getColumnIndex(projection[1]);
        int idColumnIndex = imageCursor.getColumnIndex(projection[2]);
        int dataColumnIndex = imageCursor.getColumnIndex(projection[3]);
        int orientationColumnIndex = imageCursor.getColumnIndex(projection[4]);
        int dateTakenColumnIndex = imageCursor.getColumnIndex(projection[5]);

        if (imageCursor == null) {
            // Error 발생
        } else if (imageCursor.moveToFirst()) {
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                String imageId = imageCursor.getString(idColumnIndex);
                String bucketDisplayName = imageCursor.getString(bucketDisplayNameColumnIndex);
                String bucketId = imageCursor.getString(bucketIdColumnIndex);
                String orientation = imageCursor.getString(orientationColumnIndex);
                String dateTaken = imageCursor.getString(dateTakenColumnIndex);

                Uri originUri = Uri.parse(filePath);
                Log.d("ASYNC", "filePath: "+filePath);
                Log.d("ASYNC", "imageId: "+imageId);
                Log.d("ASYNC", "bucketName: "+bucketDisplayName);
                Log.d("ASYNC", "bucketId: "+bucketId);
                Log.d("ASYNC", "orientation: "+orientation); //null 값을 갖을 수 있다. 처리해야함.
                Log.d("ASYNC", "dateTaken: "+dateTaken);

                //ImageFile photo = new ImageFile(originUri, thumbnailUri);
                //FileHelper.list.add(photo);
            } while(imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
    }
}

package team.code.effect.digitalbinder.main;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import team.code.effect.digitalbinder.common.FileHelper;
import team.code.effect.digitalbinder.common.ImageFile;

public class FileAsync extends AsyncTask<Void, String, Void> {
    SplashActivity splashActivity;

    public FileAsync(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        fetchAllImages();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Intent intent = new Intent(splashActivity, MainActivity.class);
        splashActivity.startActivity(intent);
        splashActivity.finish();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if(values[0].equals("count")){
            splashActivity.progress.setProgress(Integer.parseInt(values[1]));
        }
    }

    Uri uriToThumbnail(String imageId) {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = { MediaStore.Images.Thumbnails.DATA };
        ContentResolver contentResolver = splashActivity.getApplicationContext().getContentResolver();

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
        String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };

        Cursor imageCursor = splashActivity.getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA, _ID를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        ArrayList<ImageFile> result = new ArrayList<>(imageCursor.getCount());
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);
        int idColumnIndex = imageCursor.getColumnIndex(projection[1]);
        int count = 0;
        if (imageCursor == null) {
            // Error 발생
        } else if (imageCursor.moveToFirst()) {
            splashActivity.progress.setMax(imageCursor.getCount()); //프로그레스바 설정
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                String imageId = imageCursor.getString(idColumnIndex);

                Uri originUri = Uri.parse(filePath);
                Uri thumbnailUri = uriToThumbnail(imageId);
                // 원본 이미지와 썸네일 이미지의 uri를 모두 담을 수 있는 클래스를 선언합니다.
                Log.d("ASYNC", "originUri: "+originUri);
                Log.d("ASYNC","thumbnailUri:"+thumbnailUri);
                ImageFile photo = new ImageFile(originUri, thumbnailUri);
                FileHelper.list.add(photo);

                onProgressUpdate("count", Integer.toString(++count));
            } while(imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
    }
}

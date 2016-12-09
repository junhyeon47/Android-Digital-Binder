package team.code.effect.digitalbinder.explorer;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

public class ExplorerActivity extends AppCompatActivity {
    static final int CACHE_SIZE = 200;
    String TAG;
    Toolbar toolbar;
    ArrayList<ImageFolder> listFolders;
    LinearLayout layout_folders, layout_images;
    RecyclerView recycler_view_folders, recycler_view_images, recycler_view_selected;
    FolderRecyclerAdapter folderRecyclerAdapter;
    ImageRecyclerAdapter imageRecyclerAdapter;
    TextView txt_folder_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
        setContentView(R.layout.activity_explorer);

        listFolders = getAllImageFolders();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setToolbar();

        layout_folders = (LinearLayout)findViewById(R.id.layout_folders);
        layout_images = (LinearLayout)findViewById(R.id.layout_images);
        txt_folder_name = (TextView)findViewById(R.id.txt_folder_name);

        //폴더 관련 RecyclerView 설정
        recycler_view_folders = (RecyclerView)findViewById(R.id.recycler_view_folders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recycler_view_folders.setLayoutManager(layoutManager);
        recycler_view_folders.setHasFixedSize(true);
        recycler_view_folders.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        folderRecyclerAdapter = new FolderRecyclerAdapter(this);
        recycler_view_folders.setAdapter(folderRecyclerAdapter);

        //썸네일 관련 RecyclerView 설정
        recycler_view_images = (RecyclerView)findViewById(R.id.recycler_view_images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        imageRecyclerAdapter= new ImageRecyclerAdapter(this);
        recycler_view_images.setLayoutManager(gridLayoutManager);
        recycler_view_images.setItemViewCacheSize(CACHE_SIZE);
        ImageViewItemDecoration imageViewItemDecoration = new ImageViewItemDecoration(1);
        recycler_view_images.addItemDecoration(imageViewItemDecoration);
        recycler_view_images.setAdapter(imageRecyclerAdapter);
    }

    public void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("사진 선택");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //툴바의 메뉴 터치 이벤트
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(layout_images.getVisibility() == View.VISIBLE){
            layout_images.setVisibility(View.GONE);
            layout_folders.setVisibility(View.VISIBLE);
            imageRecyclerAdapter.resetList();
        }else {
            finish(); //현재 액티비티에서 이전 액티비티로 전환.
        }
    }

    public ArrayList<ImageFolder> getAllImageFolders() {
        String[] projection = {
                MediaStore.Images.Media.BUCKET_ID, //폴더 ID
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //폴더 명.
                MediaStore.Images.Media.DATA,
        };
        String where = MediaStore.Images.Media.BUCKET_DISPLAY_NAME+"="+MediaStore.Images.Media.BUCKET_DISPLAY_NAME+") GROUP BY (";
        where += MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
        String orderBy = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC";

        Cursor imageCursor = getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA, _ID를 출력
                where,       // 모든 개체 출력
                null,
                orderBy); //폴더별 정렬하고, 최신순으로 정렬.

        ArrayList<ImageFolder> result = new ArrayList<>();
        int bucketIdColumnIndex = imageCursor.getColumnIndex(projection[0]);
        int bucketDisplayNameColumnIndex = imageCursor.getColumnIndex(projection[1]);
        int dataColumnIndex = imageCursor.getColumnIndex(projection[2]);

        if (imageCursor == null) {
            // Error 발생
        } else if (imageCursor.moveToFirst()) {

            do {
                String bucketId = imageCursor.getString(bucketIdColumnIndex);
                String bucketDisplayName = imageCursor.getString(bucketDisplayNameColumnIndex);
                String data = imageCursor.getString(dataColumnIndex);
                Log.d(TAG, "path: "+data.substring(0, data.lastIndexOf("/")));
                ImageFolder imageFolder = new ImageFolder(
                        Integer.parseInt(bucketId),
                        bucketDisplayName,
                        data.substring(0, data.lastIndexOf("/")+1)
                );
                result.add(imageFolder);
            } while(imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
        return result;
    }

    public ArrayList<ImageFile> getAllImages(int bucket_id) {
        String[] projection = {
                MediaStore.Images.Media.BUCKET_ID, //폴더 ID
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //폴더 명.
                MediaStore.Images.Media._ID, //이미지 ID
                MediaStore.Images.Media.DATA, //이미지 경로
                MediaStore.Images.Media.ORIENTATION, //이미지 회전 각도.
                MediaStore.Images.Media.DATE_TAKEN //이미지 촬영날짜.
        };
        String where = MediaStore.Images.Media.BUCKET_ID+"="+Integer.toString(bucket_id);
        String orderBy = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC, ";
        orderBy += MediaStore.Images.Media.DATE_TAKEN +" DESC";

        Cursor imageCursor = getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA, _ID를 출력
                where,       // 모든 개체 출력
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
                String bucketId = imageCursor.getString(bucketIdColumnIndex);
                String bucketDisplayName = imageCursor.getString(bucketDisplayNameColumnIndex);
                String imageId = imageCursor.getString(idColumnIndex);
                String filePath = imageCursor.getString(dataColumnIndex);
                String orientation = imageCursor.getString(orientationColumnIndex);
                String dateTaken = imageCursor.getString(dateTakenColumnIndex);

//                Log.d("ASYNC", "filePath: "+filePath);
//                Log.d("ASYNC", "imageId: "+imageId);
//                Log.d(TAG, "bucketName: "+bucketDisplayName);
//                Log.d("ASYNC", "bucketId: "+bucketId);
//                Log.d("ASYNC", "orientation: "+orientation); //null 값을 갖을 수 있다. 처리해야함.
//                Log.d("ASYNC", "dateTaken: "+dateTaken);

                ImageFile imageFile = new ImageFile(
                        Integer.parseInt(bucketId),
                        bucketDisplayName,
                        Integer.parseInt(imageId),
                        Uri.parse(filePath),
                        (orientation == null)? 0: Integer.parseInt(orientation),
                        dateTaken
                );
                result.add(imageFile);
            } while(imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
        return result;
    }
}

package team.code.effect.digitalbinder.explorer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AlertHelper;
import team.code.effect.digitalbinder.common.ColorPalette;
import team.code.effect.digitalbinder.common.ColorPaletteHelper;
import team.code.effect.digitalbinder.common.ColorPaletteRecyclerAdapter;
import team.code.effect.digitalbinder.common.ImageFile;
import team.code.effect.digitalbinder.main.MainActivity;

public class ExplorerActivity extends AppCompatActivity {
    static final int CACHE_SIZE = 200;
    String TAG;
    Toolbar toolbar;
    ArrayList<ImageFolder> listFolders;
    LinearLayout layout_folders, layout_images, layout_detail, layout_selected;
    RecyclerView recycler_view_folders, recycler_view_images, recycler_view_selected;
    FolderRecyclerAdapter folderRecyclerAdapter;
    ImageRecyclerAdapter imageRecyclerAdapter;
    TextView txt_folder_name;
    ImageSelectedRecyclerAdapter imageSelectedRecyclerAdapter;
    ArrayList<ColorPalette> colorPaletteList = new ArrayList<>();
    RecyclerView recycler_view_color;
    ColorPaletteRecyclerAdapter colorPaletteRecyclerAdapter;
    static ExplorerActivity explorerActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
        setContentView(R.layout.activity_explorer);
        explorerActivity=this;
        listFolders = getAllImageFolders();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();

        layout_folders = (LinearLayout) findViewById(R.id.layout_folders);
        layout_images = (LinearLayout) findViewById(R.id.layout_images);
        layout_selected = (LinearLayout) findViewById(R.id.layout_selected);

        txt_folder_name = (TextView) findViewById(R.id.txt_folder_name);

        //폴더 관련 RecyclerView 설정
        recycler_view_folders = (RecyclerView) findViewById(R.id.recycler_view_folders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recycler_view_folders.setLayoutManager(layoutManager);
        recycler_view_folders.setHasFixedSize(true);
        recycler_view_folders.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        folderRecyclerAdapter = new FolderRecyclerAdapter(this);
        recycler_view_folders.setAdapter(folderRecyclerAdapter);

        //썸네일 관련 RecyclerView 설정
        recycler_view_images = (RecyclerView) findViewById(R.id.recycler_view_images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        imageRecyclerAdapter = new ImageRecyclerAdapter(this);
        recycler_view_images.setLayoutManager(gridLayoutManager);
        recycler_view_images.setItemViewCacheSize(CACHE_SIZE);
        ImageViewItemDecoration imageViewItemDecoration = new ImageViewItemDecoration(1);
        recycler_view_images.addItemDecoration(imageViewItemDecoration);
        recycler_view_images.setAdapter(imageRecyclerAdapter);

        //이미지 선택시 선택된 이미지만 출력하는 RecyclerView 설정
        recycler_view_selected = (RecyclerView) findViewById(R.id.recycler_view_selected);
        LinearLayoutManager selectedManager = new LinearLayoutManager(getApplicationContext());
        selectedManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view_selected.setLayoutManager(selectedManager);
        imageSelectedRecyclerAdapter = new ImageSelectedRecyclerAdapter(this);
        recycler_view_selected.setAdapter(imageSelectedRecyclerAdapter);
        recycler_view_selected.setItemViewCacheSize(CACHE_SIZE);
        recycler_view_selected.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        layout_selected.setVisibility(View.GONE);

        colorPaletteRecyclerAdapter = new ColorPaletteRecyclerAdapter();
        colorPaletteRecyclerAdapter.setList(colorPaletteList);
    }

    public void setToolbar() {
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
            case R.id.ex_select:
                Toast.makeText(this, "모두 선택", Toast.LENGTH_SHORT).show();
                break;
            case R.id.make_book:
                if (imageSelectedRecyclerAdapter.checkedList.size() == 0) {
                    Toast.makeText(this, "한개 이상의 파일을 선택하셔야 합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    makeBook();
                    //this.finish();
                }
                break;
        }
        return true;
    }

    public void makeBook() {
        ArrayList<File> bookList = new ArrayList<File>();
        for (int i = 0; i < imageSelectedRecyclerAdapter.checkedList.size(); i++) {
            File file = new File(imageSelectedRecyclerAdapter.checkedList.get(i).path.toString());
            bookList.add(file);
        }
        btnSaveClick(bookList);
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_explorer_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (layout_images.getVisibility() == View.VISIBLE) {
            layout_images.setVisibility(View.GONE);
            layout_folders.setVisibility(View.VISIBLE);
            imageRecyclerAdapter.resetList();
        } else {
            finish(); //현재 액티비티에서 이전 액티비티로 전환.
        }
    }

    public ArrayList<ImageFolder> getAllImageFolders() {
        String[] projection = {
                MediaStore.Images.Media.BUCKET_ID, //폴더 ID
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //폴더 명.
                MediaStore.Images.Media.DATA,
        };
        String where = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=" + MediaStore.Images.Media.BUCKET_DISPLAY_NAME + ") GROUP BY (";
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
                Log.d(TAG, "path: " + data.substring(0, data.lastIndexOf("/")));
                ImageFolder imageFolder = new ImageFolder(
                        Integer.parseInt(bucketId),
                        bucketDisplayName,
                        data.substring(0, data.lastIndexOf("/") + 1)
                );
                result.add(imageFolder);
            } while (imageCursor.moveToNext());
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
        String where = MediaStore.Images.Media.BUCKET_ID + "=" + Integer.toString(bucket_id);
        String orderBy = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC, ";
        orderBy += MediaStore.Images.Media.DATE_TAKEN + " DESC";

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
                        (orientation == null) ? 0 : Integer.parseInt(orientation),
                        dateTaken
                );
                result.add(imageFile);
            } while (imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
        return result;
    }


    public void btnSaveClick(ArrayList<File> list) {
        if (list.size() == 0) {
            Toast.makeText(this, "선택된 사진이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        final ArrayList<File> fileList=list;
        AlertDialog.Builder builder = AlertHelper.getAlertDialog(this, "알림", "선택한 사진을 하나로 묶습니다.");
        builder.setView(R.layout.layout_alert_save);
        builder.setPositiveButton("저장", null);
        builder.setNegativeButton("취소", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                initColorPaletteList();
                recycler_view_color = (RecyclerView) ((Dialog) dialogInterface).findViewById(R.id.recycler_view_color);
                GridLayoutManager layoutManager = new GridLayoutManager(((Dialog) dialogInterface).getContext(), 5);
                recycler_view_color.setLayoutManager(layoutManager);
                recycler_view_color.setAdapter(colorPaletteRecyclerAdapter);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = (Dialog) dialogInterface;
                        EditText txt_file_name = (EditText) dialog.findViewById(R.id.txt_file_name);
                        TextView txt_color = (TextView) dialog.findViewById(R.id.txt_color);

                        int colorValue;
                        //유효성 체크가 되면 AsyncTask 이용해 파일로 저장.
                        if ((colorValue = checkValidity(txt_file_name, txt_color)) != -1) {
                            /*StoreFileAsync async = new StoreFileAsync(getApplicationContext(), dialog);
                            async.execute(txt_file_name.getText().toString(), Integer.toString(colorValue));*/
                            BookMaker bookMaker=new BookMaker(getApplicationContext(), dialog, fileList);
                            bookMaker.execute(txt_file_name.getText().toString(), Integer.toString(colorValue));
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    public void initColorPaletteList() {
        if (colorPaletteList.size() == 0) {
            ColorPalette colorPalette;
            for (int i = 0; i < ColorPaletteHelper.VALUE.length; ++i) {
                colorPalette = new ColorPalette();
                colorPalette.setCheck(false);
                colorPalette.setColorValue(ColorPaletteHelper.VALUE[i]);
                colorPaletteList.add(colorPalette);
            }
        } else {
            for (int i = 0; i < ColorPaletteHelper.VALUE.length; ++i) {
                colorPaletteList.get(i).setCheck(false);
            }
        }
    }

    //파일명 중복 유효성 체크
    public boolean isExistFile(String filename) {
        return MainActivity.dao.isDuplicatedTitle(filename);
    }

    //Color Palette 유효성 체크
    public int isCheckedColor() {
        int result = -1;
        for (int i = 0; i < colorPaletteList.size(); ++i) {
            if (colorPaletteList.get(i).isCheck()) {
                result = i;
                break;
            }
        }
        return result;
    }

    //유효성 체크
    public int checkValidity(EditText txt_file_name, TextView txt_color) {
        int result = isCheckedColor();
        boolean flagDuplicate, flagFileName, flagColor;
        //파일명 중복 여부 확인
        if (isExistFile(txt_file_name.getText() + ".zip")) {
            txt_file_name.setText("");
            txt_file_name.setHint("중복된 이름이 존재합니다.");
            txt_file_name.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            flagDuplicate = false;
        } else {
            flagDuplicate = true;
        }

        if (txt_file_name.getText().length() == 0) {
            txt_file_name.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            flagFileName = false;
        } else {
            flagFileName = true;
        }

        //색상 선택여부를 확인
        if (result == -1) {
            txt_color.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            flagColor = false;
        } else {
            txt_color.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            flagColor = true;
        }

        if (flagDuplicate && flagFileName && flagColor)
            return result;
        else
            return -1;
    }

}

package team.code.effect.digitalbinder.photobook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.ImageFile;
import team.code.effect.digitalbinder.common.MediaStorageHelper;
import team.code.effect.digitalbinder.common.Photobook;
import team.code.effect.digitalbinder.main.MainActivity;

public class PhotobookActivity extends AppCompatActivity {
    private static int CACHE_SIZE = 100;
    String TAG;
    int photobook_id;
    LinearLayout layout_bottom;
    ImageButton ib_back, ib_show_list, ib_hide_list;
    ArrayList<ImageFile> list;
    ViewPager view_pager;
    RecyclerView recycler_view;
    PhotobookPagerAdapter photobookPagerAdapter;
    PhotobookRecyclerAdapter photobookRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photobook);
        TAG = this.getClass().getName();

        photobook_id = getIntent().getIntExtra("photobook_id", -1);
        if(photobook_id == -1){
            Log.d(TAG, "에러");
        }


        layout_bottom = (LinearLayout)findViewById(R.id.layout_bottom);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_show_list = (ImageButton)findViewById(R.id.ib_show_list);
        ib_hide_list = (ImageButton)findViewById(R.id.ib_hide_list);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        view_pager.setCurrentItem(0);
        view_pager.setOffscreenPageLimit(CACHE_SIZE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        unzipFileAndInitAdapters();

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(photobookRecyclerAdapter != null) {
                    photobookRecyclerAdapter.selectedPosition = position;
                    photobookRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        checkDirectory();
        finish();
    }

    public void checkDirectory() {
        File dir = new File(AppConstans.APP_PATH_PHOTOBOOK);
        if (!dir.isDirectory()) {
            if (dir.mkdirs()) {

            }
        } else {
            //DigitalBinder폴더에 사진이 존재하면 모두 삭제
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equals("temp") || files[i].getName().equals("data"))
                    continue;
                if (!files[i].isDirectory()) {
                    if (files[i].delete()) {
                        Log.d(TAG, "파일 삭제 성공: " + Uri.fromFile(files[i]));
                    } else {
                        Log.d(TAG, "파일 삭제 실패: " + files[i].getAbsolutePath());
                    }
                }
            }
            MediaStorageHelper.deleteAll(this, MediaStorageHelper.WHERE_PHOTOBOOK);
        }
    }

    public void initAdapters(){
        photobookPagerAdapter = new PhotobookPagerAdapter(this);
        photobookRecyclerAdapter = new PhotobookRecyclerAdapter(this);
        view_pager.setAdapter(photobookPagerAdapter);
        recycler_view.setAdapter(photobookRecyclerAdapter);
        recycler_view.setItemViewCacheSize(CACHE_SIZE);
    }

    public void unzipFileAndInitAdapters() {
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                Photobook photobook = MainActivity.dao.select(params[0]);

                //디렉터리 존재 여부 확인 및 사진파일 삭제.
                checkDirectory();

                //압축풀기 시작
                InputStream is = null;
                ZipInputStream zis = null;

                try {
                    is = new FileInputStream(AppConstans.APP_PATH_DATA + photobook.getFilename()+AppConstans.EXT_DAT);
                    zis = new ZipInputStream(new BufferedInputStream(is));
                    ZipEntry entry;
                    while ((entry = zis.getNextEntry()) != null) {
                        FileOutputStream fos = new FileOutputStream(AppConstans.APP_PATH_PHOTOBOOK + entry.getName());
                        Bitmap bitmap = BitmapFactory.decodeStream(zis);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.close();
                        zis.closeEntry();

                        File file = new File(AppConstans.APP_PATH_PHOTOBOOK + entry.getName());
                        PhotobookActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (zis != null) {
                        try {
                            zis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                while(true){
                    list = MediaStorageHelper.getImageFiles(PhotobookActivity.this, MediaStorageHelper.WHERE_PHOTOBOOK, MediaStorageHelper.ASC);
                    File[] files = new File(AppConstans.APP_PATH_PHOTOBOOK).listFiles();
                    if(list.size() == files.length)
                        break;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                initAdapters();
                super.onPostExecute(aVoid);
            }
        }.execute(photobook_id);
    }

    public void btnClick(View view){
        switch (view.getId()){
            case R.id.ib_show_list:
                layout_bottom.setVisibility(View.VISIBLE);
                ib_show_list.setVisibility(View.GONE);
                break;
            case R.id.ib_hide_list:
                layout_bottom.setVisibility(View.GONE);
                ib_show_list.setVisibility(View.VISIBLE);
                break;
            case R.id.ib_back:
                onBackPressed();
                break;
        }
    }
}

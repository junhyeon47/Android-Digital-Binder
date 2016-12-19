package team.code.effect.digitalbinder.photobook;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.ImageFile;

public class PhotobookActivity extends AppCompatActivity {
    String TAG;
    int photobook_id;
    ArrayList<ImageFile> list = new ArrayList<>();
    ViewPager view_pager;
    PhotobookPagerAdapter photobookPagerAdapter;

    RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photobook);
        TAG = this.getClass().getName();

        photobook_id = getIntent().getIntExtra("photobook_id", 0);

        view_pager = (ViewPager)findViewById(R.id.view_pager);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        photobookPagerAdapter = new PhotobookPagerAdapter(this);
        view_pager.setAdapter(photobookPagerAdapter);

        new UnzipAsync(this).execute(photobook_id);
    }

    public void checkDirectory(){
        File dir = new File(AppConstans.APP_PATH);
        if(!dir.isDirectory()) {
            if(dir.mkdirs()){

            }
        }else{
            //DigitalBinder폴더에 사진이 존재하면 모두 삭제
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++){
                if(files[i].getName().equals("temp") || files[i].getName().equals("data"))
                    continue;
                if(!files[i].isDirectory()){
                    if(files[i].delete()){
                        Log.d(TAG, "파일 삭제 성공: "+ Uri.fromFile(files[i]));
                        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(files[i])));
                    }else{
                        Log.d(TAG, "파일 삭제 실패: "+files[i].getAbsolutePath());
                    }
                }
            }
            String where = "bucket_display_name='DigitalBinder'";
            getContentResolver().delete(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    where,
                    null
            );
        }
    }

    @Override
    public void onBackPressed() {
        checkDirectory();
        finish();
    }
}

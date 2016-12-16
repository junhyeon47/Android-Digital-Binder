package team.code.effect.digitalbinder.photobook;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AppConstans;

public class PhotobookActivity extends AppCompatActivity {
    String TAG;
    int photobook_id;
    ViewPager view_pager;
    RecyclerView recycler_view;
    PhotobookPagerAdapter photobookPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photobook);
        TAG = this.getClass().getName();
        photobook_id = getIntent().getIntExtra("photobook_id", 0);


        view_pager = (ViewPager)findViewById(R.id.view_pager);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        photobookPagerAdapter = new PhotobookPagerAdapter(getApplicationContext());
        setList();
        //view_pager.setAdapter(photobookPagerAdapter);
    }

    public void setList(){
        if(photobook_id != 0)
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
                    files[i].delete();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        checkDirectory();
        finish();
    }
}

package team.code.effect.digitalbinder.photobook;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.view.View;

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

    ImageButton btn_upward, btn_downward;
    RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photobook);
        TAG = this.getClass().getName();

        photobook_id = getIntent().getIntExtra("photobook_id", 0);

        view_pager = (ViewPager)findViewById(R.id.view_pager);
        btn_upward = (ImageButton)findViewById(R.id.btn_upward);
        btn_downward = (ImageButton)findViewById(R.id.btn_downward);
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
                    files[i].delete();
                }
            }
        }
    }

    public void btnClick(View view){
        switch (view.getId()){
            case R.id.btn_upward:
                recycler_view.setVisibility(View.VISIBLE);
                btn_upward.setVisibility(View.GONE);
                btn_downward.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_downward:
                recycler_view.setVisibility(View.GONE);
                btn_downward.setVisibility(View.GONE);
                btn_upward.setVisibility(View.VISIBLE);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        checkDirectory();
        finish();
    }
}

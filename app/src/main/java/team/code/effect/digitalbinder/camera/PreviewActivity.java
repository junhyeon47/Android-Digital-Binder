package team.code.effect.digitalbinder.camera;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.ImageFile;
import team.code.effect.digitalbinder.common.MediaStorageHelper;
import team.code.effect.digitalbinder.main.MainActivity;
import team.code.effect.digitalbinder.main.SplashActivity;

public class PreviewActivity extends AppCompatActivity {
    private static int CACHE_SIZE = 100;
    String TAG;
    ArrayList<StoreTempFileAsync> listAsync;
    ArrayList<ImageFile> list;
    File[] files;

    ViewPager view_pager;
    PreviewPagerAdapter previewPagerAdapter;
    LinearLayout layout_bottom;
    ImageButton ib_back, ib_show_list, ib_hide_list;
    RecyclerView recycler_view;
    PreviewRecyclerAdapter previewRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        TAG = this.getClass().getName();
        listAsync = getIntent().getParcelableArrayListExtra("listAsync");

        files = new File(AppConstans.APP_PATH_TEMP).listFiles();

        view_pager = (ViewPager)findViewById(R.id.view_pager);
        layout_bottom = (LinearLayout)findViewById(R.id.layout_bottom);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_show_list = (ImageButton)findViewById(R.id.ib_show_list);
        ib_hide_list = (ImageButton)findViewById(R.id.ib_hide_list);
        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemViewCacheSize(CACHE_SIZE);
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                while (true){
                    boolean isComplete = true;
                    for(int i=0; i<CameraActivity.listAsync.size(); ++i){
                        StoreTempFileAsync async = CameraActivity.listAsync.get(i);
                        Log.d(TAG, "AsyncTask "+i+": Status: "+async.getStatus());
                        if(async.getStatus() != Status.FINISHED){
                            isComplete = false;
                            break;
                        }
                    }
                    if(isComplete)
                        break;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list = MediaStorageHelper.getImageFiles(PreviewActivity.this, MediaStorageHelper.WHERE_TEMP, MediaStorageHelper.ASC);

                        previewPagerAdapter = new PreviewPagerAdapter(PreviewActivity.this);
                        view_pager.setAdapter(previewPagerAdapter);

                        previewRecyclerAdapter = new PreviewRecyclerAdapter(PreviewActivity.this);
                        recycler_view.setAdapter(previewRecyclerAdapter);

                        view_pager.setCurrentItem(0);
                        view_pager.setOffscreenPageLimit(CACHE_SIZE);
                    }
                }, 1000);
            }
        }.execute();

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(previewRecyclerAdapter != null) {
                    previewRecyclerAdapter.selectedPositon = position;
                    previewRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
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

package team.code.effect.digitalbinder.camera;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.ImageFile;
import team.code.effect.digitalbinder.common.MediaStorageHelper;
import team.code.effect.digitalbinder.main.MainActivity;
import team.code.effect.digitalbinder.main.SplashActivity;

public class PreviewActivity extends AppCompatActivity {
    String TAG;
    ArrayList<StoreTempFileAsync> listAsync;
    ArrayList<ImageFile> list;
    File[] files;

    ViewPager view_pager;
    PreviewPagerAdapter previewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        TAG = this.getClass().getName();
        listAsync = getIntent().getParcelableArrayListExtra("listAsync");

        files = new File(AppConstans.APP_PATH_TEMP).listFiles();

        view_pager = (ViewPager)findViewById(R.id.view_pager);
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
                        list = MediaStorageHelper.getImageFiles(PreviewActivity.this, MediaStorageHelper.WHERE, MediaStorageHelper.ASC);
                        previewPagerAdapter = new PreviewPagerAdapter(PreviewActivity.this);
                        view_pager.setAdapter(previewPagerAdapter);
                    }
                }, 1000);
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

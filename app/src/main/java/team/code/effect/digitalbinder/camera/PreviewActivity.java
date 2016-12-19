package team.code.effect.digitalbinder.camera;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

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

    RecyclerView recycler_view;
    PreviewRecyclerAdapter previewRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        TAG = this.getClass().getName();
        listAsync = getIntent().getParcelableArrayListExtra("listAsync");

        files = new File(AppConstans.APP_PATH_TEMP).listFiles();

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemViewCacheSize(CACHE_SIZE);

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
                        previewRecyclerAdapter = new PreviewRecyclerAdapter(PreviewActivity.this);
                        recycler_view.setAdapter(previewRecyclerAdapter);
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

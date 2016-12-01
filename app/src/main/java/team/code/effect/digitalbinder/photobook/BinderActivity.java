package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.ZipCode;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Photobook Item 1개를 확인할 화면
 */

public class BinderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ImageView img;
    HorizontalListView listView;
    PhotoItemListAdapter adapter;
    ZipCode zipCode;
    Photobook photobook;
    String TAG;
    int selectedIndex; // 선택한 이미지
    PhotoViewAttacher photoViewAttacher;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        Intent intent=getIntent();
        photobook=intent.getParcelableExtra("photobook");
        listView = (HorizontalListView)findViewById(R.id.listView);
        img=(ImageView)findViewById(R.id.img);
        photoViewAttacher = new PhotoViewAttacher(img);
        photoViewAttacher.setMaximumScale(2048);
        TAG=getClass().getName();
        init(photobook);
    }
    /**/
    public void init(Photobook photobook){
        Log.d(TAG,"압축해제 시작");
        zipCode=new ZipCode();
        File dir= new File(AppConstans.APP_PATH);
        File file = new File(dir+"/"+photobook.getFilename());
        Log.d(TAG,"압축파일 선택"+file.getAbsolutePath());
        try {
            ArrayList fileList=(ArrayList) zipCode.unzip(file.getAbsolutePath(),dir.getAbsolutePath(),true);
            Log.d(TAG,"압축파일 목록 생성"+fileList.size());
            adapter = new PhotoItemListAdapter(this,fileList);
            img.setImageBitmap(adapter.list.get(0));
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            Log.d(TAG,"어뎁터 연결");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chageImg(Bitmap bitmap){
        img.setImageBitmap(bitmap);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG,i+"번째 item을 누름");

        PhotoItem item = (PhotoItem)view;
        /*선택한 Item 색칠하기!*/
        selectedIndex=i;
        for(int j=0; j<adapter.viewList.size();j++){
            View view1=adapter.viewList.get(j);
            view1.setSelected(false);
        }
        item.setSelected(true);
        chageImg(item.bitmap);
        photoViewAttacher.update();
    }

}

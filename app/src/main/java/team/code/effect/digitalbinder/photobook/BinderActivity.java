package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

/**
 * Created by huho on 2016-11-26.
 */

public class BinderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    Context context;
    ImageView img;
    HorizontalListView listView;
    PhotoItemListAdapter adapter;
    ZipCode zipCode;
    Photobook photobook;
    String TAG;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        Intent intent=getIntent();
        photobook=intent.getParcelableExtra("photobook");
        listView = (HorizontalListView)findViewById(R.id.listView);
        img=(ImageView)findViewById(R.id.img);
        TAG=getClass().getName();
        init(photobook);
    }

    public void init(Photobook photobook){
        Log.d(TAG,"압축해제 시작");
        zipCode=new ZipCode();
        File dir= new File(Environment.getExternalStorageDirectory(),"DigtalBinder");
        File file = new File(dir+"/"+photobook.getFilename());
        Log.d(TAG,"압축파일 선택"+file.getAbsolutePath());
        try {
            ArrayList<File> fileList=(ArrayList<File>) zipCode.unzip(file.getAbsolutePath(),dir.getAbsolutePath(),true);
            Log.d(TAG,"압축파일 목록 생성"+fileList.size());
            adapter = new PhotoItemListAdapter(this,fileList);
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
        chageImg(item.bitmap);
    }

}

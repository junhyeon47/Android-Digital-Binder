package team.code.effect.digitalbinder.explorer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

public class ExplorerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    String TAG;
    static final int REQUEST_STORAGE_PERMISSION=1;
    ListView listView;
    ExplorerTitleAdapter explorerTitleAdapter;
    ArrayList<Explorer> cameraFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=this.getClass().getName();
        setContentView(R.layout.activity_explorer);

        listView=(ListView)findViewById(R.id.ex_titleList);

        init();
        connectStorage();

        explorerTitleAdapter=new ExplorerTitleAdapter(this, cameraFileList);
        listView.setAdapter(explorerTitleAdapter);
        listView.setOnItemClickListener(this);

    }

    //저장소 접근권한
    public void init(){
        int explorerPermission=ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(explorerPermission== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }
    }

    //이미지 저장소 접근
    public void connectStorage(){
        File dcimCamera=new File(Environment.DIRECTORY_DCIM, "Camera");
        File storageCamera=new File(Environment.getExternalStorageDirectory(), dcimCamera.getAbsolutePath());
        Log.d(TAG, "실경로 "+storageCamera);
        File[] cameraFiles=storageCamera.listFiles();

        Explorer explorer=new Explorer();

        String[] cameraTitle=storageCamera.toString().split("/");

        explorer.setTitle(cameraTitle[cameraTitle.length-1]);

        //Bitmap bitmap= BitmapFactory.decodeFile(storageCamera+"/"+cameraFiles[0].getName());
        //img.setImageBitmap(bitmap);

        Log.d(TAG, "첫번째 파일"+cameraFiles[0].getName());

        cameraFileList=new ArrayList<Explorer>();

        cameraFileList.add(explorer);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this, ExplorerItemList.class);
        Toast.makeText(this, "작동하나요?", Toast.LENGTH_SHORT).show();


    }

}

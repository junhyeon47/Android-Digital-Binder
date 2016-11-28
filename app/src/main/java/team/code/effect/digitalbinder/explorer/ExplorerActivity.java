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
import java.util.List;

import team.code.effect.digitalbinder.R;

public class ExplorerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    String TAG;
    static final int REQUEST_STORAGE_PERMISSION=1;
    ListView listView;
    ExplorerTitleAdapter explorerTitleAdapter;
    ArrayList<Explorer> explorerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=this.getClass().getName();
        setContentView(R.layout.activity_explorer);

        listView=(ListView)findViewById(R.id.ex_titleList);

        init();
        connectStorage();

        explorerTitleAdapter=new ExplorerTitleAdapter(this, explorerList);
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
    public List connectStorage(){
        File dcimFacebook=new File(Environment.DIRECTORY_DCIM, "Facebook");
        File storageFacebook=new File(Environment.getExternalStorageDirectory(), dcimFacebook.getAbsolutePath());

        File dcimCamera=new File(Environment.DIRECTORY_DCIM, "Camera");
        File storageCamera=new File(Environment.getExternalStorageDirectory(), dcimCamera.getAbsolutePath());

        Log.d(TAG, "실경로 "+storageCamera);

        File[] cameraFiles=storageCamera.listFiles();


        String[] facebookTitle=storageFacebook.toString().split("/");
        String[] cameraTitle=storageCamera.toString().split("/");

        String[] titleList=new String[]{
           facebookTitle[facebookTitle.length-1]
                , cameraTitle[cameraTitle.length-1]
        };

        String[] filelist=new String[]{
            storageFacebook.toString(), storageCamera.toString()
        };

        Log.d(TAG, "실경로는???"+filelist[0] );

        explorerList=new ArrayList<Explorer>();

        for (int i=0; i<titleList.length;i++){
            Explorer explorer=new Explorer();
            explorer.setTitle(titleList[i]);
            explorer.setFilename(filelist[i]);
            explorerList.add(explorer);
        }

        //Bitmap bitmap= BitmapFactory.decodeFile(storageCamera+"/"+cameraFiles[0].getName());
        //img.setImageBitmap(bitmap);

        Log.d(TAG, "첫번째 파일"+cameraFiles[0].getName());

        return explorerList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ExplorerTitleItem explorerItem=(ExplorerTitleItem) view;
        Explorer explorer=explorerItem.explorer;
        Intent intent=new Intent(this, ExplorerItemListActivity.class);
        Toast.makeText(this, "작동하나요?", Toast.LENGTH_SHORT).show();

        intent.putExtra("data", explorer);

        //intent.putParcelableArrayListExtra("title", );
        startActivity(intent);
    }

}

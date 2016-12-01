package team.code.effect.digitalbinder.explorer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import team.code.effect.digitalbinder.R;

public class ExplorerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String TAG;
    static final int REQUEST_STORAGE_PERMISSION = 1;
    ListView listView;
    ExplorerTitleAdapter explorerTitleAdapter;
    ArrayList<Explorer> explorerList;


    ArrayList<String> dirList;
    ArrayList<Explorer> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
        setContentView(R.layout.activity_explorer);

        listView = (ListView) findViewById(R.id.ex_titleList);

        init();
        //connectStorage();
        connectImgFolder();
        titleList=new ArrayList<>();
        for (int i=0; i<dirList.size();i++){
            Explorer explorer = new Explorer();
            explorer.setTitle(dirList.get(i));
            titleList.add(explorer);
        }
        /*ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        arrayAdapter.addAll(dirList);*/


        explorerTitleAdapter=new ExplorerTitleAdapter(this, titleList);

        //getDir(root);

        listView.setAdapter(explorerTitleAdapter);
        listView.setOnItemClickListener(this);


    }

    //저장소 접근권한
    public void init() {
        int explorerPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (explorerPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }
    }



    //이미지 저장소 접근
    public List connectStorage() {


        Uri uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor=getContentResolver().query(
            uri
                , new String[] {MediaStore.Images.ImageColumns.DATA}, null, null, null
        );





        File dcimFacebook = new File(Environment.DIRECTORY_DCIM, "Facebook");
        File storageFacebook = new File(Environment.getExternalStorageDirectory(), dcimFacebook.getAbsolutePath());

        File dcimCamera = new File(Environment.DIRECTORY_DCIM, "Camera");
        File storageCamera = new File(Environment.getExternalStorageDirectory(), dcimCamera.getAbsolutePath());

        File dir = new File(Environment.getExternalStorageDirectory().toString());
        File[] fileTitle = dir.listFiles();

        String[] fileTitles = dir.toString().split("/");

        File[] cameraFiles = storageCamera.listFiles();



        String[] facebookTitle = storageFacebook.toString().split("/");
        String[] cameraTitle = storageCamera.toString().split("/");

        String[] titleList = new String[]{
                facebookTitle[facebookTitle.length - 1]
                , cameraTitle[cameraTitle.length - 1]
        };

        String[] filelist = new String[]{
                storageFacebook.toString(), storageCamera.toString()
        };

        explorerList = new ArrayList<Explorer>();


        for (int i = 0; i < fileTitle.length; i++) {
            Explorer explorer = new Explorer();
            explorer.setTitle(fileTitle[i].getName());
            explorer.setFilename(fileTitle[i].getPath());
            explorerList.add(explorer);
        }

        return explorerList;
    }


    public void connectImgFolder(){
        Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection={ MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATA};
        Cursor cursor=getContentResolver().query(uri, projection, null,null,null);
        explorerList = new ArrayList<Explorer>();

        int count=cursor.getCount();
        dirList=new ArrayList<String>();
        Log.d(TAG, "1111"+cursor.getColumnName(0));
        cursor.moveToFirst();
        String dir=null;
        for(int i=0;i<count;i++) {
            boolean flag=true;
            for(String column : cursor.getColumnNames()) {
                if(column.equals("bucket_display_name")) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(column));
                    dir=name;
                    if (dirList.size() == 0) {
                        dirList.add(name);
                    } else {
                        for (int j = 0; j < dirList.size(); j++) {
                            if (name.equals(dirList.get(j))) {
                                flag = false;
                            }
                        }
                        if (flag) dirList.add(name);
                    }
                }else if(column.equals("_data")){
                    String filePath = cursor.getString(cursor.getColumnIndexOrThrow(column));

                    Explorer explorer = new Explorer();
                    explorer.setTitle(dir);
                    explorer.setFilename(filePath);
                    explorerList.add(explorer);

                }
            }
            cursor.moveToNext();

        }

    }


    public void setItemList(String titleItem){
        ArrayList<Explorer> itemList=new ArrayList<Explorer>();
        for (int i=0; i<explorerList.size();i++){
            Explorer explorer=explorerList.get(i);
            if (explorer.getTitle().equals(titleItem)){
                itemList.add(explorer);
            }
        }
        Intent intent=new Intent(this, ExplorerItemListActivity.class);
        intent.putParcelableArrayListExtra("data", itemList);
        startActivity(intent);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        ExplorerTitleItem explorerTitleItem=(ExplorerTitleItem)view;
        String titleItem=explorerTitleItem.explorer.getTitle();
        Toast.makeText(this, "비교분석 "+explorerTitleItem.explorer.getTitle(), Toast.LENGTH_SHORT).show();
        setItemList(titleItem);
    }

}

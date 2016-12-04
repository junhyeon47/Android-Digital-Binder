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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import team.code.effect.digitalbinder.R;

public class ExplorerActivity extends AppCompatActivity {
    String TAG;
    static final int REQUEST_STORAGE_PERMISSION = 1;
    ArrayList<String> dirList;
    HashMap<String, String> hashMap;
    RecyclerView recyclerView;
    FolderRecyclerAdapter folderRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
        setContentView(R.layout.activity_explorer);

        recyclerView = (RecyclerView) findViewById(R.id.ex_titleList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        folderRecyclerAdapter=new FolderRecyclerAdapter(this);
        recyclerView.setAdapter(folderRecyclerAdapter);
        connectImgFolder();
        initList();
    }


    public void connectImgFolder(){
        Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection={ MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATA};
        Cursor cursor=getContentResolver().query(uri, projection, null,null,null);

        int count=cursor.getCount();
        dirList=new ArrayList<String>();
        hashMap = new HashMap<>();
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

                    String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
                    Log.d(TAG, dirPath);
                    Explorer explorer = new Explorer();
                    explorer.setTitle(dir);
                    explorer.setFilename(filePath);
                    hashMap.put(dirPath, dir);
                }
            }
            cursor.moveToNext();
        }
    }
    public void initList(){
        Iterator iter = hashMap.entrySet().iterator();
        Explorer explorer = null;
        while(iter.hasNext()){
            Map.Entry pair = (Map.Entry) iter.next();
            explorer = new Explorer();
            explorer.setFilename(pair.getKey().toString());
            explorer.setTitle(pair.getValue().toString());
            folderRecyclerAdapter.list.add(explorer);
            iter.remove();
        }
    }
}

package team.code.effect.digitalbinder.photobook;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.BinderDAO;

/**
 * Created by student on 2016-11-28.
 */

public class PhotobookAddActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //private static String DB_PATH;
    //private static String DB_NAME="digitalBinder.sqlite";
    PhotobookAddActivity activity;
    ListView listView;
    PhotobookListAdapter photobookListAdapter;
    ArrayList<Photobook> list;
    SQLiteDatabase db;
    BinderDAO photobookDAO;
    String TAG;
    File dir;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photobook);
        activity =this;
        TAG=getClass().getName();
        //DB_PATH=getDBPath();
        dir = new File(AppConstans.APP_PATH);
        db = SQLiteDatabase.openDatabase(AppConstans.DB_PATH,null,SQLiteDatabase.OPEN_READWRITE);
        photobookDAO =new BinderDAO(db);
        init();
        Log.d(TAG,"추가할수있는 파일 길이는"+list.size());
        listView = (ListView) findViewById(R.id.listView);
        photobookListAdapter = new PhotobookListAdapter(this, list);
        listView.setAdapter(photobookListAdapter);
        listView.setOnItemClickListener(this);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Photobook 추가하기");
    }

    public String getDBPath(){
        String pack=getClass().getPackage().toString();
        Log.d(TAG,pack);
        String path="/data/data/team.code.effect.digitalbinder/databases/";
        return path;
    }

    public void init() {
        File[] files = dir.listFiles();
        /* zip 파일만 검색하기!!*/
        if (files.length > 0) {
            ArrayList<File> fileList = new ArrayList<File>();
            for (int i = 0; i < files.length; i++) {
                Log.d(TAG,"data 길이"+files[i].getName());
               String[] data = files[i].getName().split("\\.");
               Log.d(TAG,"data 길이"+data.length);
                String ext = data[(data.length-1)];
                if (ext.equals("zip")) {
                    fileList.add(files[i]);
                }
            }
            if (fileList.size() > 0) {
                list = new ArrayList<Photobook>();
                for (int a = 0; a < fileList.size(); a++) {
                    File file = fileList.get(a);
                    Log.d(TAG,"파일 이름"+file.getName());
                    /*확장자를 뺀 파일명 구해오기*/
                    int lastIndex = file.getName().lastIndexOf(".");
                    String title = file.getName().substring(0, lastIndex);

                    Photobook photobook = new Photobook();
                    photobook.setFilename(file.getName());
                    photobook.setTitle(title);
                    list.add(photobook);
                }
            }else {
                Toast.makeText(activity, "추가할수있는 파일이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /*아이템 터치시 다이얼 로그 뜨고 DAO로 입력 요청하기*/
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PhotobookCheckboxItem item = (PhotobookCheckboxItem) view;
        Photobook photobook = item.photobook;

        /*Text 입력 가능한 다이얼 로그 생성*/
        Log.d(TAG,"다이얼 띄우기!!");
        txtDialog(photobook);
    }

    public void txtDialog(final Photobook photobook) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("포토북 추가하기");
        alert.setMessage("포토북 제목을 입력해주세요");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setHint(photobook.getTitle());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                /*입력을 했을시*/
                if (!value.equals("")) {
                    photobook.setTitle(value);
                }
                photobookDAO.insert(photobook);
                finish();
            }
        });


        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();
        Log.d(TAG,"다이얼 보이니?");
    }

}

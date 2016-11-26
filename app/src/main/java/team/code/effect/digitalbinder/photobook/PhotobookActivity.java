package team.code.effect.digitalbinder.photobook;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import team.code.effect.digitalbinder.R;

public class PhotobookActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView;
    PhotobookDAO photobookDAO;
    PhotobookListAdapter photobookListAdapter;
    PhotobookOpenHelper helper;
    SQLiteDatabase db;
    List list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photobook);

        listView=(ListView)findViewById(R.id.listView);
        init();
    }
    //db 연결 및 Photobook목록 불러와 listView에 뿌리기
    public void init(){
        sqliteDB();
        photobookDAO = new PhotobookDAO(db);
        list=photobookDAO.selectAll();
        photobookListAdapter = new PhotobookListAdapter(this,list);
        listView.setAdapter(photobookListAdapter);
    }
    //SQLite 생성(SQLiteOpenhelper) 및 SQLitedatabase 생성
    public void sqliteDB(){
        helper = new PhotobookOpenHelper(this,"digitalBinder.sqlite",null,1);
        db = helper.getWritableDatabase();
    }
    //Photobook Item 선택시 Detail화면으로 이동 시킬것!!!!(Activity 이동)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PhotobookItem item = (PhotobookItem) view;
        Intent intent = new Intent(this,BinderActivity.class);//이동할 Activity
        intent.putExtra("photobook",item.photobook);
        startActivity(intent);
    }
}

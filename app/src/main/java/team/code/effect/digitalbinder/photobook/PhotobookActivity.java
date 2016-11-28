package team.code.effect.digitalbinder.photobook;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import team.code.effect.digitalbinder.R;

public class PhotobookActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,Toolbar.OnMenuItemClickListener{
    Toolbar toolbar;
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

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    /*Toolbar menu 생성*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photobook,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.insert: ; break;
            case R.id.delete: ; break;
            case R.id.share: ; break;

        }
        return true;
    }
    /*등록 메서드*/
    public void regist(){
        Intent intent = new Intent(this,PhotobookAddActivity.class);
        startActivity(intent);

    }
    /*삭제 메서드*/
    public void remove(){

    }
    /*공유 메서드*/
    public void share(){

    }
}

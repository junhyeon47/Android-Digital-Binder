package team.code.effect.digitalbinder.photobook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.PhotobookDAO;
import team.code.effect.digitalbinder.main.BluetoothActivity;
import team.code.effect.digitalbinder.main.MainActivity;

public class PhotobookActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    String TAG;
    private Menu menu;
    Toolbar toolbar;
    ListView listView;
    PhotobookDAO photobookDAO = MainActivity.dao;;
    PhotobookListAdapter photobookListAdapter;
    List list;
    Boolean mode = false;
    PhotobookActivity photobookActivity;
    RecyclerView recycler_view;
    PhotobookRecyclerAdapter photobookRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photobook);
        TAG = getClass().getName();
        Log.d(TAG, "액티비티 생성 현액티비티주소" + this);
        photobookActivity=this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("포토북");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바에 뒤로가기 버튼 추가.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp); //뒤로가기 버튼 아이콘 변경
        toolbar.setOnMenuItemClickListener(this);
        listView = (ListView) findViewById(R.id.listView);


        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        photobookRecyclerAdapter = new PhotobookRecyclerAdapter(this);
        recycler_view.setAdapter(photobookRecyclerAdapter);
        //init();
    }

    //db 연결 및 Photobook목록 불러와 listView에 뿌리기
//    public void init() {
//        checkFolder();
//        photobookDAO = MainActivity.dao;
//        list = photobookDAO.selectAll();
//       Log.d(TAG,"리스트1"+list.size());
//        photobookListAdapter = new PhotobookListAdapter(this, list);
//        Log.d(TAG,"리스트1"+photobookListAdapter.getCount());
//        listView.setAdapter(photobookListAdapter);
//        listView.setOnItemClickListener(this);
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //       Log.d(TAG,"리스타트 현액티비티주소"+this);
//        if (mode) {
//            showCheckbox(mode);
//        } else {
//            mode = !mode;
//            showCheckbox(mode);
//        }
//        list = photobookDAO.selectAll();
//        //       Log.d(TAG,"리스트2"+list.size());
//        photobookListAdapter.setList(list);
//        photobookListAdapter.notifyDataSetChanged();
        //   Log.d(TAG,"리스트3"+photobookListAdapter.getCount());
    }

    // App 폴더 있는지 확인
    public void checkFolder() {
        File dir = new File(AppConstans.APP_PATH);
        if (dir.exists() == false) {
            if (dir.mkdirs()) {
                Log.d(TAG, "폴더 생성");
            }
        } else {
            Log.d(TAG, "폴더가 있어요");
        }
    }

    //Photobook Item 선택시 Detail화면으로 이동 시킬것!!!!(Activity 이동)
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if (mode) {
//            itemCheck(view);
//        } else {
//            showBinder(view);
//        }
//    }

    /*(delete mode off일때)binderActivity로 연결하기*/
//    public void showBinder(View view) {
//        PhotobookCheckboxItem item = (PhotobookCheckboxItem) view;
//        Intent intent = new Intent(this, BinderActivity.class);//이동할 Activity
//        intent.putExtra("photobook", item.photobook);
//        startActivity(intent);
//    }

    /*(delete mode On일때)item 선택시 check박스 설정되게 하기*/
    public void itemCheck(View view) {
        PhotobookCheckboxItem item = (PhotobookCheckboxItem) view;
        item.checkBox.setChecked(!item.checkBox.isChecked());
    }

    /*Toolbar menu 생성*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_photobook, menu);
        hideMenu(R.id.cancel);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                Toast.makeText(this, "Photobook을 선택해주세요", Toast.LENGTH_SHORT).show();
                regist();
                break;
            case R.id.delete:
                //remove();
                showCheckbox();
                break;
            case R.id.share:
                Log.d(TAG, "공유하기");
                share();
                break;
            case R.id.cancel:
                //showCheckbox(mode);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    /*메뉴 리스트 변경*/
    public void showMenu(int id) {
        MenuItem menuItem = menu.findItem(id);
        menuItem.setVisible(true);
    }

    public void hideMenu(int id) {
        MenuItem menuItem = menu.findItem(id);
        menuItem.setVisible(false);
    }

    public void changePhotobookMenu(Boolean flag) {
        if (flag) {
            showMenu(R.id.cancel);
            hideMenu(R.id.insert);
            showMenu(R.id.delete);
            hideMenu(R.id.share);
        } else {
            hideMenu(R.id.cancel);
            showMenu(R.id.insert);
            showMenu(R.id.delete);
            showMenu(R.id.share);
        }
    }

    /*등록 메서드*/
    public void regist() {
        Intent intent = new Intent(this, PhotobookAddActivity.class);
        startActivity(intent);
    }

    /*삭제 메서드*/
    public void remove() {
//        Toast.makeText(this, "mode" + mode, Toast.LENGTH_SHORT).show();
//        if (!mode) {
//            showCheckbox(mode);
//        } else {
//            /*삭제 동작*/
//            mode = !mode;
//            deleteItemList(photobookListAdapter.itemList);
//        }
    }

    /*삭제 item 선택 checkbox 보이게 하기!*/
    public void showCheckbox() {
//        this.mode = !mode;
//        List list = photobookListAdapter.itemList;
//        for (int i = 0; i < list.size(); i++) {
//            PhotobookCheckboxItem item = (PhotobookCheckboxItem) list.get(i);
//            item.checkBox.setChecked(false);
//        }
//        photobookListAdapter.flag = this.mode;
//        photobookListAdapter.notifyDataSetChanged();
//        photobookListAdapter.itemList.removeAll(photobookListAdapter.itemList);
//        changePhotobookMenu(photobookListAdapter.flag);
        photobookRecyclerAdapter.isDeleteMemuClicked = !photobookRecyclerAdapter.isDeleteMemuClicked;
        photobookRecyclerAdapter.notifyDataSetChanged();
    }

    /*삭제 목록 만들기*/
    public void deleteItemList(List list) {
        Log.d(TAG, list.size() + "삭제 목록 길이!");
        for (int i = 0; i < list.size(); i++) {
            PhotobookCheckboxItem item = (PhotobookCheckboxItem) list.get(i);
            if (item.checkBox.isChecked()) {
                int photobook_id = item.photobook.getPhotobook_id();
                photobookDAO.delete(photobook_id);
                Log.d(TAG, item.photobook.getTitle() + "삭제");
            }
        }
        onRestart();
    }

    /*공유 메서드*/
    public void share() {
        setAlert("공유하기");
    }

    public void setAlert(String title) {
        final CharSequence[] items = {
                AppConstans.BLUETOOTH,
                AppConstans.NFC,
                AppConstans.GDRIVE,
                AppConstans.DROPBOX
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setIcon(R.drawable.share);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                switch (items[i].toString()) {
                    case AppConstans.BLUETOOTH:
                        Intent intent = new Intent(photobookActivity, BluetoothActivity.class);
                        startActivity(intent);
                        break;
                    case AppConstans.NFC:
                        ;
                        break;
                    case AppConstans.GDRIVE:
                        ;
                        break;
                    case AppConstans.DROPBOX:
                        ;
                        break;

                }
                Toast.makeText(getApplicationContext(),
                        items[i], Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    /**/


}

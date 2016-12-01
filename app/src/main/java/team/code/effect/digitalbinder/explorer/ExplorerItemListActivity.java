package team.code.effect.digitalbinder.explorer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-11-28.
 */

public class ExplorerItemListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    String TAG;
    GridView ex_gridView;
    ArrayList<Explorer> fileList;
    Explorer explorer;
    ExplorerItem explorerItem;
    ExplorerItemAdapter explorerItemAdapter;
    int count;
    boolean flag=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploreritemlist);
        TAG=this.getClass().getName();
        Intent intent = getIntent();
        fileList = intent.getParcelableArrayListExtra("data");

        Toolbar toolbar = (Toolbar) findViewById(R.id.ex_toolbar);
        setSupportActionBar(toolbar);

        ex_gridView = (GridView) findViewById(R.id.ex_gridView);

        explorerItemAdapter = new ExplorerItemAdapter(this, fileList);
        ex_gridView.setAdapter(explorerItemAdapter);
        ex_gridView.setOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_explorer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ex_select:
                for(int i=0; i<explorerItemAdapter.itemList.size();i++){
                    explorerItemAdapter.itemList.get(i).checkBox.setChecked(true);
                    count=explorerItemAdapter.itemList.get(i).getChildCount();
                }
                break;
            case R.id.make_book:
                if(count==0){
                    showMsg("안내", "한개 이상의 이미지를 선택하셔야 합니다.");
                }else{
                    showMsg("안내", "포토북을 만드시겠습니까?");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        explorerItem=(ExplorerItem) view;
        explorerItem.checkBox.setChecked(flag);
        if(flag==true){
            count++;
        }else{
            count--;
        }
        flag=!flag;
        Log.d(TAG, "count= "+count);

    }


    public void showMsg(String title, String message){
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle(title).setMessage(message).setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }


}

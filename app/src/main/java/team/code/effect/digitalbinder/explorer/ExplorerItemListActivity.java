package team.code.effect.digitalbinder.explorer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-11-28.
 */

public class ExplorerItemListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    GridView ex_gridView;
    Explorer explorer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploreritemlist);
        Intent intent = getIntent();
        explorer = intent.getParcelableExtra("data");

        Toolbar toolbar = (Toolbar) findViewById(R.id.ex_toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Explorer> list = new ArrayList<Explorer>();
        File dir = new File(explorer.getFilename());
        File[] photo = dir.listFiles();
        for (int i = 0; i < photo.length; i++) {
            File file = photo[i];
            Explorer explorer = new Explorer();
            explorer.setTitle(file.getName());
            explorer.setFilename(file.getAbsolutePath());
            list.add(explorer);
        }

        ex_gridView = (GridView) findViewById(R.id.ex_gridView);

        ExplorerItemAdapter explorerItemAdapter = new ExplorerItemAdapter(this, list);
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
                Toast.makeText(this, "전체선택 메뉴", Toast.LENGTH_SHORT).show();
                break;
            case R.id.make_book:
                showMsg("안내","포토북을 만드시겠습니까?");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "누름?", Toast.LENGTH_SHORT).show();
    }

    public void showMsg(String title, String message){
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle(title).setMessage(message).show();
    }


}

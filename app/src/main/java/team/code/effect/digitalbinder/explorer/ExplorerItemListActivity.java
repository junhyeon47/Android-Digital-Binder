package team.code.effect.digitalbinder.explorer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.lang.reflect.Array;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-11-28.
 */

public class ExplorerItemListActivity extends AppCompatActivity{

    String TAG;
    Explorer explorer;
    Intent intent;
    RecyclerView recyclerView;
    ImageRecyclerAdapter imageRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploreritemlist);
        TAG=this.getClass().getName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.ex_toolbar);
        setSupportActionBar(toolbar);

        recyclerView=(RecyclerView)findViewById(R.id.ex_photo);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(), 3);
        imageRecyclerAdapter=new ImageRecyclerAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(imageRecyclerAdapter);

        intent = getIntent();
        String path = intent.getStringExtra("data");

        Log.d(TAG, path);
        File dir = new File(path);
        File[] images = dir.listFiles();
        ArrayList<Explorer> abc=new ArrayList<Explorer>();
        imageRecyclerAdapter.list.removeAll(imageRecyclerAdapter.list);
        for(int i=0;i<images.length;++i){
            Log.d(TAG, "images 실경로 "+images[i].getAbsoluteFile() );
            Explorer explorer=new Explorer();
            explorer.setFilename(images[i].getAbsolutePath());
            Log.d(TAG, " explorer 실경로 "+ explorer.getFilename() );
            abc.add(explorer);
        }
        imageRecyclerAdapter.list=abc;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_explorer, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

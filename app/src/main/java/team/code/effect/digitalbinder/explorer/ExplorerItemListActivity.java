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

public class ExplorerItemListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    String TAG;
    Explorer explorer;
    Intent intent;

    GridView gridView;
    ExplorerItemAdapter explorerItemAdapter;

    RecyclerView recyclerView;
    ImageRecyclerAdapter imageRecyclerAdapter;

    boolean flag=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploreritemlist);
        TAG=this.getClass().getName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.ex_toolbar);
        setSupportActionBar(toolbar);

        gridView=(GridView)findViewById(R.id.ex_gridView);
        explorerItemAdapter=new ExplorerItemAdapter();

/*        recyclerView=(RecyclerView)findViewById(R.id.ex_recyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(), 3);
        imageRecyclerAdapter=new ImageRecyclerAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(imageRecyclerAdapter);*/

        intent = getIntent();
        String path = intent.getStringExtra("data");

        Log.d(TAG, path);
        File dir = new File(path);
        File[] images = dir.listFiles();
        ArrayList<Explorer> photoPath=new ArrayList<Explorer>();


//        imageRecyclerAdapter.list.removeAll(imageRecyclerAdapter.list);


        for(int i=0;i<images.length;++i){
            Log.d(TAG, "images 실경로 "+images[i].getAbsoluteFile() );
            Explorer explorer=new Explorer();
            explorer.setFilename(images[i].getAbsolutePath());
            Log.d(TAG, " explorer 실경로 "+ explorer.getFilename() );
            photoPath.add(explorer);
            explorerItemAdapter.phtoList.add(explorer);
        }

        gridView.setAdapter(explorerItemAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_explorer_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

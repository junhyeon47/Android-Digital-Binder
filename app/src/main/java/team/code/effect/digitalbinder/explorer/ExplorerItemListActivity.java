package team.code.effect.digitalbinder.explorer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-11-28.
 */

public class ExplorerItemListActivity extends AppCompatActivity {

    GridView ex_gridView;
    Explorer explorer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploreritemlist);
        Intent intent=getIntent();
        explorer=intent.getParcelableExtra("data");

        ArrayList<Explorer> list=new ArrayList<Explorer>();
        File dir=new File(explorer.getFilename());
        File[] photo=dir.listFiles();
        for(int i=0; i<photo.length;i++){
            File file=photo[i];
            Explorer explorer=new Explorer();
            explorer.setTitle(file.getName());
            explorer.setFilename(file.getAbsolutePath());
            list.add(explorer);
        }
        //File ex_dir=new File(explorer.getFilename());

        ex_gridView=(GridView)findViewById(R.id.ex_gridView);

        ExplorerItemAdapter explorerItemAdapter=new ExplorerItemAdapter(this, list);
        ex_gridView.setAdapter(explorerItemAdapter);
    }

}

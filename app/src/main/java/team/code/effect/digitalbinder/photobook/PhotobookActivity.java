package team.code.effect.digitalbinder.photobook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import team.code.effect.digitalbinder.R;

public class PhotobookActivity extends AppCompatActivity {

    ListView listView;
    PhotobookDAO photobookDAO;
    PhotobookListAdapter photobookListAdapter;
    List list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photobook);

        listView=(ListView)findViewById(R.id.listView);
        init();
    }

    public void init(){
        photobookDAO = new PhotobookDAO();
        list=photobookDAO.selectAll();
        photobookListAdapter = new PhotobookListAdapter(this,list);
        listView.setAdapter(photobookListAdapter);
    }
}

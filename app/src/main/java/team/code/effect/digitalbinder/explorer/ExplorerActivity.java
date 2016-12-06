package team.code.effect.digitalbinder.explorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import team.code.effect.digitalbinder.R;

public class ExplorerActivity extends AppCompatActivity {
    String TAG;
    List<ImageFile> listFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
        setContentView(R.layout.activity_explorer);
        new FileAsync(this).execute();
//        listFile = fetchAllImages();
//
//        for(int i=0; i<listFile.size(); ++i){
//
//        }
    }


}

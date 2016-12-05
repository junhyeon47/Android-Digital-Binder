package team.code.effect.digitalbinder.explorer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.ImageFile;

public class ExplorerActivity extends AppCompatActivity {
    String TAG;
    List<ImageFile> listFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
        setContentView(R.layout.activity_explorer);

//        listFile = fetchAllImages();
//
//        for(int i=0; i<listFile.size(); ++i){
//
//        }
    }


}

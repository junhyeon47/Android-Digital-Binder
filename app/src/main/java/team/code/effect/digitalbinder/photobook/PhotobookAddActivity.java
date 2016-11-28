package team.code.effect.digitalbinder.photobook;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-11-28.
 */

public class PhotobookAddActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    PhotobookListAdapter photobookListAdapter;
    ArrayList<Photobook> list;
    SQLiteDatabase db;
    PhotobookDAO photobookDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        db = SQLiteDatabase.openDatabase("digitalBinder.sqlite",null,1);
        photobookDAO =new PhotobookDAO(db);
        listView = (ListView) findViewById(R.id.listView);
        photobookListAdapter = new PhotobookListAdapter(this, list);
        listView.setAdapter(photobookListAdapter);

    }

    public void init() {
        File dir = new File(Environment.getExternalStorageDirectory(), "DigitalBinder");
        File[] files = dir.listFiles();
        /* zip 파일만 검색하기!!*/
        if (files.length > 0) {
            ArrayList<File> fileList = new ArrayList<File>();
            for (int i = 0; i < files.length; i++) {
                String[] data = files[i].getName().split(".");
                String ext = data[data.length - 1];
                if (ext.equals("zip")) {
                    fileList.add(files[i]);
                }
            }
            if (fileList.size() > 0) {
                list = new ArrayList<Photobook>();
                for (int a = 0; a < fileList.size(); a++) {
                    File file = fileList.get(a);
                    /*확장자를 뺀 파일명 구해오기*/
                    int lastIndex = file.getName().lastIndexOf(".");
                    String title = file.getName().substring(0, lastIndex - 1);

                    Photobook photobook = new Photobook();
                    photobook.setFilename(file.getName());
                    photobook.setTitle(title);
                    list.add(photobook);
                }
            }
        }

    }

    /*아이템 터치시 다이얼 로그 뜨고 DAO로 입력 요청하기*/
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PhotobookItem item = (PhotobookItem) view;
        Photobook photobook = item.photobook;

        /*Text 입력 가능한 다이얼 로그 생성*/
        txtDialog(photobook);
    }

    public void txtDialog(final Photobook photobook) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setHint(photobook.getTitle());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                /*입력을 했을시*/
                if (!value.equals("")) {
                    photobook.setTitle(value);
                }
                photobookDAO.insert(photobook);
            }
        });


        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();

    }

}

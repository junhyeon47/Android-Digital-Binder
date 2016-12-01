package team.code.effect.digitalbinder.main;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.BinderDAO;
import team.code.effect.digitalbinder.photobook.Photobook;
import team.code.effect.digitalbinder.photobook.PhotobookCheckboxItem;
import team.code.effect.digitalbinder.photobook.PhotobookListAdapter;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final int DISCOVER_DURATION = 300;
    public static final int REQUEST_BLU = 1;
    String TAG;
    ListView listView;
    PhotobookListAdapter listAdapter;
    List list;
    File file;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth); //activity_bluetooth안의 리소스들을 객체화
        TAG = getClass().getName();
        listView = (ListView) findViewById(R.id.listView);
        toolbar = (Toolbar)findViewById(R.id.toolbar); //activity_bluetooth의 id 값이 toolbar인 툴바를 연결.
        setSupportActionBar(toolbar); //toolbar를 이 화면의 앱바로 설정.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //앱바에 뒤로가기 버튼 추가.

        //리스트뷰 관련
//        list = MainActivity.dao.selectAll();
//        if(list.size()<=0){
//            Toast.makeText(this, "전송할 파일이 없습니다.", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        listAdapter = new PhotobookListAdapter(this, list);
//        listAdapter.flag = true;
//        listAdapter.notifyDataSetChanged();
//        listView.setAdapter(listAdapter);
//        listView.setOnItemClickListener(this);
    }

    public List getFileList() {
        List list = new ArrayList();
        File dir = new File(AppConstans.APP_PATH);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            Log.d(TAG, "파일 이름" + file.getName());
                    /*확장자를 뺀 파일명 구해오기*/
            int lastIndex = file.getName().lastIndexOf(".");
            String title = file.getName().substring(0, lastIndex);
            Photobook photobook = new Photobook();
            photobook.setFilename(file.getName());
            photobook.setTitle(title);
            list.add(photobook);
        }
        return list;
    }

    public void sendViaBluetooth(View v) {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(this, "이 기기는 블루투스를 지원하지 않습니다. ", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            enableBluetooth();
            Toast.makeText(this, "전송할 파일을 선택하여 주세요!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void enableBluetooth() {
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
        startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == DISCOVER_DURATION && requestCode == REQUEST_BLU) {
            //  for (int i = 0; i < fileList.size(); i++) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            //    File file = fileList.get(i);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            PackageManager pm = getPackageManager();
            List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);

            if (appsList.size() > 0) {
                String packageName = null;
                String className = null;
                boolean found = false;

                for (ResolveInfo info : appsList) {
                    packageName = info.activityInfo.packageName;
                    if (packageName.equals("com.android.bluetooth")) {
                        className = info.activityInfo.name;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Toast.makeText(this, "블루투스가 발견되지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    intent.setClassName(packageName, className);
                    startActivity(intent);
                }
            }
            //  }

        } else {
            Toast.makeText(this, "블루투스가 취소되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        itemCheck(view);
    }

    /*item 선택시 check박스 설정되게 하기*/
    public void itemCheck(View view) {
        PhotobookCheckboxItem item = (PhotobookCheckboxItem) view;
        item.checkBox.setChecked(!item.checkBox.isChecked());
        file = new File(AppConstans.APP_PATH + "/" + item.photobook.getFilename());
        enableBluetooth();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Android.R.id.home 앱바의 뒤로가기 버튼을 눌렀을 때.
                onBackPressed(); //액티비티가 이전 액티비티로 전환됨.
                break;
        }
        return true;
    }
}

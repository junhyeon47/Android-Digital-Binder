package team.code.effect.digitalbinder.main;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    Button bt_send;
    ArrayList<File> fileList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        TAG = getClass().getName();
        listView = (ListView) findViewById(R.id.listView);
        bt_send = (Button) findViewById(R.id.bt_send);
        list = getFileList();
        listAdapter = new PhotobookListAdapter(this, list);
        listAdapter.flag = true;
        listAdapter.notifyDataSetChanged();
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
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
            fileList = (ArrayList<File>) getItemList(listAdapter.itemList);
            if (fileList.size() > 0) {
                enableBluetooth();
            } else {
                Toast.makeText(this, "전송할 파일을 선택하여 주세요!!", Toast.LENGTH_SHORT).show();
            }
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
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");

            for (int i = 0; i < fileList.size(); i++) {
                File file = fileList.get(i);
                Log.d(TAG,file.getName()+"file전송");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                PackageManager pm = getPackageManager();
                List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);

                if (appsList.size() > 0) {
                    Log.d(TAG, "파일보내기 시작");
                    String packageName = null;
                    String className = null;
                    boolean found = false;

                    for (ResolveInfo info : appsList) {
                        packageName = info.activityInfo.packageName;
                        Log.d(TAG, "BT검색" + packageName);
                        if (packageName.equals("com.android.bluetooth")) {
                            className = info.activityInfo.name;
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        Toast.makeText(this, "블루투스가 발견되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "파일보내기 진짜 시작");
                        intent.setClassName(packageName, className);
                        startActivity(intent);
                    }
                }
            }

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
    }

    //
    public List getItemList(List list) {
        Log.d(TAG, list.size() + "파일 목록!");
        ArrayList<File> sendlist = new ArrayList<File>();
        for (int i = 0; i < list.size(); i++) {
            PhotobookCheckboxItem item = (PhotobookCheckboxItem) list.get(i);
            if (item.checkBox.isChecked()) {
                Photobook photobook = item.photobook;
                File file = new File(AppConstans.APP_PATH+"/"+photobook.getFilename());
                Log.d(TAG,AppConstans.APP_PATH+"/"+photobook.getFilename());
                sendlist.add(file);
            }
        }
        Log.d(TAG, sendlist.size() + "전송할 파일 목록!");
        return sendlist;
    }


}

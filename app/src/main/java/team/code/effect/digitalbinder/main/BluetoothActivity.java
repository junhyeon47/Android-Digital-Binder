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
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

import team.code.effect.digitalbinder.R;

public class BluetoothActivity extends AppCompatActivity {

    public static  final int DISCOVER_DURATION = 300;
    public static final int REQUEST_BLU = 1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
    }

    public void sendViaBluetooth(View v){

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter == null){
            Toast.makeText(this, "이 기기는 블루투스를 지원하지 않습니다. ", Toast.LENGTH_SHORT).show();
        }else {
            enableBluetooth();
        }
    }

    public void enableBluetooth() {

        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);

        startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==DISCOVER_DURATION && requestCode == REQUEST_BLU){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            File dir = new File(Environment.getExternalStorageDirectory(), "DigitalBinder");
            File[] files=dir.listFiles();
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(dir));

            PackageManager pm=getPackageManager();

            List<ResolveInfo> appsList=pm.queryIntentActivities(intent, 0);

            if(appsList.size() > 0){
                String packageName = null;
                String className = null;
                boolean found = false;

                for(ResolveInfo info : appsList){
                    packageName = info.activityInfo.packageName;
                    if(packageName.equals("com.android.bluetooth")){
                        className=info.activityInfo.name;
                        found = true;
                        break;
                    }
                }
                if(!found){
                    Toast.makeText(this, "블루투스가 발견되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    intent.setClassName(packageName, className);
                    startActivity(intent);
                }
            }
        }else {
            Toast.makeText(this, "블루투스가 취소되었습니다.", Toast.LENGTH_SHORT).show();

        }
    }


}

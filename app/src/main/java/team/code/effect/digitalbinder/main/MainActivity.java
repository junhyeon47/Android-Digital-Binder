package team.code.effect.digitalbinder.main;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.List;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.camera.CameraActivity;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.explorer.ExplorerActivity;
import team.code.effect.digitalbinder.photobook.PhotobookActivity;

import static team.code.effect.digitalbinder.main.BluetoothActivity.DISCOVER_DURATION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    static final int PERMISSION_CAMERA = 1;
    NavigationView navigationView;
    Intent intent;
    MenuItem item;


    static final int EVENT_MIN_DELTA_X = 150;
    float oldEventX, newEventX;

    //마지막으로 뒤로가기 버튼이 터치된 시간
    private long lastTimeBackPressed;

    /*블루투스*/
    public static  final int DISCOVER_DURATION = 300;
    public static final int REQUEST_BLU = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkFolder();
        //메뉴 리스너
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    checkCameraPermssion();
                } else {
                    intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_explorer:
                intent = new Intent(MainActivity.this, ExplorerActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_photobook:
                intent = new Intent(MainActivity.this, PhotobookActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldEventX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                newEventX = event.getX();
                float deltaX = newEventX - oldEventX;
                if (deltaX > EVENT_MIN_DELTA_X) {
                    Toast.makeText(this, "swipe", Toast.LENGTH_SHORT).show();

                }
                break;
        }
        return true;
    }


    public void btnClick() {
        Toast.makeText(this, "클릭했어?", Toast.LENGTH_SHORT).show();
    }

    public void checkCameraPermssion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        } else {
            intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
        }
    }
    /*뒤로가기 두번 누르면 종료*/
    @Override
    public void onBackPressed() {
        //1.5초 내로 뒤로가기 버튼을 또 터치했으면 앱을 종료한다.
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        //뒤로가기 버튼을 터치할 때마다 현재시간을 기록해둔다.
        lastTimeBackPressed =System.currentTimeMillis();
    }

    public void moreSend(){
        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setView(getLayoutInflater().inflate(R.layout.activity_bluetooth, null));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_share:
                intent = new Intent(MainActivity.this, BluetoothActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }
    // App 폴더 있는지 확인
    public void checkFolder() {
        File dir = new File(AppConstans.APP_PATH);
        if (dir.exists() == false) {
            if (dir.mkdir()) {
            }
        } else {
        }
    }

    /*블루투스*/
/*
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
            File f = new File(Environment.getExternalStorageDirectory(), "splash.png");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));

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
    }*/
}
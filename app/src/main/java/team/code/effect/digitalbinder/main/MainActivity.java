package team.code.effect.digitalbinder.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.io.File;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.camera.CameraActivity;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.BinderDAO;
import team.code.effect.digitalbinder.common.DatabaseHelper;
import team.code.effect.digitalbinder.explorer.ExplorerActivity;
import team.code.effect.digitalbinder.photobook.PhotobookActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final int PERMISSION_USING_CAMERA = 1;
    static final int PERMISSION_USING_EXPLORER = 2;
    static final int PERMISSION_USING_PHOTOBOOK = 3;
    private Intent intent;
    private long lastTimeBackPressed; //마지막으로 뒤로가기 버튼이 터치된 시간
    public  static BinderDAO dao; //DAO 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkFolder();
        //메뉴 리스너
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //데이터베이스 얻어오기
        dao = new BinderDAO(DatabaseHelper.initialize(getApplicationContext()));
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkCameraPermssion();
                } else {
                    intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_explorer:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkExplorerPermission();
                } else {
                    intent = new Intent(MainActivity.this, ExplorerActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_photobook:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPhotobookPermission();
                } else {
                    intent = new Intent(MainActivity.this, PhotobookActivity.class);
                    startActivity(intent);
                }
                break;

        }
    }

    public void checkCameraPermssion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_USING_CAMERA);
        } else {
            intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        }
    }

    public void checkExplorerPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_USING_EXPLORER);
        } else {
            intent = new Intent(MainActivity.this, ExplorerActivity.class);
            startActivity(intent);
        }
    }
    public void checkPhotobookPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_USING_PHOTOBOOK);
        } else {
            intent = new Intent(MainActivity.this, PhotobookActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_USING_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
                break;
            case PERMISSION_USING_EXPLORER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intent = new Intent(MainActivity.this, ExplorerActivity.class);
                    startActivity(intent);
                }
                break;
            case PERMISSION_USING_PHOTOBOOK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intent = new Intent(MainActivity.this, PhotobookActivity.class);
                    startActivity(intent);
                }
                break;
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_share:
                intent = new Intent(MainActivity.this, SendActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_info:
                intent = new Intent(MainActivity.this, InfoActivity.class);
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
}
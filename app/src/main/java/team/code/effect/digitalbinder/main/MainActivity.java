package team.code.effect.digitalbinder.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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



import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.camera.CameraActivity;
import team.code.effect.digitalbinder.explorer.ExplorerActivity;
import team.code.effect.digitalbinder.photobook.PhotobookActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    static final int PERMISSION_CAMERA = 1;
    Intent intent;
    MenuItem item;

    static final int EVENT_MIN_DELTA_X = 150;
    float oldEventX, newEventX;

    //마지막으로 뒤로가기 버튼이 터치된 시간
    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
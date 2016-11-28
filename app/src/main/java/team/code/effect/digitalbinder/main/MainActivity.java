package team.code.effect.digitalbinder.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.camera.CameraActivity;
import team.code.effect.digitalbinder.explorer.ExplorerActivity;
import team.code.effect.digitalbinder.photobook.PhotobookActivity;

public class MainActivity extends AppCompatActivity{
    static final int PERMISSION_CAMERA = 1;
    Intent intent;

    //메뉴가 표시되어 있음을 나타냄
    boolean alreadyShowing = false;
    //현재창 너비
    int windowWidth;
    //현재창 높이
    int windowHeight;
    //화면을 흐리게
    PopupWindow fadePopup;
    //애니메이션 전환
    Animation ta;
    //전환할때 필요한 뷰
    RelativeLayout baseView;
    //LayoutInflater의 레퍼런스
    LayoutInflater inflater;

    static final int EVENT_MIN_DELTA_X = 150;
    float oldEventX, newEventX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics disp = getApplicationContext().getResources().getDisplayMetrics();
        windowWidth = disp.widthPixels;
        windowHeight = disp.heightPixels;
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.btn_camera:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    checkCameraPermssion();
                }else{
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
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                oldEventX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                newEventX = event.getX();
                float deltaX = newEventX - oldEventX;
                if (deltaX > EVENT_MIN_DELTA_X)
                {
                    Toast.makeText(this, "swipe", Toast.LENGTH_SHORT).show ();
                    if(!alreadyShowing){
                        alreadyShowing = true;
                        openSlidingMenu();
                    }
                }
                break;
        }
        return true;
    }
    /*전체 화면을 흐리게 처리하고 배경이 희미 해집니다.*/
    public void showFadePopup() {
        final View layout = inflater.inflate(R.layout.item_fadepopup, (ViewGroup) findViewById(R.id.fadePopup));
        fadePopup = new PopupWindow(layout, windowWidth, windowHeight, false);
        fadePopup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 0);
    }
    /*슬라이딩 메뉴 열기*/
    public void openSlidingMenu() {
        showFadePopup();
        // 메뉴의 너비
        int width = (int) (windowWidth * 0.6);
        translateView((float) (width));
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        // 팝업창 만들기
        final View layout = inflater.inflate(R.layout.item_popup,(ViewGroup) findViewById(R.id.popup_element));

        final PopupWindow optionsPopup = new PopupWindow(layout, width, height, true);
        optionsPopup.setBackgroundDrawable(new PaintDrawable());

        optionsPopup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 0);

        optionsPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            public void onDismiss() {
                //페이드 효과 제거
                fadePopup.dismiss();
                //애니메이션 전환 전 지우기
                cleanUp();
                //뷰 전환
                translateView(0);
                //애니메이션 전환 지우기
                cleanUp();
                //변수 재설정
                alreadyShowing = false;
            }
        });
    }

    /**
     이 메서드는 뷰 변환을 담당합니다.
     */
    public void translateView(float right) {

        ta = new TranslateAnimation(0f, right, 0f, 0f);
        ta.setDuration(300);
        ta.setFillEnabled(true);
        ta.setFillAfter(true);

        //baseView =  (RelativeLayout) findViewById(R.id.baseView);
        //baseView.startAnimation(ta);
        //baseView.setVisibility(View.VISIBLE);
    }

    /**
     메모리 문제를 피하기위한 기본 정리
     */
    public void cleanUp(){
        if (null != baseView) {
            baseView.clearAnimation();
            baseView = null;
        }
        if (null != ta) {
            ta.cancel();
            ta = null;
        }
        fadePopup = null;
    }

    public void btnClick(){
        Toast.makeText(this, "클릭했어?", Toast.LENGTH_SHORT).show ();
    }

    public void checkCameraPermssion(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CAMERA:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
        }
    }
}
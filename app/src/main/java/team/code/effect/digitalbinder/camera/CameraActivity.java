package team.code.effect.digitalbinder.camera;

import android.app.Dialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AlertHelper;
import team.code.effect.digitalbinder.common.DeviceHelper;
import team.code.effect.digitalbinder.main.MainActivity;
import team.code.effect.digitalbinder.photobook.Photobook;

public class CameraActivity extends AppCompatActivity implements SensorEventListener{
    final String TAG = getClass().getName();

    //레이아웃 관련 멤버 변수 정의
    FrameLayout preview;
    ImageButton btn_open_preview, btn_close_preview, btn_save, btn_shutter, btn_back, btn_shutter_ring;
    CustomCamera customCamera;
    View popupPreview;
    PopupWindow popupWindow;
    RecyclerView recyclerview;
    PreviewRecyclerAdapter previewRecyclerAdapter;
    static ArrayList<Preview> list = new ArrayList<Preview>();
    FrameLayout.LayoutParams layoutParams;

    //센서 관련 멤버 변수 정의
    SensorManager sensorManager;
    Sensor accelerometer, magnetometer;
    float[] lastAccelerometer = new float[3];
    float[] lastMagnetometer = new float[3];
    boolean lastAccelerometerSet = false;
    boolean lastMagnetometerSet = false;
    float[] rotation = new float[9];
    float[] orientationData = new float[3];
    static int orientation;
    int oldOrientation;
    int newOrientation;

    //애니메이션 관련 멤버변수 정의
    ArrayList<ImageButton> btnList = new ArrayList<ImageButton>();
    RotateAnimation rotate;
    int[] angle = new int[]{-90, 0, 90, 180};
    Animation anim_shutter;

    //SQLite 관련 멤버 변수 정의


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        preview = (FrameLayout)findViewById(R.id.preview);
        btn_open_preview = (ImageButton)findViewById(R.id.btn_open_preview);
        btn_close_preview = (ImageButton)findViewById(R.id.btn_close_preview);
        btn_save = (ImageButton)findViewById(R.id.btn_save);
        btn_shutter = (ImageButton)findViewById(R.id.btn_shutter);
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_shutter_ring = (ImageButton)findViewById(R.id.btn_shutter_ring);
        popupPreview = View.inflate(this, R.layout.popup_preview, null);

        layoutParams = (FrameLayout.LayoutParams)btn_back.getLayoutParams();

        recyclerview = (RecyclerView)popupPreview.getRootView().findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
        previewRecyclerAdapter = new PreviewRecyclerAdapter(this);
        recyclerview.setAdapter(previewRecyclerAdapter);

        Log.d(TAG, "SDK Version: "+Build.VERSION.SDK_INT);
        customCamera = new CustomCamera(this);
        preview.addView(customCamera);
        Log.d(TAG, "Previous Camera");
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //리스트 애니메이션 적용할 버튼 리스트에 추가
        btnList.add(btn_back);
        btnList.add(btn_open_preview);
        btnList.add(btn_save);

        //적용할 애니메이션
        oldOrientation = CameraActivity.orientation;
        anim_shutter = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shutter);

        //데이터베이스 관련 테스트
        Photobook photobook = new Photobook();
        photobook.setFilename("메롱.zip");
        photobook.setIcon("아이콘");
        photobook.setRegdate("123124");
        photobook.setTitle("메롱");
        MainActivity.dao.insert(photobook);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastAccelerometerSet = false;
        lastMagnetometerSet = false;
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void openPopupPreview(){
        btn_open_preview.clearAnimation();
        btn_save.clearAnimation();
        btn_back.clearAnimation();
        btn_open_preview.setVisibility(View.GONE);
        btn_close_preview.setVisibility(View.VISIBLE);
        btn_shutter.setEnabled(false);
        btn_shutter_ring.setImageResource(R.drawable.ic_panorama_fish_eye_gray_48dp);

        Log.d(TAG, "Popup Window Size - width: "+preview.getWidth()+", height: "+preview.getHeight());
        popupWindow = new PopupWindow(popupPreview, preview.getWidth(), preview.getHeight(), false);
        popupWindow.showAtLocation(preview, Gravity.NO_GRAVITY, 0, 0);
    }

    private void closePopupPreview() {
        btn_open_preview.setVisibility(View.VISIBLE);
        btn_close_preview.setVisibility(View.GONE);
        btn_shutter.setEnabled(true);
        btn_shutter_ring.setImageResource(R.drawable.ic_panorama_fish_eye_white_48dp);
        popupWindow.dismiss();
    }

    public void btnClick(View view){
        switch (view.getId()){
            case R.id.btn_open_preview:
                openPopupPreview();
                break;
            case R.id.btn_close_preview:
                closePopupPreview();
                break;
            case R.id.btn_shutter:
                customCamera.takePicture();
                btn_shutter.startAnimation(anim_shutter);
                btn_shutter.setEnabled(false);
                break;
            case R.id.btn_save:
                btnSaveClick();
                break;
            case R.id.btn_back:
                finishActivity();
                break;
        }
    }

    public void changeButtonRoation(){
        switch (CameraActivity.orientation){
            case DeviceHelper.ORIENTATION_REVERSE_LANDSCAPE:
                layoutParams.gravity=Gravity.START;
                btn_back.setLayoutParams(layoutParams);
                newOrientation = DeviceHelper.ORIENTATION_REVERSE_LANDSCAPE;
                break;
            case DeviceHelper.ORIENTATION_PORTRAIT:
                layoutParams.gravity=Gravity.START;
                btn_back.setLayoutParams(layoutParams);
                newOrientation = DeviceHelper.ORIENTATION_PORTRAIT;
                break;
            case DeviceHelper.ORIENTATION_LANDSCAPE:
                layoutParams.gravity=Gravity.END;
                btn_back.setLayoutParams(layoutParams);
                newOrientation = DeviceHelper.ORIENTATION_LANDSCAPE;
                break;
            case DeviceHelper.ORIENTATION_REVERSE_PORTRAIT:
                layoutParams.gravity=Gravity.END;
                btn_back.setLayoutParams(layoutParams);
                newOrientation = DeviceHelper.ORIENTATION_REVERSE_PORTRAIT;
                break;
        }

        if(oldOrientation != newOrientation){
            animateRotation(angle[oldOrientation], angle[newOrientation]);
            oldOrientation = newOrientation;
        }
    }

    public void animateRotation(int fromAngle, int toAngle){
        if(popupWindow != null)
            if (popupWindow.isShowing())
                return;




        for(int i=0; i<btnList.size(); ++i){
            if(btnList.get(i).getId() == R.id.btn_back && toAngle == -90)
                rotate = new RotateAnimation(fromAngle, -toAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            else
                rotate = new RotateAnimation(fromAngle, toAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            btnList.get(i).startAnimation(rotate);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == accelerometer) {
            System.arraycopy(sensorEvent.values, 0, lastAccelerometer, 0, sensorEvent.values.length);
            lastAccelerometerSet = true;
        } else if (sensorEvent.sensor == magnetometer) {
            System.arraycopy(sensorEvent.values, 0, lastMagnetometer, 0, sensorEvent.values.length);
            lastMagnetometerSet = true;
        }
        if (lastAccelerometerSet && lastMagnetometerSet) {
            SensorManager.getRotationMatrix(rotation, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(rotation, orientationData);
            float pitch = (float)Math.toDegrees(orientationData[1]);
            float roll = (float) Math.toDegrees(orientationData[2]);

            if (pitch >= -45 && pitch < 45 && roll >= 45)
                orientation = 0;
            else if (pitch < -45 && roll >= -45 && roll < 45)
                orientation = 1;
            else if (pitch >= -45 && pitch < 45 && roll < -45)
                orientation = 2;
            else if (pitch >= 45 && roll >= -45 && roll < 45 )
                orientation = 3;

            changeButtonRoation();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void finishActivity(){
        AlertDialog.Builder alert = AlertHelper.getAlertDialog(this, "알림", "지금까지 촬영한 모든 사진이 삭제됩니다. 계속 하시겠습니까?");
        alert.setPositiveButton("뒤로가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CameraActivity.list.removeAll(CameraActivity.list);
                finish();
            }
        });
        alert.setNegativeButton("취소", null);

        alert.show();
    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                closePopupPreview();
                return;
            }
        }
        finishActivity();
    }

    /*
     * 파일 저장 순서
     * 1. 입력 받은 값(txt_file_name)이 중복되는지 확인한다.
     * 1-1. 중복이 된다면. 다이알로그를 닫지 않고 중복되어서 다시 압력하라고 해야함.
     * 1-2. 중복된 것이 없다면, 바이트로 저장된 이미지를 순서대로 저장한다.
     * 2. 파일 저장을 byte 파일을 바로 zip 파일로 저장할 수 있는지 찾아본다.
     * 3. 바로 저장할 수 없다면, 임시 폴더에 저장한 후 파일을 zip 파일로 압축시킨다.
     */
    public void btnSaveClick(){
        //if(CameraActivity.list.size() == 0 ) return;
        AlertDialog.Builder builder = AlertHelper.getAlertDialog(this, "알림", "지금까지 촬영한 모든 사진을 하나로 묶습니다.");
        builder.setView(R.layout.layout_alert_txt);
        builder.setPositiveButton("저장", null);
        builder.setNegativeButton("취소", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = (Dialog)dialogInterface;
                        EditText txt_file_name = (EditText)dialog.findViewById(R.id.txt_file_name);
                        if(isExistFile(txt_file_name.getText()+".zip")){
                            //중복일 경우
                            txt_file_name.setText("");
                            txt_file_name.setHint("중복된 이름이 존재합니다.");
                            txt_file_name.setHintTextColor(getResources().getColor(R.color.colorAccent));
                        }else{
                            //중복이 없을 때.
                            //AsyncTask 이용해 파일로 저장.
                            StoreFileAsync async = new StoreFileAsync(getApplicationContext(), dialog);
                            async.execute(txt_file_name.getText().toString());
                            //dialog.dismiss();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    public boolean isExistFile(String filename){
        return MainActivity.dao.isDuplicatedTitle(filename);
    }
}
package team.code.effect.digitalbinder.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

public class CameraActivity extends AppCompatActivity implements SensorEventListener{
    final String TAG = getClass().getName();

    //레이아웃 관련 멤버 변수 정의
    FrameLayout preview;
    ImageButton btn_open_preview, btn_close_preview, btn_save, btn_shutter;
    CustomCamera customCamera;
    View popupPreview;
    PopupWindow popupWindow;
    RecyclerView recyclerview;
    ViewPager viewPager; //not use
    PreviewRecyclerAdapter previewRecyclerAdapter;
    PreviewPagerAdapter previewPagerAdapter;
    static ArrayList<Preview> list = new ArrayList<Preview>();

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

    static int previewWidth, previewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        preview = (FrameLayout)findViewById(R.id.preview);
        btn_open_preview = (ImageButton)findViewById(R.id.btn_open_preview);
        btn_close_preview = (ImageButton)findViewById(R.id.btn_close_preview);
        btn_save = (ImageButton)findViewById(R.id.btn_save);
        btn_shutter = (ImageButton)findViewById(R.id.btn_shutter);
        popupPreview = View.inflate(this, R.layout.popup_preview, null);

        recyclerview = (RecyclerView)popupPreview.getRootView().findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
        previewRecyclerAdapter = new PreviewRecyclerAdapter(getApplicationContext(), this);
        recyclerview.setAdapter(previewRecyclerAdapter);
        //iv_selected = (ImageView)popupPreview.getRootView().findViewById(R.id.iv_selected);
        //viewPager = (ViewPager)popupPreview.getRootView().findViewById(R.id.view_pager);
        //previewPagerAdapter = new PreviewPagerAdapter(getApplicationContext(), this);
        //viewPager.setAdapter(previewPagerAdapter);
        //Log.d(TAG, "ViewPager - width: "+viewPager.getWidth()+", height: "+viewPager.getHeight());

        Log.d(TAG, "SDK Version: "+Build.VERSION.SDK_INT);
        customCamera = new CustomCamera(getApplicationContext(), this);
        preview.addView(customCamera);
        Log.d(TAG, "Previous Camera");
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

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
        btn_open_preview.setVisibility(View.GONE);
        btn_close_preview.setVisibility(View.VISIBLE);
        btn_save.setEnabled(false);
        btn_shutter.setEnabled(false);

        Log.d(TAG, "Popup Window Size - width: "+preview.getWidth()+", height: "+preview.getHeight());
        popupWindow = new PopupWindow(popupPreview, preview.getWidth(), preview.getHeight(), false);
        popupWindow.showAtLocation(preview, Gravity.NO_GRAVITY, 0, 0);
    }

    private void closePopupPreview() {
        btn_open_preview.setVisibility(View.VISIBLE);
        btn_close_preview.setVisibility(View.GONE);
        btn_save.setEnabled(true);
        btn_shutter.setEnabled(true);
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
                btn_shutter.setEnabled(false);
                break;
            case R.id.btn_save:
                //저장 하기.
                break;
        }
    }

    public void changeButtonRoation(int orientation){
        switch (orientation){
            case 0:
                btn_open_preview.setRotation(-90f);
                btn_save.setRotation(-90f);
                break;
            case 1:
                btn_open_preview.setRotation(0f);
                btn_save.setRotation(0f);
                break;
            case 2:
                btn_open_preview.setRotation(90f);
                btn_save.setRotation(90f);
                break;
            case 3:
                btn_open_preview.setRotation(180f);
                btn_save.setRotation(180f);
                break;
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
            //Log.d(TAG, "[orientation]: "+orientation);

            changeButtonRoation(orientation);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(popupWindow != null) {
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
                }
                list.removeAll(list);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
package team.code.effect.digitalbinder.camera;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AlertHelper;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.ColorPalette;
import team.code.effect.digitalbinder.common.ColorPaletteHelper;
import team.code.effect.digitalbinder.common.ColorPaletteRecyclerAdapter;
import team.code.effect.digitalbinder.common.DeviceHelper;
import team.code.effect.digitalbinder.common.MediaStorageHelper;
import team.code.effect.digitalbinder.main.MainActivity;

public class CameraActivity extends AppCompatActivity implements SensorEventListener{
    final String TAG = getClass().getName();

    //레이아웃 관련 멤버 변수 정의
    FrameLayout preview;
    ImageButton btn_open_preview, btn_save, btn_shutter, btn_back, btn_shutter_ring;
    CustomCamera customCamera;
    RecyclerView recyclerview;
    PreviewRecyclerAdapter previewRecyclerAdapter;
    RelativeLayout.LayoutParams layoutParams;

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

    TextView txt_sensor;

    //애니메이션 관련 멤버변수 정의
    ArrayList<ImageButton> btnList = new ArrayList<ImageButton>();
    RotateAnimation rotate;
    Animation anim_shutter;

    //Color Palette 관련 멤버변수 정의
    RecyclerView recycler_view_color;
    ArrayList<ColorPalette> colorPaletteList = new ArrayList<>();
    ColorPaletteRecyclerAdapter colorPaletteRecyclerAdapter;

    //파일 저장관련 멤버 변수 정의
    File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        txt_sensor = (TextView)findViewById(R.id.txt_sensor);
        preview = (FrameLayout)findViewById(R.id.preview);
        btn_open_preview = (ImageButton)findViewById(R.id.btn_open_preview);
        btn_save = (ImageButton)findViewById(R.id.btn_save);
        btn_shutter = (ImageButton)findViewById(R.id.btn_shutter);
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_shutter_ring = (ImageButton)findViewById(R.id.btn_shutter_ring);

        layoutParams = (RelativeLayout.LayoutParams)btn_back.getLayoutParams();

//        recyclerview = (RecyclerView)findViewById(R.id.recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
//        recyclerview.setLayoutManager(layoutManager);
//        recyclerview.setHasFixedSize(true);
//        previewRecyclerAdapter = new PreviewRecyclerAdapter(this);
//        recyclerview.setAdapter(previewRecyclerAdapter);

        customCamera = new CustomCamera(this);
        preview.addView(customCamera);


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

        //Color Palette 관련 초기화
        colorPaletteRecyclerAdapter = new ColorPaletteRecyclerAdapter();
        colorPaletteRecyclerAdapter.setList(colorPaletteList);

        //폴더 관련 초기화
        checkDirectory();
        checkPreviousFiles();
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

    public void openPreviewActivity(){
        files = new File(AppConstans.APP_PATH_TEMP).listFiles();
        if(files.length == 0){
            Toast.makeText(this, "촬영된 사진이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(this, PreviewActivity.class);
        startActivity(intent);
    }

    public void btnClick(View view){
        switch (view.getId()){
            case R.id.btn_open_preview:
                openPreviewActivity();
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
            case DeviceHelper.ORIENTATION_REVERSE_LANDSCAPE: //180
                //layoutParams.setLayoutDirection(RelativeLayout.ALIGN_LEFT);
                //btn_back.setLayoutParams(layoutParams);
                newOrientation = DeviceHelper.ORIENTATION_REVERSE_LANDSCAPE;
                break;
            case DeviceHelper.ORIENTATION_PORTRAIT: //90
                //layoutParams.gravity=Gravity.START;
                //btn_back.setLayoutParams(layoutParams);
                newOrientation = DeviceHelper.ORIENTATION_PORTRAIT;
                break;
            case DeviceHelper.ORIENTATION_LANDSCAPE: //0
                //layoutParams.gravity=Gravity.END;
                //btn_back.setLayoutParams(layoutParams);
                newOrientation = DeviceHelper.ORIENTATION_LANDSCAPE;
                break;
            case DeviceHelper.ORIENTATION_REVERSE_PORTRAIT: //270
                //layoutParams.gravity=Gravity.END;
                //btn_back.setLayoutParams(layoutParams);
                newOrientation = DeviceHelper.ORIENTATION_REVERSE_PORTRAIT;
                break;
        }

        if(oldOrientation != newOrientation){
            animateRotation(oldOrientation-90, newOrientation-90);
            oldOrientation = newOrientation;
        }
    }

    public void animateRotation(int fromAngle, int toAngle){
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
            float azimuth = (float)Math.toDegrees(orientationData[0]);
            float pitch = (float)Math.toDegrees(orientationData[1]);
            float roll = (float) Math.toDegrees(orientationData[2]);

            //txt_sensor.setText("azimuth: "+azimuth+"\npitch: "+pitch+"\nroll: "+roll);
            if (pitch >= -45 && pitch < 45 && roll >= 45)
                orientation = DeviceHelper.ORIENTATION_REVERSE_LANDSCAPE;
            else if (pitch < -45 && roll >= -45 && roll < 45)
                orientation = DeviceHelper.ORIENTATION_PORTRAIT;
            else if (pitch >= -45 && pitch < 45 && roll < -45)
                orientation = DeviceHelper.ORIENTATION_LANDSCAPE;
            else if (pitch >= 45 && roll >= -45 && roll < 45 )
                orientation = DeviceHelper.ORIENTATION_REVERSE_PORTRAIT;
            changeButtonRoation();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void finishActivity(){
        files = new File(AppConstans.APP_PATH_TEMP).listFiles();
        if(files.length > 0) {
            AlertDialog.Builder builder = AlertHelper.getAlertDialog(this, "알림", "촬영한 사진들을 임시폴더에 저장합니다. (다음 촬영에 이어서 촬영할 수 있습니다.)");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteAllFiles();
                    finish();
                }
            });
            builder.show();
        }else{
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    public void takePicture(byte[] bytes, int orientation){
        new StoreTempFileAsync(this, orientation).execute(bytes);
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
        files = new File(AppConstans.APP_PATH_TEMP).listFiles();
        if(files.length == 0 ) {
            Toast.makeText(this, "촬영된 사진이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = AlertHelper.getAlertDialog(this, "알림", "지금까지 촬영한 모든 사진을 하나로 묶습니다.");
        builder.setView(R.layout.layout_alert_save);
        builder.setPositiveButton("저장", null);
        builder.setNegativeButton("취소", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                initColorPaletteList();
                recycler_view_color = (RecyclerView)((Dialog)dialogInterface).findViewById(R.id.recycler_view_color);
                GridLayoutManager layoutManager = new GridLayoutManager(((Dialog)dialogInterface).getContext(), 5);
                recycler_view_color.setLayoutManager(layoutManager);
                recycler_view_color.setAdapter(colorPaletteRecyclerAdapter);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = (Dialog)dialogInterface;
                        EditText txt_file_name = (EditText)dialog.findViewById(R.id.txt_file_name);
                        TextView txt_color = (TextView)dialog.findViewById(R.id.txt_color);

                        int colorValue;
                        //유효성 체크가 되면 AsyncTask 이용해 파일로 저장.
                        if((colorValue=checkValidity(txt_file_name, txt_color)) != -1){
                            StoreFileAsync async = new StoreFileAsync(getApplicationContext(), dialog);
                            async.execute(txt_file_name.getText().toString(), Integer.toString(colorValue));
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    public void initColorPaletteList() {
        if (colorPaletteList.size() == 0) {
            ColorPalette colorPalette;
            for (int i = 0; i < ColorPaletteHelper.VALUE.length; ++i) {
                colorPalette = new ColorPalette();
                colorPalette.setCheck(false);
                colorPalette.setColorValue(ColorPaletteHelper.VALUE[i]);
                colorPaletteList.add(colorPalette);
            }
        } else {
            for (int i = 0; i < ColorPaletteHelper.VALUE.length; ++i) {
                colorPaletteList.get(i).setCheck(false);
            }
        }
    }

    //파일명 중복 유효성 체크
    public boolean isExistFile(String filename){
        return MainActivity.dao.isDuplicatedTitle(filename);
    }
    //Color Palette 유효성 체크
    public int isCheckedColor(){
        int result = -1;
        for(int i=0; i<colorPaletteList.size(); ++i){
            if(colorPaletteList.get(i).isCheck()){
                result = i;
                break;
            }
        }
        return result;
    }

    //유효성 체크
    public int checkValidity(EditText txt_file_name, TextView txt_color){
        int result = isCheckedColor();
        boolean flagDuplicate, flagFileName, flagColor;
        //파일명 중복 여부 확인
        if(isExistFile(txt_file_name.getText()+".zip")){
            txt_file_name.setText("");
            txt_file_name.setHint("중복된 이름이 존재합니다.");
            txt_file_name.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            flagDuplicate = false;
        }else{
            flagDuplicate = true;
        }

        if(txt_file_name.getText().length() == 0){
            txt_file_name.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            flagFileName = false;
        }else{
            flagFileName = true;
        }

        //색상 선택여부를 확인
        if(result == -1) {
            txt_color.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            flagColor = false;
        }else {
            txt_color.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            flagColor = true;
        }

        if(flagDuplicate && flagFileName && flagColor)
            return result;
        else
            return -1;
    }
    public void checkDirectory(){
        File tempDir = new File(AppConstans.APP_PATH_TEMP);
        boolean isCreateDir;
        if(!tempDir.exists()) { //temp 폴더가 존재하지 않으면 폴더 생성.
            isCreateDir = tempDir.mkdirs();
            if(!isCreateDir) {
                //폴더 생성 실패로 액티비티를 종료해야한다.
                finish();
            }
        }
    }
    public void checkPreviousFiles(){
        files = new File(AppConstans.APP_PATH_TEMP).listFiles();
        if(files.length > 0) {
            AlertDialog.Builder builder = AlertHelper.getAlertDialog(this, "알림", "이전에 촬영했던 사진이 존재합니다. 이어서 촬영하시겠습니까?");
            builder.setCancelable(false);
            builder.setPositiveButton("이어서 촬영", null);
            builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteAllFiles();
                }
            });
            builder.show();
        }
    }

    public void deleteAllFiles(){
        for(int i=0; i<files.length; ++i){
            if(files[i].delete()){
                Log.d(TAG, "파일 삭제: "+files[i].getAbsolutePath());
            }
        }
        MediaStorageHelper.deleteAll(this, MediaStorageHelper.WHERE_TEMP);
    }
}
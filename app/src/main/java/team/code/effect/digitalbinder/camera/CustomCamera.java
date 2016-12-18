package team.code.effect.digitalbinder.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;

import team.code.effect.digitalbinder.common.DeviceHelper;

public class CustomCamera extends TextureView implements TextureView.SurfaceTextureListener{
    String TAG;
    CameraActivity cameraActivity;
    Camera camera;


    public CustomCamera(CameraActivity cameraActivity) {
        super(cameraActivity.getApplicationContext());
        TAG = getClass().getName();
        this.setSurfaceTextureListener(this);
        this.cameraActivity = cameraActivity;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.autoFocus(null);
            }
        });
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.d(TAG, "onSurfaceTextureAvailable() called");
        camera = Camera.open();
        //showPreviewSize();
        Camera.Size preViewSize = camera.getParameters().getSupportedPreviewSizes().get(0);
        Log.d(TAG, "[preView Size] width: "+preViewSize.width+", height: "+preViewSize.height);
        Log.d(TAG, "[TextureView Size] width: "+cameraActivity.preview.getWidth()+", height: "+cameraActivity.preview.getHeight());
        Log.d(TAG, "[Device Size] width: "+ DeviceHelper.width+", height: "+DeviceHelper.height);

        this.setLayoutParams(new FrameLayout.LayoutParams(cameraActivity.preview.getWidth(), cameraActivity.preview.getHeight(), Gravity.CENTER));
        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.setDisplayOrientation(90);
        camera.startPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.d(TAG, "onSurfaceTextureSizeChanged() called");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.d(TAG, "onSurfaceTextureDestroyed() called");
        camera.stopPreview();
        camera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        //Log.d(TAG, "onSurfaceTextureUpdated() called");
    }

    public void takePicture(){
        camera.takePicture(shutterCallback, null, pictureCallback);
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.d(TAG, "shutterCallback called");
        }
    };

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            camera.stopPreview();
            cameraActivity.takePicture(bytes);
            camera.startPreview();
            cameraActivity.btn_shutter.setEnabled(true);
        }
    };

    public void showPreviewSize(){
        Camera.Parameters parameters = camera.getParameters();
        if(parameters != null){
            List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();

            for(Camera.Size size : pictureSizeList){
                Log.d(TAG, "[Picture Size] width: "+size.width+", height :"+size.height);
            } //지원하는 사진의 크기

            List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
            for(Camera.Size size : previewSizeList){
                Log.d(TAG, "[Preview Size] width: "+size.width+", height :"+size.height);
            } //지원하는 프리뷰 크기
        }
    }
}

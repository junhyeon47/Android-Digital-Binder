package team.code.effect.digitalbinder.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;

public class CustomCamera extends TextureView implements TextureView.SurfaceTextureListener{
    String TAG;
    Context context;
    CameraActivity cameraActivity;
    Camera camera;
    static int pictureWidth, pictureHegith;


    public CustomCamera(Context context, CameraActivity cameraActivity) {
        super(context);
        TAG = getClass().getName();
        this.context = context;
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
        Camera.Size preViewSize = camera.getParameters().getPreviewSize();
        Log.d(TAG, "preViewSize - width: "+preViewSize.width+", height: "+preViewSize.height);
        this.setLayoutParams(new FrameLayout.LayoutParams(preViewSize.width, preViewSize.height, Gravity.CENTER));

        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.startPreview();
        setRotation(90.0f);
        CameraActivity.previewWidth = cameraActivity.preview.getWidth();
        CameraActivity.previewHeight = cameraActivity.preview.getHeight();

        Log.d(TAG, "[TextureView Size] width: "+cameraActivity.preview.getWidth()+", height"+cameraActivity.preview.getHeight());
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
            cameraActivity.btn_shutter.setEnabled(true);
        }
    };

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Log.d(TAG, "pictureCallback called");
            Log.d(TAG, "image length: "+bytes.length);
            Preview preview = new Preview();
            preview.setBytes(bytes);
            preview.setOrientation(cameraActivity.orientation);
            cameraActivity.list.add(preview);
            //cameraActivity.previewPagerAdapter.notifyDataSetChanged();
        }
    };

    public void showPreviewSize(){
        Camera.Parameters parameters = camera.getParameters();
        if(parameters != null){
            List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
            CustomCamera.pictureWidth = pictureSizeList.get(0).width;
            CustomCamera.pictureHegith = pictureSizeList.get(0).height;

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

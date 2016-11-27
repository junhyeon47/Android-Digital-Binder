package team.code.effect.digitalbinder.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.support.v4.content.ContextCompat;
import android.view.TextureView;

public class UpdatedCamera extends TextureView implements TextureView.SurfaceTextureListener{
    private String TAG;
    private Context context;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder builder;
    private CameraCaptureSession cameraCaptureSession;
    private CameraActivity cameraActivity;

    public UpdatedCamera(Context context, CameraActivity cameraActivity) {
        super(context);
        this.context = context;
        this.cameraActivity = cameraActivity;
        TAG = this.getClass().getName();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void initCamera(){
//        CameraManager manager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
//        try {
//            String[] cameraId = manager.getCameraIdList();
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
    }
}

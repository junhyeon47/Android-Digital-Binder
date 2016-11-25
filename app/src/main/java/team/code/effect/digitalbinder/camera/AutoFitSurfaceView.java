package team.code.effect.digitalbinder.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

import team.code.effect.digitalbinder.R;

public class AutoFitSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder holder;
    Camera camera;

    public AutoFitSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflater.inflate(R.layout.auto_fit_surface_view, this);
    }

    // SurfaceView 생성시 카메라 오픈하고 미리보기 설정
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            camera.release();
            camera = null;
        }
    }

    // SurfaceView 크기가 결정될 때 최적의 미리보기 크기를 구해 설정한다.
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> arSize = params.getSupportedPreviewSizes();
        if (arSize == null) {
            params.setPreviewSize(width, height);
        } else {
            int diff = 10000;
            Camera.Size opti = null;
            for (Camera.Size s : arSize) {
                if (Math.abs(s.height - height) < diff) {
                    diff = Math.abs(s.height - height);
                    opti = s;

                }
            }
            params.setPreviewSize(opti.width, opti.height);
        }
        camera.setParameters(params);
        camera.setDisplayOrientation(90);
        camera.startPreview();
    }

    // SurfaceView 종료할 때 카메라도 종료.
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}

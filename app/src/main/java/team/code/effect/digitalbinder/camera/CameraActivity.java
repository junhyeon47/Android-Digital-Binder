package team.code.effect.digitalbinder.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import team.code.effect.digitalbinder.R;

public class CameraActivity extends AppCompatActivity {
    LinearLayout preview;
    AutoFitSurfaceView surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Log.d("CameraActivity", "1");
        preview = (LinearLayout)findViewById(R.id.preview);
        Log.d("CameraActivity", "2");

        Log.d("CameraActivity", surfaceView+"");
        //preview.addView(surfaceView);
    }
}

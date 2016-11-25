package team.code.effect.digitalbinder.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.camera.CameraActivity;
import team.code.effect.digitalbinder.explorer.ExplorerActivity;
import team.code.effect.digitalbinder.photobook.PhotobookActivity;

public class MainActivity extends AppCompatActivity{
    static final int EVENT_MIN_DELTA_X = 150;
    private float oldEventX, newEventX;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.btn_camera:
                intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
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
                }
                break;
        }
        return true;
    }
}

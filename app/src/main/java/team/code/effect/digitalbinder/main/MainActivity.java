package team.code.effect.digitalbinder.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import team.code.effect.digitalbinder.R;

public class MainActivity extends AppCompatActivity {
    static final int EVENT_MIN_DELTA_X = 150;
    private float oldEventX, newEventX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show ();
                }
                break;
        }
        return true;
    }
}

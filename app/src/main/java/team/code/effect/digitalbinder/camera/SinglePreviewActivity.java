package team.code.effect.digitalbinder.camera;

import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import team.code.effect.digitalbinder.R;
import uk.co.senab.photoview.PhotoView;

public class SinglePreviewActivity extends AppCompatActivity{
    private final String TAG = this.getClass().getName();
    private PhotoView iv_original;
    private ImageButton btn_close;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_preview);

        iv_original = (PhotoView)findViewById(R.id.iv_original);
        btn_close = (ImageButton)findViewById(R.id.btn_close);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        if(position != -1) {
            SinglePreviewAsync async = new SinglePreviewAsync(iv_original, btn_close);
            async.execute(position);
        }
    }
    public void btnClick(View view){
        switch (view.getId()){
            case R.id.btn_close:
                finish();
                break;
        }
    }
}

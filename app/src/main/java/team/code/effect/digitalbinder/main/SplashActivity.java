package team.code.effect.digitalbinder.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import team.code.effect.digitalbinder.R;

public class SplashActivity extends AppCompatActivity {
    ProgressBar progress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progress = (ProgressBar)findViewById(R.id.progress);

        new FileAsync(this).execute();
    }
}

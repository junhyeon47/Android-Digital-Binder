package team.code.effect.digitalbinder.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import team.code.effect.digitalbinder.R;

/**
 * Created by 1238TX on 2016-11-30.
 */

public class SendItem extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_send);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Photobook");
        toolbar.setOnMenuItemClickListener(this);

    }


    public void sendViaBluetooth(View view){
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }


}

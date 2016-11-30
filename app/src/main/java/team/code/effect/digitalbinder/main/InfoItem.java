package team.code.effect.digitalbinder.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import team.code.effect.digitalbinder.R;

/**
 * Created by 1238TX on 2016-11-30.
 */

public class InfoItem extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);

        //Button bt1 = (Button)findViewById(R.id.developer);
        //bt1.setOnClickListener(R.layout.item_info);

    }

    public void bt1(View view){
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:01012345678"));
        startActivity(intent);
    }
}

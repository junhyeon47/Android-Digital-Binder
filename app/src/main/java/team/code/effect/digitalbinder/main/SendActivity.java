package team.code.effect.digitalbinder.main;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import team.code.effect.digitalbinder.R;

/**
 * Created by 1238TX on 2016-11-30.
 */

public class SendActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        toolbar = (Toolbar)findViewById(R.id.toolbar); //XML 툴바의 주소를 toolbar로 연결.
        setSupportActionBar(toolbar); //툴바를 현재 액티비티의 액션바로 설정.
        setToolbar(); //툴바의 설정을 변경하는 메소드 호출.

    }

    //툴바의 설정을 변경하는 메소드
    public void setToolbar(){
        getSupportActionBar().setTitle("내보내기");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFF8B7D)); //툴바색상 변경
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바에 뒤로가기 버튼 추가.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp); //뒤로가기 버튼 아이콘 변경
    }

    public void layoutClick(View view){
        switch (view.getId()){
            case R.id.layout_bluetooth:
                openBluetoothActivity();
                break;
            case R.id.layout_cloud:
                openCloundActivity();
                break;
        }
    }

    public void openBluetoothActivity(){
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }

    public void openCloundActivity(){
        Intent intent = new Intent(this, CloudActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Android.R.id.home 앱바의 뒤로가기 버튼을 눌렀을 때.
                onBackPressed(); //액티비티가 이전 액티비티로 전환됨.
                break;
        }
        return true;
    }


}

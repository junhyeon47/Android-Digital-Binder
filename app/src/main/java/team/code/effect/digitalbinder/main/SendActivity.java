package team.code.effect.digitalbinder.main;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import team.code.effect.digitalbinder.R;

public class SendActivity extends AppCompatActivity {
    View inc_layout_send, inc_layout_cloud;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        toolbar = (Toolbar)findViewById(R.id.toolbar); //XML 툴바의 주소를 toolbar로 연결.
        inc_layout_send = findViewById(R.id.inc_layout_send);
        inc_layout_cloud = findViewById(R.id.inc_layout_cloud);

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
                visibleCloudLayout();
                break;
            case R.id.layout_google_drive:
                Toast.makeText(this, "구글 드라이브", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_dropbox:
                Toast.makeText(this, "드롭박스", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void openBluetoothActivity(){
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }

    public void visibleCloudLayout(){
        getSupportActionBar().setTitle("클라우드로 내보내기");
        inc_layout_cloud.setVisibility(View.VISIBLE);
        inc_layout_send.setVisibility(View.GONE);
    }

    public void visibleSendLayout(){
        getSupportActionBar().setTitle("내보내기");
        inc_layout_cloud.setVisibility(View.GONE);
        inc_layout_send.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        if(inc_layout_cloud.getVisibility() == View.VISIBLE)
            visibleSendLayout();
        else
            finish();
    }
}

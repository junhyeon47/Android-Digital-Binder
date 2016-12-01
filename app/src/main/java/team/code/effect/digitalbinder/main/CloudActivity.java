package team.code.effect.digitalbinder.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import team.code.effect.digitalbinder.R;

public class CloudActivity extends AppCompatActivity{
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        toolbar = (Toolbar)findViewById(R.id.toolbar); //XML 툴바의 주소를 toolbar로 연결.
        setSupportActionBar(toolbar); //툴바를 현재 액티비티의 액션바로 설정.
        setToolbar(); //툴바의 설정을 변경하는 메소드 호출.

    }

    //툴바의 설정을 변경하는 메소드
    public void setToolbar(){
        getSupportActionBar().setTitle("클라우드로 내보내기");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFF2594B)); //툴바색상 변경
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바에 뒤로가기 버튼 추가.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp); //뒤로가기 버튼 아이콘 변경
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //툴바의 메뉴 터치 이벤트
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed(); //현재 액티비티에서 이전 액티비티로 전환.
                break;
        }
        return true;
    }
}

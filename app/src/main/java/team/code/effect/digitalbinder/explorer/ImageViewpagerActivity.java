package team.code.effect.digitalbinder.explorer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.ImageFile;

/**
 * Created by student on 2016-12-14.
 */

public class ImageViewpagerActivity extends AppCompatActivity {
    ViewPager viewPager;
    ImageViewPagerAdapter imageViewPagerAdapter;
    String TAG;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_viewpager);
        TAG=this.getClass().getName();
        Intent intent=getIntent();
        int index=intent.getIntExtra("data", 13);
        List list=intent.getParcelableArrayListExtra("list");
        Log.d(TAG, "list "+intent.getParcelableArrayListExtra("list"));
        Log.d(TAG, "index =? "+index);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        imageViewPagerAdapter=new ImageViewPagerAdapter(this);

        imageViewPagerAdapter.list=(ArrayList<ImageFile>) list;

        viewPager.setAdapter(imageViewPagerAdapter);
        viewPager.setCurrentItem(index, true);
    }

}

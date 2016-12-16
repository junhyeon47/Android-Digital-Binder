package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.ImageFile;
import uk.co.senab.photoview.PhotoView;


/**
 * Created by student on 2016-12-14.
 */

public class ImageViewPagerAdapter extends PagerAdapter{
    Context context;
    LayoutInflater inflater;
    ArrayList<ImageFile> list;
    public ImageViewPagerAdapter(Context context) {
        this.context=context;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView=inflater.inflate(R.layout.item_viewpager, container, false);
        PhotoView imageView=(PhotoView)itemView.findViewById(R.id.viewPager_img);

        //이미지 넣어야할 자리 작동시 버벅댈 경우 async 로 대체
        Bitmap bitmap= BitmapFactory.decodeFile(list.get(position).path.toString());
        imageView.setImageBitmap(bitmap);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

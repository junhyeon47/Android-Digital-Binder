package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.ImageFile;
import uk.co.senab.photoview.PhotoView;

public class PhotobookPagerAdapter extends PagerAdapter{
    String TAG;
    LayoutInflater inflater;
    ArrayList<ImageFile> list;

    public PhotobookPagerAdapter(Context context) {
        TAG = this.getClass().getName();
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.layout_photo_view, container, false);
        PhotoView photo_view = (PhotoView)view.findViewById(R.id.photo_view);
        ImageFile imageFile = list.get(position);
        new PhotobookAsync(photo_view).execute(imageFile.path.toString());
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}

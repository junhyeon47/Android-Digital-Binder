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
    PhotobookActivity photobookActivity;
    LayoutInflater inflater;

    public PhotobookPagerAdapter(PhotobookActivity photobookActivity) {
        TAG = this.getClass().getName();
        this.photobookActivity = photobookActivity;
        this.inflater = LayoutInflater.from(photobookActivity.getApplicationContext());
    }

    @Override
    public int getCount() {
        return photobookActivity.list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.layout_photo_view, container, false);
        PhotoView photo_view = (PhotoView)view.findViewById(R.id.photo_view);
        ImageFile imageFile = photobookActivity.list.get(position);
        new PhotobookAsync(photo_view).execute(imageFile.path.toString(), Integer.toString(imageFile.orientation));
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

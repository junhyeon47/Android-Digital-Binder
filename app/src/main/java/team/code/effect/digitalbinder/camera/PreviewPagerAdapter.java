package team.code.effect.digitalbinder.camera;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.ImageFile;
import uk.co.senab.photoview.PhotoView;

public class PreviewPagerAdapter extends PagerAdapter{
    String TAG;
    PreviewActivity previewActivity;
    LayoutInflater inflater;

    public PreviewPagerAdapter(PreviewActivity previewActivity) {
        TAG = this.getClass().getName();
        this.previewActivity = previewActivity;
        this.inflater = LayoutInflater.from(previewActivity.getApplicationContext());
    }

    @Override
    public int getCount() {
        return previewActivity.list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.layout_photo_view, container, false);
        PhotoView photo_view = (PhotoView)view.findViewById(R.id.photo_view);
        ImageFile imageFile = previewActivity.list.get(position);
        new PreviewAsync(photo_view).execute(imageFile.path.toString(), Integer.toString(imageFile.orientation));
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

package team.code.effect.digitalbinder.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import team.code.effect.digitalbinder.R;

public class PreviewPagerAdapter extends PagerAdapter{
    String TAG;
    Context context;
    CameraActivity cameraActivity;
    Thread thread;

    public PreviewPagerAdapter(Context context, CameraActivity cameraActivity) {
        TAG = getClass().getName();
        this.context = context;
        this.cameraActivity = cameraActivity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_item, null);
        ImageView iv_original = (ImageView)view.findViewById(R.id.iv_original);
        int width = cameraActivity.popupWindow.getWidth();
        int height = cameraActivity.popupWindow.getHeight();
        Preview preview = cameraActivity.list.get(position);

        //Bitmap bitmap = CameraActivity.byteToBitmap(preview.getBytes(), width, height, preview.getOrientation());
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return cameraActivity.list.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
        int viewPagerIndex = cameraActivity.viewPager.getCurrentItem();
        int viewPageItemCount = cameraActivity.previewPagerAdapter.getCount();
        Log.d(TAG, "viewPagerIndex: "+viewPagerIndex+", position: "+position);

        if(viewPagerIndex == position){
            if(viewPageItemCount-1 != 0) {
                if (position == viewPageItemCount - 1) {
                    cameraActivity.viewPager.setCurrentItem(position - 1, true);
                } else {
                    cameraActivity.viewPager.setCurrentItem(position + 1, true);
                }
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}

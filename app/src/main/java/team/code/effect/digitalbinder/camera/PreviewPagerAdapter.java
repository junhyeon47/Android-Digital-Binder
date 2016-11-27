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

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[16 * 1024];
        Preview preview = cameraActivity.list.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(preview.getBytes(), 0, preview.getBytes().length, options);
        Log.d(TAG, "[original] width: "+bitmap.getWidth()+", height: "+bitmap.getHeight());
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        Bitmap resize = Bitmap.createScaledBitmap(bitmap, width, bitmapHeight/(bitmapWidth/width), false);
        Matrix matrix = new Matrix();
        switch (preview.getOrientation()){
            case 0:
                matrix.preRotate(180f);
                break;
            case 1:
                matrix.preRotate(90f);
                break;
            case 2:
                matrix.preRotate(0f);
                break;
            case 3:
                matrix.preRotate(-90f);
                break;
        }

        Bitmap rotate = Bitmap.createBitmap(resize, 0, 0, width, bitmapHeight/(bitmapWidth/width), matrix,false);
        iv_original.setImageBitmap(rotate);
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
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}

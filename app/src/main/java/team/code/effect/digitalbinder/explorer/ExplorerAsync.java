package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import team.code.effect.digitalbinder.common.DeviceHelper;

import static android.view.View.ACCESSIBILITY_LIVE_REGION_ASSERTIVE;

/**
 * Created by student on 2016-12-01.
 */

public class ExplorerAsync extends AsyncTask<String, Void, Bitmap> {
    String TAG;
    ImageView imageView;

    public ExplorerAsync(ImageView imageView) {
        this.imageView=imageView;
        TAG=this.getClass().getName();
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        int size= DeviceHelper.width/3;
        String filename=strings[0];
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=ACCESSIBILITY_LIVE_REGION_ASSERTIVE;
        Bitmap bitmap=BitmapFactory.decodeFile(filename, options);
        Bitmap resize=Bitmap.createScaledBitmap(bitmap, size, size, true);
        return resize;
    }

    @Override
    protected void onPostExecute(Bitmap resize) {
        imageView.setImageBitmap(resize);
    }
}

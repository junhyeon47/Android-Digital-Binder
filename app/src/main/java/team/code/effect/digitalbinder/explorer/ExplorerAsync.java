package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import static android.view.View.ACCESSIBILITY_LIVE_REGION_ASSERTIVE;

/**
 * Created by student on 2016-12-01.
 */

public class ExplorerAsync extends AsyncTask<String, Void, Bitmap> {

    String TAG;
    ImageViewHolder holder;
    Bitmap bitmap;

    public ExplorerAsync(ImageViewHolder holder) {
        this.holder=holder;
        TAG=this.getClass().getName();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String filename=strings[0];

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=ACCESSIBILITY_LIVE_REGION_ASSERTIVE;
        Bitmap bitmap=BitmapFactory.decodeFile(filename, options);
        Bitmap resize=Bitmap.createScaledBitmap(bitmap, 450, 450, true);



        return resize;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);


    }

    @Override
    protected void onPostExecute(Bitmap resize) {
        super.onPostExecute(bitmap);
        holder.imageView.setImageBitmap(resize);
    }
}

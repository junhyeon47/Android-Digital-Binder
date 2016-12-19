package team.code.effect.digitalbinder.explorer;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;

import team.code.effect.digitalbinder.common.BitmapHelper;
import team.code.effect.digitalbinder.common.DeviceHelper;

/**
 * Created by student on 2016-12-19.
 */

public class ImageSelectAsync extends AsyncTask<Integer, Void, Bitmap> {

    ExplorerActivity explorerActivity;
    ImageFileSelected holder;
    String TAG;

    public ImageSelectAsync(ExplorerActivity explorerActivity, ImageFileSelected holder) {
        this.explorerActivity = explorerActivity;
        this.holder = holder;
        TAG=this.getClass().getName();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        holder.imageView.setMinimumWidth(DeviceHelper.width/4);
        holder.imageView.setMinimumHeight(DeviceHelper.width/4);
        holder.imageView.setImageBitmap(null);
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        return getThumbnaul(params[0], params[1]);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        holder.imageView.setImageBitmap(bitmap);
    }

    public Bitmap getThumbnaul(int image_id, int orientation){
        Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(
                explorerActivity.getContentResolver(),
                image_id,
                MediaStore.Images.Thumbnails.MICRO_KIND,
                null
        );

        return  BitmapHelper.changeOrientation(thumbnail, orientation);
    }

}

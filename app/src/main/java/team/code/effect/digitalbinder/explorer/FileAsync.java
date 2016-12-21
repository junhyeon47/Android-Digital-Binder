package team.code.effect.digitalbinder.explorer;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;

import team.code.effect.digitalbinder.common.BitmapHelper;
import team.code.effect.digitalbinder.common.DeviceHelper;

public class FileAsync extends AsyncTask<Integer, Void, Bitmap> {
    String TAG;
    ExplorerActivity explorerActivity;
    ImageViewHolder holder;

    public FileAsync(ExplorerActivity explorerActivity,ImageViewHolder holder) {
        TAG = getClass().getName();
        this.explorerActivity = explorerActivity;
        this.holder = holder;
    }

    @Override
    protected void onPreExecute() {
        holder.imageView.setMinimumWidth(DeviceHelper.width/4);
        holder.imageView.setMinimumHeight(DeviceHelper.width/4);
        holder.imageView.setImageBitmap(null);
        holder.checkBox.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        return getThumbnaul(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        holder.imageView.setImageBitmap(bitmap);
        holder.checkBox.setVisibility(View.VISIBLE);
        holder.checkBox.setChecked(false);
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

package team.code.effect.digitalbinder.explorer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import team.code.effect.digitalbinder.common.DeviceHelper;
import team.code.effect.digitalbinder.common.FileHelper;

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
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        return getThumbnaul(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        holder.imageView.setImageBitmap(bitmap);
    }

    public Bitmap getThumbnaul(int image_id){
        Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(
                explorerActivity.getContentResolver(),
                image_id,
                MediaStore.Images.Thumbnails.MICRO_KIND,
                null
        );
        return  thumbnail;
    }
}

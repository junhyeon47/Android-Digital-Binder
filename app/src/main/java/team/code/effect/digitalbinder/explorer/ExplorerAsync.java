package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import team.code.effect.digitalbinder.common.BitmapHelper;
import team.code.effect.digitalbinder.common.DeviceHelper;

import static android.view.View.ACCESSIBILITY_LIVE_REGION_ASSERTIVE;

/**
 * Created by student on 2016-12-01.
 */

public class ExplorerAsync extends AsyncTask<String, Void, Bitmap> {
    String TAG;
    ImageViewHolder holder;
    int position;

    public ExplorerAsync(ImageViewHolder holder, int position) {
        this.holder=holder;
        TAG=this.getClass().getName();
        this.position = position;
    }

    @Override
    protected void onPreExecute() {
        holder.imageView.setMinimumWidth(DeviceHelper.width/4);
        holder.imageView.setMinimumHeight(DeviceHelper.width/4);
        holder.imageView.setImageBitmap(null);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        int size= DeviceHelper.width/4;
        String filename=strings[0];
        return BitmapHelper.decodeFile(filename, size, size);
    }

    @Override
    protected void onPostExecute(Bitmap resize) {
        holder.imageView.setImageBitmap(resize);
        holder.textView.setText("pos: "+position);
    }
}

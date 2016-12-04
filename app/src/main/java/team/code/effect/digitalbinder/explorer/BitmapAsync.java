package team.code.effect.digitalbinder.explorer;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import team.code.effect.digitalbinder.common.BitmapHelper;
import team.code.effect.digitalbinder.common.DeviceHelper;

public class BitmapAsync extends AsyncTask<String, Void, Bitmap> {
    ImageViewHolder holder;

    public BitmapAsync(ImageViewHolder holder) {
        this.holder = holder;
    }

    @Override
    protected void onPreExecute() {
        holder.imageView.setImageBitmap(null);
        holder.imageView.setMinimumWidth(DeviceHelper.width/4);
        holder.imageView.setMinimumHeight(DeviceHelper.width/4);
        holder.checkBox.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String filename = strings[0];
        Bitmap bitmap = BitmapHelper.decodeFile(filename, DeviceHelper.width/4, DeviceHelper.width/4);
        return BitmapHelper.getThumbnail(bitmap, DeviceHelper.width/4);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(!isCancelled()) {
            holder.imageView.setImageBitmap(bitmap);
            holder.checkBox.setVisibility(View.VISIBLE);
        }else {
            holder.imageView.setImageBitmap(null);
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
    }
}

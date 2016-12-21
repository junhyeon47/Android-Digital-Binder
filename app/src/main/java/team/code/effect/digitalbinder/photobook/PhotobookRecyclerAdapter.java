package team.code.effect.digitalbinder.photobook;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.MediaStorageHelper;


public class PhotobookRecyclerAdapter extends RecyclerView.Adapter<PhotobookViewHolder> {
    PhotobookActivity photobookActivity;
    int selectedPosition;

    public PhotobookRecyclerAdapter(PhotobookActivity photobookActivity) {
        this.photobookActivity = photobookActivity;
        this.selectedPosition = 0;
        Log.d("Adapter", "constructor called?");
    }

    @Override
    public PhotobookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false);
        Log.d("Adapter", "view: "+view.toString());
        return new PhotobookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotobookViewHolder holder, int position) {
        int image_id = photobookActivity.list.get(position).image_id;
        int orientation = photobookActivity.list.get(position).orientation;
        Log.d("Adapter", "size: "+getItemCount());
        new AsyncTask<Integer, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Integer... params) {
                if(photobookActivity.isFinishing())
                    cancel(true);
                return MediaStorageHelper.getThumbnail(photobookActivity, params[0], params[1]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                holder.iv_thumbnail.setImageBitmap(bitmap);
                holder.layout_outline.setBackgroundResource(R.color.colorAccent);
                super.onPostExecute(bitmap);
            }
        }.execute(image_id, orientation);

        if(position == selectedPosition)
            holder.isSelected = true;
        else
            holder.isSelected = false;

        if(holder.isSelected){
            holder.iv_thumbnail.setPadding(6, 6, 6 ,6);
        }else{
            holder.iv_thumbnail.setPadding(0, 0, 0 ,0);
        }

        holder.iv_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPosition == holder.getAdapterPosition())
                    return;
                selectedPosition = holder.getAdapterPosition();
                photobookActivity.view_pager.setCurrentItem(holder.getAdapterPosition(),true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return photobookActivity.list.size();
    }
}

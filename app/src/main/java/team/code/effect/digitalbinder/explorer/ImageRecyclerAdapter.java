package team.code.effect.digitalbinder.explorer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

import static android.view.View.ACCESSIBILITY_LIVE_REGION_ASSERTIVE;

/**
 * Created by student on 2016-12-01.
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    String TAG;
    ArrayList<String> list = new ArrayList<>();
    ExplorerItemListActivity explorerItemListActivity;

    public ImageRecyclerAdapter(ExplorerItemListActivity explorerItemListActivity) {
        this.explorerItemListActivity = explorerItemListActivity;
        TAG = this.getClass().getName();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_explorer, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        String filename = list.get(position);
        holder.position = position;
        new BitmapAsync(holder).execute(filename);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
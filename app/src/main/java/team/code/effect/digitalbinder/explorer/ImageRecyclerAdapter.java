package team.code.effect.digitalbinder.explorer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

import static android.view.View.ACCESSIBILITY_LIVE_REGION_ASSERTIVE;

/**
 * Created by student on 2016-12-01.
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    String TAG;
    ArrayList<ImageFile> list;
    ExplorerActivity explorerActivity;

    public ImageRecyclerAdapter(ExplorerActivity explorerActivity) {
        this.explorerActivity = explorerActivity;
        TAG = this.getClass().getName();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_explorer, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        new FileAsync(explorerActivity, holder).execute(list.get(position).image_id);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(explorerActivity, "check true postion: "+position, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(explorerActivity, "check false postion: "+position, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<ImageFile> list){
        this.list = list;
    }
}
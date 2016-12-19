package team.code.effect.digitalbinder.explorer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.ImageFile;

/**
 * Created by student on 2016-12-15.
 */

public class ImageSelectedRecyclerAdapter extends RecyclerView.Adapter<ImageFileSelected> {
    ExplorerActivity explorerActivity;
    ArrayList<ImageFile> list=new ArrayList<ImageFile>();
    ArrayList<ImageViewHolder> list2=new ArrayList<ImageViewHolder>();
    @Override
    public ImageFileSelected onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected, parent, false);
        return new ImageFileSelected(view);
    }
    @Override
    public void onBindViewHolder(final ImageFileSelected holder, final int position) {
        final int index = position;
        final int orientation= list.get(index).orientation;
        explorerActivity=ExplorerActivity.explorerActivity;
        new ImageSelectAsync(explorerActivity, holder).execute(list.get(index).image_id, list.get(index).orientation);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list2.get(index).checkBox.setChecked(false);
                list.remove(list.get(position));
                list2.remove(index);
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}

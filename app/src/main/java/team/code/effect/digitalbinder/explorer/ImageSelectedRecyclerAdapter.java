package team.code.effect.digitalbinder.explorer;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-12-15.
 */

public class ImageSelectedRecyclerAdapter extends RecyclerView.Adapter<ImageFileSelected> {
    ExplorerActivity explorerActivity;
    ArrayList<ImageViewHolder> checkedList=new ArrayList<ImageViewHolder>();

    public ImageSelectedRecyclerAdapter(ExplorerActivity explorerActivity) {
        this.explorerActivity = explorerActivity;
    }

    @Override
    public ImageFileSelected onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected, parent, false);
        return new ImageFileSelected(view);
    }
    @Override
    public void onBindViewHolder(final ImageFileSelected holder, int position) {
        ImageViewHolder imageViewHolder = checkedList.get(position);
        BitmapDrawable drawable = (BitmapDrawable) imageViewHolder.imageView.getDrawable();
        holder.imageView.setImageBitmap(drawable.getBitmap());
        holder.ib_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedList.get(holder.getAdapterPosition()).checkBox.setChecked(false);
                checkedList.remove(holder.getAdapterPosition());
                if(checkedList.size()==0){
                    explorerActivity.layout_selected.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return checkedList.size();
    }
}
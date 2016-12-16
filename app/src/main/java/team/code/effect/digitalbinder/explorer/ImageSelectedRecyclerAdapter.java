package team.code.effect.digitalbinder.explorer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    @Override
    public ImageFileSelected onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected, parent, false);
        return new ImageFileSelected(view);
    }
    @Override
    public void onBindViewHolder(final ImageFileSelected holder, final int position) {
        Bitmap bitmap= BitmapFactory.decodeFile(list.get(position).path.toString());
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        Bitmap resize= Bitmap.createScaledBitmap(bitmap, width/4, height/4, true);
        holder.imageView.setImageBitmap(resize);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(list.get(position));
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}

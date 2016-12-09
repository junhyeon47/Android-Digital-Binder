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

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private String TAG;
    private ArrayList<ImageFile> list;
    private ExplorerActivity explorerActivity;

    public ImageRecyclerAdapter(ExplorerActivity explorerActivity) {
        this.explorerActivity = explorerActivity;
        TAG = this.getClass().getName();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explorer_thumbnail, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final int index = position;
        final int orientation= list.get(index).orientation;
        new FileAsync(explorerActivity, holder).execute(list.get(index).image_id, list.get(index).orientation);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(explorerActivity, "click index: "+index, Toast.LENGTH_SHORT).show();
                Toast.makeText(explorerActivity, "orientation"+orientation, Toast.LENGTH_SHORT).show();
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(explorerActivity, "check true index: "+index, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(explorerActivity, "check false index: "+index, Toast.LENGTH_SHORT).show();
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

    public void resetList(){
        list = null;
        notifyDataSetChanged();
    }
}
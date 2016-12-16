package team.code.effect.digitalbinder.explorer;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.ImageFile;

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
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        final int index = position;
        final int orientation= list.get(index).orientation;
        new FileAsync(explorerActivity, holder).execute(list.get(index).image_id, list.get(index).orientation);


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int test=0;
                //Toast.makeText(explorerActivity, "click index: "+index, Toast.LENGTH_SHORT).show();
                //Toast.makeText(explorerActivity, "orientation"+orientation, Toast.LENGTH_SHORT).show();
                //Toast.makeText(explorerActivity, "111 "+list.get(index).path, Toast.LENGTH_SHORT).show();
                Toast.makeText(explorerActivity, "111 "+list.get(index).path, Toast.LENGTH_SHORT).show();
/*                explorerActivity.imageViewDetailRecyclerAdapter.setList(list);
                explorerActivity.layout_images.setVisibility(View.GONE);
                explorerActivity.layout_detail.setVisibility(View.VISIBLE);
                explorerActivity.imageViewDetailRecyclerAdapter.notifyItemChanged(test);
                test=position;
                explorerActivity.imageViewDetailRecyclerAdapter.notifyItemChanged(test);*/
                Intent intent=new Intent(explorerActivity, ImageViewpagerActivity.class);
                String path1234=list.get(index).path.toString();
                intent.putExtra("data",index);
                Log.d(TAG, "data 담기는가? "+index);
                intent.putParcelableArrayListExtra("list", list);
                Log.d(TAG, "아이고두야 "+list);
                explorerActivity.startActivity(intent);
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

    public ArrayList<ImageFile> getList() {
        return list;
    }

    public void setList(ArrayList<ImageFile> list){
        this.list = list;
    }

    public void resetList(){
        list = null;
        notifyDataSetChanged();
    }
}
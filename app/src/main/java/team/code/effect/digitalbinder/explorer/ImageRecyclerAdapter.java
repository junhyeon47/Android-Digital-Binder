package team.code.effect.digitalbinder.explorer;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

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
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final int index = position;
        holder.path = list.get(position).path.getPath();

        new FileAsync(explorerActivity, holder).execute(list.get(index).image_id, list.get(index).orientation);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int test=0;
                Intent intent=new Intent(explorerActivity, ImageViewpagerActivity.class);
                intent.putExtra("data",index);
                intent.putParcelableArrayListExtra("list", list);
                explorerActivity.startActivity(intent);
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    explorerActivity.layout_selected.setVisibility(View.VISIBLE);
                    explorerActivity.imageSelectedRecyclerAdapter.checkedList.add(holder);
                    explorerActivity.imageSelectedRecyclerAdapter.notifyDataSetChanged();
                }else{
                    explorerActivity.imageSelectedRecyclerAdapter.checkedList.remove(holder);
                    explorerActivity.imageSelectedRecyclerAdapter.notifyDataSetChanged();
                    if( explorerActivity.imageSelectedRecyclerAdapter.checkedList.size() == 0)
                        explorerActivity.layout_selected.setVisibility(View.GONE);
                }
            }
        });
    }

    public void unCheck(ImageViewHolder holder){
        holder.checkBox.setChecked(false);
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
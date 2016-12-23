package team.code.effect.digitalbinder.explorer;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    ArrayList<ImageViewHolder> holderList = new ArrayList<>();
    static boolean flag = false;


    public ImageRecyclerAdapter(ExplorerActivity explorerActivity) {
        this.explorerActivity = explorerActivity;
        TAG = this.getClass().getName();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explorer_thumbnail, parent, false);
        ImageViewHolder holder = new ImageViewHolder(view);
        holder.flag=flag;
        holder.checkBox.setChecked(flag);
        holderList.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final int index = position;
        holder.path = list.get(position).path.getPath();
        //Log.d(TAG, "position "+position);

        new FileAsync(explorerActivity, holder).execute(list.get(index).image_id, list.get(index).orientation);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int test = 0;
                Intent intent = new Intent(explorerActivity, ImageViewpagerActivity.class);
                intent.putExtra("data", index);
                intent.putParcelableArrayListExtra("list", list);
                explorerActivity.startActivity(intent);
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.flag = !holder.flag;
                holder.checkBox.setChecked(holder.flag);
                if (holder.flag) {
                    explorerActivity.layout_selected.setVisibility(View.VISIBLE);
                    explorerActivity.imageSelectedRecyclerAdapter.checkedList.add(holder);
                    explorerActivity.imageSelectedRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "1010101010 " + explorerActivity.imageSelectedRecyclerAdapter.checkedList.size());
                    explorerActivity.imageSelectedRecyclerAdapter.checkedList.remove(holder);
                    explorerActivity.imageSelectedRecyclerAdapter.notifyDataSetChanged();
                    if (explorerActivity.imageSelectedRecyclerAdapter.checkedList.size() == 0)
                        explorerActivity.layout_selected.setVisibility(View.GONE);
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

    public void setList(ArrayList<ImageFile> list) {
        this.list = list;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void chList(ImageViewHolder holder) {
        if (flag) {
            holder.flag = flag;
            holderList.add(holder);
            holder.checkBox.setChecked(true);
        }else{
            holderList.add(holder);
        }
    }

    public void resetList() {
        list = null;
        notifyDataSetChanged();
    }
}
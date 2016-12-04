package team.code.effect.digitalbinder.explorer;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-12-01.
 */

public class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderViewHolder>{
    ArrayList<Explorer> list=new ArrayList<Explorer>();
    ExplorerActivity explorerActivity;

    public FolderRecyclerAdapter(ExplorerActivity explorerActivity) {
        this.explorerActivity = explorerActivity;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_explorer, parent, false);
        FolderViewHolder viewHolder=new FolderViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        Explorer explorer = list.get(position);
        final String path = explorer.getFilename();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(explorerActivity, ExplorerItemListActivity.class);
                intent.putExtra("data", path);
                explorerActivity.startActivity(intent);
            }
        });
        holder.textView.setText(explorer.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
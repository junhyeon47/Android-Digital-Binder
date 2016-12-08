package team.code.effect.digitalbinder.explorer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team.code.effect.digitalbinder.R;

public class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderViewHolder>{
    ExplorerActivity explorerActivity;

    public FolderRecyclerAdapter(ExplorerActivity explorerActivity) {
        this.explorerActivity = explorerActivity;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explorer_folder, parent, false);
        FolderViewHolder viewHolder=new FolderViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        final ImageFolder imageFolder = explorerActivity.listFolders.get(position);
        final int bucket_id = imageFolder.bucket_id;
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explorerActivity.imageRecyclerAdapter.setList(explorerActivity.getAllImages(bucket_id));
                explorerActivity.layout_folders.setVisibility(View.GONE);
                explorerActivity.layout_images.setVisibility(View.VISIBLE);
                explorerActivity.txt_folder_name.setText(imageFolder.path);

            }
        });
        holder.textView.setText(imageFolder.bucket_name);
    }

    @Override
    public int getItemCount() {
        return explorerActivity.listFolders.size();
    }

}
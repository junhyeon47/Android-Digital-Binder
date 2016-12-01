package team.code.effect.digitalbinder.explorer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import team.code.effect.digitalbinder.R;

public class FolderViewHolder extends RecyclerView.ViewHolder {
    LinearLayout linearLayout;
    TextView textView;
    String path;

    public FolderViewHolder(View itemView) {
        super(itemView);
        this.textView=(TextView)itemView.findViewById(R.id.folder_title);
        this.linearLayout=(LinearLayout)itemView.findViewById(R.id.layout_folder_title);
    }
}

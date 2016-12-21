package team.code.effect.digitalbinder.photobook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import team.code.effect.digitalbinder.R;

public class PhotobookViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout layout_outline;
    ImageView iv_thumbnail;
    ImageButton ib_remove;
    boolean isSelected;

    public PhotobookViewHolder(View itemView) {
        super(itemView);
        this.layout_outline = (RelativeLayout)itemView.findViewById(R.id.layout_outline);
        this.iv_thumbnail = (ImageView)itemView.findViewById(R.id.iv_thumbnail);
        this.ib_remove = (ImageButton)itemView.findViewById(R.id.ib_remove);
        this.isSelected = false;
    }
}

package team.code.effect.digitalbinder.explorer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import team.code.effect.digitalbinder.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout relativeLayout;
    ImageView imageView;
    CheckBox checkBox;
    String path;

    public ImageViewHolder(View itemView) {
        super(itemView);
        relativeLayout=(RelativeLayout)itemView.findViewById(R.id.layout_images);
        imageView=(ImageView)itemView.findViewById(R.id.iv_thumbnail);
        checkBox=(CheckBox)itemView.findViewById(R.id.ch_thumbnail);
    }
}

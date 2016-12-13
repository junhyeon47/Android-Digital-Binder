package team.code.effect.digitalbinder.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import team.code.effect.digitalbinder.R;

public class ColorPaletteViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout layout_palette;
    ImageView iv_check;
    public ColorPaletteViewHolder(View itemView) {
        super(itemView);
        layout_palette = (RelativeLayout)itemView.findViewById(R.id.layout_palette);
        iv_check = (ImageView)itemView.findViewById(R.id.iv_check);
    }
}

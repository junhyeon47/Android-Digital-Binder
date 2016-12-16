package team.code.effect.digitalbinder.explorer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-12-15.
 */

public class ImageFileSelected extends RecyclerView.ViewHolder{
    LinearLayout linearLayout;
    ImageView imageView;

    public ImageFileSelected(View itemView) {
        super(itemView);
        this.linearLayout=(LinearLayout)itemView.findViewById(R.id.item_selected);
        this.imageView=(ImageView)itemView.findViewById(R.id.img_selected);
    }
}

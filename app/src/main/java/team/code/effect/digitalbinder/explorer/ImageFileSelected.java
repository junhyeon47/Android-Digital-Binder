package team.code.effect.digitalbinder.explorer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-12-15.
 */

public class ImageFileSelected extends RecyclerView.ViewHolder{
    ImageView imageView;
    ImageButton ib_remove;

    public ImageFileSelected(View itemView) {
        super(itemView);
        this.imageView=(ImageView)itemView.findViewById(R.id.img_selected);
        this.ib_remove = (ImageButton)itemView.findViewById(R.id.ib_remove);
    }
}

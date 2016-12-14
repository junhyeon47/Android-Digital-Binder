package team.code.effect.digitalbinder.explorer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import team.code.effect.digitalbinder.R;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by student on 2016-12-12.
 */

public class ImageViewDetailHolder extends RecyclerView.ViewHolder{
    LinearLayout linearLayout;
    PhotoView photoView;
    public ImageViewDetailHolder(View itemView) {
        super(itemView);
        this.linearLayout=(LinearLayout)itemView.findViewById(R.id.layout_detail_photo);
        this.photoView=(PhotoView)itemView.findViewById(R.id.imageViewDetail);
    }
}

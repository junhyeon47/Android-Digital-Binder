package team.code.effect.digitalbinder.camera;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import team.code.effect.digitalbinder.R;
import uk.co.senab.photoview.PhotoView;

public class PreviewViewHolder extends RecyclerView.ViewHolder{
    PhotoView photo_view;

    public PreviewViewHolder(View itemView) {
        super(itemView);
        this.photo_view = (PhotoView)itemView.findViewById(R.id.photo_view);
    }
}

package team.code.effect.digitalbinder.camera;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import team.code.effect.digitalbinder.R;

public class PreviewViewHolder extends RecyclerView.ViewHolder{
    ImageView iv_thumbnail;
    TextView txt_index;

    public PreviewViewHolder(View itemView) {
        super(itemView);
        this.iv_thumbnail = (ImageView)itemView.findViewById(R.id.iv_thumbnail);
        this.txt_index = (TextView)itemView.findViewById(R.id.txt_index);
    }
}

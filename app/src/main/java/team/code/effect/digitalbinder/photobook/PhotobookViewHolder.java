package team.code.effect.digitalbinder.photobook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-12-09.
 */

public class PhotobookViewHolder extends RecyclerView.ViewHolder {
    TextView txt_title;

    public PhotobookViewHolder(View itemView) {
        super(itemView);
        txt_title = (TextView)itemView.findViewById(R.id.txt_title);
    }
}

package team.code.effect.digitalbinder.photobook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-12-09.
 */

public class PhotobookViewHolder extends RecyclerView.ViewHolder {
    LinearLayout layout_photobook;
    TextView txt_title, txt_count, txt_regdate;
    ImageButton ib_bookmark_true, ib_bookmark_false;
    CheckBox checkBox;


    public PhotobookViewHolder(View itemView) {
        super(itemView);
        layout_photobook = (LinearLayout)itemView.findViewById(R.id.layout_photobook);
        txt_title = (TextView)itemView.findViewById(R.id.txt_title);
        txt_count = (TextView)itemView.findViewById(R.id.txt_count);
        txt_regdate = (TextView)itemView.findViewById(R.id.txt_regdate);
        ib_bookmark_true = (ImageButton)itemView.findViewById(R.id.ib_bookmark_true);
        ib_bookmark_false = (ImageButton)itemView.findViewById(R.id.ib_bookmark_false);
        checkBox = (CheckBox)itemView.findViewById(R.id.checkBox);
    }
}

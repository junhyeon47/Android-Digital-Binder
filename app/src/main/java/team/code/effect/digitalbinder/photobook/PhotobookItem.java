package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-11-25.
 */

public class PhotobookItem extends LinearLayout {
    TextView txt_title;
    Photobook photobook;
    public PhotobookItem(Context context, Photobook photobook) {
        super(context);
        this.photobook= photobook;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_photobook,this);
        txt_title=(TextView)findViewById(R.id.txt_title);
        init(photobook);
    }

    public void init(Photobook photobook){
        txt_title.setText(photobook.getTitle());
    }

}

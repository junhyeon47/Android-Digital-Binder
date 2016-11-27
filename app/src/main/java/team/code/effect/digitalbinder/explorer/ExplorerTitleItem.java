package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import team.code.effect.digitalbinder.R;

/**
 * Created by 재우 on 2016-11-27.
 */

public class ExplorerTitleItem extends LinearLayout {

    TextView ex_sampleTitle;
    Explorer explorer;

    public ExplorerTitleItem(Context context, Explorer explorer) {
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_title_explorer, this);

        ex_sampleTitle=(TextView)findViewById(R.id.ex_sampleTitle);
        setExplorerTitle(explorer);
    }

    public void setExplorerTitle(Explorer explorer){
        this.explorer=explorer;
        setTitle();
    }

    public void setTitle(){
        ex_sampleTitle.setText(explorer.getTitle());
    }

}

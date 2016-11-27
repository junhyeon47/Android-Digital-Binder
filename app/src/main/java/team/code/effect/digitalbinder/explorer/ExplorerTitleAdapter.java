package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by 재우 on 2016-11-27.
 */

public class ExplorerTitleAdapter extends BaseAdapter {

    String TAG;
    ArrayList<Explorer> list=new ArrayList<Explorer>();
    Context context;

    public ExplorerTitleAdapter(Context context) {
        TAG=this.getClass().getName();
        this.context=context;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view=null;
        Explorer explorer=list.get(i);
        if(view!=convertView){
            view=convertView;
            ExplorerTitleItem explorerTitleItem=(ExplorerTitleItem)view;
            explorerTitleItem.setExplorerTitle(explorer);
        }else{
            view=new ExplorerTitleItem(context, explorer);
        }
        return view;
    }

}

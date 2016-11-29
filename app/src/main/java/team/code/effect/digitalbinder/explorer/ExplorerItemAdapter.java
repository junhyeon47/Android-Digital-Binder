package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by 재우 on 2016-11-27.
 */

public class ExplorerItemAdapter extends BaseAdapter{
    String TAG;
    ArrayList<Explorer> list=new ArrayList<Explorer>();
    ArrayList<ExplorerItem> itemList=new ArrayList();
    Context context;

    public ExplorerItemAdapter(Context context, ArrayList<Explorer> list) {
        this.context = context;
        this.list = list;
        TAG=this.getClass().getName();
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
            ExplorerItem explorerItem=(ExplorerItem)view;
            explorerItem.setExplorerImg(explorer);
            itemList.add(explorerItem);
        }else{
            view=new ExplorerItem(context, explorer);
            ExplorerItem explorerItem=(ExplorerItem)view;
            itemList.add(explorerItem);

        }

        Log.d(TAG, "itemList.size"+itemList.size());


        return view;
    }
}

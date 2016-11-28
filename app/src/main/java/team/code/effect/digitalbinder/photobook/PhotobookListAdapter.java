package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 2016-11-25.
 */

public class PhotobookListAdapter extends BaseAdapter{
    ArrayList<Photobook> list;
    Context context;
    boolean flag=false;

    public PhotobookListAdapter(Context context,List list) {
        this.list=(ArrayList) list;
        this.context=context;
    }

    public void setList(List list) {
        this.list = (ArrayList<Photobook>) list;
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
        Photobook photobook=list.get(i);
        if(convertView==null){
            view = new PhotobookCheckboxItem(context,photobook,flag);
        }else{
            view=convertView;
            PhotobookCheckboxItem item=(PhotobookCheckboxItem) view;
            item.flag=flag;
            item.init(photobook);
        }
        return view;
    }
}

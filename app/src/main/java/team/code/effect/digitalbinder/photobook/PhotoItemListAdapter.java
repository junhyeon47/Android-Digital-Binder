package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by huho on 2016-11-26.
 */

public class PhotoItemListAdapter extends BaseAdapter {
    ArrayList<Bitmap> list;
    BinderActivity context;
    String TAG;
    ArrayList<View> viewList;
    public PhotoItemListAdapter(BinderActivity context, ArrayList list) {
        this.context=context;
        this.list = list;
        TAG = getClass().getName();
        viewList=new ArrayList<View>();
        Log.d(TAG,"압축 파일 갯수는"+list.size());
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
        if(convertView==null) {
            view=new PhotoItem(context,list.get(i));
        }else {
            view=convertView;
            PhotoItem item=(PhotoItem)view;
            item.init(list.get(i));
        }
        view.setSelected(false);
        if(context.selectedIndex==i)view.setSelected(true);
        viewList.add(view);
        return view;
    }
}

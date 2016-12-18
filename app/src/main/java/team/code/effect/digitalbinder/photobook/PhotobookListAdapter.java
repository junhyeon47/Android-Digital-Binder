package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team.code.effect.digitalbinder.R;

public class PhotobookListAdapter extends BaseAdapter{
    ArrayList<Photobook> list;
    Context context;
    public ArrayList<PhotobookCheckboxItem> itemList;
    public boolean flag=false;

    public PhotobookListAdapter(Context context,List list) {
        this.list=(ArrayList) list;
        this.context=context;
        itemList = new ArrayList<PhotobookCheckboxItem>();
    }

    public void setList(List list) {
        itemList.removeAll(itemList);
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view=null;
        Photobook photobook=list.get(position);
        if(convertView==null){
            view = new PhotobookCheckboxItem(context,photobook,flag);
        }else{
            view=convertView;
            PhotobookCheckboxItem item=(PhotobookCheckboxItem) view;
            item.flag=flag;
            item.init(photobook);
        }
        String title = photobook.getTitle();
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<title.length(); ++i){
            sb.append(title.charAt(i));
            if(i != title.length()-1){
                sb.append("\n");
            }
        }
        ((TextView)view.findViewById(R.id.txt_title)).setText(sb.toString());
        itemList.add((PhotobookCheckboxItem)view);
        return view;
    }
}

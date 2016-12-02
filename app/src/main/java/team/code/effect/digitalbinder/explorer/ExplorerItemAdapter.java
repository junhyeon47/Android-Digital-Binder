package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.DeviceHelper;

/**
 * Created by 재우 on 2016-11-27.
 */

public class ExplorerItemAdapter extends BaseAdapter{

    ArrayList<Explorer> phtoList=new ArrayList<Explorer>();
    CheckBox checkBox;
    String TAG;

    public ExplorerItemAdapter() {
        TAG=this.getClass().getName();
    }

    @Override
    public int getCount() {
        return phtoList.size();
    }

    @Override
    public Object getItem(int i) {
        return phtoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int position=i;
        final Context context=viewGroup.getContext();

        if(view==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.item_photo_explorer, viewGroup, false);
        }

        ImageView imageView=(ImageView)view.findViewById(R.id.ex_img);
        checkBox=(CheckBox)view.findViewById(R.id.ex_checkBox);

        if(phtoList.size()>0) {
            Explorer explorer=phtoList.get(i);
            Log.d(TAG, "explorer "+explorer.getFilename());
//            BitmapFactory.Options option = new BitmapFactory.Options();
//            option.inSampleSize = 4;
//            Bitmap bitmap = BitmapFactory.decodeFile(explorer.getFilename(), option);
//            Bitmap resize = Bitmap.createScaledBitmap(bitmap, size, size, true);
//            imageView.setImageBitmap(resize);

            ExplorerAsync async=new ExplorerAsync(imageView);
            async.execute(explorer.getFilename());
        }

        return view;
    }

}

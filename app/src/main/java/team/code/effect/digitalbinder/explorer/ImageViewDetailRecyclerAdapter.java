package team.code.effect.digitalbinder.explorer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-12-12.
 */

public class ImageViewDetailRecyclerAdapter extends RecyclerView.Adapter<ImageViewDetailHolder>{
    String TAG;
    ExplorerActivity explorerActivity;
    private ArrayList<ImageFile> list;
    int index;
    String filePath;

    public ImageViewDetailRecyclerAdapter(ExplorerActivity explorerActivity) {
        this.explorerActivity=explorerActivity;
        TAG=this.getClass().getName();
    }

    @Override
    public ImageViewDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_photo, parent, false);
        return new ImageViewDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewDetailHolder holder, int position) {
//        position=index;
        filePath=list.get(position).path.toString();
        Log.d(TAG, "position="+position);
        Bitmap bitmap= BitmapFactory.decodeFile(filePath);
        holder.photoView.setImageBitmap(bitmap);
    }


    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }else{
            return 0;
        }
    }

    public ArrayList<ImageFile> getList() {
        return list;
    }

    public void setList(ArrayList<ImageFile> list) {
        this.list = list;
    }
}
package team.code.effect.digitalbinder.camera;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

public class PreviewRecyclerAdapter extends RecyclerView.Adapter<PreviewViewHolder> {
    String TAG;
    Context context;
    CameraActivity cameraActivity;
    PreviewThread previewThread;

    public PreviewRecyclerAdapter(Context context, CameraActivity cameraActivity) {
        this.context = context;
        this.cameraActivity = cameraActivity;
        TAG = this. getClass().getName();
    }

    //새로운 뷰 생성
    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_item, parent, false);
        PreviewViewHolder viewHolder = new PreviewViewHolder(view);
        return viewHolder;
    }

    //리스트뷰에서 getView 메서드와 동일.
    @Override
    public void onBindViewHolder(PreviewViewHolder holder, int position) {
        previewThread = new PreviewThread(cameraActivity.list.get(position), holder.iv_thumbnail);
        previewThread.start();
        holder.txt_index.setText(Integer.toString(position+1));
    }

    @Override
    public int getItemCount() {
        return cameraActivity.list.size();
    }
}

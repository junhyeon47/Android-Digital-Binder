package team.code.effect.digitalbinder.camera;

import android.content.Context;
import android.graphics.Bitmap;
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
    public void onBindViewHolder(PreviewViewHolder holder, final int position) {
        Preview preview = CameraActivity.list.get(position);
        Bitmap bitmap = CameraActivity.byteToBitmap(preview.getBytes(), CameraActivity.previewWidth, CameraActivity.previewHeight, preview.getOrientation());
        holder.txt_index.setText(Integer.toString(position+1));
        holder.iv_thumbnail.setImageBitmap(bitmap);
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraActivity.list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, CameraActivity.list.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return CameraActivity.list.size();
    }


}

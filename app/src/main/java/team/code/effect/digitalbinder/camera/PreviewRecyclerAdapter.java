package team.code.effect.digitalbinder.camera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AlertHelper;

public class PreviewRecyclerAdapter extends RecyclerView.Adapter<PreviewViewHolder>{
    final String TAG;
    CameraActivity cameraActivity;

    public PreviewRecyclerAdapter(CameraActivity cameraActivity) {
        this.TAG = this.getClass().getName();
        this.cameraActivity = cameraActivity;
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
//        PreviewAsync async = new PreviewAsync(holder.iv_thumbnail, holder.btn_remove, holder.txt_index);
//        async.execute(position);
//        holder.txt_index.setText(Integer.toString(position+1));
//        holder.iv_thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(cameraActivity, SinglePreviewActivity.class);
//                intent.putExtra("position", position);
//
//                cameraActivity.startActivity(intent);
//            }
//        });
//        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alert = AlertHelper.getAlertDialog(cameraActivity, "알림", "선택한 사진이 삭제됩니다.");
//                alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        removeItem(position);
//                    }
//                });
//                alert.setNegativeButton("취소", null);
//                alert.show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void removeItem(int position){
//        cameraActivity.list.remove(position);
//        if(getItemCount() != 0) {
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, cameraActivity.list.size());
//        }else
//            notifyDataSetChanged();
    }
}

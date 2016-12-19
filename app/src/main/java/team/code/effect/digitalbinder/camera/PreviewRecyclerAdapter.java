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
    PreviewActivity previewActivity;

    public PreviewRecyclerAdapter(PreviewActivity previewActivity) {
        this.previewActivity = previewActivity;
    }

    //새로운 뷰 생성
    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photo_view, parent, false);
        return new PreviewViewHolder(view);
    }

    //리스트뷰에서 getView 메서드와 동일.
    @Override
    public void onBindViewHolder(PreviewViewHolder holder, final int position) {
        new PreviewAsync(holder.photo_view).execute(previewActivity.list.get(position).path.toString());
    }

    @Override
    public int getItemCount() {
        return previewActivity.list.size();
    }

}

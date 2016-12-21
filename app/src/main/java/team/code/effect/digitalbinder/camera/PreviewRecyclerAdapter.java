package team.code.effect.digitalbinder.camera;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AlertHelper;
import team.code.effect.digitalbinder.common.MediaStorageHelper;

public class PreviewRecyclerAdapter extends RecyclerView.Adapter<PreviewViewHolder>{
    PreviewActivity previewActivity;
    int selectedPosition;

    public PreviewRecyclerAdapter(PreviewActivity previewActivity) {
        this.previewActivity = previewActivity;
        this.selectedPosition = 0;
    }

    //새로운 뷰 생성
    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false);
        return new PreviewViewHolder(view);
    }

    //리스트뷰에서 getView 메서드와 동일.
    @Override
    public void onBindViewHolder(final PreviewViewHolder holder, int position) {
        int image_id = previewActivity.list.get(position).image_id;
        int orientation = previewActivity.list.get(position).orientation;

        new AsyncTask<Integer, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Integer... params) {
                if(previewActivity.isFinishing())
                    cancel(true);
                return MediaStorageHelper.getThumbnail(previewActivity, params[0], params[1]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                holder.iv_thumbnail.setImageBitmap(bitmap);
                holder.ib_remove.setVisibility(View.VISIBLE);
                holder.layout_outline.setBackgroundResource(R.color.colorAccent);
                super.onPostExecute(bitmap);
            }
        }.execute(image_id, orientation);

        if(position == selectedPosition)
            holder.isSelected = true;
        else
            holder.isSelected = false;

        if(holder.isSelected){
            holder.iv_thumbnail.setPadding(6, 6, 6 ,6);
        }else{
            holder.iv_thumbnail.setPadding(0, 0, 0 ,0);
        }

        holder.iv_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPosition == holder.getAdapterPosition())
                    return;
                selectedPosition = holder.getAdapterPosition();
                previewActivity.view_pager.setCurrentItem(holder.getAdapterPosition(),true);
                notifyDataSetChanged();
            }
        });

        holder.ib_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = AlertHelper.getAlertDialog(previewActivity, "알림", "선택한 사진이 삭제됩니다. 계속하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String filePath = previewActivity.list.get(holder.getAdapterPosition()).path.getPath();
                        String image_id = Integer.toString(previewActivity.list.get(holder.getAdapterPosition()).image_id);
                        Log.d("PreviewRecyclerAdapter", "file path:"+filePath);
                        //리스트에서 삭제
                        previewActivity.list.remove(holder.getAdapterPosition());
                        //어댑터 리프레쉬
                        notifyItemRangeChanged(0, previewActivity.list.size());
                        notifyDataSetChanged();
                        previewActivity.previewPagerAdapter.notifyDataSetChanged();

                        //파일 삭제
                        new AsyncTask<String, Void, Void>(){
                            @Override
                            protected Void doInBackground(String... params) {
                                if(previewActivity.isFinishing())
                                    cancel(true);
                                File file = new File(params[0]);
                                if(file.delete()){
                                    MediaStorageHelper.deleteOne(previewActivity, params[1]);
                                }
                                return null;
                            }
                        }.execute(filePath, image_id);
                        //리스트 사이즈가 0이면 액티비티 종료
                        if(previewActivity.list.size() == 0)
                            previewActivity.onBackPressed();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return previewActivity.list.size();
    }

}

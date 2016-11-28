package team.code.effect.digitalbinder.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import team.code.effect.digitalbinder.R;

public class PreviewViewHolder extends RecyclerView.ViewHolder{
    String TAG;
    ImageView iv_thumbnail;
    TextView txt_index;
    ImageButton btn_remove;
    PreviewRecyclerAdapter adapter;

    public PreviewViewHolder(View itemView) {
        super(itemView);
        TAG = getClass().getName();
        this.iv_thumbnail = (ImageView)itemView.findViewById(R.id.iv_thumbnail);
        this.txt_index = (TextView)itemView.findViewById(R.id.txt_index);
        this.btn_remove = (ImageButton)itemView.findViewById(R.id.btn_remove);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "getAdapterPosition(): "+getAdapterPosition());
                Preview preview = CameraActivity.list.get(getAdapterPosition());
               //Bitmap bitmap = CameraActivity.byteToBitmap(preview.getBytes(), 1080, 1920, preview.getOrientation());
                //CameraActivity.iv_selected.setImageBitmap(bitmap);
            }
        });
    }
}

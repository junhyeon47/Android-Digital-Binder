package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import team.code.effect.digitalbinder.R;

/**
 * Created by huho on 2016-11-26.
 */

public class PhotoItem extends RelativeLayout {
    ImageView img;
    String TAG;
    Bitmap bitmap;
    public PhotoItem(Context context, Bitmap file) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_photo,this);
        TAG=getClass().getName();
        img = (ImageView)findViewById(R.id.img);
        init(file);
    }

    public void init(Bitmap file){
          //  Log.d(TAG,"파일 경로는 "+file.getAbsolutePath());
          //  bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
             bitmap=file;
            img.setImageBitmap(bitmap);

    }

}

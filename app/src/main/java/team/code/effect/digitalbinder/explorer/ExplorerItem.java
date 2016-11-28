package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import team.code.effect.digitalbinder.R;

import static android.R.attr.bitmap;
import static android.R.attr.height;

/**
 * Created by 재우 on 2016-11-26.
 */

public class ExplorerItem extends LinearLayout {
    String TAG;
    Context context;
    Explorer explorer;
    ImageView ex_img;

    public ExplorerItem(Context context, Explorer explorer) {
        super(context);
        TAG = this.getClass().getName();
        this.context = context;
        this.explorer = explorer;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_photo_explorer, this);
        ex_img = (ImageView) findViewById(R.id.ex_img);
        setExplorerImg(explorer);
    }

    public void setExplorerImg(Explorer explorer) {
        this.explorer = explorer;
        setImg();
    }

    public void setImg() {
        //Bitmap bitmap= BitmapFactory.decodeFile(storageCamera+"/"+cameraFiles[0].getName());
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=16;
        Bitmap bitmap = BitmapFactory.decodeFile(explorer.getFilename(), options);
        Bitmap resize=Bitmap.createScaledBitmap(bitmap, 450, 450, true);

        //ex_img.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 100, true));
        ex_img.setImageBitmap(resize);
    }


}

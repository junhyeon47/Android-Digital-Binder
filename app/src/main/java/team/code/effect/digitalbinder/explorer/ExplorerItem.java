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
        TAG=this.getClass().getName();
        this.context=context;
        this.explorer=explorer;
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_photo_explorer, this);
        ex_img=(ImageView)findViewById(R.id.ex_img);
        setExplorerImg(explorer);
    }

    public void setExplorerImg(Explorer explorer){
        this.explorer=explorer;
        setImg();
    }

    public void setImg(){
        File dir=new File(explorer.getFilename());
        File[] photo=dir.listFiles();
        //Bitmap bitmap= BitmapFactory.decodeFile(storageCamera+"/"+cameraFiles[0].getName());
        for(int i=0; i<photo.length;i++) {

            Bitmap bitmap = BitmapFactory.decodeFile(dir + "/" + photo[i].getName());
            ex_img.setImageBitmap(bitmap);
        }
        Log.d(TAG, "길이"+photo.length);
    }



}

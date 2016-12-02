package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import team.code.effect.digitalbinder.R;

import static android.R.attr.bitmap;
import static android.R.attr.height;

/**
 * Created by 재우 on 2016-11-26.
 */

public class ExplorerItem{
    private ImageView imageView;


    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

}

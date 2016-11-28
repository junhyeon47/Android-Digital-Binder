package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import team.code.effect.digitalbinder.R;

/**
 * Created by student on 2016-11-28.
 */

public class PhotobookCheckboxItem extends LinearLayout{
    CheckBox checkBox;
    TextView txt_title;
    Photobook photobook;
    Boolean flag=false;
    public PhotobookCheckboxItem(Context context, Photobook photobook) {
        super(context);
        this.photobook= photobook;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_checkbox_photobook,this);
        checkBox =(CheckBox)findViewById(R.id.checkBox);
        txt_title=(TextView)findViewById(R.id.txt_title);
        init(photobook);
    }
    public PhotobookCheckboxItem(Context context, Photobook photobook,boolean flag) {
        super(context);
        this.photobook= photobook;
        this.flag=flag;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_checkbox_photobook,this);
        checkBox =(CheckBox)findViewById(R.id.checkBox);
        txt_title=(TextView)findViewById(R.id.txt_title);
        init(photobook);
    }


    public void init(Photobook photobook){
        showCheckBox(flag);
        txt_title.setText(photobook.getTitle());
    }

    public void showCheckBox(Boolean flag){
        if(flag) {
            checkBox.setVisibility(View.VISIBLE);
        }else {
            checkBox.setVisibility(View.GONE);
        }
    }



}

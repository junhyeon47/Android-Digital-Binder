package team.code.effect.digitalbinder.common;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import team.code.effect.digitalbinder.R;

public class ColorPaletteRecyclerAdapter extends RecyclerView.Adapter<ColorPaletteViewHolder>{
    private ArrayList<ColorPalette> list;
    @Override
    public ColorPaletteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color_palette, parent, false);
        return new ColorPaletteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorPaletteViewHolder holder, int position) {
        final ColorPalette colorPalette = list.get(position);
        final int index = position;
        holder.layout_palette.setBackgroundResource(colorPalette.getColorValue());
        holder.layout_palette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = colorPalette.isCheck();
                Log.d("Adapter", "click: "+b);
                if(!b) {
                    colorPalette.setCheck(!b);
                    for (int i = 0; i < list.size(); ++i) {
                        if (i == index)
                            continue;
                        list.get(i).setCheck(false);
                    }
                }else{
                    colorPalette.setCheck(false);
                }
                notifyDataSetChanged();
            }
        });

        if(colorPalette.isCheck())
            holder.iv_check.setVisibility(View.VISIBLE);
        else
            holder.iv_check.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<ColorPalette> getList() {
        return list;
    }

    public void setList(ArrayList<ColorPalette> list) {
        this.list = list;
    }
}

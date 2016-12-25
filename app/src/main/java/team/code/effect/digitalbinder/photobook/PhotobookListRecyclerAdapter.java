package team.code.effect.digitalbinder.photobook;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.ColorPaletteHelper;
import team.code.effect.digitalbinder.common.Photobook;
import team.code.effect.digitalbinder.main.MainActivity;

public class PhotobookListRecyclerAdapter extends RecyclerView.Adapter<PhotobookListViewHolder> {
    PhotobookListActivity photobookListActivity;

    boolean isDeleteMemuClicked = false;

    public PhotobookListRecyclerAdapter(PhotobookListActivity photobookListActivity) {
        this.photobookListActivity = photobookListActivity;
    }

    @Override
    public PhotobookListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photobook, parent, false);
        return new PhotobookListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotobookListViewHolder holder, int position) {
        final Photobook photobook = photobookListActivity.list.get(position);
        String title = photobook.getTitle();
        StringBuffer sb = new StringBuffer();
        String regdate = photobook.getRegdate().substring(0, 11);

        //포토북 제목 세로로 출력.
        for(int i=0; i<title.length(); ++i){
            sb.append(title.charAt(i));
            if(i != title.length()-1){
                sb.append("\n");
            }
        }


        holder.txt_title.setText(sb.toString());
        holder.txt_count.setText(Integer.toString(photobook.getNumber()));
        holder.txt_regdate.setText(regdate.replaceFirst("-", "\n").replaceFirst("-", "."));
        holder.layout_photobook.setBackgroundResource(ColorPaletteHelper.VALUE[photobook.getColor()]);
        holder.layout_photobook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDeleteMemuClicked) {
                    Intent intent = new Intent(photobookListActivity, PhotobookActivity.class);
                    intent.putExtra("photobook_id", photobook.getPhotobook_id());
                    photobookListActivity.startActivity(intent);
                }else{
                    holder.checkBox.setChecked(!holder.checkBox.isChecked());
                }
            }
        });
        holder.ib_bookmark_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photobook.setBookmark(0); //false;
                MainActivity.db.beginTransaction();
                MainActivity.dao.update(photobook);
                photobookListActivity.list = (ArrayList)MainActivity.dao.selectAll();
                MainActivity.db.setTransactionSuccessful();
                MainActivity.db.endTransaction();
                notifyDataSetChanged();
            }
        });
        holder.ib_bookmark_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photobook.setBookmark(1); //true;
                MainActivity.db.beginTransaction();
                MainActivity.dao.update(photobook);
                photobookListActivity.list = (ArrayList)MainActivity.dao.selectAll();
                MainActivity.db.setTransactionSuccessful();
                MainActivity.db.endTransaction();
                notifyDataSetChanged();
            }
        });

        if(photobook.getBookmark() == 0){
            holder.ib_bookmark_false.setVisibility(View.GONE);
            holder.ib_bookmark_true.setVisibility(View.VISIBLE);
        }else{
            holder.ib_bookmark_false.setVisibility(View.VISIBLE);
            holder.ib_bookmark_true.setVisibility(View.GONE);
        }

        if(!isDeleteMemuClicked) {
            holder.checkBox.setChecked(false);
            holder.checkBox.setVisibility(View.GONE);
            if(photobook.getBookmark() == 0){
                holder.ib_bookmark_false.setVisibility(View.GONE);
                holder.ib_bookmark_true.setVisibility(View.VISIBLE);
            }else{
                holder.ib_bookmark_false.setVisibility(View.VISIBLE);
                holder.ib_bookmark_true.setVisibility(View.GONE);
            }
        }else {
            photobookListActivity.checkedList.removeAll(photobookListActivity.checkedList);
            holder.checkBox.setChecked(false);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.ib_bookmark_false.setVisibility(View.GONE);
            holder.ib_bookmark_true.setVisibility(View.GONE);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(!photobookListActivity.checkedList.contains(photobook)){
                        photobookListActivity.checkedList.add(photobook);
                    }
                }else{
                    if(photobookListActivity.checkedList.contains(photobook)){
                        photobookListActivity.checkedList.remove(photobook);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return photobookListActivity.list.size();
    }
}

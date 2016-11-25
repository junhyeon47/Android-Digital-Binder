package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by student on 2016-11-25.
 */

public class PhotobookDAO {
    PhotobookOpenHelper helper;
    SQLiteDatabase db;

    public PhotobookDAO(Context context) {
        helper = new PhotobookOpenHelper(context,"digitalBinder.sqlite",null,1);
        db = helper.getWritableDatabase();

    }

    public void insert(Photobook photobook){
        String sql="insert into photobook(title,filename,icon) values(?,?,?)";
        db.execSQL(sql,new String[]{
                photobook.getTitle(),
                photobook.getFilename(),
                photobook.getIcon()
        });

    }

    public Photobook select(int photobook_id){

        return null;
    }

    public List selectAll(){
        String sql="select * from photobook order by regdate desc";
        db.rawQuery(sql,null);
        return null;
    }

    public void delete(int photobook_id){

    }
    public void update(Photobook photobook){

    }
}

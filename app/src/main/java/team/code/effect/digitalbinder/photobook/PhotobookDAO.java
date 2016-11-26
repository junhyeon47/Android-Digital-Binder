package team.code.effect.digitalbinder.photobook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 2016-11-25.
 */

public class PhotobookDAO {
    String TAG;
    SQLiteDatabase db;

    public PhotobookDAO(SQLiteDatabase db) {
        this.db = db;
        TAG=getClass().getName();
    }
    //Photobook 1건 추가
    public void insert(Photobook photobook){
        String sql="insert into photobook(title,filename,icon) values(?,?,?)";
       db.execSQL(sql,new String[]{
                photobook.getTitle(),
                photobook.getFilename(),
                photobook.getIcon()
        });
        Log.d(TAG,"insert");
    }
    //Photobook 1건 가져오기
    public Photobook select(int photobook_id){
        String sql="select * from photobook where photobook_id=?";
        Cursor rs=db.rawQuery(sql,new String[]{ Integer.toString(photobook_id)});
        rs.moveToNext();
        Photobook photobook = new Photobook();
        photobook.setPhotobook_id(rs.getInt(rs.getColumnIndex("photobook_id")));
        photobook.setTitle(rs.getString(rs.getColumnIndex("title")));
        photobook.setFilename(rs.getString(rs.getColumnIndex("filename")));
        photobook.setIcon(rs.getString(rs.getColumnIndex("icon")));
        photobook.setRegdate(rs.getString(rs.getColumnIndex("regdate")));
        return photobook;
    }
    //Photobook 모두 가져오기
    public List selectAll(){
        String sql="select * from photobook order by photobook_id asc";
        Cursor rs=db.rawQuery(sql,null);
        List list = new ArrayList();
        while(rs.moveToNext()){
            Photobook photobook = new Photobook();
            photobook.setPhotobook_id(rs.getInt(rs.getColumnIndex("photobook_id")));
            photobook.setTitle(rs.getString(rs.getColumnIndex("title")));
            photobook.setFilename(rs.getString(rs.getColumnIndex("filename")));
            photobook.setIcon(rs.getString(rs.getColumnIndex("icon")));
            photobook.setRegdate(rs.getString(rs.getColumnIndex("regdate")));
            list.add(photobook);
        }
        Log.d(TAG,"selectAll"+list.size());
        return list;
    }
    //Photobook 1건 삭제하기
    public void delete(int photobook_id){
        String sql="delete from photobook where photobook_id =?";
        db.execSQL(sql,new Integer[]{photobook_id});
        Log.d(TAG,"delete");
    }
    //Photobook 1건 수정
    public void update(Photobook photobook){
        String sql="update photobook set title=?,filename=?,icon=? where photobook_id=?";
        db.execSQL(sql,new Object[]{
                photobook.getTitle(),
                photobook.getFilename(),
                photobook.getIcon(),
                photobook.getPhotobook_id()
        });
        Log.d(TAG,"update");
    }
}

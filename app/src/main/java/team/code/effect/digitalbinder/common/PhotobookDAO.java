package team.code.effect.digitalbinder.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PhotobookDAO {
    String TAG;
    SQLiteDatabase db;

    public PhotobookDAO(SQLiteDatabase db) {
        this.db = db;
        TAG=getClass().getName();
    }
    //Photobook 1건 추가
    public void insert(Photobook photobook) {
        String sql = "insert into photobook(title, filename, color, number) values(?,?,?,?)";
        db.execSQL(sql, new Object[]{
                photobook.getTitle(),
                photobook.getFilename(),
                photobook.getColor(),
                photobook.getNumber()
        });
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
        photobook.setColor(rs.getInt(rs.getColumnIndex("color")));
        photobook.setBookmark(rs.getInt(rs.getColumnIndex("bookmark")));
        photobook.setRegdate(rs.getString(rs.getColumnIndex("regdate")));
        photobook.setNumber(rs.getInt(rs.getColumnIndex("number")));
        return photobook;
    }
    //Photobook 모두 가져오기
    public List selectAll(){
        String sql="select * from photobook order by bookmark desc, photobook_id asc";
        Cursor rs=db.rawQuery(sql,null);
        List list = new ArrayList();
        while(rs.moveToNext()){
            Photobook photobook = new Photobook();
            photobook.setPhotobook_id(rs.getInt(rs.getColumnIndex("photobook_id")));
            photobook.setTitle(rs.getString(rs.getColumnIndex("title")));
            photobook.setFilename(rs.getString(rs.getColumnIndex("filename")));
            photobook.setColor(rs.getInt(rs.getColumnIndex("color")));
            photobook.setBookmark(rs.getInt(rs.getColumnIndex("bookmark")));
            photobook.setRegdate(rs.getString(rs.getColumnIndex("regdate")));
            photobook.setNumber(rs.getInt(rs.getColumnIndex("number")));
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
        String sql="update photobook set title=?,filename=?,color=?, bookmark=? where photobook_id=?";
        db.execSQL(sql,new Object[]{
                photobook.getTitle(),
                photobook.getFilename(),
                photobook.getColor(),
                photobook.getBookmark(),
                photobook.getPhotobook_id()
        });
        Log.d(TAG,"update");
    }

    //파일 이름으로 중복 찾기.
    public boolean isDuplicatedTitle(String fileName){
        String sql = "select * from photobook where filename=?";
        Cursor rs = db.rawQuery(sql , new String[]{fileName});
        if(!rs.moveToNext())
            return false;
        else
            return true;
    }
}

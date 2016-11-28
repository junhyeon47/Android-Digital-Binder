package team.code.effect.digitalbinder.photobook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by student on 2016-11-25.
 */

public class PhotobookOpenHelper extends SQLiteOpenHelper{
    String TAG;

    public PhotobookOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        TAG=getClass().getName();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table photobook(");
        sql.append("photobook_id Integer primary key autoincrement");
        sql.append(",title varchar(30)");
        sql.append(",filename varchar(20)");
        sql.append(",icon varchar(20)");
        sql.append(",regdate timestamp DEFAULT CURRENT_TIMESTAMP");
        sql.append(");");

        db.execSQL(sql.toString());
        Log.d(TAG,"Database구축");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

}

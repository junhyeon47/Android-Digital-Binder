package team.code.effect.digitalbinder.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "digitalBinder_test.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance;
    private static SQLiteDatabase db;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

    public static SQLiteDatabase initialize(Context context){
        DatabaseHelper.getInstance(context);
        if(db == null) {
            db = DatabaseHelper.instance.getWritableDatabase();
        }
        return db;
    }
    public static void destory(){
        if(instance != null) {
            db.close();
            instance = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table photobook(");
        sql.append("photobook_id integer primary key autoincrement");
        sql.append(",title varchar(100)");
        sql.append(",filename varchar(100)");
        sql.append(",color int");
        sql.append(",regdate timestamp DEFAULT CURRENT_TIMESTAMP");
        sql.append(");");
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
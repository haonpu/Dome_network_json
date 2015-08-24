package Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by User on 2015/8/24.
 * author haosuo
 * 自定义帮助类，继承已有的
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{

    public final String CREATE_ID_RECORD = "create table Id_records (" +
            "id integer primary key autoincrement," +
            "id_num text," +
            "gender text," +
            "area text," +
            "birth_date text)" ;

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ID_RECORD);
        Toast.makeText(mContext,"create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists Id_records");
        onCreate(db);

    }
}

package app.creativestudio.dtu.truyenlalala.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

public class DatabaseSetup extends SQLiteOpenHelper {


    public DatabaseSetup(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseSetup(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DatabaseSetup(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // select
    public Cursor getData(String query){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(query,null);
    }
    // create, update , delete
    public boolean excuteQuery(String query) {
        try {
            try {
                SQLiteDatabase sqLiteDatabase = getWritableDatabase();
                sqLiteDatabase.execSQL(query);
                return true;
            } catch (SQLiteAbortException e) {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
    public SQLiteDatabase getSQLiteDatabase(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase;
    }
}

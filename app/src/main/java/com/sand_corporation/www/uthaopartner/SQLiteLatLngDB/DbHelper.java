package com.sand_corporation.www.uthaopartner.SQLiteLatLngDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 7/18/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_LALNG = "create table "+DbContract.TABLE_LATLNG +
            "("+DbContract.TABLE_LATLNG_FIRST_COLUMN_LATITUDE +" REAL,"+DbContract.TABLE_LATLNG_SECOND_COLUMN_LONGITUDE +" REAL);";
    private static final String DROP_TABLE_LATLNG = "drop table if exists " + DbContract.TABLE_LATLNG;

    public DbHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LALNG);
//        Log.i("LOG","DbHelper onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_LATLNG);
        onCreate(db);
//        Log.i("LOG","DbHelper onUpgrade");
    }

    public void saveLatLngToSQLiteDatabase(double latitude, double longitude,
                                           SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        //For contentValues put method, first we have to pass Column name & then column value.
        contentValues.put(DbContract.TABLE_LATLNG_FIRST_COLUMN_LATITUDE,latitude);
        contentValues.put(DbContract.TABLE_LATLNG_SECOND_COLUMN_LONGITUDE,longitude);
        db.insert(DbContract.TABLE_LATLNG,null,contentValues);

    }

    public Cursor retrieveLatLngFromQLiteDatabase(SQLiteDatabase db){
        String[] columns = {DbContract.TABLE_LATLNG_FIRST_COLUMN_LATITUDE,
                DbContract.TABLE_LATLNG_SECOND_COLUMN_LONGITUDE};
        //Here we will pass number of column which we want to see on the recycler view.
        //As we want to see all the column. we have passed those column name.
        Cursor cursor = db.query(DbContract.TABLE_LATLNG,columns,
                null,null,null,null,null,null);
//        Log.i("LOG","DbHelper readFromSQLiteDatabase");
        return cursor;
    }

}

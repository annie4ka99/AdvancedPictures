package com.example.advancedpictures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FavouritesDBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "favourites";
    private static final String DESCRIPTION = "description";
    private static final String URL = "url";
    private static final int DESCRIPTION_COLUMN = 1;
    private static final int URL_COLUMN = 2;

    private static SQLiteDatabase database;
    private static FavouritesDBHelper helper;

    static String getTableName() {
        return TABLE_NAME;
    }

    private FavouritesDBHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    static FavouritesDBHelper getInstance(Context context) {
        if (helper == null) {
            helper = new FavouritesDBHelper(context);
            database = helper.getWritableDatabase();
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (id integer primary key autoincrement, " + DESCRIPTION + " text, " + URL + " text);");
    }

    //get all data in cursor format
    private Cursor getData() {
        return database.rawQuery("select * from " + TABLE_NAME, null);
    }

    //check if the column exists
    private int getPictureInd(String description, String url) {
        Cursor cursor = getData();
        int ans = -1;
        while (cursor.moveToNext()) {
            if ((cursor.getString(DESCRIPTION_COLUMN)).equals(description) && (cursor.getString(URL_COLUMN)).equals(url)) {
                ans = cursor.getShort(0);
                break;
            }
        }
        cursor.close();
        return ans;
    }

    //check as observable
    Observable<Boolean> check(final String description, final String url) {
        Callable<Boolean> f = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return getPictureInd(description, url) != -1;
            }
        };
        return Observable.fromCallable(f).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    //get pictures as observable
    Observable<Picture[]> getPictures() {
        Callable<Picture[]> f = new Callable<Picture[]>() {
            @Override
            public Picture[] call(){
                Cursor cursor = getData();
                Picture[] ans = new Picture[cursor.getCount()];
                int ind = 0;
                while (cursor.moveToNext()) {
                    ans[ind] = new Picture(ind, cursor.getString(DESCRIPTION_COLUMN), cursor.getString(URL_COLUMN));
                    ind++;
                }
                cursor.close();
                return ans;
            }
        };
        return Observable.fromCallable(f).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    Observable<Boolean> delete(final String description, final String url) {
        Callable<Boolean> f = () -> {
            int ind = getPictureInd(description,url);
            if (ind == -1) {
                return false;
            }
            database.execSQL("delete from " + TABLE_NAME + " where id = " + Integer.toString(ind) + ";");
            return true;
        };
        return Observable.fromCallable(f).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    Observable<Boolean> add(final String description, final String url) {
        Callable<Boolean> f = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (FavouritesDBHelper.this.getPictureInd(description, url) == -1) {
                    database.close();
                    database = FavouritesDBHelper.this.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(DESCRIPTION, description);
                    cv.put(URL, url);
                    database.insert(TABLE_NAME, null, cv);
                    return true;
                }
                return false;
            }
        };
        return Observable.fromCallable(f).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
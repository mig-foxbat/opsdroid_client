package org.foxbat.opsdroid.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.foxbat.opsdroid.utils.AppAssetManager;
import org.foxbat.opsdroid.utils.AppObjectRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;


/**
 * Created by chlr on 9/23/14.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "opswise";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLEASSETPATH = "tables";
    private static Context context;


    public DatabaseHandler() {
        super(AppObjectRepository.getContext(), DatabaseHandler.DATABASE_NAME, null, DatabaseHandler.DATABASE_VERSION);
        DatabaseHandler.context = AppObjectRepository.getContext();
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            AssetManager assetMgr = context.getAssets();
            String[] tables = assetMgr.list(TABLEASSETPATH);
            for (String table : tables) {
                database.execSQL(getCreateTable(table.substring(0, table.indexOf('.'))));
            }
        }
        catch (IOException e) {
            Log.e(this.getClass().getName(),e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldversion, int newversion) {
      //  database.execSQL("DROP TABLE IF EXISTS " + DatabaseHandler.DATABASE_NAME);
    }

    public String getCreateTable(String tablename) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ");
        builder.append(tablename);
        builder.append("( _id integer primary key autoincrement");
        String jsoncontent = (new AppAssetManager()).readFileFromAsset(TABLEASSETPATH +"/"+ tablename + ".json");
        Log.v("temp1", jsoncontent);
        try {
            JSONObject json = new JSONObject(jsoncontent);
            Iterator<String> it = json.keys();
            while (it.hasNext()) {
                String key = it.next();
                String value = json.getString(key);
                builder.append("," + key + " " + value);
            }
            builder.append(");");
            return builder.toString();
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), e.getMessage());
            e.printStackTrace();
            return null;
        }


    }


    public void insertToTable(String tablename, JSONArray values) {
        SQLiteDatabase database = this.getWritableDatabase();
        JSONObject tableddl = null;
        try {
            tableddl = new JSONObject((new AppAssetManager()).readFileFromAsset(TABLEASSETPATH + "/" + tablename+".json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < values.length(); i++) {
            try {
                JSONObject value = values.getJSONObject(i);
                ContentValues cv = new ContentValues();
                Iterator<String> tableddlit = tableddl.keys();
                while (tableddlit.hasNext()) {
                    String key = tableddlit.next();
                    cv.put(key, value.getString(key));
                }
                database.insert(tablename, null, cv);
            } catch (JSONException e) {
                Log.e(this.getClass().getName(),e.getMessage());
            }
        }
    }


    public void insertToTable(String tablename,JSONObject value) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Iterator<String> it =  value.keys();
        while(it.hasNext()) {
            String key = it.next();
            try {cv.put(key,value.getString(key)); } catch (JSONException e) { e.printStackTrace(); }
        }
        database.insert(tablename,null,cv);
    }


    public Cursor executeSQL(String sql, String[] selectionargs) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor =  database.rawQuery(sql, selectionargs);
        return cursor;
    }

    public void execRawSQL(String sql) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(sql);
        database.close();
    }

}

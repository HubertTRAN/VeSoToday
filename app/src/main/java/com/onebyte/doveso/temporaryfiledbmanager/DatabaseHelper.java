package com.onebyte.doveso.temporaryfiledbmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.onebyte.doveso.controller.UIMonitorApplication;

import static com.onebyte.doveso.contants.Global.DB_FILE_VERSION;
import static com.onebyte.doveso.contants.Global.TEMPORARY_DB_FILE;


/**
 * Author: huy.tran@1byte.com
 * History: 2021/04/01
 * Project: veso.today
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String GIONG_NOI_TU_NHIEN = "GIONGNOITUNHIEN";
    public static final String LICH_SU_DO_SO = "LICHSUDOSO";
    public static final String LICH_XO_SO = "LICHXOSO";
    public static final String XO_SO_TRUYEN_THONG = "XOSOTRUYENTHONG";
    public static final String XO_SO_VIETLOT = "XOSOVIETLOT";
	public final static String TAG = "TemporaryFileDBManager";
	private static DatabaseHelper helper;
    private DatabaseHelper(Context context)
    {
        super(context, TEMPORARY_DB_FILE, null, DB_FILE_VERSION);
    }
    
    static DatabaseHelper getInstance(Context context)
    {
        if(helper == null)
        {
            helper = new DatabaseHelper(context.getApplicationContext());
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try {
            Log.d(TAG, "CREATE DATABASE");
            TemporaryFileDBManager.checkAndCreateDatabase(UIMonitorApplication.getContext());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
//        File f = new File(TEMP_FOLDER_DB + TEMPORARY_DB_FILE);
//        if (f.exists()) {
//            boolean deleteDB = f.delete();
//            Log.d("TemporaryFileDBManager", "deleteDB === "+ deleteDB);
//            if(deleteDB)
//            {
//                TemporaryFileDBManager.checkAndCreateDatabaseOld(UIMonitorApplication.getContext());
//            }
//        }
        onCreate(db);
    }

   public boolean checkTableExist(String tableName){
        SQLiteDatabase db = getReadableDatabase();

        if (tableName == null || db == null || !db.isOpen())
            return false;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }

        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}

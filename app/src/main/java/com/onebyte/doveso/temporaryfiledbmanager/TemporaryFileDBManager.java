package com.onebyte.doveso.temporaryfiledbmanager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.onebyte.doveso.contants.Global.TEMPORARY_DB_FILE;
import static com.onebyte.doveso.contants.Global.TEMP_FOLDER_DB;

/**
 * Author: huy.tran@1byte.com
 * History: 2021/04/01
 * Project: veso.today
 */
public class TemporaryFileDBManager {
	
	private final static String TAG = "TemporaryFileDBManager";
	DatabaseHelper dbHelper;
	SQLiteDatabase database;
	
    public TemporaryFileDBManager(Context context)
    {

    	dbHelper = DatabaseHelper.getInstance(context);
    }
    
    //---opens the database---
    public void open() throws SQLException
    {
    	database = dbHelper.getWritableDatabase();
    }

    //---closes the database---
    public void close()
    {
    	//dbHelper.close();
    }

	/**
	 * CheckAndCreateDatabase This is a method to check whether the database file in the assets directory has been added to the app or not.
	 * @param context
	 */
	public static void checkAndCreateDatabase(Context context)
	{

        File f = new File(TEMP_FOLDER_DB + TEMPORARY_DB_FILE);
		Log.d(TAG, "check file: "+ f.getAbsolutePath().toString());
        if (!f.exists()) {
        	//---make sure directory exists---
        	File directory = new File(TEMP_FOLDER_DB);
        	directory.mkdirs();
        	//---copy the db from the assets folder into 
            // the databases folder---
            try {
				CopyDB(context.getAssets().open(TEMPORARY_DB_FILE),
				        new FileOutputStream(TEMP_FOLDER_DB + TEMPORARY_DB_FILE));

			} catch (IOException e)
			{
				e.printStackTrace();
			}

			File existDB = new File(TEMP_FOLDER_DB + TEMPORARY_DB_FILE);
            
            if(existDB.exists())
            {
            	Log.d(TAG, "Successfully create database");
            }
        }
        else {
			Log.d(TAG, "f.exists()");
		}
	}

	/*public static void checkAndCreateDatabaseOld(Context context)
	{

		File f = new File(TEMP_FOLDER_DB + TEMPORARY_DB_FILE);
		Log.d(TAG, "check file: "+ f.getAbsolutePath().toString());
		if (!f.exists()) {
			Log.d(TAG, "check !f.exists(): "+ f.getAbsolutePath());
			//---make sure directory exists---
			File directory = new File(TEMP_FOLDER_DB);
			directory.mkdirs();
			//---copy the db from the assets folder into
			// the databases folder---
			try {
					CopyDB(context.getAssets().open(TEMPORARY_DB_FILE),
							new FileOutputStream(TEMP_FOLDER_DB + TEMPORARY_DB_FILE));

			} catch (IOException e)
			{
				e.printStackTrace();
			}

			File existDB = new File(TEMP_FOLDER_DB + TEMPORARY_DB_FILE);

			if(existDB.exists())
			{

				Log.d(TAG, "Successfully create database1");
			}
		}
		else {
			try {

				CopyDB(context.getAssets().open(TEMPORARY_DB_FILE),
						new FileOutputStream(TEMP_FOLDER_DB + TEMPORARY_DB_FILE));

			} catch (IOException e)
			{
				e.printStackTrace();
			}

			File existDB = new File(TEMP_FOLDER_DB + TEMPORARY_DB_FILE);

			if(existDB.exists())
			{
				Log.d(TAG, "Successfully create database f.exists()");
			}
			Log.d(TAG, "f.exists()");
		}
	}*/

	/**
	 * CopyDB This is a method to copy the database into the device's internal memory to be able to access, add, modify, and delete the database.
	 * @param inputStream file database in assets
	 * @param outputStream file database in app
	 * @throws IOException
	 */
	private static void CopyDB(InputStream inputStream, OutputStream outputStream)
			throws IOException
    {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
		Log.d(TAG, "CopyDB Successfully");
    }

}

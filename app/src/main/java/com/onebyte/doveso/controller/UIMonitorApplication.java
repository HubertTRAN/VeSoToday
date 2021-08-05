package com.onebyte.doveso.controller;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;


@SuppressLint("SimpleDateFormat")
public abstract class UIMonitorApplication extends Application {

	private static UIMonitorApplication instance;
	protected abstract void initialize();
    public UIMonitorApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

	@Override
	public void onCreate() {
		super.onCreate();
		initialize();
	}

	/*public class UIAppController extends UIMonitorApplication {

		private static final String TAG = "UIAppController";

		@Override
		protected void initialize()
		{

			// Monitor all interesting events
			try
			{
				Context context = getApplicationContext();
				//check and create database
				TemporaryFileDBManager.checkAndCreateDatabase(context);
			}
			catch(Exception e)
			{
				Log.d(TAG, e.getLocalizedMessage());
			}
		}

	}*/
	
}

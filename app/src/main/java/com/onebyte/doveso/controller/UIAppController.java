package com.onebyte.doveso.controller;

import android.content.Context;
import android.util.Log;

import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBManager;

/**
 * Author: Jame@jexpa.com
 * Class: UIAppController is monitor all interesting events
 * History: 2019/05/17
 * Project: SecondClone
 */
public class UIAppController extends UIMonitorApplication {

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

}
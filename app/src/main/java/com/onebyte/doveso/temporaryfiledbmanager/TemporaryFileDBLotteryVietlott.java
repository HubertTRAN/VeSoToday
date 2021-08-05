package com.onebyte.doveso.temporaryfiledbmanager;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.onebyte.doveso.model.LotterySchedule;

import java.util.ArrayList;
import java.util.List;


public class TemporaryFileDBLotteryVietlott extends TemporaryFileDBManager {

	private final String TAG = "DBManagerForAppLogNew";
	public final static int APP_LOG_NEW_NUMBER_SEND = 100;
	public TemporaryFileDBLotteryVietlott(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/*@SuppressLint("LongLogTag")
	public void appendAppManager(LotterySchedule lotterySchedule)
	{

		try {
			
			open();
			
			String sqlQuery = "insert into LICHXOSO (APP_NAME ,APP_ID, DATE_ACTION, IS_SENT) "
					+" select "+ "\"" + lotterySchedule.getThu() + "\", "
					+ "\"" + lotterySchedule.getDaiXoso() + "\", "
					+ "\"" + lotterySchedule.getMien() + "\", "
					+ lotterySchedule.getLinkRSS()
					+ " where not exists (select 1 from LICHXOSO where APP_ID=" + "\"" + lotterySchedule.getDaiXoso() + "\""
					+ ")";
			database.execSQL(sqlQuery);
			close();
		} 
		catch (Exception e)
		{		
			Log.d(TAG,e.getMessage()+"");
		}
	}*/

	/**
	 * appHistoryDBReadWithTime This is the method that gets the list of installed apps and is saved in SQLite with the condition fromTime to toTime.
	 * @return
	 */
	public List<LotterySchedule> LotteryScheduleDBReadWithTime() {

		List<LotterySchedule> lotteryScheduleArrayList = new ArrayList<>();
		try {

			String sqlQuery = "select ROW_ID,THU, DAI_XO_SO, MIEN_XO_SO, LINK_RSS from LICHXOSO";
			
			Log.d(TAG,sqlQuery);
			
			open();
			
			Cursor c = database.rawQuery(sqlQuery, null);
			
			if (c.moveToFirst()) {
				do 
				{
					Log.d("TemporaryFileDBManager", "add");
					LotterySchedule lotterySchedule = this.cursorToApp(c);
					lotteryScheduleArrayList.add(lotterySchedule);
						
				} while (c.moveToNext());
			}
			
			close();
			c.close();
			
		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}
		
		return lotteryScheduleArrayList;
	}

	/*public void updateAppByRecordArray(List<LotterySchedule> apps)
	{

		try {

			if (apps == null)
				return;
			if (apps.size() == 0)
				return;

			open();

			String sqlQuery = "UPDATE LICHXOSO SET IS_SENT = 1 WHERE ";

			int countApp = apps.size();

			for( int i = 0; i < countApp; i ++)
			{
				LotterySchedule aContact = apps.get(i);

				if(aContact != null)
				{
					String aRowId = aContact.getDaiXoso();
					if (i > 0)
					{
						sqlQuery = sqlQuery + " or ";
					}

					sqlQuery = sqlQuery + " rowId = " + aRowId;

				}
			}
			database.execSQL(sqlQuery);
			close();
		}
		catch (Exception e)
		{
			e.getMessage();
		}
	}*/
	
	private LotterySchedule cursorToApp(Cursor cursor) {

		//ROW_ID,THU, DAI_XO_SO, MIEN_XO_SO, LINK_RSS
		LotterySchedule lotterySchedule = new LotterySchedule();
		lotterySchedule.setRow_ID(cursor.getInt(0));
		lotterySchedule.setTHU(cursor.getString(1));
		lotterySchedule.setDAI_XO_SO(cursor.getString(2));
		lotterySchedule.setMIEN(cursor.getString(3));
		lotterySchedule.setLINK_RSS(cursor.getString(4));
	    return lotterySchedule;
	 }

}

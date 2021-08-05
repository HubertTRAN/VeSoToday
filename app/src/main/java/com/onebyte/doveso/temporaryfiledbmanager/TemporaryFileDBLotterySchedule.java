package com.onebyte.doveso.temporaryfiledbmanager;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.onebyte.doveso.model.LotterySchedule;
import java.util.ArrayList;
import java.util.List;


public class TemporaryFileDBLotterySchedule extends TemporaryFileDBManager {
	
	private final String TAG = "DBManagerForAppLogNew";
	private final String TABLE_NAME = "LICHXOSO";
	public final static int APP_LOG_NEW_NUMBER_SEND = 100;
	public TemporaryFileDBLotterySchedule(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/*public void appendAppManager(LotterySchedule lotterySchedule)
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
	 * LotteryScheduleDBReadWithTime Đây là phương thức giúp lấy ra danh sách lịch xổ số theo thứ
	 * Sử dụng cho việc chọn đài theo thứ, lấy kết quả từ trang web theo thứ, lấy kết quả của một thứ nhất định ví dụ thứ 2.
	 * @param checkDistinct có lấy ra tên thứ không trùng hay không
	 * @return
	 */
	public List<LotterySchedule> LotteryScheduleDBReadWithTime(boolean checkDistinct, String Day) {

		List<LotterySchedule> lotteryScheduleArrayList = new ArrayList<>();
		try {

			String sqlQuery;
			if(Day == null)
			{
				if(checkDistinct)
				{
					sqlQuery = "select distinct DAI_XO_SO, MIEN_XO_SO, LINK_RSS from " + TABLE_NAME +  " WHERE (MIEN_XO_SO like '2' " +
							"OR MIEN_XO_SO like '1' OR MIEN_XO_SO like '0')  ORDER BY THU";
				}else {
					sqlQuery = "select ROW_ID,THU, DAI_XO_SO, MIEN_XO_SO, LINK_RSS from " + TABLE_NAME +  " ORDER BY THU";
				}
			}else {
				sqlQuery = "select distinct DAI_XO_SO, MIEN_XO_SO, LINK_RSS from " + TABLE_NAME + " WHERE THU like '"+ Day +"'" +  " ORDER BY THU";
			}


			Log.d(TAG,sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add" );
					LotterySchedule lotterySchedule;
					if(checkDistinct)
					{
						lotterySchedule = this.cursorToApp(c, true);
					}else {
						lotterySchedule = this.cursorToApp(c, false);
					}
					lotteryScheduleArrayList.add(lotterySchedule);
					Log.d("TemporaryFileDBManager", "add = " + lotterySchedule.getDAI_XO_SO());
				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return lotteryScheduleArrayList;
	}

	/**
	 * LotteryScheduleDBReadWithTime Đây là phương thức giúp lấy ra danh sách lịch xổ số theo thứ
	 * Sử dụng cho việc chọn đài theo thứ, lấy kết quả từ trang web theo thứ, lấy kết quả của một thứ nhất định ví dụ thứ 2.
	 * @return
	 */
	public List<LotterySchedule> LotteryListDBReadWithTime( String Day, String mienXoSo) {

		List<LotterySchedule> lotteryScheduleArrayList = new ArrayList<>();
		try {

			String sqlQuery = "select distinct DAI_XO_SO, MIEN_XO_SO, LINK_RSS from " + TABLE_NAME + " WHERE THU like '"+ Day +"' AND (";
			if(mienXoSo.equals("0") || mienXoSo.equals("1") || mienXoSo.equals("2"))
			{
				//select distinct DAI_XO_SO, MIEN_XO_SO, LINK_RSS from LICHXOSO WHERE THU like 'Thu 7' AND MIEN_XO_SO like '0'
				sqlQuery += "MIEN_XO_SO like '" + mienXoSo +  "')";
			}
			else if(mienXoSo.equals("2,1")) // Nam, Trung
			{
				//select distinct DAI_XO_SO, MIEN_XO_SO, LINK_RSS from LICHXOSO WHERE THU like 'Thu 7' AND MIEN_XO_SO like '0'
				sqlQuery += "MIEN_XO_SO like '2' OR MIEN_XO_SO like '1')";
			}
			else if(mienXoSo.equals("1,0")) // Trung, Bắc
			{
				//select distinct DAI_XO_SO, MIEN_XO_SO, LINK_RSS from LICHXOSO WHERE THU like 'Thu 7' AND MIEN_XO_SO like '0'
				sqlQuery += "MIEN_XO_SO like '1' OR MIEN_XO_SO like '0')";
			}
			else if(mienXoSo.equals("2,0")) // Nam, Bắc
			{
				//select distinct DAI_XO_SO, MIEN_XO_SO, LINK_RSS from LICHXOSO WHERE THU like 'Thu 7' AND MIEN_XO_SO like '0'
				sqlQuery += "MIEN_XO_SO like '2' OR MIEN_XO_SO like '0')";
			}
			else { // Nam, Trung, Bắc
				//select distinct DAI_XO_SO, MIEN_XO_SO, LINK_RSS from LICHXOSO WHERE THU like 'Thu 7' AND MIEN_XO_SO like '0'
				sqlQuery += "MIEN_XO_SO like '2' OR MIEN_XO_SO like '1' OR MIEN_XO_SO like '0')";
			}

			Log.d("TemporaryFileDBManager", "sqlQuery ="+ sqlQuery );
			Log.d(TAG,sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add" );
					LotterySchedule lotterySchedule;
					lotterySchedule = this.cursorToApp(c, true);
					lotteryScheduleArrayList.add(lotterySchedule);

					Log.d("TemporaryFileDBManager", "addaaa = " + lotterySchedule.getDAI_XO_SO());
				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return lotteryScheduleArrayList;
	}

	/**
	 * LotteryScheduleDBReadWithTime
	 * @param checkDistinct
	 * @return
	 *//*
	public List<LotterySchedule> LotteryScheduleDBReadWithTime(boolean checkDistinct) {

		List<LotterySchedule> lotteryScheduleArrayList = new ArrayList<>();
		try {

			String sqlQuery;
			if(checkDistinct)
			{
				sqlQuery = "select distinct DAI_XO_SO, MIEN_XO_SO, LINK_RSS from " + TABLE_NAME +  " ORDER BY THU";
			}else {
				sqlQuery = "select ROW_ID,THU, DAI_XO_SO, MIEN_XO_SO, LINK_RSS from " + TABLE_NAME +  " ORDER BY THU";
			}

			Log.d(TAG,sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add" );
					LotterySchedule lotterySchedule;
					if(checkDistinct)
					{
						lotterySchedule = this.cursorToApp(c, true);
					}else {
						lotterySchedule = this.cursorToApp(c, false);
					}
					lotteryScheduleArrayList.add(lotterySchedule);
					Log.d("TemporaryFileDBManager", "add = " + lotterySchedule.getDAI_XO_SO());
				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return lotteryScheduleArrayList;
	}*/

	/**
	 * LotteryScheduleDBReadWithTime đây là phương thức lấy ra tên đài xổ số hoặc là tên miền xổ số
	 * @param date lấy ra dùng để gán vào Spinner để người dùng chọn đài xổ số
	 * @param domainLottery đưa domainLottery vào để lấy ra miền xổ số của tờ vé số 2 giá trị này phải có 1 giá trị null
	 * @return
	 */
	public List<String> LotteryScheduleDBReadProvince(String date, String domainLottery) {

		List<String> lotteryScheduleProvinceArrayList = new ArrayList<>();
		try {
			String sqlQuery;
			if(domainLottery == null)
			{
				sqlQuery = "select DAI_XO_SO from " + TABLE_NAME +
						" WHERE THU like '" + date + "' and (Mien_Xo_So <> '3') and (Mien_Xo_So <> '4')";
			}else {
				sqlQuery = "select MIEN_XO_SO from " + TABLE_NAME +
						" WHERE DAI_XO_SO like '" + domainLottery + "' and (Mien_Xo_So <> '3') and (Mien_Xo_So <> '4')";
			}

			Log.d(TAG,sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add" );
					lotteryScheduleProvinceArrayList.add(c.getString(0));
					Log.d("TemporaryFileDBManager", "add = " + c.getString(0));
				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return lotteryScheduleProvinceArrayList;
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
	
	private LotterySchedule cursorToApp(Cursor cursor, boolean checkDistinct) {

		//ROW_ID,THU, DAI_XO_SO, MIEN_XO_SO, LINK_RSS
		LotterySchedule lotterySchedule;

		if(checkDistinct)
		{
			lotterySchedule = new LotterySchedule(cursor.getString(0),cursor.getString(1), cursor.getString(2));
		}else {
			lotterySchedule = new LotterySchedule();
			lotterySchedule.setRow_ID(cursor.getInt(0));
			lotterySchedule.setTHU(cursor.getString(1));
			lotterySchedule.setDAI_XO_SO(cursor.getString(2));
			lotterySchedule.setMIEN(cursor.getString(3));
			lotterySchedule.setLINK_RSS(cursor.getString(4));
		}
	    return lotterySchedule;
	 }

}

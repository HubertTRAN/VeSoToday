package com.onebyte.doveso.temporaryfiledbmanager;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.onebyte.doveso.model.LotterySchedule;
import com.onebyte.doveso.model.NaturalVoice;

import java.util.ArrayList;
import java.util.List;


public class TemporaryFileDBNaturalVoice extends TemporaryFileDBManager {

	private final String TAG = "DBManagerForAppLogNew";
	private final String TABLE_VOICE = "GIONGNOITUNHIEN";
	public TemporaryFileDBNaturalVoice(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	/**
	 * appHistoryDBReadWithTime This is the method that gets the list of installed apps and is saved in SQLite with the condition fromTime to toTime.
	 * @return
	 */
	public List<NaturalVoice> naturalVoiceDBRead(String textFromVoice) {

		List<NaturalVoice> naturalVoiceList = new ArrayList<>();
		try {

			/*
				SELECT * from GIONGNOITUNHIEN
				WHERE instr("dong thap hom nay",CUM_TU) <> 0
			 */
			String sqlQuery = "SELECT * FROM "+ TABLE_VOICE
					+" WHERE instr('"+ textFromVoice +"',CUM_TU) <> 0";
			
			Log.d(TAG,sqlQuery);
			
			open();
			Cursor c = database.rawQuery(sqlQuery, null);
			
			if (c.moveToFirst()) {
				do 
				{
					Log.d("naturalVoiceDBRead", c.getString(2));
					NaturalVoice naturalVoice = this.cursorToApp(c);
					naturalVoiceList.add(naturalVoice);
						
				} while (c.moveToNext());
			}
			
			close();
			c.close();
			
		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}
		
		return naturalVoiceList;
	}
	
	private NaturalVoice cursorToApp(Cursor cursor) {

		//ROW_ID,CUM_TU, CUM_TU_CHINH_XAC, LOAI_TIM_KIEM, TU_NGU_TRONG_GIONG_NOI
		NaturalVoice naturalVoice = new NaturalVoice();
		naturalVoice.setROW_ID(cursor.getInt(0));
		naturalVoice.setCUM_TU(cursor.getString(1));
		naturalVoice.setCUM_TU_CHINH_XAC(cursor.getString(2));
		naturalVoice.setLOAI_TIM_KIEM(cursor.getString(3));
		naturalVoice.setTU_NGU_TRONG_GIONG_NOI(cursor.getInt(4));
	    return naturalVoice;
	 }

}

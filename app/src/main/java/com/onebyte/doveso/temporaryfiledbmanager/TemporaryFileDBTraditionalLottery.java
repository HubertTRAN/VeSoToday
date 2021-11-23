package com.onebyte.doveso.temporaryfiledbmanager;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.LinearLayout;

import com.onebyte.doveso.model.TraditionalLottery;
import java.util.ArrayList;
import java.util.List;

import static com.onebyte.doveso.api.ApiMethod.convertDateToMillisecond;
import static com.onebyte.doveso.api.ApiMethod.formatDateStart;
import static com.onebyte.doveso.api.ApiMethod.getSubDate;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_YYYY;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIENNAM_MIENTRUNG;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_BAC;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_NAM;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.KET_QUA_VIETLOTT_6_45;
import static com.onebyte.doveso.contants.Global.KET_QUA_VIETLOTT_6_55;


public class TemporaryFileDBTraditionalLottery extends TemporaryFileDBManager {

	private final String TAG = "DBManagerForAppLogNew";
	private final String TABLE_NAME = "XOSOTRUYENTHONG";
	private Context context;
	public TemporaryFileDBTraditionalLottery(Context context) {
		super(context);
		this.context = context;
	}

	public void appendTraditionalLottery(TraditionalLottery traditionalLottery)
	{

		try {
			
			open();
			String sqlQuery = "insert into " + TABLE_NAME +"(MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8, " +
					"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB) "
					+" select '"+ traditionalLottery.getMIEN_XO_SO() + "', "
					+ "'" + traditionalLottery.getTEN_DAI() + "', "
					+ "'" + traditionalLottery.getNGAY_XO_SO() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G8() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G7() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G6() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G5() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G4() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G3() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G2() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G1() + "', "
					+ "'" + traditionalLottery.getKET_QUA_DB() + "' "
					+ " where not exists (select 1 from " + TABLE_NAME + " where " +
					"TEN_DAI = " + "'" + traditionalLottery.getTEN_DAI() + "' AND NGAY_XO_SO = " + "'" + traditionalLottery.getNGAY_XO_SO() + "')";
			database.execSQL(sqlQuery);
			close();
		} 
		catch (Exception e)
		{		
			Log.d(TAG,e.getMessage()+"");
		}
	}

	/**
	 * TraditionalLotteryDBReadWithTime là phương thức lấy ra danh sách kết quả xổ số phân biệt theo loại muốn dò.
	 * @return
	 * @param domain domain = 1: vé số miền nam, miền trung, miền bắc; 2: vé số miền bắc; 3: vé số Vietlott 6/45; 4: vé số Vietlott 6/55
	 */
	public List<TraditionalLottery> TraditionalLotteryDBReadWithTime(int domain, String dateSelect) {

		List<TraditionalLottery> traditionalLotteryList = new ArrayList<>();
		try {

			String sqlQuery;

			if(dateSelect.isEmpty())
			{
				if(domain == KET_QUA_MIEN_NAM)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '2' ORDER BY NGAY_XO_SO DESC LIMIT 4";
				}else if(domain == KET_QUA_MIEN_TRUNG)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '1' ORDER BY NGAY_XO_SO DESC LIMIT 2";
				}
				else //if(domain == KET_QUA_MIEN_BAC)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '0' ORDER BY NGAY_XO_SO DESC LIMIT 1";
				}

			}else
			{
				if(domain == KET_QUA_MIEN_NAM)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '2' AND NGAY_XO_SO like '" + dateSelect + "'";
				}else if(domain == KET_QUA_MIEN_TRUNG)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '1' AND NGAY_XO_SO like '" + dateSelect + "'";
				}
				else if(domain == KET_QUA_MIEN_BAC)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '0' AND NGAY_XO_SO like '" + dateSelect + "'";
				}
				else if(domain == KET_QUA_VIETLOTT_6_45)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '3' AND NGAY_XO_SO like '" + dateSelect + "'";
				}
				else if(domain == KET_QUA_VIETLOTT_6_55)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '4' AND NGAY_XO_SO like '" + dateSelect + "'";
				}
				else {
					if(dateSelect.isEmpty())
					{
						sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
								"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
								"from "+ TABLE_NAME;
					}else {
						sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
								"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
								"from "+ TABLE_NAME + " Where NGAY_XO_SO like '" + dateSelect.replace("/","-") + "'";
					}

				}
			}


			Log.d("xxzs",sqlQuery);

			open();
			
			Cursor c = database.rawQuery(sqlQuery, null);
			
			if (c.moveToFirst()) {
				do 
				{
					Log.d("TemporaryFileDBManager", "add");
					TraditionalLottery traditionalLottery = this.cursorToApp(c);
					traditionalLotteryList.add(traditionalLottery);
						
				} while (c.moveToNext());
			}
			
			close();
			c.close();
			
		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}
		
		return traditionalLotteryList;
	}

	public TraditionalLottery TraditionalLotteryDBReadWithRowID(int rowID) {

		TraditionalLottery traditionalLottery = new TraditionalLottery();
		try {

			String sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
						"from "+ TABLE_NAME + " Where ROW_ID ="+rowID;

			Log.d("xxzs",sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					traditionalLottery = this.cursorToApp(c);

				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return traditionalLottery;
	}

    public List<TraditionalLottery>  deleteAllResultAfter30Day() {

		List<TraditionalLottery> traditionalLotteryList = new ArrayList<>();
        try {
			TraditionalLottery traditionalLottery = null;
			String sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
					"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB  FROM " + TABLE_NAME;
			open();
			Cursor c = database.rawQuery(sqlQuery, null);
			if (c.moveToFirst()) {
				do
				{
					traditionalLottery = this.cursorToApp(c);
					traditionalLotteryList.add(traditionalLottery);
					Log.d("xxzsa",traditionalLottery.getROW_ID() +"");
				} while (c.moveToNext());
			}
			Log.d("xxzsa",sqlQuery+ " " + traditionalLotteryList.size());
			close();
			c.close();
			return  traditionalLotteryList;
        } catch (Exception e) {
            Log.d(TAG,e.getMessage()+"");
        }
		return  traditionalLotteryList;
    }

	public void deleteAllResultAfter30DayStep1(List<Integer> listIDDelete) {

		try {

			if(listIDDelete.size() > 0)
			{
				open();
				for (int rowID : listIDDelete)
				{
					String sqlQuery1 = "DELETE FROM " + TABLE_NAME + " WHERE ROW_ID ="+ rowID;
					Log.d("xxzs",sqlQuery1);

					database.execSQL(sqlQuery1);
				}

			}
			close();
		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

	}


	/**
	 * getListNgayXoSo Đây là phương thức truy cập tới databases và lấy ra các ngày có kết quả xổ số của 3 miền chung.
	 * @return
	 */
	public List<Long> getListNgayXoSo() {

		List<Long> listNgayXoSo = new ArrayList<>();
		try {

			String sqlQuery = "select distinct NGAY_XO_SO from " + TABLE_NAME ;

			Log.d("xxzs",sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					long ngay_Xo_So = convertDateToMillisecond(c.getString(0));
					listNgayXoSo.add(ngay_Xo_So);
				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}
		return listNgayXoSo;
	}


	/**
	 * getListNgayXoSo3Mien Đây là phương thức truy cập tới databases và lấy ra các ngày có kết quả xổ số của từng miền riêng biệt.
	 * @return
	 */
	public List<Long> getListNgayXoSo3Mien(int mienXoSo) {

		List<Long> listNgayXoSo = new ArrayList<>();
		try {

			String sqlQuery = "select distinct NGAY_XO_SO from " + TABLE_NAME + " WHERE MIEN_XO_SO like '"+ mienXoSo +"' ORDER BY NGAY_XO_SO DESC" ;

			Log.d("xxzs",sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					long ngay_Xo_So = convertDateToMillisecond(c.getString(0));
					listNgayXoSo.add(ngay_Xo_So);
				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}
		return listNgayXoSo;
	}

	/**
	 * getTraditionalLotteryDBReadWithDomain Đây là phương thức lấy ra đối tượng TraditionalLottery tù ngày và tên đài mà người dùng muốn dò vé số.
	 * @param domainLottery
	 * @param dateLottery
	 * @return
	 */
	public TraditionalLottery getTraditionalLotteryDBReadWithDomain(String domainLottery, String dateLottery, boolean checkMienBac) {

		TraditionalLottery traditionalLottery = null;
		String sqlQuery;
		try {
				sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB "
						+ "from "+ TABLE_NAME + " Where ";
				if(checkMienBac)
				{
					sqlQuery += "Mien_XO_SO = "+ 0 + " and NGAY_XO_SO like '" + dateLottery + "'";
				}else {
					sqlQuery += "TEN_DAI like '"+ domainLottery + "' and NGAY_XO_SO like '" + dateLottery + "'";
				}

			Log.d("xxzs",sqlQuery);
			open();
			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					traditionalLottery = this.cursorToApp(c);
				} while (c.moveToNext());
			}
			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return traditionalLottery;
	}


	public int getLotteryFromDateSelected(String domainLottery, String dateLottery) {

		TraditionalLottery traditionalLottery = null;
		int cursor = 0;
		String sqlQuery;
		try {
			sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
					"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB "
					+ "from "+ TABLE_NAME + " Where ";
			if(dateLottery == null)
			{
				sqlQuery += "Mien_XO_SO = "+ 0 + " and NGAY_XO_SO like '" + dateLottery + "'";
			}else {
				sqlQuery += "TEN_DAI like '"+ domainLottery + "' and NGAY_XO_SO like '" + dateLottery + "'";
			}

			Log.d("xxzs",sqlQuery);
			open();
			Cursor c = database.rawQuery(sqlQuery, null);
			cursor = c.getCount();

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return cursor;
	}


	/**
	 * getTraditionalLotteryDBReadWithDomain Đây là phương thức lấy ra đối tượng TraditionalLottery tù ngày và tên đài mà người dùng muốn dò vé số.
	 * @param provinceLottery
	 * @param dateLottery
	 * @return
	 */
	public TraditionalLottery getTraditionalLotteryDBReadWithDomain(String provinceLottery, String dateLottery, String Domain) {

		TraditionalLottery traditionalLottery = null;
		String sqlQuery;
		try {
			sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
					"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB "
					+ "from "+ TABLE_NAME + " Where MIEN_XO_SO like '" + Domain + "' and TEN_DAI like '"+ provinceLottery + "' and NGAY_XO_SO like '" + dateLottery + "'";

			Log.d("xxzs",sqlQuery);
			open();
			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					traditionalLottery = this.cursorToApp(c);
				} while (c.moveToNext());
			}
			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return traditionalLottery;
	}


	/**
	 * CheckResultsLotteryDBReadWithTime lấy ra đối tượng đã trúng vé số của mã số, ngày, đài mà người dùng chọn.
	 */
	public TraditionalLottery CheckResultsLotteryDBReadWithTime(String codeLottery, int domain, String domainLottery, String dateLottery) {

		TraditionalLottery traditionalLottery = new TraditionalLottery();
		try {

			String sqlQuery;
			String KQ_G8MN_G7MB = codeLottery.substring(codeLottery.length()-2);
			String KQ_G7_G6MB = codeLottery.substring(codeLottery.length()-3);
			String KQ_G65MN_G45MB = codeLottery.substring(codeLottery.length()-4);
			String KQ_G4321MN_G321MB = codeLottery.substring(codeLottery.length()-5);
			String KQ_GDB_MN = codeLottery;
			String KQ_KK_MN = codeLottery.substring(0, codeLottery.length()-5);
			if(domain == KET_QUA_MIEN_NAM || domain == KET_QUA_MIEN_TRUNG)
			{
				int mien_xo_so = KET_QUA_MIEN_NAM;
				if(domain == KET_QUA_MIEN_TRUNG)
				{
					mien_xo_so = KET_QUA_MIEN_TRUNG;
				}

				//MIEN_XO_SO = '1' miền trung; MIEN_XO_SO = '2' miền nam
				sqlQuery = "SELECT * " +
						"FROM XOSOTRUYENTHONG " +
						"WHERE MIEN_XO_SO like '" + mien_xo_so + "'"+
						" AND NGAY_XO_SO like '" + dateLottery +
						"' AND TEN_DAI like '" + domainLottery +
						"' AND (KET_QUA_G8 like '"+ KQ_G8MN_G7MB + "' " + // đây là giải tám trúng 2 số cuối
						"or KET_QUA_G7 like '" + KQ_G7_G6MB + "' " + // đây là giải bảy trúng 3 số cuối
						"or KET_QUA_G6 like '%" + KQ_G65MN_G45MB + "%' " + // đây là giải sáu trúng 4 số cuối
						"or KET_QUA_G5 like '" + KQ_G65MN_G45MB + "' " + // đây là giải năm trúng 4 số cuối
						"or KET_QUA_G4 like '%" + KQ_G4321MN_G321MB + "%' " + // đây là giải tư trúng 5 số cuối
						"or KET_QUA_G3 like '%" + KQ_G4321MN_G321MB + "%' " + // đây là giải ba trúng 5 số cuối
						"or KET_QUA_G2 like '" + KQ_G4321MN_G321MB + "' " + // đây là giải nhì trúng 5 số cuối
						"or KET_QUA_G1 like '" + KQ_G4321MN_G321MB + "' " + // đây là giải nhất trúng 5 số cuối
						"or KET_QUA_DB like '%" + KQ_G4321MN_G321MB + "'" +// đây là giải phụ đặt biệt trúng 5 số cuối
						"or KET_QUA_DB like '" + KQ_GDB_MN + "')";
				//"or KET_QUA_DB like '%" + KQ_G4321MN_G321MB + "' " + // đây là giải phụ đặt biệt trúng 5 số cuối
				//"or KET_QUA_DB like '" + KQ_KK_MN + "%' " + // đây là giải khuyến khích, chúng tôi chỉ kiểm tra số đầu tiên nếu trùng thì sẽ tiếp tục lấy ra danh sách và kiểm tra 5 số còn lại.

			}
			else if(domain == KET_QUA_MIEN_BAC)
			{
				// MIEN_XO_SO = '0' là miền Bắc.
				sqlQuery = "SELECT * " +
						"FROM XOSOTRUYENTHONG " +
						"WHERE MIEN_XO_SO = '"+ KET_QUA_MIEN_BAC + "' " +
						" AND NGAY_XO_SO like '" + dateLottery + "'" +
						" AND " + "(KET_QUA_G7 like '%" + KQ_G8MN_G7MB + "%' " + // đây là giải bảy trúng 2 số cuối
						"or KET_QUA_G6 like '%" + KQ_G7_G6MB + "%' " + // đây là giải sáu trúng 3 số cuối
						"or KET_QUA_G5 like '%" + KQ_G65MN_G45MB + "%' " + // đây là giải năm trúng 4 số cuối
						"or KET_QUA_G4 like '%" + KQ_G65MN_G45MB + "%' " + // đây là giải tư trúng 4 số cuối
						"or KET_QUA_G3 like '%" + KQ_G4321MN_G321MB + "%' " + // đây là giải ba trúng 5 số cuối
						"or KET_QUA_G2 like '%" + KQ_G4321MN_G321MB + "%' " + // đây là giải nhì trúng 5 số cuối
						"or KET_QUA_G1 like '" + KQ_G4321MN_G321MB + "' " + // đây là giải nhất trúng 5 số cuối
						"or KET_QUA_DB like '%" + codeLottery.substring(3) + "'" + // đây là giải khuyến khích đặc biệt trúng 40.000đ
						"or KET_QUA_DB like '" + codeLottery + "') "; // đây là giải đặc biệt trúng 5 số cuối
			}
			else if(domain == KET_QUA_VIETLOTT_6_45)
			{

				sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
						"from "+ TABLE_NAME + " Where MIEN_XO_SO = '1'";

			}
			else
			{
				sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
						"from "+ TABLE_NAME + " Where MIEN_XO_SO like '1' or MIEN_XO_SO like '2'";
			}


			Log.d("xxzs",sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					traditionalLottery = this.cursorToApp(c);

				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return traditionalLottery;
	}

	/**
	 * CheckResultsLotteryDBReadWithTime đây là phương thức lấy danh sách đối tượng đã trúng giải với dữ liệu truyền vào codeLottery, domain
	 * @param codeLottery
	 * @param domain
	 * @return
	 */
	public List<TraditionalLottery> CheckResultsLotteryDBReadWithTime(String codeLottery, int domain) {

		List<TraditionalLottery> traditionalLotteryList = new ArrayList<>();
		try {

			String sqlQuery;
			String KQ_G8MN_G7MB = codeLottery.substring(codeLottery.length()-2);
			String KQ_G7_G6MB = codeLottery.substring(codeLottery.length()-3);
			String KQ_G65MN_G45MB = codeLottery.substring(codeLottery.length()-4);
			String KQ_G4321MN_G321MB = codeLottery.substring(codeLottery.length()-5);
			String KQ_GDB_MN = codeLottery;
			String KQ_KK_MN = codeLottery.substring(0, codeLottery.length()-5);
			if(domain == KET_QUA_MIENNAM_MIENTRUNG)
			{


				//MIEN_XO_SO = '1' miền trung; MIEN_XO_SO = '2' miền nam
				sqlQuery = "SELECT * " +
							"FROM XOSOTRUYENTHONG " +
							"WHERE (MIEN_XO_SO = '1' or MIEN_XO_SO = '2') " +
							"AND (KET_QUA_G8 like '"+ KQ_G8MN_G7MB + "' " + // đây là giải tám trúng 2 số cuối
							"or KET_QUA_G7 like '" + KQ_G7_G6MB + "' " + // đây là giải bảy trúng 3 số cuối
							"or KET_QUA_G6 like '%" + KQ_G65MN_G45MB + "%' " + // đây là giải sáu trúng 4 số cuối
							"or KET_QUA_G5 like '" + KQ_G65MN_G45MB + "' " + // đây là giải năm trúng 4 số cuối
							"or KET_QUA_G4 like '%" + KQ_G4321MN_G321MB + "%' " + // đây là giải tư trúng 5 số cuối
							"or KET_QUA_G3 like '%" + KQ_G4321MN_G321MB + "%' " + // đây là giải ba trúng 5 số cuối
							"or KET_QUA_G2 like '" + KQ_G4321MN_G321MB + "' " + // đây là giải nhì trúng 5 số cuối
							"or KET_QUA_G1 like '" + KQ_G4321MN_G321MB + "' " + // đây là giải nhất trúng 5 số cuối
							"or KET_QUA_DB like '%" + KQ_G4321MN_G321MB + "'" +// đây là giải phụ đặt biệt trúng 5 số cuối
							"or KET_QUA_DB like '" + KQ_GDB_MN + "')";
							//"or KET_QUA_DB like '%" + KQ_G4321MN_G321MB + "' " + // đây là giải phụ đặt biệt trúng 5 số cuối
							//"or KET_QUA_DB like '" + KQ_KK_MN + "%' " + // đây là giải khuyến khích, chúng tôi chỉ kiểm tra số đầu tiên nếu trùng thì sẽ tiếp tục lấy ra danh sách và kiểm tra 5 số còn lại.

			}
			else if(domain == KET_QUA_MIEN_BAC)
			{
				// MIEN_XO_SO = '0' là miền Bắc.
				sqlQuery = "SELECT * " +
							"FROM XOSOTRUYENTHONG " +
							"WHERE MIEN_XO_SO = '0' " + // Miền Bắc
							"AND " + "(KET_QUA_G7 like '%" + KQ_G8MN_G7MB + "%' " + // đây là giải bảy trúng 2 số cuối
							"or KET_QUA_G6 like '%" + KQ_G7_G6MB + "%' " + // đây là giải sáu trúng 3 số cuối
							"or KET_QUA_G5 like '%" + KQ_G65MN_G45MB + "%' " + // đây là giải năm trúng 4 số cuối
							"or KET_QUA_G4 like '%" + KQ_G65MN_G45MB + "%' " + // đây là giải tư trúng 4 số cuối
							"or KET_QUA_G3 like '%" + KQ_G4321MN_G321MB + "%' " + // đây là giải ba trúng 5 số cuối
							"or KET_QUA_G2 like '%" + KQ_G4321MN_G321MB + "%' " + // đây là giải nhì trúng 5 số cuối
							"or KET_QUA_G1 like '" + KQ_G4321MN_G321MB + "' " + // đây là giải nhất trúng 5 số cuối
							"or KET_QUA_DB like '%" + codeLottery.substring(3) + "'" + // đây là giải khuyến khích đặc biệt trúng 40.000đ
							"or KET_QUA_DB like '" + codeLottery + "') "; // đây là giải đặc biệt trúng 5 số cuối
			}
			else if(domain == KET_QUA_VIETLOTT_6_45)
			{

				sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
						"from "+ TABLE_NAME + " Where MIEN_XO_SO = '1'"; // Miền Nam

			}
			else
			{
				sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
						"from "+ TABLE_NAME + " Where MIEN_XO_SO = '1' or MIEN_XO_SO = '2'"; // Miền Trung, Miền Nam
			}

			/*String sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
					"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
					"from "+ TABLE_NAME;*/

			Log.d("xxzs",sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					TraditionalLottery traditionalLottery = this.cursorToApp(c);
					traditionalLotteryList.add(traditionalLottery);

				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return traditionalLotteryList;
	}

	//SELECT distinct NGAY_XO_SO FROM XOSOTRUYENTHONG ORDER BY NGAY_XO_SO  DESC
	
	private TraditionalLottery cursorToApp(Cursor cursor) {


		TraditionalLottery traditionalLottery = new TraditionalLottery();
		traditionalLottery.setROW_ID(cursor.getInt(0));
		traditionalLottery.setMIEN_XO_SO(cursor.getString(1));
		traditionalLottery.setTEN_DAI(cursor.getString(2));
		traditionalLottery.setNGAY_XO_SO(cursor.getString(3));
		traditionalLottery.setKET_QUA_G8(cursor.getString(4));
		traditionalLottery.setKET_QUA_G7(cursor.getString(5));
		traditionalLottery.setKET_QUA_G6(cursor.getString(6));
		traditionalLottery.setKET_QUA_G5(cursor.getString(7));
		traditionalLottery.setKET_QUA_G4(cursor.getString(8));
		traditionalLottery.setKET_QUA_G3(cursor.getString(9));
		traditionalLottery.setKET_QUA_G2(cursor.getString(10));
		traditionalLottery.setKET_QUA_G1(cursor.getString(11));
		traditionalLottery.setKET_QUA_DB(cursor.getString(12));
	    return traditionalLottery;
	 }

}

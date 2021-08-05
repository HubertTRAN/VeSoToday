package com.onebyte.doveso.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.onebyte.doveso.R;
import com.onebyte.doveso.model.LotteryResults;
import com.onebyte.doveso.model.LotterySchedule;
import com.onebyte.doveso.model.TraditionalLottery;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBLotterySchedule;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBTraditionalLottery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.List;
import static com.onebyte.doveso.api.ApiMethod.alertDialog;
import static com.onebyte.doveso.api.ApiMethod.convertDateToMillisecond;
import static com.onebyte.doveso.api.ApiMethod.convertLongToDate;
import static com.onebyte.doveso.api.ApiMethod.formatDate;
import static com.onebyte.doveso.api.ApiMethod.formatDateStart;
import static com.onebyte.doveso.api.ApiMethod.getDateNow;
import static com.onebyte.doveso.api.ApiMethod.getDayNow;
import static com.onebyte.doveso.api.ApiMethod.getMillisecondNow;
import static com.onebyte.doveso.api.ApiMethod.getSubDate;
import static com.onebyte.doveso.api.ApiMethod.isConnected;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_FORMAT;
import static com.onebyte.doveso.contants.Global.DEFAULT_HH;
import static com.onebyte.doveso.contants.Global.DEFAULT_HHmm;
import static com.onebyte.doveso.contants.Global.DEFAULT_HOUR_MINUTES;
import static com.onebyte.doveso.contants.Global.DEFAULT_MINUTES;
import static com.onebyte.doveso.contants.Global.DEFAULT_mm;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_BAC;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_NAM;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.MIEN_NAM;
import static com.onebyte.doveso.contants.Global.ONEDAY;

public class LaunchScreen extends AppCompatActivity {

    private TraditionalLottery traditionalLottery;
    private String giaiDB, giaiNhat, giaiNhi, giaiBa, giaiTu, giaiNam, giaiSau, giaiBay, giaiTam;
    private int demSizeDai, demTongDaiDaTai;
    public static boolean checkDownloaded;
    private int countDateSaved = 0;
    private ProgressDialog dialog;
    private String test = "test github";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check internet offline
        if(isConnected(getApplicationContext()))
        {
            // check đã lưu kết quả xổ số tới ngày hôm nay hay chưa.
            //boolean checkSaved = checkLotterySaved(false);

            String checkSavedHaveInternet = checkLotterySavedWhenInternet(); // lấy ra các miền còn thiếu kết quả xổ số: Saved, Failed, MNMBMT, MN, MT, MB.
            Log.d("TemporaryFileDBManager", "checkSaved = " + checkSavedHaveInternet);

            Log.d("TemporaryFileDBManager", "DEFAULT_HH = " + (Integer.parseInt(getDateNow(DEFAULT_HH)) >= 16) + " DEFAULT_mm = "+ (Integer.parseInt(getDateNow(DEFAULT_mm)) >= 45));

            boolean checkDownload30Date = false;
            if(!checkSavedHaveInternet.equals("Saved"))
            {
                Log.d("TemporaryFileDBManager", "countDateSaved = " + countDateSaved);
                TemporaryFileDBLotterySchedule temporaryFileDBManagerForLichXoSo = new TemporaryFileDBLotterySchedule(getApplicationContext());
                List<LotterySchedule> lotteryScheduleList;
                String THU = getDayNow(getDateNow(DEFAULT_DATE_SELECT_FORMAT));
                String dateDowloading = getDateNow(DEFAULT_DATE_SELECT_FORMAT);
                if((Integer.parseInt(getDateNow(DEFAULT_HHmm)) <= DEFAULT_HOUR_MINUTES))
                {
                    THU =  getDayNow(getSubDate(1));
                    dateDowloading = getSubDate(1);
                    Log.d("TemporaryFileDBManager", "getDayNow(getSubDate(1)) = " + getDayNow(getSubDate(1)));
                }
                if(!checkSavedHaveInternet.equals("Failed"))
                {
                    if(checkSavedHaveInternet.equals("MN")) // Miền Nam
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "2");
                    else if(checkSavedHaveInternet.equals("MT")) // Miền Trung
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "1");
                    else if(checkSavedHaveInternet.equals("MB")) // Miền Bắc
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "0");
                    else if(checkSavedHaveInternet.equals("MNMT")) // Miền Nam, Miền Trung
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "2,1");
                    else if(checkSavedHaveInternet.equals("MNMB")) // Miền Nam, Miền Bắc
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "2,0");
                    else if(checkSavedHaveInternet.equals("MTMB")) //Miền Trung, Miền Bắc
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "1,0");
                    else // Miền Nam, Miền Trung, Miền Bắc
                    {
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "2,1,0");
                        Log.d("TemporaryFileDBManager", "getDayNow(getSubDate(1)) = " + getDayNow(getSubDate(1)));
                    }

                }else
                {
                    checkDownload30Date = true;
                    // chưa đầy đủ 30 ngày nên cần tải lại từ đầu cho 3 miền.
                    lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryScheduleDBReadWithTime(true, null);
                }

                Log.d("TemporaryFileDBManager", "Check 4h30 SIZE = "+lotteryScheduleList.size());
                if(lotteryScheduleList.size() != 0)
                {
                    demSizeDai = lotteryScheduleList.size();
                    demTongDaiDaTai = 0;
                    checkDownloaded = false;
                    dialog = ProgressDialog.show(LaunchScreen.this, "",
                            getResources().getString(R.string.tai_du_lieu), true);


                    for (LotterySchedule lotterySchedule : lotteryScheduleList)
                    {
                        if(lotterySchedule.getLINK_RSS() != null)
                        {
                            if(!lotterySchedule.getLINK_RSS().isEmpty())
                            {
                                Log.d("TemporaryFileDBManager", "\r\r\r\r\r\r\r\r\rTên Đài = "+ lotterySchedule.getDAI_XO_SO());
                                ///RSSGetResultsLottery(lotterySchedule.getLINK_RSS(), lotterySchedule.getMIEN(), lotterySchedule.getDAI_XO_SO());
                                // domainLottery, URL, domain;
                                // new RSSGetResultsLottery(lotterySchedule.getDAI_XO_SO(), lotterySchedule.getLINK_RSS(), lotterySchedule.getMIEN()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                if(checkDownload30Date)
                                    new RSSGetResultsLottery(lotterySchedule.getDAI_XO_SO(), lotterySchedule.getLINK_RSS(), lotterySchedule.getMIEN(), null).execute();
                                else {
                                    new RSSGetResultsLottery(lotterySchedule.getDAI_XO_SO(), lotterySchedule.getLINK_RSS(), lotterySchedule.getMIEN(), dateDowloading).execute();
                                }
                            }
                        }
                    }
                }
            }
            else {
                //Toast.makeText(this, "Dữ liệu đã được tải từ trước.", Toast.LENGTH_SHORT).show();
                Log.d("TemporaryFileDBManager", "Dữ liệu đã được tải từ trước.");
                startActivityDashboard(50);
            }

           // startActivityDashboard();
        }
        else {
            Log.d("TemporaryFileDBManager", "no internet!");
            // no internet
            // check đã lưu kết quả xổ số tới ngày hôm nay hay chưa.
            checkLotterySaved(true);
            //Toast.makeText(this, "Hiện tại thiết bị của bạn đang bị mất kết nối internet. Bạn vui lòng kiểm tra và thử lại sau!", Toast.LENGTH_LONG).show();
            alertDialog(LaunchScreen.this,getResources().getString(R.string.ket_noi_mang_loi),getResources().getString(R.string.ket_noi_mang_loi_chi_tiet));
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( alertDialog!=null && alertDialog.isShowing() ){
            alertDialog.dismiss();
        }
        if ( dialog!=null && dialog.isShowing() ){
            dialog.dismiss();
        }

    }

    /**
     * checkLotterySaved Là phương thức kiểm tra khi không có internet
     * nếu không có kết quả của đủ 30 ngày thì không cho phép người dùng dò số vì sẽ sai kết quả.
     */
    private boolean checkLotterySaved(boolean noInternet) {
        List<Long> listNgayXoSo;
        TemporaryFileDBTraditionalLottery traditionalLotteryDB = new TemporaryFileDBTraditionalLottery(getApplicationContext());
        listNgayXoSo = traditionalLotteryDB.getListNgayXoSo();

        if(listNgayXoSo != null && listNgayXoSo.size() > 0)
        {
            // kiểm tra và đếm các ngày lớn hơn ngày hết hạn lãnh thưởng vé số tính đến hôm nay
            String ngayHetHang = getSubDate(30);
            long ngayHetHangLong = convertDateToMillisecond(ngayHetHang);
            int tongNgay = 0;
            for (long ngayLayRa : listNgayXoSo)
            {
                if(ngayLayRa >= ngayHetHangLong)
                {
                    tongNgay += 1;
                    Log.d("NgayHetHang", "ngayHetHang = " + ngayHetHang + "  ngayHetHangLong = " + ngayHetHangLong + " Ngay Lay Ra = "+ convertLongToDate(ngayLayRa)  + " ngayLayRa = "+ ngayLayRa);
                }
            }

            if((Integer.parseInt(getDateNow(DEFAULT_HHmm)) <= DEFAULT_HOUR_MINUTES) && tongNgay == 30)
            {
                countDateSaved = 30;
                //Toast.makeText(this, "Đã lưu đủ kết quả trong 30 ngày.", Toast.LENGTH_SHORT).show();
                if(noInternet)
                {
                    Log.d("TemporaryFileDBManager", "no internet! startActivityDashboard(50)");
                    // Start home activity
                    startActivityDashboard(50);
                }
               return true;
            }else if(Integer.parseInt(getDateNow(DEFAULT_HHmm)) >= DEFAULT_HOUR_MINUTES)
            {
                if(tongNgay == 30 || tongNgay == 31)
                {
                    countDateSaved = 30;
                }
                return false;
            }
            else {
                if(tongNgay == 30 || tongNgay == 31)
                {
                    countDateSaved = 30;
                    return false;
                }
                return false;
            }
        }else {
            return false;
        }
    }


    /**
     * checkLotterySavedWhenInternet Là phương thức kiểm tra hôm nay đã tới giờ xổ số chưa
     * nếu tới giờ xổ số thì miền nào đã có kết quả và tính toán để chỉ tải kết quả đã xổ của hôm nay để giúp ngắn quá trình tải
     */
    private String checkLotterySavedWhenInternet() {

        List<Long> listNgayXoSoMienNam, listNgayXoSoMienTrung, listNgayXoSoMienBac;
        TemporaryFileDBTraditionalLottery traditionalLotteryDB = new TemporaryFileDBTraditionalLottery(getApplicationContext());
        listNgayXoSoMienNam = traditionalLotteryDB.getListNgayXoSo3Mien(KET_QUA_MIEN_NAM);
        listNgayXoSoMienTrung = traditionalLotteryDB.getListNgayXoSo3Mien(KET_QUA_MIEN_TRUNG);
        listNgayXoSoMienBac = traditionalLotteryDB.getListNgayXoSo3Mien(KET_QUA_MIEN_BAC);

        if(listNgayXoSoMienNam.size() > 0
                && listNgayXoSoMienTrung.size() > 0
                && listNgayXoSoMienBac.size() > 0)
        {

            int tongNgayMN = getSumDate(listNgayXoSoMienNam);
            int tongNgayMT = getSumDate(listNgayXoSoMienTrung);;
            int tongNgayMB = getSumDate(listNgayXoSoMienBac);;

            if((Integer.parseInt(getDateNow(DEFAULT_HHmm)) <= DEFAULT_HOUR_MINUTES))
            {
                // if(tongNgayMN == 30 && tongNgayMT == 30 && tongNgayMB == 30)
                if(tongNgayMT == 30 && tongNgayMB == 30)
                {
                    countDateSaved = 30;
                    return "Saved";
                }else
                    {
                    String failed ="";
                    /*if(tongNgayMN <30)
                        failed += "MN"; // Miền Nam chưa đầy đủ kết quả xổ số*/
                    if(tongNgayMT <30)
                        failed += "MT"; // Miền Trung chưa đầy đủ kết quả xổ số
                    if(tongNgayMB <30)
                        failed += "MB"; // Miền Bắc chưa đầy đủ kết quả xổ số

                    return failed;
                }

            } else
            {
                //if(tongNgayMN >= 30 && tongNgayMT >= 30 && tongNgayMB >= 30)
                if(tongNgayMT >= 30 && tongNgayMB >= 30)
                {
                    if(Integer.parseInt(getDateNow(DEFAULT_HH)) >= 18)
                    {
                        if(Integer.parseInt(getDateNow(DEFAULT_HH)) >= 19)
                        {
                            String failed ="";
                           /* if(tongNgayMN != 31)
                                failed += "MN";*/
                            if(tongNgayMT != 31)
                                failed += "MT";
                            if(tongNgayMB != 31)
                                failed += "MB";

                            if(failed.isEmpty()) // các kết quả đã được tải đầy đủ
                                return "Saved";

                            return failed; // có miền nào đó chưa tải đủ kết quả
                        }else {
                            String failed ="";
                           /* if(tongNgayMN != 31)
                                failed += "MN";*/
                            if(tongNgayMT != 31)
                                failed += "MT";

                            if(failed.isEmpty()) // các kết quả đã được tải đầy đủ
                                return "Saved";

                            return failed; // có miền nào đó chưa tải đủ kết quả
                        }
                    }else {
                        /*if(tongNgayMN != 31)
                            return "MN";
                        else*/
                            return "Saved"; // không cần tải thêm đài nào hết
                    }
                }else {
                    return "Failed"; // nếu Failed thì tải lại tất cả 3 miền xổ số
                }
            }
        }else {
            return "Failed";
        }
    }

    private int getSumDate(List<Long> listNgayXoSo)
    {
        // kiểm tra và đếm các ngày lớn hơn ngày hết hạn lãnh thưởng vé số tính đến hôm nay
        String ngayHetHang = getSubDate(30);
        long ngayHetHangLong = convertDateToMillisecond(ngayHetHang);

        int tongNgay = 0;
        for (long ngayLayRa : listNgayXoSo)
        {
            if(ngayLayRa >= ngayHetHangLong)
            {
                tongNgay += 1;
                Log.d("NgayHetHang", "ngayHetHang = " + ngayHetHangLong + "  ngayHetHangLong = " + ngayHetHangLong + " Ngay Lay Ra = "+ convertLongToDate(ngayLayRa)  + " ngayLayRa = "+ ngayLayRa);
            }
        }
        return tongNgay;
    }

    /**
     * startActivityDashboard là phương thức nén lại 0,3s để mở Intent Dashboard
     */
    private void startActivityDashboard(int milliSecond)
    {
        // Start home activity
        try {
            Thread.sleep(milliSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(LaunchScreen.this, Dashboard.class));
        // close splash activity
        finish();
    }

    /**
     * Method: getTraditionalFromRSS là phương thức lấy kết quả xổ số từ RSS
     * @param domainLottery Tên đài xổ số cần lấy RSS
     * @param pubDate Ngày xổ số
     * @param ResultsLottery kết quả xổ số
     * @param domain miền của kết quả xổ số
     * @return
     */
    private void getTraditionalFromRSS (String domainLottery, String pubDate, String ResultsLottery, String domain)
    {
        traditionalLottery.setTEN_DAI(domainLottery);
        traditionalLottery.setMIEN_XO_SO(domain);
        traditionalLottery.setNGAY_XO_SO(pubDate);
        setResutlLottery(ResultsLottery);
    }

    /**
     * Method: setResutlLottery là phương thức gán giá trị kết quả cho các giải khi đã có dữ liệu kết quả.
     * @param resultsLottery
     */
    private void setResutlLottery( String resultsLottery) {

        if(resultsLottery.contains("<![CDATA["))
        {
            if(!resultsLottery.startsWith("<![CDATA[ GDB: -1"))
            {
                String [] listResults = resultsLottery.split(",");
                if(listResults.length == 8)
                {
                    Log.d("www", resultsLottery);
                /*
                <![CDATA[ GDB: 49255,
                 G1: 06649,
                  G2: 23570 - 97897,
                   G3: 17815 - 78585 - 28443 - 63237 - 25403 - 81764,
                    G4: 0137 - 4313 - 4219 - 2492,
                     G5: 5514 - 5159 - 5418 - 2343 - 9268 - 8470,
                      G6: 089 - 068 - 072,
                       G7: 64 - 50 - 81 - 58 ]]>
                 */

                    giaiDB = listResults[0].replace("<![CDATA[ GDB:","").trim(); //  <![CDATA[ GDB: 49255,
                    giaiNhat = listResults[1].replace("G1:","").trim(); // G1: 06649,
                    giaiNhi = listResults[2].replace("G2:","").trim(); // G2: 23570 - 97897,
                    giaiBa = listResults[3].replace("G3:","").trim(); //  G3: 17815 - 78585 - 28443 - 63237 - 25403 - 81764,
                    giaiTu = listResults[4].replace("G4:","").trim(); // G4: 0137 - 4313 - 4219 - 2492,
                    giaiNam = listResults[5].replace("G5:","").trim(); // G5: 5514 - 5159 - 5418 - 2343 - 9268 - 8470,
                    giaiSau = listResults[6].replace("G6:","").trim(); //  G6: 089 - 068 - 072,
                    giaiBay = listResults[7].replace("G7:","").replace("]]>","").trim(); // G7: 64 - 50 - 81 - 58 ]]>
                    giaiTam = "100";
                    setTraditionalLottery();
                }
                else {
                    Log.d("TAG"," Không đủ kết quả xổ số của đài này hoặc kết quả xổ số bị lỗi! " + resultsLottery);
                }
            }
            else {
                Log.d("TAG"," startsWith(<![CDATA[ GDB: -1) ");
            }
        }
        else {
            String [] listResults = resultsLottery.split(":");
            Log.d("wwwe", listResults.length + "");
            if(listResults.length == 10)
            {
                Log.d("www", resultsLottery);
                /*
                ĐB
                 9456921
                  671792
                   219803
                    79153 - 566604
                     14454 - 19922 - 88612 - 76890 - 67529 - 01661 - 993925
                      27626
                       2790 - 5561 - 26957
                        8978
                         42
                */

                giaiDB = listResults[1].substring(0,listResults[1].length()-1).trim(); // 945692 1
                giaiNhat = listResults[2].substring(0,listResults[2].length()-1).trim(); // 67179 2
                giaiNhi = listResults[3].substring(0,listResults[3].length()-1).trim(); // 21980 3
                giaiBa = listResults[4].substring(0,listResults[4].length()-1).trim(); // 79153 - 56660 4
                giaiTu = listResults[5].substring(0,listResults[5].length()-1).trim(); // 14454 - 19922 - 88612 - 76890 - 67529 - 01661 - 99392 5
                giaiNam = listResults[6].substring(0,listResults[6].length()-1).trim(); // 2762 6
                giaiSau = listResults[7].substring(0,listResults[7].length()-1).trim(); // 2790 - 5561 - 2695 7
                giaiBay = listResults[8].substring(0,listResults[8].length()-1).trim(); // 897 8
                giaiTam = listResults[9].trim(); // 42
                Log.d("giaiDB", giaiBay );
                setTraditionalLottery();

            }else {
                Log.d("TAG"," Không đủ kết quả xổ số của đài này hoặc kết quả xổ số bị lỗi! " + resultsLottery);
            }
        }
    }

    /**
     * Method: setTraditionalLottery là phương thức gán các kết quả vào đối tượng TraditionalLottery
     */
    private void setTraditionalLottery()
    {
        traditionalLottery.setKET_QUA_DB(giaiDB);
        traditionalLottery.setKET_QUA_G1(giaiNhat);
        traditionalLottery.setKET_QUA_G2(giaiNhi);
        traditionalLottery.setKET_QUA_G3(giaiBa);
        traditionalLottery.setKET_QUA_G4(giaiTu);
        traditionalLottery.setKET_QUA_G5(giaiNam);
        traditionalLottery.setKET_QUA_G6(giaiSau);
        traditionalLottery.setKET_QUA_G7(giaiBay);
        traditionalLottery.setKET_QUA_G8(giaiTam);
    }


    /**
     * RSSGetResultsLottery Đây là phương thức giúp tải về kết quả xổ số của tất các các đài trong 30 ngày vừa qua
     */
    public class RSSGetResultsLottery extends AsyncTask<String, Void, String> {
        String  domainLottery, // tên đài xổ số
                URL, // đường dẫn rss của kết quả đài xổ số
                domain, // tên miền của kết quả xổ số
                oneDay; // Lấy kết quả xổ số của một ngày duy nhất

        public RSSGetResultsLottery(  String domainLottery, String URL, String domain , String oneDay) {
            this.domainLottery = domainLottery;
            this.URL = URL;
            this.domain = domain;
            this.oneDay = oneDay;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(URL);
                Log.i("cccd",url.toString() );

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                int code = urlConnection.getResponseCode();

                if(code == 200){
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
                        String line = "";

                        while ((line = bufferedReader.readLine()) != null)
                            result += line;

                    bufferedReader.close();
                }else {
                    // code = 503 Xin loi! Hien tai server bi qua tai. ban vui long tro lai sau
                    //Thong thuong loi nay chi la tam thoi. Ban co the tro lai sau it phut
                    result = "";
                    Log.d("aaaaca","code = " + code + " domainLinkLottery = " + URL);
                }
                Log.d("xxx", result);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            JSONObject jsonObj = null;
            try {

                jsonObj = XML.toJSONObject(result);

                JSONObject jsonObjRSS = jsonObj.getJSONObject("rss");
                JSONObject jsonObjChannel = jsonObjRSS.getJSONObject("channel");
                Log.d("xxxaaaa", jsonObjChannel.toString());
                JSONArray GPSJson = jsonObjChannel.getJSONArray("item"); // 375 item tổng tất cả kết quả củacác đài

                demTongDaiDaTai += 1; // đếm số đài đã tải hoàn tất
                //if(demTongDaiDaTai == (demSizeDai - 2)) // demSizeDai - 2 là 2 giải vietlott
                if(demTongDaiDaTai == demSizeDai) // demSizeDai - 2 là 2 giải vietlott
                {
                    //Toast.makeText(getApplicationContext(), "Đã tải dữ liệu hoàn tất", Toast.LENGTH_SHORT).show();
                    Log.d("xxxaaaa", "Đã tải dữ liệu hoàn tất"+ giaiSau);
                    checkDownloaded = true;
                    dialog.dismiss();
                    startActivityDashboard(10);
                }
                Log.d("xxxaaaa", "demTongDaiDaTai = "+ demTongDaiDaTai + " demSizeDai = " + (demSizeDai-2));

                if (GPSJson.length() != 0)
                {

                    TemporaryFileDBTraditionalLottery dbTraditionalLottery = new TemporaryFileDBTraditionalLottery(getApplicationContext());
                    for (int i = 0; i < GPSJson.length(); i++) {

                        Gson gson = new Gson();
                        String a = GPSJson.get(i).toString();
                        Log.d("aaaaa", a);
                        LotteryResults lotteryResults = gson.fromJson(String.valueOf(GPSJson.get(i)), LotteryResults.class);

                        String pubDate = "";
                        String resultsLottery = "";
                        traditionalLottery = new TraditionalLottery();
                        // listCall.add(callHistory);
                        if(URL.contains("https://xskt.com.vn"))
                        {
                            int positionNgay = lotteryResults.getLink().indexOf("ngay-");

                            //https://xskt.com.vn/xsbth/ngay-25-2-2021
                            // Toast.makeText(LaunchScreen.this, " Description " + lotteryResults.getDescription(), Toast.LENGTH_LONG).show();
                            try {

                                pubDate = formatDate(lotteryResults.getLink().substring(positionNgay + 5).trim());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d("checkDateMN","Tên đài = " + domainLottery + " ngày = " + pubDate);
                            Log.d("checkDateMN","Tên đài = " + domainLottery + " Description " + lotteryResults.getDescription());
                            traditionalLottery.setNGAY_XO_SO(pubDate);

                           // Log.d("xxxaaaa","Tên đài = " +  getDateNow() + " ngày = " + pubDate);
                            /*
                            ĐB: 9456921: 671792: 219803: 79153 - 566604: 14454 - 19922 - 88612 - 76890 - 67529 - 01661 - 993925: 27626: 2790 - 5561 - 26957: 8978: 42
                             */
                          /*  getTraditionalFromRSS (domainLottery, pubDate, lotteryResults.getDescription(), domain);
                            // Xuất ra kết quả xổ số đã lấy được
                            TraditionalLottery.TraditionalLotteryToString(traditionalLottery);*/

                        }else {
                            traditionalLottery.setNGAY_XO_SO(lotteryResults.getPubDate());
                            try {
                                pubDate = formatDateStart(lotteryResults.getPubDate(), DEFAULT_DATE, true); // chúng tôi dùng formatDateStart() để chuyển đổi từ dd/mm/yyyy sang dd-mm-yyyy.
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d("checkDateMB","Tên đài = " + domainLottery + " pubDate " + pubDate);
                             /*
                                <![CDATA[ GDB: 49255, G1: 06649, G2: 23570 - 97897, G3: 17815 - 78585 - 28443 - 63237 - 25403 - 81764, G4: 0137 - 4313 - 4219 - 2492, G5: 5514 - 5159 - 5418 - 2343 - 9268 - 8470, G6: 089 - 068 - 072, G7: 64 - 50 - 81 - 58 ]]>
                               */
                        }

                        if(getMillisecondNow() - convertDateToMillisecond(pubDate) < (ONEDAY * 32))
                        {
                            if(oneDay == null)
                            {
                                getTraditionalFromRSS (domainLottery, pubDate, lotteryResults.getDescription(), domain);
                                if(traditionalLottery.getKET_QUA_DB() != null && !traditionalLottery.getKET_QUA_DB().isEmpty())
                                {
                                    Log.d("yyyy", " getKET_QUA_DB = "+ traditionalLottery.getKET_QUA_DB());
                                    // Xuất ra kết quả xổ số đã lấy được
                                    TraditionalLottery.TraditionalLotteryToString(traditionalLottery);
                                    // Thêm kết quả xổ số vào databases SQLite
                                    dbTraditionalLottery.appendTraditionalLottery(traditionalLottery);
                                }
                            }
                            else { // chỉ lưu kết quả xổ số của hôm nay
                                Log.d("yyyy", " pubDate = "+ pubDate + " oneDay = "+ oneDay);
                                if(pubDate.equals(oneDay))
                                {
                                    getTraditionalFromRSS (domainLottery, pubDate, lotteryResults.getDescription(), domain);
                                    if(traditionalLottery.getKET_QUA_DB() != null && !traditionalLottery.getKET_QUA_DB().isEmpty())
                                    {
                                        Log.d("yyyy", " getKET_QUA_DB1 = "+ traditionalLottery.getKET_QUA_DB());
                                        // Xuất ra kết quả xổ số đã lấy được
                                        TraditionalLottery.TraditionalLotteryToString(traditionalLottery);
                                        // Thêm kết quả xổ số vào databases SQLite
                                        dbTraditionalLottery.appendTraditionalLottery(traditionalLottery);
                                    }
                                }
                            }
                        }else {
                            Log.d("xxxx", "ngày xổ số này đã hết hạn dò vé số: "+ pubDate + " = " + ((getMillisecondNow() - convertDateToMillisecond(pubDate))/ONEDAY));
                        }
                        Log.d("yyyy", " PubDate = "+ pubDate + " ===== " + getMillisecondNow() + " - " + convertDateToMillisecond(pubDate) + " = " + ((getMillisecondNow() - convertDateToMillisecond(pubDate))/ONEDAY));
                    }

                    /*List<TraditionalLottery> traditionalLotteryList = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(5, "");
                    Log.d("zsize", "Size = " + traditionalLotteryList.size() + "");*/
                   // Log.d("zsize", "Size = " + 1 + "");
                }
            } catch (JSONException e) {
                Log.e("aaaaa", e.getMessage());
                e.printStackTrace();
            }

            Log.d("bbbbb", result);

            Log.d("cccc", jsonObj.toString());

            super.onPostExecute(result);
        }
    }
}

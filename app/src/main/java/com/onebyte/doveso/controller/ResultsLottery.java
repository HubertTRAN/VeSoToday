package com.onebyte.doveso.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.onebyte.doveso.R;
import com.onebyte.doveso.adapter.AdapterDetailResultsLotteryMB;
import com.onebyte.doveso.adapter.AdapterDetailResultsLotteryMN;
import com.onebyte.doveso.adapter.AdapterDetailResultsLotteryMT;
import com.onebyte.doveso.adapter.AdapterResultsLottery;
import com.onebyte.doveso.model.PrizeResults;
import com.onebyte.doveso.model.TraditionalLottery;
import com.onebyte.doveso.model.WinningLottery;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBLotterySchedule;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBTraditionalLottery;
import com.r0adkll.slidr.Slidr;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static com.onebyte.doveso.api.ApiMethod.convertDateToMillisecond;
import static com.onebyte.doveso.api.ApiMethod.convertLongToDate;
import static com.onebyte.doveso.api.ApiMethod.formatDate;
import static com.onebyte.doveso.api.ApiMethod.formatDateStart;
import static com.onebyte.doveso.api.ApiMethod.formatDateStartEnd;
import static com.onebyte.doveso.api.ApiMethod.getColoredSpanned;
import static com.onebyte.doveso.api.ApiMethod.getDateNow;
import static com.onebyte.doveso.api.ApiMethod.getDayOfMonth;
import static com.onebyte.doveso.api.ApiMethod.getDomain;
import static com.onebyte.doveso.api.ApiMethod.getMillisecondNow;
import static com.onebyte.doveso.api.ApiMethod.getSharedPreference;
import static com.onebyte.doveso.api.ApiMethod.getSubDate;
import static com.onebyte.doveso.api.ApiMethod.setSharedPreference;
import static com.onebyte.doveso.contants.Global.COLOR_BLACK;
import static com.onebyte.doveso.contants.Global.DATE_DELETE;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_FORMAT;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_YYYY;
import static com.onebyte.doveso.contants.Global.DEFAULT_HH;
import static com.onebyte.doveso.contants.Global.DEFAULT_HHmm;
import static com.onebyte.doveso.contants.Global.DEFAULT_HOUR_MINUTES;
import static com.onebyte.doveso.contants.Global.DEFAULT_MINUTES;
import static com.onebyte.doveso.contants.Global.DEFAULT_mm;
import static com.onebyte.doveso.contants.Global.G_1;
import static com.onebyte.doveso.contants.Global.G_2;
import static com.onebyte.doveso.contants.Global.G_3;
import static com.onebyte.doveso.contants.Global.G_4;
import static com.onebyte.doveso.contants.Global.G_5;
import static com.onebyte.doveso.contants.Global.G_6;
import static com.onebyte.doveso.contants.Global.G_7;
import static com.onebyte.doveso.contants.Global.G_8;
import static com.onebyte.doveso.contants.Global.G_DB;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_BAC;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_NAM;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.KHUYEN_KHICH_MN;
import static com.onebyte.doveso.contants.Global.LOTTERY_SELECT_MIEN;
import static com.onebyte.doveso.contants.Global.MIEN_BAC;
import static com.onebyte.doveso.contants.Global.MIEN_NAM;
import static com.onebyte.doveso.contants.Global.MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.ONEDAY;
import static com.onebyte.doveso.contants.Global.SELECT_PROVINCE;
import static com.onebyte.doveso.controller.UITraditionalLottery.KiemTraGiaiTrungThuongMB;
import static com.onebyte.doveso.controller.UITraditionalLottery.KiemTraGiaiTrungThuongMN;
import static com.onebyte.doveso.controller.UITraditionalLottery.checkConsolationPrizes;
import static com.onebyte.doveso.controller.UITraditionalLottery.setPriority;

public class ResultsLottery extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btn_Results;
    private TextView txt_DatePicker, txt_Name_Mien_Nam;
    private LinearLayout lnl_Mien_Nam;
    private Spinner spn_Name_Lottery;
    private EditText edt_Code_Lottery;
    private ImageView img_Closes_Code;
    private CardView cv_Mien_Xo_So;
    public static String lastSelectedDay;
    private static List<String> dateSelect;
    private List<PrizeResults> prizeResultsListMN, prizeResultsListMT;
    public static List<PrizeResults> prizeResultsListMB_Province;
    private String domainLottery;
    public static List<WinningLottery>traditionalLotteryListResults;
    private boolean provinceNow;
    private AdapterDetailResultsLotteryMN mAdapterMN;
    private AdapterDetailResultsLotteryMT mAdapterMT;
    private RecyclerView.Adapter mAdapterMB;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RadioButton rdb_Mien_Nam_Lottery, rdb_Mien_Trung_Lottery, rdb_Mien_Bac_Lottery;
    private int MienNguoiDungChon;
    private boolean checkBackPressed; // đây là giá trị kiểm tra xem người dùng có muốn trở về home hay không, khi họ đã bấm nút dò vé số.

    private RecyclerView rcv_MienNam_Lottery, rcv_MienTrung_Lottery,rcv_MienBac_Lottery;
    private LinearLayout lnl_TableRow_MT, lnl_TableRow_MN, lnl_TableRow_MB_Province,
            lnl_Next_MN, lnl_Date_Lottery_MN, lnl_Prevoius_MN,
                            lnl_Prevoius_MT, lnl_Date_Lottery_MT,lnl_Next_MT,
                            lnl_Prevoius_MB, lnl_Next_MB, lnl_Date_Lottery_MB, lnl_Next_And_Previous_MB, lnl_Results_Lottery;
    private TextView txt_Date_Three_MN, txt_Date_Lottery_MT, txt_Date_Lottery_MB;
    private TextView  txt_Truy_Van_Do_Ve_So, txt_Ket_Qua_Xo_So_Theo_Dai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_lottery);

        setID();
        setEvent();
        Slidr.attach(this);

        clearAllResultAfter30Day();

    }

    private void clearAllResultAfter30Day() {

        if(!getSharedPreference(getApplicationContext(), DATE_DELETE).isEmpty())
        {
            Long dateDeleted = Long.parseLong(getDateNow(DEFAULT_DATE_SELECT_YYYY).replace("-",""));
            long dateNowDelete = Long.parseLong(getSharedPreference(getApplicationContext(), DATE_DELETE).replace("-",""));
            Log.d("xxzsa", "DATE_DELETE = " + dateDeleted  + "   Date Now = " + dateNowDelete);
            if((dateDeleted - dateNowDelete) > 60)
            {
                TemporaryFileDBTraditionalLottery dbTraditionalLottery = new TemporaryFileDBTraditionalLottery(getApplicationContext());
                List<TraditionalLottery> traditionalLotteryList = dbTraditionalLottery.deleteAllResultAfter30Day();
                String date = getSubDate(60);
                Log.d("xxzsa", "getSubDate = " + date);
                List<Integer> listIDDelete = new ArrayList<>();
                try {
                    setSharedPreference(getApplicationContext(), DATE_DELETE, getDateNow(DEFAULT_DATE_SELECT_YYYY));
                    date = formatDateStartEnd(date,DEFAULT_DATE_SELECT_FORMAT, DEFAULT_DATE_SELECT_YYYY).replace("-","");
                    long dateNow30 = Long.parseLong(date);
                    if (traditionalLotteryList.size() > 0)
                    {
                        for (int i=0; i<traditionalLotteryList.size(); i++ )
                        {
                            long dateNow = Long.parseLong(formatDateStartEnd(traditionalLotteryList.get(i).getNGAY_XO_SO(),DEFAULT_DATE_SELECT_FORMAT, DEFAULT_DATE_SELECT_YYYY).replace("-",""));

                            Log.d("xxzsa", "dateNow = " + dateNow + "  dateNow30 = " + dateNow30);
                            if(dateNow < dateNow30)
                            {
                                listIDDelete.add(traditionalLotteryList.get(i).getROW_ID());
                                Log.d("xxzsa", traditionalLotteryList.get(i).getROW_ID() + "");
                            }

                        }
                    }
                    dbTraditionalLottery.deleteAllResultAfter30DayStep1(listIDDelete);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            setSharedPreference(getApplicationContext(), DATE_DELETE, getDateNow(DEFAULT_DATE_SELECT_YYYY));
        }

    }

    /**
     * Method: loadDataLotteryHome đây là phương thức load data kết quả xổ số của ba miền lên giao diện người dùng.
     */
    private void loadDataLotteryHome(String dateSelect, int mienXoSo) {

        List<TraditionalLottery> traditionalLotteryListMN;
        List<TraditionalLottery> traditionalLotteryListMT;
        List<TraditionalLottery> traditionalLotteryListMB;
        TemporaryFileDBTraditionalLottery dbTraditionalLottery = new TemporaryFileDBTraditionalLottery(getApplicationContext());

        if(dateSelect.equals(getDateNow(DEFAULT_DATE_SELECT_FORMAT)))
        {
            /*
                Nếu kết quả xổ số mà người dùng muốn xem là ngày hôm nay thì mình phải kiếm tra xem đã tới giờ xổ số của các miền chưa.
                Nếu trước 17h thì hiển thị kết quả xổ số của ngày hôm qua vì hôm nay chưa xổ số miền Nam
                Nếu trước 18h thì hiển thị kết quả xổ số của ngày hôm qua vì hôm nay chưa xổ số miền Trung
                Nếu trước 19h thì hiển thị kết quả xổ số của ngày hôm qua vì hôm nay chưa xổ số miền Bắc
             */
            String dateMienNam;
            Log.d("dateMien", "DEFAULT_HH = "+ Integer.parseInt(getDateNow(DEFAULT_HHmm)) + "< " + DEFAULT_HOUR_MINUTES);
            if(Integer.parseInt(getDateNow(DEFAULT_HHmm)) <= DEFAULT_HOUR_MINUTES )
            {
                Log.d("dateMien", "dateMienNam chưa xổ số");
                dateMienNam = getSubDate(1);
            }else
            {
                dateMienNam = getDateNow(DEFAULT_DATE_SELECT_FORMAT);
                Log.d("dateMien", "dateMienNam đã xổ số");
            }

            // nhớ bỏ khi có kết quả xổ số
            //dateMienNam = "08-07-2021";
            String dateMienTrung = (Integer.parseInt(getDateNow(DEFAULT_HH)) < 18) ? getSubDate(1): getDateNow(DEFAULT_DATE_SELECT_FORMAT);
            String dateMienNamBac = (Integer.parseInt(getDateNow(DEFAULT_HH)) < 19) ? getSubDate(1): getDateNow(DEFAULT_DATE_SELECT_FORMAT);
            Log.d("dateMien", "\ndateMienNam = " + dateMienNam+ "\ndateMienTrung = "+ dateMienTrung
                    + "\ndateMienNamBac" + dateMienNamBac);
            traditionalLotteryListMB = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_BAC, dateMienNamBac);
           // traditionalLotteryListMB = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_BAC, "21-07-2021");
            traditionalLotteryListMT = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_TRUNG, dateMienTrung);
            traditionalLotteryListMN = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_NAM, dateMienNam);
            Log.d("dateMien", "traditionalLotteryListMB = " + traditionalLotteryListMB.size() + "\ntraditionalLotteryListMT = "+ traditionalLotteryListMT.size()
            + "\ntraditionalLotteryListMN" + traditionalLotteryListMN.size());
        }
        else {
            traditionalLotteryListMB = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_BAC, dateSelect);
            //traditionalLotteryListMB = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_BAC, "21-07-2021");
            traditionalLotteryListMT = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_TRUNG, dateSelect);
            //traditionalLotteryListMN = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_NAM, dateSelect);
            traditionalLotteryListMN = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_NAM, dateSelect);
        }

        Log.d("checkKetQuazz",  "mienXoSo = " + mienXoSo);

        if(mienXoSo == KET_QUA_MIEN_NAM)
        {
            lnl_TableRow_MN.setVisibility(View.VISIBLE);
            lnl_TableRow_MT.setVisibility(View.GONE);
            lnl_TableRow_MB_Province.setVisibility(View.GONE);
            Log.d("dateMien", "KET_QUA_MIEN_NAM " + traditionalLotteryListMN.size());


            if(traditionalLotteryListMN.size() > 0)
            {
                setDataLotteryForTableRowMN(traditionalLotteryListMN);
            }
            else {
                traditionalLotteryListMN = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_NAM, "");
                if(traditionalLotteryListMN.size() > 0)
                {
                    setDataLotteryForTableRowMN(traditionalLotteryListMN);
                }
                else {
                    Log.d("checkKetQuaMB",  "Lỗi hiển thị kết quả sổ xố miền Trung");
                }
            }
        }
        else if(mienXoSo == KET_QUA_MIEN_TRUNG)
        {
            lnl_TableRow_MN.setVisibility(View.GONE);
            lnl_TableRow_MT.setVisibility(View.VISIBLE);
            lnl_TableRow_MB_Province.setVisibility(View.GONE);
            Log.d("dateMien", "KET_QUA_MIEN_TRUNG " + traditionalLotteryListMT.size());

            if(traditionalLotteryListMT.size() > 0)
            {
                setDataLotteryForTableRowMT(traditionalLotteryListMT);
            }
            else {
                traditionalLotteryListMT = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_TRUNG, "");
                if(traditionalLotteryListMT.size() > 0)
                {
                    setDataLotteryForTableRowMT(traditionalLotteryListMT);
                }
                else {
                    Log.d("checkKetQuaMB",  "Lỗi hiển thị kết quả sổ xố miền Trung");
                }
            }
        }
        else
        {

            lnl_TableRow_MN.setVisibility(View.GONE);
            lnl_TableRow_MT.setVisibility(View.GONE);
            lnl_TableRow_MB_Province.setVisibility(View.VISIBLE);
            Log.d("dateMien", "KET_QUA_MIEN_BAC" + traditionalLotteryListMN.size());
            if(traditionalLotteryListMB.size() > 0)
            {
                setDataLotteryForTableRowMB(traditionalLotteryListMB);
                Log.d("checkKetQuaMB",  "traditionalLotteryListMB.size() = " + traditionalLotteryListMB.size());
            }
            else {
                Log.d("checkKetQuaMB",  "dbTraditionalLottery.TraditionalLotteryDBReadWithTime");
                traditionalLotteryListMB = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(KET_QUA_MIEN_BAC, "");
                if(traditionalLotteryListMB.size() > 0)
                {
                    Log.d("checkKetQuaMB",  "traditionalLotteryListMB.size() = " + traditionalLotteryListMB.size());
                    setDataLotteryForTableRowMB(traditionalLotteryListMB);
                }
                else {
                    Log.d("checkKetQuaMB",  "Lỗi hiển thị kết quả sổ xố miền Bắc");
                }
            }

        }

        Log.d("checkKetQua",  "Giờ = " + getDateNow(DEFAULT_HH) + " Miền Bắc = " + traditionalLotteryListMB.size()
                + " Miền Trung = " + traditionalLotteryListMT.size()
                + " Miền Nam = " + traditionalLotteryListMN.size() + " Ngày = "+ dateSelect);
    }

    /**
     * setDataLotteryForTableRowMT Là phương thức lấy ra kết quả xổ số miền trung của ngày người dùng đã chọn xem.
     * @param traditionalLotteryListMT
     */
    private void setDataLotteryForTableRowMT(List<TraditionalLottery> traditionalLotteryListMT) {

        /**
         * Miền Trung sẽ có ngày xổ kết quả 3 tỉnh và có ngày xổ kết quả 2 tỉnh. Nếu xổ số chỉ 1 tỉnh là databases lưu bị lỗi.
         */

        if(traditionalLotteryListMT.size() == 2) // sửa lại chỗ này có khi có 3 đài
        {
            int size = 2;
            prizeResultsListMT = setPrizeResultsList(size, traditionalLotteryListMT.get(0), traditionalLotteryListMT.get(1));
        }
        else if(traditionalLotteryListMT.size() == 3)
        {
            int size = 3;
            prizeResultsListMT = setPrizeResultsList(size, traditionalLotteryListMT.get(0), traditionalLotteryListMT.get(1), traditionalLotteryListMT.get(2));
        }
        else {
            Log.e("TAG", "xổ số chỉ 1 tỉnh là databases lưu bị lỗi"+ traditionalLotteryListMT.size());
        }

        if(traditionalLotteryListMT.size() == 2 || traditionalLotteryListMT.size() == 3)
        {
            String aDate = traditionalLotteryListMT.get(0).getNGAY_XO_SO();
            txt_Date_Lottery_MT.setText(aDate);

            if(prizeResultsListMT.size()>0)
            {
                // adapter
                mAdapterMT = new AdapterDetailResultsLotteryMT(this, prizeResultsListMT);
                rcv_MienTrung_Lottery.setAdapter(mAdapterMT);
                //recyclerView.setNestedScrollingEnabled(false); // tắt scroll trong recyclerview
                mAdapterMT.notifyDataSetChanged();
            }
        }
    }

    private List<PrizeResults> setPrizeResultsList(int size, TraditionalLottery traditionalLotteryProvince1, TraditionalLottery traditionalLotteryProvince2, TraditionalLottery traditionalLotteryProvince3, TraditionalLottery traditionalLotteryProvince4)
    {
        List<PrizeResults> prizeResultsList = new ArrayList<>();
        String ngayXoSo = traditionalLotteryProvince1.getNGAY_XO_SO();
        prizeResultsList.add(new PrizeResults(ngayXoSo,size, traditionalLotteryProvince1.getTEN_DAI(), traditionalLotteryProvince2.getTEN_DAI(), traditionalLotteryProvince3.getTEN_DAI(), traditionalLotteryProvince4.getTEN_DAI()));
        prizeResultsList.add(new PrizeResults(G_8,size, traditionalLotteryProvince1.getKET_QUA_G8(), traditionalLotteryProvince2.getKET_QUA_G8(), traditionalLotteryProvince3.getKET_QUA_G8(), traditionalLotteryProvince4.getKET_QUA_G8()));
        prizeResultsList.add(new PrizeResults(G_7,size,  traditionalLotteryProvince1.getKET_QUA_G7(), traditionalLotteryProvince2.getKET_QUA_G7(), traditionalLotteryProvince3.getKET_QUA_G7(), traditionalLotteryProvince4.getKET_QUA_G7()));
        prizeResultsList.add(new PrizeResults(G_6,size,  traditionalLotteryProvince1.getKET_QUA_G6(), traditionalLotteryProvince2.getKET_QUA_G6(), traditionalLotteryProvince3.getKET_QUA_G6(), traditionalLotteryProvince4.getKET_QUA_G6()));
        prizeResultsList.add(new PrizeResults(G_5,size,  traditionalLotteryProvince1.getKET_QUA_G5(), traditionalLotteryProvince2.getKET_QUA_G5(), traditionalLotteryProvince3.getKET_QUA_G5(), traditionalLotteryProvince4.getKET_QUA_G5()));
        prizeResultsList.add(new PrizeResults(G_4,size,  traditionalLotteryProvince1.getKET_QUA_G4(), traditionalLotteryProvince2.getKET_QUA_G4(), traditionalLotteryProvince3.getKET_QUA_G4(), traditionalLotteryProvince4.getKET_QUA_G4()));
        prizeResultsList.add(new PrizeResults(G_3,size,  traditionalLotteryProvince1.getKET_QUA_G3(), traditionalLotteryProvince2.getKET_QUA_G3(), traditionalLotteryProvince3.getKET_QUA_G3(), traditionalLotteryProvince4.getKET_QUA_G3()));
        prizeResultsList.add(new PrizeResults(G_2,size,  traditionalLotteryProvince1.getKET_QUA_G2(), traditionalLotteryProvince2.getKET_QUA_G2(), traditionalLotteryProvince3.getKET_QUA_G2(), traditionalLotteryProvince4.getKET_QUA_G2()));
        prizeResultsList.add(new PrizeResults(G_1,size,  traditionalLotteryProvince1.getKET_QUA_G1(), traditionalLotteryProvince2.getKET_QUA_G1(), traditionalLotteryProvince3.getKET_QUA_G1(), traditionalLotteryProvince4.getKET_QUA_G1()));
        prizeResultsList.add(new PrizeResults(G_DB,size, traditionalLotteryProvince1.getKET_QUA_DB(), traditionalLotteryProvince2.getKET_QUA_DB(), traditionalLotteryProvince3.getKET_QUA_DB(), traditionalLotteryProvince4.getKET_QUA_DB()));

        return prizeResultsList;
    }

    /**
     * setDataLotteryForTableRowMT Là phương thức lấy ra kết quả xổ số miền Nam của ngày người dùng đã chọn xem.
     * @param traditionalLotteryListMN
     */
    private void setDataLotteryForTableRowMN(List<TraditionalLottery> traditionalLotteryListMN) {

        /**
         * Miền Nam sẽ có ngày xổ kết quả 3 tỉnh và có ngày xổ kết quả 4 tỉnh. Nếu xổ số chỉ 1 tỉnh là databases lưu bị lỗi.
         */

        if(traditionalLotteryListMN.size() == 3) // sửa lại chỗ này có khi có 3 đài
        {
            Log.e("MNN", "traditionalLotteryListMN.size() == 3");
            prizeResultsListMN = setPrizeResultsList(traditionalLotteryListMN.size(), traditionalLotteryListMN.get(0), traditionalLotteryListMN.get(1), traditionalLotteryListMN.get(2));
        }
        else if(traditionalLotteryListMN.size() == 4)
        {
            Log.e("MNN", "traditionalLotteryListMN.size() == 3");
            prizeResultsListMN = setPrizeResultsList(traditionalLotteryListMN.size(), traditionalLotteryListMN.get(0), traditionalLotteryListMN.get(1), traditionalLotteryListMN.get(2), traditionalLotteryListMN.get(3));
        }
        else {
            Log.e("TAG", "xổ số chỉ 1 tỉnh là databases lưu bị lỗi"+ traditionalLotteryListMN.size());

        }

        if(traditionalLotteryListMN.size() == 3 || traditionalLotteryListMN.size() == 4)
        {

            String aDate = traditionalLotteryListMN.get(0).getNGAY_XO_SO();
            Log.e("TAG", "aDateMN"+ aDate);
            txt_Date_Three_MN.setText(aDate);
            if(prizeResultsListMN.size() > 0)
            {
                txt_Ket_Qua_Xo_So_Theo_Dai.setText(getString(R.string.ket_qua_mien_bac, MIEN_BAC));
                // adapter
                mAdapterMN = new AdapterDetailResultsLotteryMN(this, prizeResultsListMN);
                rcv_MienNam_Lottery.setAdapter(mAdapterMN);
                //recyclerView.setNestedScrollingEnabled(false); // tắt scroll trong recyclerview
                mAdapterMN.notifyDataSetChanged();
            }
        }
    }

    /**
     * setDataLotteryForTableRowMT Là phương thức lấy ra kết quả xổ số miền Nam của ngày người dùng đã chọn xem.
     * @param traditionalLotteryListMB
     */
    @SuppressLint("SetTextI18n")
    private void setDataLotteryForTableRowMB(List<TraditionalLottery> traditionalLotteryListMB) {

        /**
         * Miền Nam sẽ có ngày xổ kết quả 3 tỉnh và có ngày xổ kết quả 4 tỉnh. Nếu xổ số chỉ 1 tỉnh là databases lưu bị lỗi.
         */

        if(traditionalLotteryListMB.size() > 0)
        {
            if(traditionalLotteryListMB.size() == 1) // sửa lại chỗ này có khi có 3 đài
            {
                String aDate = traditionalLotteryListMB.get(0).getNGAY_XO_SO();
                Log.e("aDate", "aDate"+ aDate);
                txt_Date_Lottery_MB.setText(aDate);
                prizeResultsListMB_Province = setPrizeResultsList(traditionalLotteryListMB.size(), traditionalLotteryListMB.get(0), true);

                // adapter
                mAdapterMB = new AdapterDetailResultsLotteryMB(this, prizeResultsListMB_Province, true);
                rcv_MienBac_Lottery.setAdapter(mAdapterMB);
                //recyclerView.setNestedScrollingEnabled(false); // tắt scroll trong recyclerview
                mAdapterMB.notifyDataSetChanged();
            }
            else {
                Log.e("aDate", "xổ số chỉ 1 tỉnh là databases lưu bị lỗi"+ traditionalLotteryListMB.size());
            }
        }
        else
        {
            Log.e("aDate", "xổ số chỉ 1 tỉnh là databases lưu bị lỗi"+ traditionalLotteryListMB.size());
        }
    }

    /**
     * setSpinner là phương thức kiểm tra sự thay đổi của Spinner khi người dùng bấm lựa chọn item con trong Spinner
     * @param dateSelect
     */
    private void setSpinner(List<String> dateSelect) {


        // (@resource) android.R.layout.simple_spinner_item:
        //   The resource ID for a layout file containing a TextView to use when instantiating views.
        //    (Layout for one ROW of Spinner)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                dateSelect);

        // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spn_Name_Lottery.setAdapter(adapter);

        // When user select a List-Item.
        this.spn_Name_Lottery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * onItemSelectedHandler đây là sự kiện thay đổi tên của item trong Spinner khi người dùng chọn lựa chọn khác.
     * @param adapterView
     * @param view
     * @param position
     * @param id
     */
    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        domainLottery = adapter.getItem(position).toString();
        //Toast.makeText(getApplicationContext(), "Selected Employee: " + domainLottery ,Toast.LENGTH_SHORT).show();
    }

    private void setID() {

        toolbar = findViewById(R.id.toolbar_Results_Lottery);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.app_name_alias));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btn_Results = findViewById(R.id.btn_Results);
        txt_DatePicker = findViewById(R.id.txt_DatePicker);
        txt_Truy_Van_Do_Ve_So = findViewById(R.id.txt_Truy_Van_Do_Ve_So);//
        txt_Ket_Qua_Xo_So_Theo_Dai = findViewById(R.id.txt_Ket_Qua_Xo_So_Theo_Dai);
        cv_Mien_Xo_So = findViewById(R.id.cv_Mien_Xo_So);
        spn_Name_Lottery = findViewById(R.id.spn_Name_Lottery);
        edt_Code_Lottery = findViewById(R.id.edt_Code_Lottery);
        img_Closes_Code = findViewById(R.id.img_Closes_Code);
        rdb_Mien_Nam_Lottery = findViewById(R.id.rdb_Mien_Nam_Lottery);
        rdb_Mien_Trung_Lottery = findViewById(R.id.rdb_Mien_Trung_Lottery);
        rdb_Mien_Bac_Lottery = findViewById(R.id.rdb_Mien_Bac_Lottery);
        lastSelectedDay = checkDateDaiXoSo();
        setSpinnerWithDay(checkDateDaiXoSo());

       /* private TextView txt_DatePicker, txt_Name_Mien_Nam;
        private LinearLayout lnl_Mien_Nam;*/
        txt_Name_Mien_Nam = findViewById(R.id.txt_Name_Mien_Nam);
        lnl_Mien_Nam = findViewById(R.id.lnl_Mien_Nam);

        txt_DatePicker.setText(lastSelectedDay);
        img_Closes_Code.setVisibility(View.GONE);
        provinceNow = false;
        checkBackPressed = false;
        // Khai báo ID của các kết quả xổ xố 3 miền
        rcv_MienNam_Lottery = findViewById(R.id.rcv_MienNam_Lottery);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcv_MienNam_Lottery.setLayoutManager(mLayoutManager);

        rcv_MienTrung_Lottery = findViewById(R.id.rcv_MienTrung_Lottery);
        RecyclerView.LayoutManager mLayoutManagerMT = new LinearLayoutManager(this);
        rcv_MienTrung_Lottery.setLayoutManager(mLayoutManagerMT);


        rcv_MienBac_Lottery = findViewById(R.id.rcv_MienBac_Lottery);
        RecyclerView.LayoutManager mLayoutManagerMB = new LinearLayoutManager(this);
        rcv_MienBac_Lottery.setLayoutManager(mLayoutManagerMB);

        lnl_Prevoius_MN = findViewById(R.id.lnl_Prevoius_MN);
        lnl_Date_Lottery_MN = findViewById(R.id.lnl_Date_Lottery_MN);
        lnl_Next_MN = findViewById(R.id.lnl_Next_MN);
        lnl_Prevoius_MT = findViewById(R.id.lnl_Prevoius_MT);
        lnl_Date_Lottery_MT = findViewById(R.id.lnl_Date_Lottery_MT);
        lnl_Next_MT = findViewById(R.id.lnl_Next_MT);
        lnl_Prevoius_MB = findViewById(R.id.lnl_Prevoius_MB);
        lnl_Next_MB = findViewById(R.id.lnl_Next_MB);
        lnl_Date_Lottery_MB = findViewById(R.id.lnl_Date_Lottery_MB);
        lnl_Next_And_Previous_MB = findViewById(R.id.lnl_Next_And_Previous_MB);

        //lnl_TableRow_MT, lnl_TableRow_MN, lnl_TableRow_MB_Province
        lnl_TableRow_MT = findViewById(R.id.lnl_TableRow_MT);
        lnl_TableRow_MN = findViewById(R.id.lnl_TableRow_MN);
        lnl_TableRow_MB_Province = findViewById(R.id.lnl_TableRow_MB_Province);

        // Ẩn lnl hiển thị kết quả trước khi người dùng bấm dò vé số. Sau khi bấm dò vé số sẽ hiển thị lnl này lên.
        lnl_Results_Lottery = findViewById(R.id.lnl_Results_Lottery);
        lnl_Results_Lottery.setVisibility(View.GONE);

        txt_Date_Three_MN = findViewById(R.id.txt_Date_Three_MN);
        txt_Date_Lottery_MT = findViewById(R.id.txt_Date_Lottery_MT);
        txt_Date_Lottery_MB = findViewById(R.id.txt_Date_Lottery_MB);

        traditionalLotteryListResults = new ArrayList<>();
        mRecyclerView = findViewById(R.id.rcv_Results_Lottery);
        mRecyclerView.setHasFixedSize(true);

        setRadioButtonMien();

        AdRequest adRequest = new AdRequest.Builder().build();
        // AdSize adSize = new AdSize(320, 100);
        AdView mAdView = findViewById(R.id.adView_Result);
        mAdView.loadAd(adRequest);

    }

    /**
     * setRadioButtonMien Đây là phương thức load đầu tiên xem người dùng đang chọn xem kết quả xổ số của miền nào là ưu tiên
     * và hiển thị kết quả xổ số của miền đó.
     */
    private void setRadioButtonMien() {

        String lottery_Type = getSharedPreference(getApplicationContext(),LOTTERY_SELECT_MIEN);
        Log.d("lottery_Type", "lottery_Type = "+ lottery_Type);
        if(!lottery_Type.isEmpty())
        {

            if(lottery_Type.equals(MIEN_NAM))
            {
                MienNguoiDungChon = KET_QUA_MIEN_NAM;

                lnl_TableRow_MN.setVisibility(View.VISIBLE);
                txt_Name_Mien_Nam.setVisibility(View.VISIBLE);
                lnl_Mien_Nam.setVisibility(View.VISIBLE);
                lnl_TableRow_MT.setVisibility(View.GONE);
                lnl_TableRow_MB_Province.setVisibility(View.GONE);

                rdb_Mien_Nam_Lottery.setChecked(true);
                rdb_Mien_Trung_Lottery.setChecked(false);
                rdb_Mien_Bac_Lottery.setChecked(false);
                txt_Ket_Qua_Xo_So_Theo_Dai.setText(getString(R.string.ket_qua_mien_bac, MIEN_BAC));
                // đây là phương thức load data kết quả xổ số của ba miền lên giao diện người dùng.
                loadDataLotteryHome(getDateNow(DEFAULT_DATE_SELECT_FORMAT), KET_QUA_MIEN_NAM);
                Log.d("lottery_Type", "MienNguoiDungChon22 = "+ MienNguoiDungChon);


            }else if(lottery_Type.equals(MIEN_TRUNG)){
                MienNguoiDungChon = KET_QUA_MIEN_TRUNG;
                lnl_TableRow_MT.setVisibility(View.VISIBLE);
                lnl_TableRow_MN.setVisibility(View.GONE);
                lnl_TableRow_MB_Province.setVisibility(View.GONE);
                //lnl_Date_Lottery_MT.setVisibility(View.VISIBLE);
                rdb_Mien_Nam_Lottery.setChecked(false);
                rdb_Mien_Trung_Lottery.setChecked(true);
                rdb_Mien_Bac_Lottery.setChecked(false);
                txt_Ket_Qua_Xo_So_Theo_Dai.setText(getString(R.string.ket_qua_mien_bac, MIEN_BAC));
                // đây là phương thức load data kết quả xổ số của ba miền lên giao diện người dùng.
                loadDataLotteryHome(getDateNow(DEFAULT_DATE_SELECT_FORMAT), KET_QUA_MIEN_TRUNG);

            }else {
                MienNguoiDungChon = KET_QUA_MIEN_BAC;
                lnl_Date_Lottery_MT.setVisibility(View.GONE);
                lnl_TableRow_MN.setVisibility(View.GONE);
                lnl_TableRow_MB_Province.setVisibility(View.VISIBLE);
                rdb_Mien_Nam_Lottery.setChecked(false);
                rdb_Mien_Trung_Lottery.setChecked(false);
                rdb_Mien_Bac_Lottery.setChecked(true);
                txt_Ket_Qua_Xo_So_Theo_Dai.setText(getString(R.string.ket_qua_mien_bac, MIEN_BAC));
                // đây là phương thức load data kết quả xổ số của ba miền lên giao diện người dùng.
                loadDataLotteryHome(getDateNow(DEFAULT_DATE_SELECT_FORMAT), KET_QUA_MIEN_BAC);

            }
        }
        else
        {

            MienNguoiDungChon = KET_QUA_MIEN_NAM;
            Log.d("lottery_Type", "MienNguoiDungChon2 = "+ MienNguoiDungChon);
            lnl_TableRow_MN.setVisibility(View.VISIBLE);
            lnl_TableRow_MT.setVisibility(View.GONE);
            lnl_TableRow_MB_Province.setVisibility(View.GONE);

            rdb_Mien_Nam_Lottery.setChecked(true);
            rdb_Mien_Trung_Lottery.setChecked(false);
            rdb_Mien_Bac_Lottery.setChecked(false);
            txt_Ket_Qua_Xo_So_Theo_Dai.setText(getString(R.string.ket_qua_mien_bac, MIEN_BAC));

            // đây là phương thức load data kết quả xổ số của ba miền lên giao diện người dùng.
            loadDataLotteryHome(getDateNow(DEFAULT_DATE_SELECT_FORMAT), KET_QUA_MIEN_NAM);
            setSharedPreference(getApplicationContext(), LOTTERY_SELECT_MIEN, MIEN_NAM);
        }
        Log.d("MienNguoiDungChon", MienNguoiDungChon +" ");

    }

    /**
     * checkDateDaXoSo đây là phương thức kiếm tra xem đã có kết quả xổ số của ngày hôm nay chưa
     * nếu chưa có kết quả xổ số của ngày hôm nay thì hiển thị ngày hôm qua.
     * @return ngày
     */
    public static String checkDateDaiXoSo() {
        if(Integer.parseInt(getDateNow(DEFAULT_HH)) >= 17)
            return getDateNow(DEFAULT_DATE_SELECT_FORMAT);
        else
            return getSubDate(1);
    }

    private void setEvent() {

        setEventButtonResults();

        edt_Code_Lottery.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do something, e.g. set your TextView here via .setText()
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        txt_DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(txt_DatePicker);
            }
        });

        img_Closes_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_Code_Lottery.setText("");
                img_Closes_Code.setVisibility(View.GONE);
            }
        });

        lnl_Date_Lottery_MN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(txt_Date_Three_MN);

            }
        });

        lnl_Date_Lottery_MT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(txt_Date_Lottery_MT);

            }
        });

        lnl_Date_Lottery_MB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDateDialog(txt_Date_Lottery_MB);
            }
        });

        setLinearLayoutMN();
        setLinearLayoutMT();
        setLinearLayoutMB();

        setEventRadioButton();

    }

    private void setEventRadioButton() {

        rdb_Mien_Bac_Lottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSharedPreference(getApplicationContext(), LOTTERY_SELECT_MIEN, MIEN_BAC);
                MienNguoiDungChon = KET_QUA_MIEN_BAC;

                lnl_TableRow_MT.setVisibility(View.GONE);
                lnl_TableRow_MN.setVisibility(View.VISIBLE);
                lnl_TableRow_MB_Province.setVisibility(View.VISIBLE);
                rdb_Mien_Bac_Lottery.setChecked(true);
                rdb_Mien_Nam_Lottery.setChecked(false);
                rdb_Mien_Trung_Lottery.setChecked(false);
                // đây là phương thức load data kết quả xổ số của ba miền lên giao diện người dùng.
                loadDataLotteryHome(getDateNow(DEFAULT_DATE_SELECT_FORMAT), KET_QUA_MIEN_BAC);
            }
        });

        rdb_Mien_Nam_Lottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSharedPreference(getApplicationContext(), LOTTERY_SELECT_MIEN, MIEN_NAM);
                MienNguoiDungChon = KET_QUA_MIEN_NAM;

                lnl_Date_Lottery_MN.setVisibility(View.VISIBLE);
                txt_Name_Mien_Nam.setVisibility(View.VISIBLE);
                lnl_Mien_Nam.setVisibility(View.VISIBLE);

                lnl_TableRow_MT.setVisibility(View.GONE);
                lnl_TableRow_MN.setVisibility(View.VISIBLE);
                lnl_TableRow_MB_Province.setVisibility(View.GONE);

                rdb_Mien_Bac_Lottery.setChecked(false);
                rdb_Mien_Nam_Lottery.setChecked(true);
                rdb_Mien_Trung_Lottery.setChecked(false);
                // đây là phương thức load data kết quả xổ số của ba miền lên giao diện người dùng.
                loadDataLotteryHome(getDateNow(DEFAULT_DATE_SELECT_FORMAT), KET_QUA_MIEN_NAM);
            }
        });

        rdb_Mien_Trung_Lottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSharedPreference(getApplicationContext(), LOTTERY_SELECT_MIEN, MIEN_TRUNG);
                MienNguoiDungChon = KET_QUA_MIEN_TRUNG;
                lnl_Date_Lottery_MN.setVisibility(View.GONE);
                lnl_TableRow_MB_Province.setVisibility(View.GONE);
                txt_Name_Mien_Nam.setVisibility(View.GONE);
                lnl_Mien_Nam.setVisibility(View.GONE);
                lnl_TableRow_MT.setVisibility(View.VISIBLE);

                rdb_Mien_Bac_Lottery.setChecked(false);
                rdb_Mien_Nam_Lottery.setChecked(false);
                rdb_Mien_Trung_Lottery.setChecked(true);
                // đây là phương thức load data kết quả xổ số của ba miền lên giao diện người dùng.
                loadDataLotteryHome(getDateNow(DEFAULT_DATE_SELECT_FORMAT), KET_QUA_MIEN_TRUNG);

            }
        });
    }

    private void setEventButtonResults() {

        btn_Results.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                traditionalLotteryListResults = new ArrayList<>();
                mAdapter = new AdapterResultsLottery(ResultsLottery.this, traditionalLotteryListResults);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                provinceNow = false;
                // lấy mã số; lấy đài xổ số; lấy ngày xổ số.
                String codeLottery = edt_Code_Lottery.getText().toString();
                String dateLottery = null;

                // Check if no view has focus:
               /* InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.getMessage();
                }
                try {
                    lnl_Results_Lottery.setVisibility(View.VISIBLE);
                    dateLottery = formatDate(txt_DatePicker.getText().toString());
                    boolean checkValidInput = checkValidInputLottery(codeLottery, domainLottery, getApplicationContext(), txt_Truy_Van_Do_Ve_So);
                    //boolean checkValidInput = checkValidInputLottery(codeLottery, domainLottery);
                    if(checkValidInput)
                    {

                          /*
                        Nếu kết quả xổ số mà người dùng muốn xem là ngày hôm nay thì mình phải kiếm tra xem đã tới giờ xổ số của các miền chưa.
                        Nếu trước 17h thì hiển thị kết quả xổ số của ngày hôm qua vì hôm nay chưa xổ miền Nam và miền Trung
                        Nếu trước 19h thì hiển thị kết quả xổ số của ngày hôm qua vì hôm nay chưa xổ miền Bắc
                     */
                        String getDomain = (getDomainLottery(domainLottery, getApplicationContext()).isEmpty())?"Không xác định":getDomainLottery(domainLottery, getApplicationContext());

                       //Kết quả xổ số % - Xổ số % mở thưởng ngày % hiện chưa có trên hệ thống vui lòng bấm đây chờ mở thưởng.
                        int getDomainVerify = checkDomain(domainLottery, getApplicationContext());
                        if(checkActionResults(dateLottery, getDomainVerify) && checkLotteryFromDateSelected(domainLottery, dateLottery, getApplicationContext()))
                        {

                            cv_Mien_Xo_So.setVisibility(View.GONE);
                            // provinceNow: xử lý lưu tên mà người dùng vừa tìm kiếm kết quả xổ số.
                            provinceNow = true;
                            lnl_Next_And_Previous_MB.setVisibility(View.GONE);
                            // ẩn kết quả xổ số của các miền xổ số khác miền xổ số mà người dùng đang dò vé số.
                            disableLinearLayout();
                            Log.d("ve_so_chua_xo",  checkActionResults(dateLottery, getDomainVerify) + "1111");
                            /*(TextView txt_Ket_Qua_Xo_So_Theo_Dai, String domainLottery, String dateLottery,
                                                 RecyclerView rcv_MienBac_Lottery, Activity context)*/
                            lnl_TableRow_MB_Province.setVisibility(View.VISIBLE);
                            setResultsLotteryProvince(txt_Ket_Qua_Xo_So_Theo_Dai, domainLottery, dateLottery, rcv_MienBac_Lottery, ResultsLottery.this);
                            //<string name="truy_van_do_ve_so_hom_nay">Bạn đã truy vấn dò kết quả vé số %s - Xổ số %s\nLoại vé %s chữ số mệnh giá 10,000 đ, mở thưởng ngày %s.\nDãy số của bạn là: %s</string>
                            // Bạn đã truy vấn dò kết quả vé số %s - Xổ số %s\nLoại vé %s chữ số mệnh giá 10,000đ, mở thưởng ngày %s.\nDãy số của bạn là: %s
                            txt_Truy_Van_Do_Ve_So.setText("");
                            txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.black));
                            txt_Truy_Van_Do_Ve_So.append(Html.fromHtml("Bạn đã truy vấn dò kết quả vé số " + getColoredSpanned(domainLottery, COLOR_BLACK) + " - Xổ số " + getDomain));
                            txt_Truy_Van_Do_Ve_So.append("\n");
                            txt_Truy_Van_Do_Ve_So.append(Html.fromHtml("Mở thưởng ngày: " + getColoredSpanned(dateLottery, COLOR_BLACK)));
                            txt_Truy_Van_Do_Ve_So.append("\n");
                            txt_Truy_Van_Do_Ve_So.append(Html.fromHtml(" Dãy số của bạn là: "+ getColoredSpanned(codeLottery, COLOR_BLACK)));
                           /* txt_Truy_Van_Do_Ve_So.append(Html.fromHtml("Bạn đã truy vấn dò kết quả vé số " + getColoredSpanned(domainLottery, COLOR_BLACK) + "- Xổ số " + getDomain + "\nMở thưởng ngày: "
                                    + getColoredSpanned(dateLottery, COLOR_BLACK) + "\nDãy số của bạn là: "+ getColoredSpanned(codeLottery, COLOR_BLACK)));
*/
                            //txt_Truy_Van_Do_Ve_So.setText(getString(R.string.truy_van_do_ve_so_hom_nay, domainLottery, getDomain, String.valueOf(codeLottery.length()), dateLottery, codeLottery));

                            Log.d("checkDomain", " codeLottery = "+ codeLottery + " dateLottery = "+ dateLottery +" domainLottery = " + domainLottery);
                            //(String domainLottery, String dateLottery, String codeLottery,
                            //                                               Activity context, RecyclerView mRecyclerView)
                            if(getDomainVerify == 0)
                            {
                                // Miền Bắc nên set = true và không cần dò ngày
                                detectingLotteryTickets(domainLottery, dateLottery, codeLottery, ResultsLottery.this, mRecyclerView, true);
                            }else {
                                detectingLotteryTickets(domainLottery, dateLottery, codeLottery, ResultsLottery.this, mRecyclerView, false);
                            }

                            img_Closes_Code.setVisibility(View.VISIBLE);
                        }else {
                            Log.d("ve_so_chua_xo",  domainLottery + getDomain + dateLottery); //Kết quả xổ số % - Xổ số % mở thưởng ngày % hiện chưa có trên hệ thống vui lòng bấm đây chờ mở thưởng.
                            txt_Truy_Van_Do_Ve_So.setText(getString(R.string.ve_so_chua_xo, domainLottery, getDomain, dateLottery));
                            txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.bgLogo));
                            //05-05-2021
                            Log.d("checkDomain", " codeLottery = "+ codeLottery + " dateLottery = "+ dateLottery +" domainLottery = " + domainLottery);
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static boolean checkLotteryFromDateSelected(String tenDai, String dateLottery, Context context)
    {
        TemporaryFileDBTraditionalLottery dbTraditionalLottery = new TemporaryFileDBTraditionalLottery(context);
        int getResultLotteryOfDate = dbTraditionalLottery.getLotteryFromDateSelected(tenDai, dateLottery);
        if(getResultLotteryOfDate >= 1)
            return true;
        else
            return false;
    }

    /**
     * Kiêm tra đài đó có kết quả xổ số hay chưa
     * @param dateLottery
     * @param getDomainVerify
     * @return
     */
    public static boolean checkActionResults(String dateLottery, int getDomainVerify) {

        boolean checkActionResults = true; // kiểm tra xem có đủ điều kiện để dò vé số hay không

        Log.d("ngayXoSo", "DEFAULT_DATE_SELECT_FORMAT = " + getDateNow(DEFAULT_DATE_SELECT_FORMAT) + " dateLottery= "+ dateLottery);
        if(getDateNow(DEFAULT_DATE_SELECT_FORMAT).equals(dateLottery))
        {
            if(getDomainVerify == KET_QUA_MIEN_BAC)
            {
                if(Integer.parseInt(getDateNow(DEFAULT_HH)) < 19)
                {
                    checkActionResults = false;
                }
            }
            else if(getDomainVerify == KET_QUA_MIEN_TRUNG)
            {
                if(Integer.parseInt(getDateNow(DEFAULT_HH)) < 18)
                {
                    checkActionResults = false;
                }
            }
            else // Mien Trung và Mien Nam
            {
                Log.d("DEFAULT_mm",  "DEFAULT_HH = " +  Integer.parseInt(getDateNow(DEFAULT_HH)) + " DEFAULT_mm = " + Integer.parseInt(getDateNow(DEFAULT_mm)));
                if(Integer.parseInt(getDateNow(DEFAULT_HH)) < 16)
                {
                    checkActionResults = false;
                }
                else if((Integer.parseInt(getDateNow(DEFAULT_HH)) == 16) && (Integer.parseInt(getDateNow(DEFAULT_mm)) < DEFAULT_MINUTES))
                {
                    checkActionResults = false;
                }
                else
                {
                    checkActionResults = true;
                }
                Log.d("DEFAULT_mm",  "checkActionResults = " +  checkActionResults);
            }
        }
        Log.d("ve_so_chua_xo",  checkActionResults + ""); //Kết quả xổ số % - Xổ số % mở thưởng ngày % hiện chưa có trên hệ thống vui lòng bấm đây chờ mở thưởng.
        return checkActionResults;
    }

    private void setLinearLayoutMB() {
        lnl_Prevoius_MB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataWhenClickButtonPrevious(KET_QUA_MIEN_BAC);
            }
        });

        //provinceNow

        lnl_Next_MB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataWhenClickButtonNext(KET_QUA_MIEN_BAC);
            }
        });
    }

    private void setLinearLayoutMT() {
        lnl_Prevoius_MT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataWhenClickButtonPrevious(KET_QUA_MIEN_TRUNG);
            }
        });


        lnl_Next_MT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDataWhenClickButtonNext(KET_QUA_MIEN_TRUNG);
            }
        });
    }

    private void setLinearLayoutMN() {

        lnl_Prevoius_MN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataWhenClickButtonPrevious(KET_QUA_MIEN_NAM);
            }
        });

        lnl_Next_MN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataWhenClickButtonNext(KET_QUA_MIEN_NAM);
            }
        });
    }

    /**
     * setDataWhenClickButtonNext là chức năng next sang kết quả xổ số ngày kế tiếp
     * @param DomainLottery
     */
    private void setDataWhenClickButtonNext(int DomainLottery)
    {
        String dateNext;
        if(DomainLottery == KET_QUA_MIEN_NAM)
            dateNext = txt_Date_Three_MN.getText().toString();
        else if(DomainLottery == KET_QUA_MIEN_TRUNG)
            dateNext = txt_Date_Lottery_MT.getText().toString();
        else
            dateNext = txt_Date_Lottery_MB.getText().toString();

        // đây là phương thức load data kết quả xổ số của ba miền lên giao diện người dùng.
        long dateConvert = convertDateToMillisecond(dateNext) + ONEDAY;
        Log.d("ngayy", dateNext + " === " + dateConvert  + " ===== " + (getMillisecondNow() + 10000));
        String timeNow = getDateNow(DEFAULT_HH);
        boolean checkNextData = false;
        if(Integer.parseInt(timeNow) < 17 && DomainLottery == KET_QUA_MIEN_NAM
        || (Integer.parseInt(timeNow) < 19 && DomainLottery == KET_QUA_MIEN_BAC)
        || (Integer.parseInt(timeNow) < 18 && DomainLottery == KET_QUA_MIEN_TRUNG))
        {
            if(dateConvert < (getMillisecondNow() - ONEDAY))
            {
                checkNextData = true;
            }else {
                if(Integer.parseInt(timeNow) < 17)
                {
                    Toast.makeText(getApplicationContext(), "Ngày "+ convertLongToDate(convertDateToMillisecond(dateNext) + ONEDAY) + " chưa có kết quả xổ số 3 Miền.", Toast.LENGTH_SHORT).show();
                }else if((Integer.parseInt(timeNow) < 18))
                {
                    Toast.makeText(getApplicationContext(), "Ngày "+ convertLongToDate(convertDateToMillisecond(dateNext) + ONEDAY) + " chưa có kết quả xổ số Miền Trung và Miền Bắc.", Toast.LENGTH_SHORT).show();
                }
                else if((Integer.parseInt(timeNow) < 19))
                {
                    Toast.makeText(getApplicationContext(), "Ngày "+ convertLongToDate(convertDateToMillisecond(dateNext) + ONEDAY) + " chưa có kết quả xổ số Miền Bắc.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            if(dateConvert < getMillisecondNow())
            {
                checkNextData = true;
            }else {
                Toast.makeText(getApplicationContext(), "Ngày "+ convertLongToDate(convertDateToMillisecond(dateNext) + ONEDAY) + " chưa có kết quả xổ số ", Toast.LENGTH_SHORT).show();
            }
        }

        if(checkNextData)
        {
            String date = convertLongToDate(dateConvert);

            if(DomainLottery == KET_QUA_MIEN_NAM)
                txt_Date_Three_MN.setText(date);
            else if(DomainLottery == KET_QUA_MIEN_TRUNG)
                txt_Date_Lottery_MT.setText(date);
            else
                txt_Date_Lottery_MB.setText(date);

            loadDataLotteryHome(date, MienNguoiDungChon);
        }
    }

    /**
     * setDataWhenClickButtonPrevious là chức năng previous lại kết quả xổ số giảm lại 1 ngày
     * @param DomainLottery
     */
    private void setDataWhenClickButtonPrevious(int DomainLottery)
    {
        String datePreviuos;
        if(DomainLottery == KET_QUA_MIEN_NAM)
            datePreviuos = txt_Date_Three_MN.getText().toString();
        else if(DomainLottery == KET_QUA_MIEN_TRUNG)
            datePreviuos = txt_Date_Lottery_MT.getText().toString();
        else
            datePreviuos = txt_Date_Lottery_MB.getText().toString();

            Log.d("ngayy", datePreviuos + " === " + (convertDateToMillisecond(datePreviuos) - ONEDAY)  + " ===== " + ((getMillisecondNow() - ONEDAY*30 )));
            // đây là phương thức load data kết quả xổ số của ba miền lên giao diện người dùng.
            if((convertDateToMillisecond(datePreviuos) - ONEDAY) > (getMillisecondNow() - ONEDAY*31 ))
            {

                String date = convertLongToDate(convertDateToMillisecond(datePreviuos) - ONEDAY);
                Log.d("ngayy", "date = " + convertLongToDate(convertDateToMillisecond(datePreviuos) - ONEDAY) + " === " + txt_Date_Lottery_MB.getText().toString() + " === " + (convertDateToMillisecond(datePreviuos) - ONEDAY)  + " ===== " + ((getMillisecondNow() - ONEDAY*30 )));
                if(DomainLottery == KET_QUA_MIEN_NAM)
                    txt_Date_Three_MN.setText(date);
                else if(DomainLottery == KET_QUA_MIEN_TRUNG)
                    txt_Date_Lottery_MT.setText(date);
                else
                    txt_Date_Lottery_MB.setText(date);

                loadDataLotteryHome(date, MienNguoiDungChon);

            }else {
                Toast.makeText(getApplicationContext(), "Ngày "+ convertLongToDate(convertDateToMillisecond(datePreviuos)) + " đã hết hạn lãnh thưởng xổ số!", Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * Đây là phương thức lấy ra kết quả xổ số của 1 tỉnh và cập nhật vào Recyclerview cho người dùng xem chi tiết.
     */
    public static void setResultsLotteryProvince(TextView txt_Ket_Qua_Xo_So_Theo_Dai, String domainLottery, String dateLottery,
                                                 RecyclerView rcv_MienBac_Lottery, Activity context) {

        TemporaryFileDBTraditionalLottery dbTraditionalLottery = new TemporaryFileDBTraditionalLottery(context);
        TraditionalLottery traditionalLottery = dbTraditionalLottery.getTraditionalLotteryDBReadWithDomain(domainLottery, dateLottery, String.valueOf(checkDomain(domainLottery, context)));
        int getDomainVerify = checkDomain(domainLottery, context);
        //List<PrizeResults> prizeResultsListMB_Province;
        RecyclerView.Adapter mAdapterMB1;

        if(getDomainVerify == 0)
        {
            prizeResultsListMB_Province = setPrizeResultsList(1, traditionalLottery, true);
            // adapter
            mAdapterMB1 = new AdapterDetailResultsLotteryMB(context, prizeResultsListMB_Province, true);
        }
        else
        {
            prizeResultsListMB_Province = setPrizeResultsList(1, traditionalLottery, false);
            // adapter
            mAdapterMB1 = new AdapterDetailResultsLotteryMB(context, prizeResultsListMB_Province, false);
        }

        if(prizeResultsListMB_Province != null)
        {
            String getDomain = (getDomainLottery(domainLottery, context).isEmpty())?"Không xác định":getDomainLottery(domainLottery, context);
            txt_Ket_Qua_Xo_So_Theo_Dai.setText(context.getString(R.string.ket_qua_mien_bac, getDomain + " - "+ dateLottery.replace("-","/")));
            Log.d("CheckHienThi", "prizeResultsListMB_Province = " + prizeResultsListMB_Province.size());
            rcv_MienBac_Lottery.setAdapter(mAdapterMB1);
            //recyclerView.setNestedScrollingEnabled(false); // tắt scroll trong recyclerview
            mAdapterMB1.notifyDataSetChanged();
        }
    }

    /**
     * Đây là phương thức ẩn LinearLayout khi người dùng bấm dò vé số của đài nào đó cụ thể
     */
    private void disableLinearLayout() {
        //lnl_TableRow_MT, lnl_TableRow_MN, lnl_TableRow_MB_Province
        lnl_TableRow_MT.setVisibility(View.GONE);
        lnl_TableRow_MN.setVisibility(View.GONE);
        //lnl_TableRow_MB_Province.setVisibility(View.GONE);
    }

    public static String getDomainLottery(String domainLottery, Context context)
    {

        if(checkDomain(domainLottery, context) >=0 && checkDomain(domainLottery, context) < 5)
        {
            Log.d("checkDomain", "domainLottery =  " + checkDomain(domainLottery, context));
           return getDomain(checkDomain(domainLottery, context));
        }else {
            return "";
        }
    }

    /**
     * detectingLotteryTickets xử lý dò vé số và đưa ra kết quả trúng hoặc không trúng
     * @param domainLottery
     * @param dateLottery
     * @param codeLottery
     */
    public static void detectingLotteryTickets(String domainLottery, String dateLottery, String codeLottery,
                                               Activity context, RecyclerView mRecyclerView, boolean checkMienBac) {


        Log.d("checkDomain", "domainLottery = " +domainLottery + " dateLottery = " +dateLottery + " codeLottery = " +codeLottery);
        int RowID = -100;
        TemporaryFileDBTraditionalLottery dbTraditionalLottery = new TemporaryFileDBTraditionalLottery(context);
        TraditionalLottery traditionalLottery = dbTraditionalLottery.getTraditionalLotteryDBReadWithDomain(domainLottery, dateLottery, checkMienBac);
        if(traditionalLottery != null)
        {
            RowID = traditionalLottery.getROW_ID();
            // Đây là phương thức quét kiểm tra xem mã vé số này có trúng thưởng giải nào của tờ vé số người dùng dò hay không
            if(!checkMienBac)
            {
                checkGiaiKhuyeKhich(traditionalLottery, codeLottery); // chỉ kiểm tra miền nam hoặc miền trung
            }

        }else {
            Log.d("checkDomain", "traditionalLottery != null");
        }

        Log.d("checkDomain", "checkDomain = " +checkDomain(domainLottery, context));
        TraditionalLottery traditionalLotteryDB = dbTraditionalLottery.CheckResultsLotteryDBReadWithTime(codeLottery, checkDomain(domainLottery, context), domainLottery, dateLottery);

        Log.d("checkDomain", "domainLottery = " + domainLottery + " dateLottery = " + dateLottery);
        if(traditionalLotteryDB != null && traditionalLotteryDB.getMIEN_XO_SO()!= null)
        {
            // Đây là phương thức quét kiểm tra xem mã vé số này có trúng thưởng giải nào của tờ vé số người dùng dò hay không
            checkKetQuaMaVeSo(codeLottery, traditionalLotteryDB, context);
        }else {
            Log.d("checkDomain", "traditionalLotteryDB == null");
        }

        RecyclerView.Adapter mAdapter;
        if(traditionalLotteryListResults != null && traditionalLotteryListResults.size()>0)
        {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // sắp xếp lại giải với thứ từ từ đặc biệt tới giải 8
            Collections.sort(traditionalLotteryListResults, new Comparator<WinningLottery>()
            {
                @Override
                public int compare(WinningLottery lhs, WinningLottery rhs) {

                    return Integer.compare(lhs.getPriority(), rhs.getPriority());
                }
            });


        }else {
            Log.d("checkDomain", "traditionalLotteryDB == null1");
            traditionalLotteryListResults.add(new WinningLottery(RowID,"imageLottery","","","","",codeLottery,0));
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLayoutManager);
          /*  // adapter
            if(checkLotteryImage)
                mAdapter = new AdapterResultsLottery(context, traditionalLotteryListResults, true);
            else
                mAdapter = new AdapterResultsLottery(context, traditionalLotteryListResults, false);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();*/
        }

        mAdapter = new AdapterResultsLottery(context, traditionalLotteryListResults);

      /*  if(checkLotteryImage)
            mAdapter = new AdapterResultsLottery(context, traditionalLotteryListResults, false);
        else
            mAdapter = new AdapterResultsLottery(context, traditionalLotteryListResults, false);*/
        // adapter

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }


    /**
     * Method: checkKetQuaMaVeSo Đây là phương thức quét kiểm tra xem mã vé số này có trúng thưởng giải nào trong 30 ngày qua hay không
     * @param codeLottery mã vé số
     * @param traditionalLottery danh sách các kết quả xổ số trong 30 ngày gần nhất.
     */
    public static void checkKetQuaMaVeSo(String codeLottery, TraditionalLottery traditionalLottery, Activity activity) {
        if(traditionalLottery!= null && traditionalLottery.getMIEN_XO_SO() != null)
        {


                List<String> listTongKetQua;
                if(codeLottery.length() == 5)
                    listTongKetQua = KiemTraGiaiTrungThuongMB(codeLottery, traditionalLottery);
                else // codeLottery.length() == 6
                    listTongKetQua = KiemTraGiaiTrungThuongMN(codeLottery, traditionalLottery);

                if(listTongKetQua.size() > 0)
                {
                    for (String ketQua : listTongKetQua) {
                        String [] splitKetQua = ketQua.split("-");
                        //String Giai = getColoredSpanned(splitKetQua[0], "#cc0029");
                        String maVeSo = getColoredSpanned(splitKetQua[1], "#cc0029");
                        Log.d("gii", "giải = " +splitKetQua[0]);
                        traditionalLotteryListResults.add(new WinningLottery(traditionalLottery.getROW_ID(),
                                splitKetQua[0],
                                maVeSo,
                                traditionalLottery.getTEN_DAI(),
                                traditionalLottery.getMIEN_XO_SO(),
                                traditionalLottery.getNGAY_XO_SO(),
                                codeLottery,
                                setPriority(splitKetQua[0])));
                    }
                }
        }
        else {
           // Toast.makeText(activity, "traditionalLottery.getMIEN_XO_SO() != null", Toast.LENGTH_SHORT).show();
        }
    }

    public static void checkGiaiKhuyeKhich(TraditionalLottery traditionalLottery, String codeLottery) {
        if (traditionalLottery != null && (!traditionalLottery.getMIEN_XO_SO().isEmpty())) {
            int positionSoKhongTrung = checkConsolationPrizes(codeLottery, traditionalLottery.getKET_QUA_DB());
           // Log.d("checkcc", "Check = " + positionSoKhongTrung + " Kết quả DB = " + traditionalLottery.getKET_QUA_DB() + " Mã vé số = " + codeLottery);
            if (positionSoKhongTrung != 0) // == 0 là không trúng giải khuyến khích còn nếu bằng số khác thì đã trúng giải khuyến khích và vị trí trả về là vị trí sai không trúng.
            {
                        traditionalLotteryListResults.add(new WinningLottery(traditionalLottery.getROW_ID(),
                                KHUYEN_KHICH_MN,
                                (KHUYEN_KHICH_MN + positionSoKhongTrung),
                                traditionalLottery.getTEN_DAI(),
                                traditionalLottery.getMIEN_XO_SO(),
                                traditionalLottery.getNGAY_XO_SO(),
                                codeLottery,
                                setPriority(KHUYEN_KHICH_MN)));
            }
        }
    }

    /**
     * checkDomain đây là phương thức lấy ra tên miền xổ số của tờ vé số người dùng đang dòn
     */
    public static int checkDomain(String domainLottery, Context context) {
        TemporaryFileDBLotterySchedule fileDBLotterySchedule = new TemporaryFileDBLotterySchedule(context);
        List<String> dateSelected = fileDBLotterySchedule.LotteryScheduleDBReadProvince(null, domainLottery);
        if(dateSelected.size() > 0)
        {
            return Integer.parseInt(dateSelected.get(0));
        }
        else {
            return KET_QUA_MIEN_BAC; // mặc định là 2
        }
    }

    /**
     * checkValidInputLottery đây là phương thức check valid dữ liệu nhập vào bao gồm mã số và đài mà người dùng chọn dò vé số.
     * @param codeLottery mã vé
     * @param domainLottery
     */
    @SuppressLint("SetTextI18n")
    public static boolean checkValidInputLottery(String codeLottery, String domainLottery, Context context, TextView txt_Truy_Van_Do_Ve_So) {

        boolean checkValid = true;
        if(codeLottery.length() < 5 && domainLottery.equals(SELECT_PROVINCE))
        {
            txt_Truy_Van_Do_Ve_So.setText(context.getResources().getString(R.string.dai_ma_ve_so_khong_hop_le));
            checkValid = false;
        }
        else {

            if(domainLottery.isEmpty() || domainLottery.equals(SELECT_PROVINCE) )
            {
                txt_Truy_Van_Do_Ve_So.setText(context.getResources().getString(R.string.dai_khong_hop_le));
                checkValid = false;
            }

            if(codeLottery.isEmpty() || codeLottery.length() < 5)
            {
                txt_Truy_Van_Do_Ve_So.setText(context.getResources().getString(R.string.ma_ve_so_khong_hop_le));
                checkValid = false;
            }else if(codeLottery.length() == 5 && (!domainLottery.equals(SELECT_PROVINCE))) // kiểm tra có phải xổ số miền Bắc hay miền Nam
            {
                if(checkDomain(domainLottery, context) != 0)
                {
                    txt_Truy_Van_Do_Ve_So.setText(context.getResources().getString(R.string.ma_ve_so_khong_hop_le_mien_nam));
                    checkValid = false;
                }
            }else if(codeLottery.length() == 6 && (!domainLottery.equals(SELECT_PROVINCE)))
            {
                if(checkDomain(domainLottery, context) == 0)
                {
                    txt_Truy_Van_Do_Ve_So.setText(context.getResources().getString(R.string.ma_ve_so_khong_hop_le_mien_bac));
                    checkValid = false;
                }
            }
        }
        if(checkValid)
            return true;
        else
        {
            txt_Truy_Van_Do_Ve_So.setTextColor(context.getResources().getColor(R.color.bgLogo));
            return false;
        }


    }

    /**
     * showDateDialog đây là phương thức hiển thị Dialog của datetime picker. và kiểm tra xem người dùng có chọn ngày khác với ngày hiện tại hay không?
     * @param txt_datePicker_Here truyền vào ngày tháng mà người dùng chọn để dò kết quả xổ số.
     */
    private void showDateDialog(final TextView txt_datePicker_Here) {
        final Calendar calendar = Calendar.getInstance();
        final String dateNow = txt_datePicker_Here.getText().toString();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                try {
                    lastSelectedDay = formatDate((dayOfMonth + "-" + (month +1) + "-" + year)); // do DatePickerDialog lấy ra tháng từ 0 -> 11 nên sẽ cộng thêm 1 cho tháng.
                    //Toast.makeText(ResultsLottery.this, lastSelectedDay, Toast.LENGTH_SHORT).show();
                    txt_datePicker_Here.setText(lastSelectedDay);
                    txt_DatePicker.setText(lastSelectedDay);
                    // xử lý khi người dùng chọn ngày thì đổi lại
                    setSpinnerWithDay(lastSelectedDay);

                    if(!dateNow.equals(lastSelectedDay))
                    {
                        loadDataLotteryHome(lastSelectedDay, MienNguoiDungChon);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-10000);
        DatePickerDialog dialog = new DatePickerDialog(ResultsLottery.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - (ONEDAY*30));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();


    }

    /**
     * setSpinnerWithDay thêm các tỉnh vào Spinner và cập nhật lại danh sách các tỉnh khi có thay đổi day.
     * @param day
     */
    private void setSpinnerWithDay(String day)
    {
        getProvinceLottery(day);
        if(dateSelect!= null && dateSelect.size()>0)
        {
            setSpinner(dateSelect);
        }
        else {
            //Toast.makeText(ResultsLottery.this, "Lỗi hiển thị tên các tỉnh!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * getProvinceLottery đây là phương thức lấy ra danh sách các đài xổ số theo thứ vd Thu 2: An Giang; Bạc Liêu; Vĩnh Long.
     * @param date
     */
    private void getProvinceLottery(String date){

        dateSelect = new ArrayList<>();

        String aDay = getDayOfMonth(date, false);
        TemporaryFileDBLotterySchedule fileDBLotterySchedule = new TemporaryFileDBLotterySchedule(getApplicationContext());
        dateSelect = fileDBLotterySchedule.LotteryScheduleDBReadProvince(aDay, null);
        dateSelect.add(0, SELECT_PROVINCE);
    }

    /**
     * setPrizeResultsList đây là phương thức lấy ra kết quả xổ số của 2 tỉnh.
     * @param size số lượng tỉnh
     * @param traditionalLotteryProvince1 tỉnh thứ nhất
     * @param traditionalLotteryProvince2 tỉnh thứ hai
     */
    private List<PrizeResults> setPrizeResultsList(int size, TraditionalLottery traditionalLotteryProvince1, TraditionalLottery traditionalLotteryProvince2)
    {
        List<PrizeResults> prizeResultsList = new ArrayList<>();
        String ngayXoSo = traditionalLotteryProvince1.getNGAY_XO_SO();
        prizeResultsList.add(new PrizeResults(ngayXoSo,size, traditionalLotteryProvince1.getTEN_DAI(), traditionalLotteryProvince2.getTEN_DAI()));
        prizeResultsList.add(new PrizeResults(G_8,size, traditionalLotteryProvince1.getKET_QUA_G8(), traditionalLotteryProvince2.getKET_QUA_G8()));
        prizeResultsList.add(new PrizeResults(G_7,size,  traditionalLotteryProvince1.getKET_QUA_G7(), traditionalLotteryProvince2.getKET_QUA_G7()));
        prizeResultsList.add(new PrizeResults(G_6,size,  traditionalLotteryProvince1.getKET_QUA_G6(), traditionalLotteryProvince2.getKET_QUA_G6()));
        prizeResultsList.add(new PrizeResults(G_5,size,  traditionalLotteryProvince1.getKET_QUA_G5(), traditionalLotteryProvince2.getKET_QUA_G5()));
        prizeResultsList.add(new PrizeResults(G_4,size,  traditionalLotteryProvince1.getKET_QUA_G4(), traditionalLotteryProvince2.getKET_QUA_G4()));
        prizeResultsList.add(new PrizeResults(G_3,size,  traditionalLotteryProvince1.getKET_QUA_G3(), traditionalLotteryProvince2.getKET_QUA_G3()));
        prizeResultsList.add(new PrizeResults(G_2,size,  traditionalLotteryProvince1.getKET_QUA_G2(), traditionalLotteryProvince2.getKET_QUA_G2()));
        prizeResultsList.add(new PrizeResults(G_1,size,  traditionalLotteryProvince1.getKET_QUA_G1(), traditionalLotteryProvince2.getKET_QUA_G1()));
        prizeResultsList.add(new PrizeResults(G_DB,size, traditionalLotteryProvince1.getKET_QUA_DB(), traditionalLotteryProvince2.getKET_QUA_DB()));

        return prizeResultsList;
    }

    /**
     * setPrizeResultsList đây là phương thức lấy ra kết quả xổ số của 1 tỉnh.
     * @param size số lượng tỉnh
     * @param traditionalLotteryProvince1 tỉnh thứ nhất
     */
    public static List<PrizeResults> setPrizeResultsList(int size, TraditionalLottery traditionalLotteryProvince1, boolean checkMB)
    {
        List<PrizeResults> prizeResultsList = new ArrayList<>();
        String ngayXoSo = traditionalLotteryProvince1.getNGAY_XO_SO();
        Log.d("ngayXoSo", "ngayXoSo = " + ngayXoSo);
        prizeResultsList.add(new PrizeResults(ngayXoSo,size, traditionalLotteryProvince1.getTEN_DAI()));
        if(!checkMB)
        {
            prizeResultsList.add(new PrizeResults(G_8,size, traditionalLotteryProvince1.getKET_QUA_G8()));
            Log.d("G8", "G8 = " + traditionalLotteryProvince1.getKET_QUA_G8());
        }
        //prizeResultsList.add(new PrizeResults(G_8,size, traditionalLotteryProvince1.getKET_QUA_G8()));
        prizeResultsList.add(new PrizeResults(G_7,size,  traditionalLotteryProvince1.getKET_QUA_G7()));
        prizeResultsList.add(new PrizeResults(G_6,size,  traditionalLotteryProvince1.getKET_QUA_G6()));
        prizeResultsList.add(new PrizeResults(G_5,size,  traditionalLotteryProvince1.getKET_QUA_G5()));
        prizeResultsList.add(new PrizeResults(G_4,size,  traditionalLotteryProvince1.getKET_QUA_G4()));
        prizeResultsList.add(new PrizeResults(G_3,size,  traditionalLotteryProvince1.getKET_QUA_G3()));
        prizeResultsList.add(new PrizeResults(G_2,size,  traditionalLotteryProvince1.getKET_QUA_G2()));
        prizeResultsList.add(new PrizeResults(G_1,size,  traditionalLotteryProvince1.getKET_QUA_G1()));
        prizeResultsList.add(new PrizeResults(G_DB,size, traditionalLotteryProvince1.getKET_QUA_DB()));
        Log.d("CheckHienThi", "traditionalLotteryProvince1.getKET_QUA_G7() = " + traditionalLotteryProvince1.getKET_QUA_G7());
        return prizeResultsList;
    }

   
    /**
     * setPrizeResultsList đây là phương thức lấy ra kết quả xổ số của 3 tỉnh.
     * @param size số lượng tỉnh
     * @param traditionalLotteryProvince1 tỉnh thứ nhất
     * @param traditionalLotteryProvince2 tỉnh thứ hai
     * @param traditionalLotteryProvince3 tỉnh thứ ba
     */
    private List<PrizeResults> setPrizeResultsList(int size, TraditionalLottery traditionalLotteryProvince1, TraditionalLottery traditionalLotteryProvince2, TraditionalLottery traditionalLotteryProvince3)
    {
        List<PrizeResults> prizeResultsList = new ArrayList<>();
        String ngayXoSo = traditionalLotteryProvince1.getNGAY_XO_SO();
        prizeResultsList.add(new PrizeResults(ngayXoSo,size, traditionalLotteryProvince1.getTEN_DAI(), traditionalLotteryProvince2.getTEN_DAI(), traditionalLotteryProvince3.getTEN_DAI()));
        prizeResultsList.add(new PrizeResults(G_8,size, traditionalLotteryProvince1.getKET_QUA_G8(), traditionalLotteryProvince2.getKET_QUA_G8(), traditionalLotteryProvince3.getKET_QUA_G8()));
        prizeResultsList.add(new PrizeResults(G_7,size,  traditionalLotteryProvince1.getKET_QUA_G7(), traditionalLotteryProvince2.getKET_QUA_G7(), traditionalLotteryProvince3.getKET_QUA_G7()));
        prizeResultsList.add(new PrizeResults(G_6,size,  traditionalLotteryProvince1.getKET_QUA_G6(), traditionalLotteryProvince2.getKET_QUA_G6(), traditionalLotteryProvince3.getKET_QUA_G6()));
        prizeResultsList.add(new PrizeResults(G_5,size,  traditionalLotteryProvince1.getKET_QUA_G5(), traditionalLotteryProvince2.getKET_QUA_G5(), traditionalLotteryProvince3.getKET_QUA_G5()));
        prizeResultsList.add(new PrizeResults(G_4,size,  traditionalLotteryProvince1.getKET_QUA_G4(), traditionalLotteryProvince2.getKET_QUA_G4(), traditionalLotteryProvince3.getKET_QUA_G4()));
        prizeResultsList.add(new PrizeResults(G_3,size,  traditionalLotteryProvince1.getKET_QUA_G3(), traditionalLotteryProvince2.getKET_QUA_G3(), traditionalLotteryProvince3.getKET_QUA_G3()));
        prizeResultsList.add(new PrizeResults(G_2,size,  traditionalLotteryProvince1.getKET_QUA_G2(), traditionalLotteryProvince2.getKET_QUA_G2(), traditionalLotteryProvince3.getKET_QUA_G2()));
        prizeResultsList.add(new PrizeResults(G_1,size,  traditionalLotteryProvince1.getKET_QUA_G1(), traditionalLotteryProvince2.getKET_QUA_G1(), traditionalLotteryProvince3.getKET_QUA_G1()));
        prizeResultsList.add(new PrizeResults(G_DB,size, traditionalLotteryProvince1.getKET_QUA_DB(), traditionalLotteryProvince2.getKET_QUA_DB(), traditionalLotteryProvince3.getKET_QUA_DB()));
        return prizeResultsList;
    }

    @Override
    public void onBackPressed() {

        if(checkBackPressed)
        {
            super.onBackPressed();
        }
        if(provinceNow)
        {
            checkBackPressed = true;
            lnl_Next_And_Previous_MB.setVisibility(View.VISIBLE);
            lnl_TableRow_MN.setVisibility(View.VISIBLE);
            lnl_TableRow_MT.setVisibility(View.VISIBLE);
            lnl_Results_Lottery.setVisibility(View.GONE);
            edt_Code_Lottery.setText("");
            loadDataLotteryHome(getDateNow(DEFAULT_DATE_SELECT_FORMAT), MienNguoiDungChon);
            img_Closes_Code.setVisibility(View.GONE);
            cv_Mien_Xo_So.setVisibility(View.VISIBLE);

        }else {
            super.onBackPressed();
        }


    }
}

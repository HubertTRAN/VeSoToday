package com.onebyte.doveso.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.onebyte.doveso.R;
import com.onebyte.doveso.adapter.AdapterResultsLottery;
import com.onebyte.doveso.model.NaturalVoice;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBNaturalVoice;
import com.r0adkll.slidr.Slidr;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import static com.onebyte.doveso.api.ApiMethod.changePosition;
import static com.onebyte.doveso.api.ApiMethod.formatDateStart;
import static com.onebyte.doveso.api.ApiMethod.getDateNow;
import static com.onebyte.doveso.api.ApiMethod.getSubDate;
import static com.onebyte.doveso.api.ApiMethod.getTextNormalizer;
import static com.onebyte.doveso.contants.Global.DATE_SELECT_FORMAT_D;
import static com.onebyte.doveso.contants.Global.DATE_SELECT_FORMAT_DM;
import static com.onebyte.doveso.contants.Global.DATE_SELECT_FORMAT_M;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_FORMAT;
import static com.onebyte.doveso.contants.Global.DEFAULT_MM;
import static com.onebyte.doveso.contants.Global.DEFAULT_YEAR;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_BAC;
import static com.onebyte.doveso.contants.Global.MIEN_BAC;
import static com.onebyte.doveso.contants.Global.MIEN_NAM;
import static com.onebyte.doveso.contants.Global.MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.THANG;
import static com.onebyte.doveso.controller.LotteryByImage.checkValidDateLottery;
import static com.onebyte.doveso.controller.LotteryByImage.isNumeric;
import static com.onebyte.doveso.controller.ResultsLottery.checkActionResults;
import static com.onebyte.doveso.controller.ResultsLottery.checkDomain;
import static com.onebyte.doveso.controller.ResultsLottery.checkLotteryFromDateSelected;
import static com.onebyte.doveso.controller.ResultsLottery.detectingLotteryTickets;
import static com.onebyte.doveso.controller.ResultsLottery.getDomainLottery;
import static com.onebyte.doveso.controller.ResultsLottery.traditionalLotteryListResults;

public class LotteryByVoice extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView img_Voice_Lottery;
    private static final int RECOGNIZER_RESULT = 1111;
    private TextView txt_Results_Lottery_Voice;
    private TextView txt_Truy_Van_Do_Ve_So;
    private Button btn_Help_Desk;
    private LinearLayout lnl_Help_Desk, lnl_Results_Lottery;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String dateNow = "";
    private boolean btn_Help_Desk_Is_Click; // đây là giá trị để kiểm tra xem btn_Help_Desk đã được click hay chưa click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_by_voice);
        setID();
        Slidr.attach(this);

    }

    /**
     * setID Khai báo ID cho các biến TextView, Button, ImageView...
     */
    private void setID() {

        toolbar = findViewById(R.id.toolbar_Lottery_By_Voice);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.app_name_alias));
        img_Voice_Lottery = findViewById(R.id.img_Voice_Lottery);
        img_Voice_Lottery.setOnClickListener(this);
        txt_Results_Lottery_Voice = findViewById(R.id.txt_Results_Lottery_Voice);
        txt_Truy_Van_Do_Ve_So = findViewById(R.id.txt_Truy_Van_Do_Ve_So_Voice);
        lnl_Help_Desk = findViewById(R.id.lnl_Help_Desk);
        lnl_Help_Desk.setVisibility(View.GONE);
        lnl_Results_Lottery = findViewById(R.id.lnl_Results_Lottery);
        lnl_Results_Lottery.setVisibility(View.GONE);
        btn_Help_Desk = findViewById(R.id.btn_Help_Desk);
        btn_Help_Desk_Is_Click = false;
        btn_Help_Desk.setOnClickListener(this);
        // mRecyclerView hiển thị kết quả xổ số khi người dùng bấm dò vé số.
        mRecyclerView = findViewById(R.id.rcv_Results_Lottery_Top);
        mRecyclerView.setHasFixedSize(true);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        // AdSize adSize = new AdSize(320, 100);
        AdView mAdView = findViewById(R.id.adView_Voice);
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * onClick gán sự kiện Onclick cho các biến TextView, Button, ImageView...
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.img_Voice_Lottery:
            {
                txt_Truy_Van_Do_Ve_So.setText("");
                Intent intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi_VN");
                intentSpeech.putExtra(RecognizerIntent.EXTRA_PROMPT,"Xin mời bạn nói");
                try {

                    startActivityForResult(intentSpeech,RECOGNIZER_RESULT);
                }catch (Exception e)
                {
                    Toast.makeText(LotteryByVoice.this, "Không thể bật Google giọng nói!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_Help_Desk:
            {
                if(!btn_Help_Desk_Is_Click)
                {
                    lnl_Help_Desk.setVisibility(View.VISIBLE);
                    btn_Help_Desk_Is_Click = true;
                }else {
                    lnl_Help_Desk.setVisibility(View.GONE);
                    btn_Help_Desk_Is_Click = false;
                }
                break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case RECOGNIZER_RESULT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt_Results_Lottery_Voice.setText(matches.get(0));
                    handleVoiceToText(matches.get(0));
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("SetTextI18n")
    private void handleVoiceToText(String textFromVoice) {
        try {
            if(textFromVoice != null || textFromVoice.isEmpty())
            {
                lnl_Results_Lottery.setVisibility(View.VISIBLE);
                Log.d("textFromVoice", "textFromVoice = " + textFromVoice);
                textFromVoice = getTextNormalizer(textFromVoice);
                Log.d("textFromVoice", "getTextNormalizer = " + textFromVoice);

                // xử lý tìm kiếm trong đoạn text mà người dùng nói để lấy ra đài cần dò của tờ vé số
                String provinceLottery = getProvinceFromText(textFromVoice);

                // xử lý tìm kiếm trong đoạn text mà người dùng nói để lấy ra ngày của tờ vé số
                String dateLottery = getDateFromText(textFromVoice.toLowerCase().replace(" ",""));
                Log.d("textFromVoice", "dateLottery = dateLottery "+dateLottery);
                String checkMien;
                // xử lý tìm kiếm trong đoạn text mà người dùng nói để lấy ra mã vé số cần dò của tờ vé số
                String codeLottery = "";
                if(!provinceLottery.isEmpty())
                {
                    int getDomainVerify;
                    if(provinceLottery.equals("Miền bắc"))
                    {
                        getDomainVerify = KET_QUA_MIEN_BAC;
                    }
                    else {
                        getDomainVerify = checkDomain(provinceLottery, getApplicationContext());
                    }


                    String textGetCode;
                    if(!dateNow.isEmpty())
                        textGetCode = textFromVoice.replace(" ", "").replace(dateNow,"");
                    else
                        textGetCode = textFromVoice.replace(" ", "");

                    if(getDomainVerify == KET_QUA_MIEN_BAC)
                        codeLottery = getCodeFromText(textGetCode, 5);
                    else
                        codeLottery = getCodeFromText(textGetCode, 6);
                }


                if(!provinceLottery.isEmpty() && !dateLottery.isEmpty() && !codeLottery.isEmpty())
                {
                    Log.d("textFromVoice", "handelGetResultLottery provinceLottery = "+ provinceLottery + " codeLottery = " + codeLottery + " dateLottery = "+ dateLottery);
                    handelGetResultLottery(provinceLottery, dateLottery, codeLottery);

                }
                else {

                    txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.bgLogo));
                    if(codeLottery.isEmpty() && provinceLottery.isEmpty() && dateLottery.isEmpty())
                    {
                        txt_Truy_Van_Do_Ve_So.setText("Không có thông tin tên Đài, Ngày, Mã vé số mà bạn muốn dò trong câu bạn vừa nói. Bạn vui lòng thử nói lại lần nữa.");
                    }
                    else {
                        if(codeLottery.isEmpty())
                        {
                            Log.d("textFromVoice", "codeLottery = isEmpty");
                            txt_Truy_Van_Do_Ve_So.setText("Mã vé số mà bạn nói đã không hợp lệ. Bạn vui lòng thử nói lại lần nữa.");
                        }
                        if(provinceLottery.isEmpty())
                        {
                            Log.d("textFromVoice", "provinceLottery = isEmpty");
                            txt_Truy_Van_Do_Ve_So.setText("Tên đài bạn nói đã không hợp lệ. Bạn vui lòng thử nói lại lần nữa.");
                        }
                        if(dateLottery.isEmpty())
                        {
                            Log.d("textFromVoice", "dateLottery = isEmpty");
                            txt_Truy_Van_Do_Ve_So.setText("Ngày cần dò của vé số mà bạn nói đã không hợp lệ. Bạn vui lòng thử nói lại lần nữa.");
                        }
                    }


                }
                Log.d("textFromVoice", "provinceLottery = "+ provinceLottery + " codeLottery = " + codeLottery + " dateLottery = "+ dateLottery);
            }
        }catch (Exception e)
        {
            e.getMessage();
        }
    }

    /**
     * handelGetResultLottery Đây là phương thức xử lý lấy ra kết quả xổ số và dò vé số cho người dùng từ ngày, đài, mã số đã lấy được từ voice.
     * @param provinceLottery tên đài
     * @param dateLottery ngày
     * @param codeLottery mã vé số
     */
    private void handelGetResultLottery(String provinceLottery, String dateLottery, String codeLottery)
    {

        try {
            if(provinceLottery.equals(MIEN_BAC))
                provinceLottery = "Hà Nội";

            int getDomainVerify = checkDomain(provinceLottery, getApplicationContext());
            Log.d("textFromVoice", getDomainVerify + " checkValidDateLottery " + checkValidDateLottery(dateLottery, txt_Truy_Van_Do_Ve_So, getApplicationContext()));
            if(checkValidDateLottery(dateLottery, txt_Truy_Van_Do_Ve_So, getApplicationContext()))
            {
                traditionalLotteryListResults = new ArrayList<>();
                mAdapter = new AdapterResultsLottery(LotteryByVoice.this, traditionalLotteryListResults);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                String getDomain = (getDomainLottery(provinceLottery, getApplicationContext()).isEmpty())?"Không xác định":getDomainLottery(provinceLottery, getApplicationContext());

                if(checkActionResults(dateLottery, getDomainVerify) && checkLotteryFromDateSelected(provinceLottery, dateLottery, getApplicationContext()))
                {
                    txt_Truy_Van_Do_Ve_So.setText(getString(R.string.truy_van_do_ve_so_hom_nay,provinceLottery, getDomain, String.valueOf(codeLottery.length()), dateLottery, codeLottery));
                    txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.black));

                    if(getDomainVerify == 0)
                        detectingLotteryTickets(provinceLottery, dateLottery, codeLottery, LotteryByVoice.this, mRecyclerView, true);
                    else
                        detectingLotteryTickets(provinceLottery, dateLottery, codeLottery, LotteryByVoice.this, mRecyclerView, false);
                }
                else {
                    Log.d("ve_so_chua_xo",  provinceLottery + getDomain + dateLottery); //Kết quả xổ số % - Xổ số % mở thưởng ngày % hiện chưa có trên hệ thống vui lòng bấm đây chờ mở thưởng.
                    txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
                    txt_Truy_Van_Do_Ve_So.setText(getString(R.string.ve_so_chua_xo, provinceLottery, getDomain, dateLottery));
                    txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.bgLogo));
                }

            }
        }catch (Exception e)
        {
            e.getMessage();
            Log.d("textFromVoice", "e.getMessage() = " + e.getMessage());
        }

    }


    /**
     * getProvinceFromText đây là phương thức tìm kiếm và lấy ra tên đài hoặc tên miền trong đoạn text mà người dùng nói.
     * @param textFromVoice đoạn text mà người dùng nói
     * @return tên đài hoặc tên miền
     */
    private String getProvinceFromText(String textFromVoice) {

        Log.d("textFromVoice", "textFromVoice = " + textFromVoice);
        TemporaryFileDBNaturalVoice temporaryFileDBNaturalVoice = new TemporaryFileDBNaturalVoice(getApplicationContext());
        List<NaturalVoice> naturalVoiceList = temporaryFileDBNaturalVoice.naturalVoiceDBRead(textFromVoice);
        List<String> provinceOrDomainLottery = new ArrayList<>();
        String province = "";
        if(naturalVoiceList.size()>0)
        {
                for (NaturalVoice naturalVoice: naturalVoiceList)
                {
                    // lấy ra miền hoặc tên đài trong naturalVoice đã lấy ra được.
                    provinceOrDomainLottery.add(naturalVoice.getCUM_TU_CHINH_XAC());
                }

                if(provinceOrDomainLottery.size()>0)
                {
                    for (String domain: provinceOrDomainLottery) {

                        if(domain.equals(MIEN_BAC))
                        {
                            Log.d("textFromVoice", "province = " + domain);
                            return domain;
                        }else {
                            if(!domain.equals(MIEN_NAM) && !domain.equals(MIEN_TRUNG))
                                province = domain;
                        }
                    }
                }
        }
        else {
            Log.d("textFromVoice", "naturalVoiceList.size() == 0");
            return "";
        }
        Log.d("textFromVoice", "province = " + province);
       return province;
    }

    /**
     * getCodeFromText Đây là phương thức lấy ra 6 ký tự số của tờ vé số.
     * @param textFromVoice đoạn text mà người dùng nói từ google voice sang text
     * @param numberCodeDomain là tổng số mã vé số cần lấy ra ví dụ miền bắc thì lấy 5 số miền nam thì lấy 6 số
     * @return trả về 6 ký tự của mã vé số
     */
    private String getCodeFromText(String textFromVoice, int numberCodeDomain) {

        textFromVoice = replaceDayToNumeric(textFromVoice);

        if(!textFromVoice.isEmpty())
        {
            if(!textFromVoice.contains(THANG) && !textFromVoice.contains("/"))
            {
                return getCodeLottery(textFromVoice, numberCodeDomain);
            }
            else if(textFromVoice.contains(THANG) || textFromVoice.contains("/"))
            {
                //String[] splitThang =  textFromVoice.split(THANG);
                String[] splitThang;
                textFromVoice = textFromVoice.replace(" ","");
                if(textFromVoice.contains(THANG))
                    splitThang  = textFromVoice.split(THANG);
                else
                    splitThang  = textFromVoice.split("/");

                if(splitThang.length == 2)
                {
                    String beforeThang = getCodeLottery(changePosition(splitThang[0]), numberCodeDomain);
                    String afterThang = getCodeLottery(splitThang[1], numberCodeDomain);

                    // kiểm tra cụm từ phía trước từ "tháng" có mã vé số hay không
                    if(!beforeThang.isEmpty())
                    {
                        beforeThang = changePosition(beforeThang);
                        Log.d("textFromVoice", "beforeThang = "+beforeThang);
                        return beforeThang;
                    }else {
                        Log.d("textFromVoice", "beforeThang is empty");
                    }
                    // kiểm tra cụm từ phía sau từ "tháng" có mã vé số hay không
                    if(!afterThang.isEmpty())
                    {
                        Log.d("textFromVoice", "afterThang = "+afterThang);
                        return afterThang;
                    }
                    else
                        Log.d("textFromVoice", "afterThang is empty");
                }
            }
        }
        return "";
    }

    /**
     * getCodeLottery Đây là phương thức con của phương thức getCodeFromText()
     * Nó giúp tách số và lấy ra 6 ký tự số của tờ vé số mà người dùng nói bằng giọng nói.
     */
    private String getCodeLottery(String textCode, int numberCodeDomain)
    {
        int demSo = 0;
        String maVeSo = "";
        for (int i = 0; i < textCode.length(); i++) {
            if (isNumeric(String.valueOf(textCode.charAt(i)))) {

                    if (demSo < 6) {
                        maVeSo += String.valueOf(textCode.charAt(i));
                        Log.d("CheckMaVeSo2", "Ký tự số là: " + textCode.charAt(i));
                        demSo += 1;
                    }
            } else { //|| (String.valueOf(text.charAt(i)).equals("O") && (demSo == 0))
                if (demSo < numberCodeDomain) {
                    demSo = 0;
                    maVeSo = "";
                    Log.d("CheckMaVeSo", "Ký tự khác: \t\t" + textCode.charAt(i));
                }
            }
        }

        if(numberCodeDomain == 5)
        {
            if ((demSo == numberCodeDomain || demSo == numberCodeDomain +1) && maVeSo.length() > 0) {
                Log.d("CheckMaVeSo1", "CheckMaVeSo = " + demSo + " Text = " + textCode + " Mã vé số là: " + maVeSo);
                return maVeSo;
            }
        }
        else {
            if (demSo == numberCodeDomain && maVeSo.length() > 0) {
                Log.d("CheckMaVeSo1", "CheckMaVeSo = " + demSo + " Text = " + textCode + " Mã vé số là: " + maVeSo);
                return maVeSo;
            }
        }
            return "";
    }

    /**
     * replaceDayToNumeric Đây là phương thức Replace các từ ngữ thuộc dạng số sang con số chính xác
     * Ví dụ: Một => 1
     * @param textVoice đoạn text lấy ra được từ google voice
     * @return đoạn text đã chuyển đổi số dạng chữ sang dạng con số chính xác.
     */
    private String replaceDayToNumeric(String textVoice)
    {
        if(!textVoice.isEmpty())
        {
            return textVoice.replace("mot","1")
                    .replace("hai", "2")
                    .replace("ba", "2")
                    .replace("bon", "2")
                    .replace("nam", "2")
                    .replace("sau", "2")
                    .replace("bay", "2")
                    .replace("tam", "2")
                    .replace("chin", "2")
                    .replace("muoi", "10");
        }
        return textVoice;
    }

    private String getDateFromText(String textFromVoice) {

        if(textFromVoice.contains("homnay") || textFromVoice.contains("buanay"))// Hôm nay || bữa nay
        {
            Log.d("textFromVoice", getSubDate(1));
            return getDateNow(DEFAULT_DATE_SELECT_FORMAT);
        }else if(textFromVoice.contains("homqua"))
        {
            Log.d("textFromVoice", getSubDate(1));
            return getSubDate(1);
        }else if(textFromVoice.contains("homkia"))
        {
            Log.d("textFromVoice", getSubDate(2));
            return getSubDate(2);
        }else {

            if(textFromVoice.contains(THANG) || textFromVoice.contains("/") || textFromVoice.contains("-"))
            {
                Log.d("textFromVoice", "textFromVoice.contains(THANG)");
                // ngày 25 tháng 5
                // 2 tháng 5
                // 25/5
                // xổ số An Giang ngày 25 tháng 5 mã số 488122
                // An Giang

                String[] splitDate;
                textFromVoice = textFromVoice.replace(" ","");
                String checkIsThang;
                if(textFromVoice.contains(THANG))
                {
                    splitDate  = textFromVoice.split(THANG);
                    checkIsThang = THANG;
                }
                else if(textFromVoice.contains("/"))
                {
                    splitDate  = textFromVoice.split("/");
                    checkIsThang = "/";
                }
                else
                {
                    splitDate  = textFromVoice.split("-");
                    checkIsThang = "-";
                }


                String aNgay = "";
                String aMonth = "";
                if(splitDate.length == 2)
                {
                    // Lấy ra ngày của tờ vé số
                    aNgay = getDateLottery(splitDate[0]);

                    aMonth = getMonthLottery(splitDate[1]);
                    Log.d("textFromVoice", "splitDate.length == 2");
                    if((!aNgay.isEmpty()) && (!aMonth.isEmpty()))
                    {

                        dateNow = aNgay+checkIsThang+aMonth;
                        String year;
                        if(aMonth.equals("12"))
                        {
                            if(getDateNow(DEFAULT_MM).equals("12"))
                            {
                                year = getDateNow(DEFAULT_YEAR);
                            }else {
                                year = (Integer.parseInt(getDateNow(DEFAULT_YEAR)) -1) +"";
                            }
                        }else {
                            year = getDateNow(DEFAULT_YEAR);
                        }
                        Log.d("textFromVoice", "Date == " +aNgay+"-" + aMonth + "-"+ year);
                        String dateLottery = aNgay+"-" + aMonth + "-" + year;
                        try {
                            if(aNgay.length() == 1 && aMonth.length() == 1)
                            {
                                dateLottery = formatDateStart(dateLottery, DATE_SELECT_FORMAT_DM, true);
                            }else if(aNgay.length() == 1 && aMonth.length() == 2)
                            {
                                dateLottery = formatDateStart(dateLottery, DATE_SELECT_FORMAT_D, true);
                            }
                            else if(aNgay.length() == 2 && aMonth.length() == 1)
                            {
                                dateLottery = formatDateStart(dateLottery, DATE_SELECT_FORMAT_M, true);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return "";
                        }

                        Log.d("textFromVoice", "dateLottery== "+ dateLottery);
                        return dateLottery;
                    }
                    return "";
                }
                else
                {
                    return "";
                }
            }else {
                Log.d("textFromVoice", "Chưa thể lấy được ngày từ đoạn text này");
            }
            return "";
        }
    }

    /**
     * getDateLottery Là phương thức lấy ra ngày từ google giọng nói mà người dùng nói vào
     * @param text Xổ số An Giang ngày
     * @return
     */
    private String getDateLottery(String text) {

        Log.d("textFromVoice", "getDateLottery text = "+ text);
        String aNgay = "";
        if(text.length()>=2)
        {

            String position1 = String.valueOf(text.charAt(text.length() - 2));
            String position2 = String.valueOf(text.charAt(text.length() - 1));

            String ngayTamp = position1 + position2;
            Log.d("textFromVoice", "ngayTamp = "+ ngayTamp);
            Log.d("textFromVoice", "ngayTamp = "+ position1 + "-" + position2);
            if(isNumeric(position1) && isNumeric(position2))
            {
                Log.d("textFromVoice", "ngayTamp = isNumeric(position1) && isNumeric(position2)");
                Log.d("textFromVoice", "position1.charAt(0) = "+ position1.charAt(0));
                if(Integer.parseInt(position1)<=3)
                {
                    Log.d("textFromVoice", "position1.charAt(0) <=3");
                    aNgay = ngayTamp;
                }
                else {
                    Log.d("textFromVoice", "position1.charAt(0) >3");
                    aNgay = position2;
                }
            }
            else if(isNumeric(position2) && (!isNumeric(position1)))
            {
                Log.d("textFromVoice", "isNumeric(position2) && (!isNumeric(position1))");
                aNgay = position2;
                Log.d("textFromVoice","aNgay = "+aNgay);
            }
            else {
                Log.d("textFromVoice", "lỗi không lấy được ngày");
                // lỗi không lấy được ngày
                // Không phải định dạng ngày
            }
        }
        else if(text.length()== 1)
        {
            if(isNumeric(text))
            {
                aNgay = text;
            }
        }
        else {
            // lỗi không lấy được ngày
        }

        return aNgay;
    }

    /**
     * getDateLottery Là phương thức lấy ra ngày từ google giọng nói mà người dùng nói vào
     * @param text Xổ số An Giang ngày
     * @return
     */
    private String getMonthLottery(String text) {

        String aMonth = "";
        if(text.length()>=2)
        {

            /*
                đếm thử phía sau có bao nhiêu số
                ví dụ 8 số thì 2 số đầu là tháng 6 số sau là mã vé số
                nếu 7 số thì số đầu tiên là tháng 6 số sau là mã vé số
             */
            int countNumber = 0;
            for (int i=0; i<text.length(); i++)
            {
                if(isNumeric(String.valueOf(text.charAt(i))))
                {
                    countNumber += 1;
                }
            }

            /*
                Nếu đếm tổng number trong đoạn text lớn hơn 2 thì có khả năng lấy được tháng.
             */
            if(countNumber>=2)
            {
                Log.d("textFromVoice", "getMonthLottery text == "+text);
                Log.d("textFromVoice", "getMonthLottery countNumber == "+countNumber);
                String aMonthFull = text.charAt(0) +""+ text.charAt(1);
                if(countNumber == 7) // 11 mã số 12345
                {
                    if(isNumeric(String.valueOf(text.charAt(0))) && isNumeric(String.valueOf(text.charAt(1))))
                    {
                        String position0= String.valueOf(text.charAt(0));
                        if(Integer.parseInt(position0) == 1)
                        {
                            Log.d("textFromVoice", "getMonthLottery text == "+text);
                            String position1= String.valueOf(text.charAt(1));
                            if(Integer.parseInt(position1) < 3)//Integer.parseInt(position1)
                            {
                                aMonth = aMonthFull;
                                Log.d("textFromVoice", "Integer.parseInt(position1) < 3 aMonth  == "+aMonth);
                            }
                            else {
                                aMonth = String.valueOf(text.charAt(0));
                                Log.d("textFromVoice", "Integer.parseInt(position1) >= 3 aMonth  == "+aMonth);
                            }
                        }
                        else if(Integer.parseInt(position0) == 0)
                        {
                            aMonth = aMonthFull;
                            Log.d("textFromVoice", "Integer.parseInt(position0) == 0 aMonth  == "+aMonth);
                        }
                        else {
                            aMonth = String.valueOf(text.charAt(0));
                            Log.d("textFromVoice", "aMonth  == "+aMonth);
                        }
                    }
                    else if(isNumeric(String.valueOf(text.charAt(0))) && (!isNumeric(String.valueOf(text.charAt(1)))))
                    {
                        aMonth = String.valueOf(text.charAt(0));
                        Log.d("textFromVoice", "isNumeric(String.valueOf(text.charAt(0))) && (!isNumeric(String.valueOf(text.charAt(1)) aMonth  == "+aMonth);
                    }
                }
                else // 11 mã số 123456
                {
                    if(isNumeric(String.valueOf(text.charAt(0))) && isNumeric(String.valueOf(text.charAt(1))))
                    {
                        String position0= String.valueOf(text.charAt(0));
                        if( Integer.parseInt(position0) == 0 || Integer.parseInt(position0) == 1)
                        {
                            Log.d("textFromVoice", "getMonthLottery aMonthFull == "+aMonthFull);
                            aMonth = aMonthFull;
                        }
                        else {
                            aMonth = String.valueOf(text.charAt(0));
                        }
                    }
                    else if(isNumeric(String.valueOf(text.charAt(0))) && (!isNumeric(String.valueOf(text.charAt(1)))))
                    {
                        aMonth = String.valueOf(text.charAt(0));
                    }
                }
            }
            else // đây là trường hợp có hoặc có 1 number
            {
                if(countNumber==1) // có 1 number trong đoạn text thì check xem có phải là tháng hay không
                {
                    if(isNumeric(String.valueOf(text.charAt(0))))
                    {
                        aMonth = String.valueOf(text.charAt(0));
                    }
                }
            }
        }
        else if(text.length()== 1)
        {
            if(isNumeric(text))
            {
                aMonth = text;
            }
        }
        Log.d("textFromVoice", "aMonth  == "+aMonth);
        return aMonth;
    }

}

package com.onebyte.doveso.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.onebyte.doveso.R;
import com.onebyte.doveso.adapter.AdapterResultsLottery;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBLotterySchedule;
import com.r0adkll.slidr.Slidr;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import static com.onebyte.doveso.api.ApiMethod.convertDateToMillisecond;
import static com.onebyte.doveso.api.ApiMethod.formatDate;
import static com.onebyte.doveso.api.ApiMethod.formatDateStart;
import static com.onebyte.doveso.api.ApiMethod.getDateNow;
import static com.onebyte.doveso.api.ApiMethod.getDayOfMonth;
import static com.onebyte.doveso.api.ApiMethod.getSubDate;
import static com.onebyte.doveso.api.ApiMethod.getTextNormalizer;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_M;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_FORMAT;
import static com.onebyte.doveso.contants.Global.DEFAULT_YEAR;
import static com.onebyte.doveso.contants.Global.MAXDATE;
import static com.onebyte.doveso.contants.Global.MAXMONTH;
import static com.onebyte.doveso.contants.Global.MA_DAI;
import static com.onebyte.doveso.contants.Global.MIEN_BAC;
import static com.onebyte.doveso.contants.Global.ONEDAY;
import static com.onebyte.doveso.contants.Global.SELECT_PROVINCE;
import static com.onebyte.doveso.contants.Global.TAI_CHINH;
import static com.onebyte.doveso.contants.Global.TEN_DAI;
import static com.onebyte.doveso.contants.Global.TEN_DAI_FULL;
import static com.onebyte.doveso.controller.ResultsLottery.checkActionResults;
import static com.onebyte.doveso.controller.ResultsLottery.checkDateDaiXoSo;
import static com.onebyte.doveso.controller.ResultsLottery.checkDomain;
import static com.onebyte.doveso.controller.ResultsLottery.checkLotteryFromDateSelected;
import static com.onebyte.doveso.controller.ResultsLottery.checkValidInputLottery;
import static com.onebyte.doveso.controller.ResultsLottery.detectingLotteryTickets;
import static com.onebyte.doveso.controller.ResultsLottery.getDomainLottery;
import static com.onebyte.doveso.controller.ResultsLottery.traditionalLotteryListResults;//
import static com.onebyte.doveso.controller.ResultsLottery.prizeResultsListMB_Province;
import static com.onebyte.doveso.controller.TakeAPicture.EXTRA_DATA;
import static com.onebyte.doveso.controller.TakeAPicture.SAVED;
import static com.onebyte.doveso.controller.TakeAPicture.currentPhotoPath;

public class LotteryByImage extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView img_Image_Lottery;
    private TextView txt_Truy_Van_Do_Ve_So;
    private static int REQUEST_CODE_TAKE_IMAGE = 200;
    private static String defaultYear;
    private LinearLayout lnl_Lottery, lnl_Results_Lottery, lnl_Guide_Image_Lottery;
    private TextView txt_DatePicker, txt_Province_Lottery;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Spinner spn_Name_Lottery;
    private EditText edt_Code_Lottery;
    private Button btn_Results, btn_Help_Desk;
    public static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    public String lastSelectedDay;
    private String domainLottery;
    int checkClickSpinner = 0;
    private boolean btn_Help_Desk_Is_Click; // đây là giá trị để kiểm tra xem btn_Help_Desk đã được click hay chưa click
    private final int RQ_CAMERA_PERMISSION = 200;
    // checkValidLottery đây là giá trị để kiểm tra xem có đủ điều kiện để dò vé số hay chưa
    // nếu checkValidLottery = 3 thì xem như đủ điều kiện dò vé số.
    private boolean checkValidLottery = false;
    private boolean provinceNow;
    private List<String> dateSelect;
    private  boolean checkSaveImage; // đây là giá trị biến lưu trữ trạng thái hình ảnh vừa chụp có được lưu thành công hay không.

    // xử lý check lấy ngày, đài, mã vé số
    String dateHere = "";
    boolean checkNotDate = false;
    boolean checkCodeLottery = false;
    boolean checkDaiXoSo = false;
    boolean checkAllowPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_by_camera);
        setID();
        setEvent();
        Slidr.attach(this);
        //getIntentFromTakeAPicture();
    }

    /**
     * getIntentFromTakeAPicture đây là phương thức nhận dữ liệu Intent từ class TakeAPicture
     * Nó sẽ nhận được giá trị Saved hoặc Failed;
     * Sau đó tiếp tục xử lý giải mã từ hình ảnh sang kiểu text để dò vé số.
     */
   /* private void getIntentFromTakeAPicture()
    {
        String getResultIntent = getIntent().getStringExtra(EXTRA_DATA);
        assert getResultIntent != null;
        if(getResultIntent.equals(SAVED))
            handleReadImageToText(Activity.RESULT_OK, null, true);
        else
            handleReadImageToText(Activity.RESULT_OK, null, false);
    }*/

    private void setID() {

        toolbar = findViewById(R.id.toolbar_Lottery_By_Voice);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.app_name_alias));

        // khai báo id cho các ô hiển thị dữ liệu nhập vào khi dò vé số
        lnl_Lottery = findViewById(R.id.lnl_Lottery);
        lnl_Results_Lottery = findViewById(R.id.lnl_Results_Lottery);
        //lnl_Ket_Qua_Test.setVisibility(View.GONE);
        lnl_Guide_Image_Lottery = findViewById(R.id.lnl_Guide_Image_Lottery);
        txt_DatePicker = findViewById(R.id.txt_DatePicker);
        txt_Province_Lottery = findViewById(R.id.txt_Province_Lottery);

        prizeResultsListMB_Province = new ArrayList<>();

        // mRecyclerView hiển thị kết quả xổ số khi người dùng bấm dò vé số.
        mRecyclerView = findViewById(R.id.rcv_Results_Lottery_Top);
        mRecyclerView.setHasFixedSize(true);

        //spn_Name_Lottery đây là Spinner để người dùng chọn đài cần dò. Nó được đồng bộ với ngày mà người dùng chọn dò vé số.
        spn_Name_Lottery = findViewById(R.id.spn_Name_Lottery);
        edt_Code_Lottery = findViewById(R.id.edt_Code_Lottery);
        btn_Results = findViewById(R.id.btn_Results);
        btn_Help_Desk = findViewById(R.id.btn_Help_Desk);
        btn_Help_Desk.setVisibility(View.VISIBLE);
        lnl_Guide_Image_Lottery.setVisibility(View.VISIBLE); // khi nào người dùng bấm vào btn_Help_Desk thì sẽ hiện ra hướng dẫn này.

        lnl_Lottery.setVisibility(View.GONE);
        lnl_Results_Lottery.setVisibility(View.GONE);
        img_Image_Lottery = findViewById(R.id.img_Image_Lottery);
        txt_Truy_Van_Do_Ve_So = findViewById(R.id.txt_Truy_Van_Do_Ve_So_Image);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        defaultYear = getDateNow(DEFAULT_YEAR);

        lastSelectedDay = checkDateDaiXoSo();
        setSpinnerWithDay(checkDateDaiXoSo());
        txt_Province_Lottery.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.custom_edt_date));

        AdRequest adRequest = new AdRequest.Builder().build();
        // AdSize adSize = new AdSize(320, 100);
        AdView mAdView = findViewById(R.id.adView_Image);
        mAdView.loadAd(adRequest);

        checkPermission();
    }

    /**
     * checkPermission là phương thức kiểm tra người dùng đã chấp nhận quyền chụp ảnh hay chưa
     */
    private void checkPermission()
    {
        //Check realtime permission if run higher API 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]
                        {
                                Manifest.permission.CAMERA},RQ_CAMERA_PERMISSION);
                checkAllowPermission = false;
            }else {
                checkAllowPermission = true;
            }
        }
        else {
            checkAllowPermission = true;
        }
    }

    private void setEvent() {

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

        img_Image_Lottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAllowPermission)
                {
                    Log.d("RQ_CAMERA_PERMISSION", "Đã chấp nhận quyền dò vé số.");
                    txt_DatePicker.setText("");
                    edt_Code_Lottery.setText("");
                    edt_Code_Lottery.setVisibility(View.VISIBLE);
                    txt_Province_Lottery.setText("");
                    lnl_Guide_Image_Lottery.setVisibility(View.GONE);
                    Intent featureIntent = new Intent(LotteryByImage.this, TakeAPicture.class);
                    startActivityForResult(featureIntent, REQUEST_CODE_TAKE_IMAGE);
                    //startActivity(featureIntent);
                    //finish();
                }else {
                    Log.d("RQ_CAMERA_PERMISSION", "Bạn vui lòng chấp nhận quyền để có thể chụp ảnh tờ vé số!");
                    Toast.makeText(LotteryByImage.this, "Bạn vui lòng chấp nhận quyền để có thể chụp ảnh tờ vé số!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(LotteryByImage.this,new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },RQ_CAMERA_PERMISSION);
                }

            }
        });

        btn_Help_Desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btn_Help_Desk_Is_Click)
                {
                    lnl_Guide_Image_Lottery.setVisibility(View.VISIBLE);
                    btn_Help_Desk_Is_Click = true;
                }else {
                    lnl_Guide_Image_Lottery.setVisibility(View.GONE);
                    btn_Help_Desk_Is_Click = false;
                }
            }
        });

        txt_DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(txt_DatePicker);
            }
        });

        btn_Results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnl_Guide_Image_Lottery.setVisibility(View.GONE);
                lnl_Results_Lottery.setVisibility(View.VISIBLE);
                handleDoVeSo(edt_Code_Lottery.getText().toString(), txt_DatePicker.getText().toString(), txt_Province_Lottery.getText().toString());
            }
        });
    }

    /**
     * handleDoVeSo Khi đã có đầy đủ thông tin của tờ vé số thì sẽ bắt đầu xử lý dò vé số và lấy kết quả hiển thị cho người dùng.
     * @param edt_Code_Lottery
     * @param txt_DatePicker
     * @param txt_Province_Lottery
     */
    private void handleDoVeSo(String edt_Code_Lottery, String txt_DatePicker, String txt_Province_Lottery) {

        traditionalLotteryListResults = new ArrayList<>();
        mAdapter = new AdapterResultsLottery(LotteryByImage.this, traditionalLotteryListResults);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        provinceNow = false;

        // lấy mã số; lấy đài xổ số; lấy ngày xổ số.
        String dateLottery = null;
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.getMessage();
        }

        try {

            dateLottery = formatDate(txt_DatePicker);
            Log.d("getSubdate", "dateLottery = "+ dateLottery);
            if(!txt_Province_Lottery.isEmpty())
            {
                domainLottery = txt_Province_Lottery;

                if(domainLottery.equals(MIEN_BAC))
                    domainLottery = "Hà Nội"; // khi máy ảnh quét tờ vé số thì chỉ lấy ra được Miền Bắc chứ không lấy được tên đài nên dùng đại diện đài Hà Nội để thể hiện miền bắc.
            }
            lnl_Lottery.setVisibility(View.VISIBLE);
            boolean checkValidInput = checkValidInputLottery(edt_Code_Lottery, domainLottery, getApplicationContext(), txt_Truy_Van_Do_Ve_So);
            Log.d("CheckHienThi", "checkValidInput = " + checkValidInput + " domainLottery = "+ domainLottery);

            if(checkValidDateLottery(dateLottery, txt_Truy_Van_Do_Ve_So, getApplicationContext()))
            {
                // xử lý trả về kế quả khi thông tin vé số đã được xác thực.
                checkValidInputIsTrue(checkValidInput, dateLottery, edt_Code_Lottery);
            }


        }catch (Exception e)
        {
            e.getMessage();
        }
    }

    @SuppressLint("SetTextI18n")
    public static boolean checkValidDateLottery(String dateLottery, TextView txt_Truy_Van_Do_Ve_So, Context context)
    {
         // check nếu quá ngày thì không dò vé số
        boolean checkQuaHanDoVeSo = checkValidDateLottery(dateLottery, true);
        Log.d("CheckHienThi", "checkQuaHanDoVeSo = "+ checkQuaHanDoVeSo);
        if(!checkQuaHanDoVeSo) // kiểm tra đã quá hạn dò vé số hay chưa
        {
            txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
            txt_Truy_Van_Do_Ve_So.setText("Vé số của bạn đã hết hạn lãnh thưởng. Vì đã quá 30 ngày kể từ ngày xổ số");
            Log.d("CheckHienThi", "Vé số của bạn đã hết hạn lãnh thưởng. Vì đã quá 30 ngày kể từ ngày xổ số");
            txt_Truy_Van_Do_Ve_So.setTextColor(context.getResources().getColor(R.color.red));
            return false;
        }
        else {
            boolean checkVeSoChuaXo = checkValidDateLottery(dateLottery, false);
            /**
             * check nếu chưa tới ngày xổ số thì không cho hành động dò vé số
             */
            if(!checkVeSoChuaXo) // kiểm tra đã quá hạn dò vé số hay chưa
            {
                txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
                txt_Truy_Van_Do_Ve_So.setText("Vé số của bạn chưa tới ngày xổ số!");
                txt_Truy_Van_Do_Ve_So.setTextColor(context.getResources().getColor(R.color.red));
                return false;
            }
        }
        return true;
    }
    /**
     * checkValidInputIsTrue Sau khi đã check valid thông tin của tờ vé số thì xử lý dò vé số và hiển thị kết quả.
     * @param checkValidInput thông tin của tờ vé số đã đầy đủ hay chưa
     * @param dateLottery ngày của tờ vé số cần dò
     */
    private void checkValidInputIsTrue(boolean checkValidInput, String dateLottery, String edt_Code_Lottery) {

        if(checkValidInput)
        {
            String getDomain = (getDomainLottery(domainLottery, getApplicationContext()).isEmpty())?"Không xác định":getDomainLottery(domainLottery, getApplicationContext());

            int getDomainVerify = checkDomain(domainLottery, getApplicationContext());

            if(checkActionResults(dateLottery, getDomainVerify) && checkLotteryFromDateSelected(domainLottery, dateLottery, getApplicationContext()))
            {
                Log.d("CheckHienThi", "checkActionResults = " + checkActionResults(dateLottery, getDomainVerify));
                // provinceNow: xử lý lưu tên tỉnh mà người dùng vừa tìm kiếm kết quả xổ số.
                provinceNow = true;

                Log.d("ve_so_chua_xo",  checkActionResults(dateLottery, getDomainVerify) + "1111");
                Log.d("ve_so_chua_xo",  domainLottery + getDomain + dateLottery);
                txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
                //<string name="truy_van_do_ve_so_hom_nay">Bạn đã truy vấn dò kết quả vé số %s - Xổ số %s\nLoại vé %s chữ số mệnh giá 10,000 đ, mở thưởng ngày %s.\nDãy số của bạn là: %s</string>
                txt_Truy_Van_Do_Ve_So.setText(getString(R.string.truy_van_do_ve_so_hom_nay,domainLottery, getDomain, String.valueOf(edt_Code_Lottery.length()), dateLottery, edt_Code_Lottery));
                txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.black));
                Log.d("checkDomain", " codeLottery = "+ edt_Code_Lottery + " dateLottery = "+ dateLottery +" domainLottery = " + domainLottery);

                    /*
                        Kiểm tra xem có phải miền bắc không nếu là đài miền bắc thì không cần dò thêm ngày.
                     */
                if(getDomainVerify == 0)
                    detectingLotteryTickets(domainLottery, dateLottery, edt_Code_Lottery, LotteryByImage.this, mRecyclerView, true);
                else
                    detectingLotteryTickets(domainLottery, dateLottery, edt_Code_Lottery, LotteryByImage.this, mRecyclerView, false);
                //img_Closes_Code.setVisibility(View.VISIBLE);
            }else {
                Log.d("ve_so_chua_xo",  domainLottery + getDomain + dateLottery); //Kết quả xổ số % - Xổ số % mở thưởng ngày % hiện chưa có trên hệ thống vui lòng bấm đây chờ mở thưởng.
                txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
                txt_Truy_Van_Do_Ve_So.setText(getString(R.string.ve_so_chua_xo, domainLottery, getDomain, dateLottery));
                txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.bgLogo));
                //05-05-2021
                Log.d("checkDomain", " codeLottery = "+ edt_Code_Lottery + " dateLottery = "+ dateLottery +" domainLottery = " + domainLottery);
            }
        }
//        else {
//           //lnl_Guide_Image_Lottery.setVisibility(View.VISIBLE);
//        }
    }

    /**
        checkValidDateLottery Đây là phương thức kiểm tra ngày của tờ vé số đã quá hạn dò vé số hay chưa.
        @param  checkQuaHan là giá trị kiểm tra ngày có quá hạn nếu = true hoặc kiểm tra ngày chưa có kết quả dò
     */
    public static boolean checkValidDateLottery(String dateLottery, boolean checkQuaHan) {

        if(checkQuaHan)
        {
            String getSubdate = getSubDate(30);
            Log.d("getSubdate", "getSubdate = "+ getSubdate);
            long date30 = convertDateToMillisecond(getSubdate);
            long dateLotteryLong =   convertDateToMillisecond(dateLottery);
            Log.d("getSubdate", "dateLotteryLong = "+ dateLotteryLong + "  date30= "+ date30);
            if(dateLotteryLong >= date30)
                return true;
            else
                return false;
        }
        else {
            long dateNow = convertDateToMillisecond(getDateNow(DEFAULT_DATE_SELECT_FORMAT));
            long dateLotteryLong =   convertDateToMillisecond(dateLottery);
            Log.d("getSubdate", "dateLotteryLong = "+ dateLotteryLong + "  dateNow= "+ dateNow);
            if(dateLotteryLong <= dateNow)
                return true;
            else
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
                    //Toast.makeText(LotteryByImage.this, lastSelectedDay, Toast.LENGTH_SHORT).show();
                    txt_datePicker_Here.setText(lastSelectedDay);
                    //txt_DatePicker.setText(lastSelectedDay);
                    // xử lý khi người dùng chọn ngày thì đổi lại
                    setSpinnerWithDay(lastSelectedDay);

                    txt_Province_Lottery.setText("");
                    txt_Province_Lottery.setVisibility(View.GONE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-10000);
        DatePickerDialog dialog = new DatePickerDialog(LotteryByImage.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
        Log.d("checkGetCodeqa", "dateSelect = "+ dateSelect);
        if(dateSelect!= null && dateSelect.size()>0)
        {
            setSpinner(dateSelect);
        }
        else {
            //Toast.makeText(LotteryByImage.this, "Lỗi hiển thị tên các tỉnh!", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(getApplicationContext(), "onNothingSelected" ,Toast.LENGTH_SHORT).show();
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
        if(checkClickSpinner == 0)
        {
            checkClickSpinner = 1;
        }else
        {
            txt_Province_Lottery.setText("");
            txt_Province_Lottery.setVisibility(View.GONE);
            //Toast.makeText(getApplicationContext(), "Selected Employee: " + domainLottery ,Toast.LENGTH_SHORT).show();
        }


        //Toast.makeText(getApplicationContext(), "Selected Employee: " + domainLottery ,Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // Khi kết quả được trả về từ Activity khác, hàm onActivityResult sẽ được gọi.
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // Kiểm tra requestCode có trùng với REQUEST_CODE vừa dùng
            if(requestCode == REQUEST_CODE_TAKE_IMAGE) {
                if(resultCode == Activity.RESULT_OK)
                {
                    handleReadImageToText(resultCode, data, false);
                }
                else {
                    Toast.makeText(this, "Không lấy được thông tin từ tờ vé số đã chụp!", Toast.LENGTH_SHORT).show();
                }
            }

        }catch (Exception e)
        {
            e.getMessage();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            if (requestCode == RQ_CAMERA_PERMISSION) {
                if (grantResults.length != 1) {
                    Toast.makeText(getApplicationContext(), "Bạn Chưa chấp nhận quyền để chụp hình ảnh tờ vé số!", Toast.LENGTH_SHORT).show();
                    Log.d("RQ_CAMERA_PERMISSION", "Chưa chấp nhận quyền dò vé số!");
                    checkAllowPermission = false;
                }
                else {
                    Log.d("RQ_CAMERA_PERMISSION", "Đã chấp nhận quyền dò vé số.");
                    checkAllowPermission = true;
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
    }

    /**
     * handleReadImageToText kiểm tra và check trường hợp có đầy đủ điều kiện dò vé số chưa
     * Nếu đủ điều kiện dò vé số thì sẽ tiến hành dò và đưa ra kết quả
     * @param resultCode
     * @param data
     * @param isSaveImage
     */
    private void handleReadImageToText(int resultCode, Intent data, boolean isSaveImage)
    {
        // RESULT_OK chỉ ra rằng kết quả này đã thành công
        if(resultCode == Activity.RESULT_OK) {
            Log.d("eeee", " Activity.RESULT_OK");
            if(currentPhotoPath != null && (!currentPhotoPath.isEmpty()))
            {
                Log.d("eeee", currentPhotoPath);
                // Nhận dữ liệu từ Intent trả về
                String result;
                if(data != null)
                {
                    result = data.getStringExtra(EXTRA_DATA);
                    assert result != null;
                }
                else {
                    result = "None";
                }

                if(result.equals(SAVED) || isSaveImage)
                {
                    Log.d("eeee",currentPhotoPath);
                    Bitmap myBitmap = BitmapFactory.decodeFile(currentPhotoPath);

                    if(myBitmap != null)
                    {
                        Matrix rotationMatrix = new Matrix();

                        Log.d("eeee","myBitmap.getWidth()  = " + myBitmap.getWidth()  + " myBitmap.getHeight() = " + myBitmap.getHeight());
                        if(myBitmap.getWidth() > myBitmap.getHeight())
                            rotationMatrix.postRotate(0);
                        else
                            rotationMatrix.postRotate(270);

                        Bitmap rotatedBitmap = Bitmap.createBitmap(myBitmap,0,0,myBitmap.getWidth(),myBitmap.getHeight(),rotationMatrix,true);
                        readTextFromImages(rotatedBitmap,txt_Truy_Van_Do_Ve_So);
                    }
                }else
                {
                    //Toast.makeText(this, "Result: " + result, Toast.LENGTH_LONG).show();
                    txt_Truy_Van_Do_Ve_So.setText(getResources().getString(R.string.khong_the_luu_hinh_anh));
                }
                // Sử dụng kết quả result bằng cách hiện Toast
                //Toast.makeText(this, "Result: " + result, Toast.LENGTH_LONG).show();
            }
        } else {
            // DetailActivity không thành công, không có data trả về.
            //Toast.makeText(this, "Result: " + getResources().getString(R.string.chua_chup_hinh_anh), Toast.LENGTH_LONG).show();
            txt_Truy_Van_Do_Ve_So.setText(getResources().getString(R.string.chua_chup_hinh_anh));
        }
    }

    private void readTextFromImages(Bitmap bitmap, final TextView textView){
        try {
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
            firebaseVisionTextDetector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    textView.setText("");
                    displayTextFromImage(firebaseVisionText, textView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("BUG", "Lỗi Detect image to text!" + e.getMessage());
                }
            });
        }catch (Exception e)
        {
            e.getMessage();
        }

    }

    /**
     * displayTextFromImage Khi đã lấy được text từ FirebaseVisionText thì bắt đầu xử lý triểm tra và hiển thị
     * mã vé số, đài, ngày xổ số và đầy đủ dữ liệu thì sẽ dò vé số tự động.
     * @param firebaseVisionText
     * @param textView
     */
    private void displayTextFromImage(FirebaseVisionText firebaseVisionText, TextView textView) {
        List<FirebaseVisionText.Block> firebaseVisionTextList = firebaseVisionText.getBlocks();

        if(firebaseVisionTextList.size() == 0)
        {
            Log.d("BUG", "Không tìm thấy chữ trong hình ảnh!");
            //lnl_Lottery.setVisibility(View.VISIBLE);
            lnl_Lottery.setVisibility(View.GONE);
        }
        else {
            StringBuilder lotteryResultFromImage = new StringBuilder();
            for (FirebaseVisionText.Block block: firebaseVisionText.getBlocks())
            {
                String text = block.getText();
                Log.d("BUG", "text = " + text);
               // textView.append(text +"\n"); // nhớ sửa lại chỗ này
                lotteryResultFromImage.append(text).append("\n");
            }
            //textView.setVisibility(View.GONE);
            lnl_Lottery.setVisibility(View.VISIBLE);
            // handleTextToLottery Khi đã lấy được dữ liệu text từ hình ảnh thì xử lý kiểm tra xem có đầy đủ ngày, mã vé số, đài xổ số hay chưa
            handleTextToLottery(lotteryResultFromImage.toString());


        }
    }

    /**
     * handleTextToLottery Khi đã lấy được dữ liệu text từ hình ảnh thì xử lý kiểm tra xem có đầy đủ ngày, mã vé số, đài xổ số hay chưa
     * nếu đủ điều kiện thì tiến hành dò vé số luôn.
     * @param lotteryResultFromImage Dữ liệu text lấy được từ hình ảnh
     */
    private void handleTextToLottery(String lotteryResultFromImage) {

        Log.d("lotteryResultFromImage", lotteryResultFromImage.toString());
        String [] splitResult = lotteryResultFromImage.toString().split("\n");
        Log.d("lotteryResultFromImage", splitResult.length+ " SIZE");
        checkCodeLottery = false;
        checkNotDate = false;
        checkDaiXoSo = false;
        dateHere = "";
        checkClickSpinner = 0;
        for(String text : splitResult)
        {
            Log.d("lotteryResultFromImages", "\t\t\t\t\t Kết quả = "+ text);
            /* Xử lý lấy ra ngày xổ số từ hình ảnh chụp tờ vé số của người dùng*/
            getDateLottery(text);
            // kiểm tra lấy ra mã số vé số từ hình ảnh người dùng chập
            getCodeLottery(text, checkNotDate);
            // kiểm tra lấy ra đài xổ số hoặc miền xổ số
            getProvince(text);
        }

        Log.d("TEN_DAz","checkDaiXoSo = " + checkDaiXoSo);
        if(checkDaiXoSo)
        {
            txt_Province_Lottery.setVisibility(View.VISIBLE);
           // checkClickSpinner = 1;
        }else
        {
            txt_Province_Lottery.setVisibility(View.GONE);
            checkClickSpinner = 1;
            //domainLottery = "Chọn Tỉnh";
        }
        // đây là phương thức kiểm tra xem đã đủ điều kiện dò vé số chưa
        // nếu đủ điều kiện thì sẽ tiền hành dò vé số
        // nếu chưa đủ điều kiện thì sẽ để người dùng chỉnh sửa và nhập thêm cho chính xác.
        Log.d("checkGetCodeq", "getDateLottery = " + txt_DatePicker.getText().toString()
                + "\rgetCodeLottery = " + edt_Code_Lottery.getText().toString()
                + "\rgetDateLottery = " + txt_Province_Lottery.getText().toString());
        checkAndShowResult(checkCodeLottery, dateHere, checkDaiXoSo);
        //checkClickSpinner = 1;
    }

    /**
     * checkAndShowResult Đây là phương thức kiểm tra xem đã đủ điều kiện dò vé số chưa
     * nếu đủ điều kiện thì sẽ tiền hành dò vé số
     * nếu chưa đủ điều kiện thì sẽ để người dùng chỉnh sửa và nhập thêm cho chính xác.
     * @param checkCodeLottery Mã của tờ vé số đã lấy được hay không
     * @param dateHere Ngày của tờ vé số có lấy được hay không
     * @param checkDaiXoSo Đài của tờ vé số có lấy được hay không
     */
    private void checkAndShowResult(boolean checkCodeLottery, String dateHere, boolean checkDaiXoSo) {
        if(checkCodeLottery && (!dateHere.isEmpty()) && checkDaiXoSo)
        {
            checkValidLottery = true;
            // Đủ điều kiện tiến hành dò vé số
            Log.d("checkGetCode", "đủ điều kiện tiến hành dò vé số");
            lnl_Results_Lottery.setVisibility(View.VISIBLE);
            txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
            txt_Province_Lottery.setVisibility(View.VISIBLE);
            Log.d("checkGetCodeq", "getDateLottery = " + txt_DatePicker.getText().toString()
                    + "getCodeLottery = " + edt_Code_Lottery.getText().toString()
                    + "getDateLottery = " + txt_Province_Lottery.getText().toString());

            handleDoVeSo(edt_Code_Lottery.getText().toString(), txt_DatePicker.getText().toString(), txt_Province_Lottery.getText().toString());
            setSpinnerWithDay(txt_DatePicker.getText().toString());
            //checkClickSpinner = 1;
        }else {
            //chưa đầy đủ điều kiện để dò vé số nên người dùng cần nhập thêm thông tin để bắt đầu dò vé số.
            Log.d("checkGetCode", "Không lấy được mã vé số từ hình ảnh tờ vé số");
            lnl_Results_Lottery.setVisibility(View.GONE);
            checkClickSpinner = 1;
        }
    }

    /**
     * getDateLottery đây là phương thức tìm kiếm và lấy ra ngày xổ số từ đoạn text được đọc ra từ hình ảnh tờ vé số.
     * @param text Đoạn text được đọc ra từ hình ảnh tờ vé số.
     */
    private void getDateLottery(String text) {
        /* Xử lý lấy ra ngày xổ số từ hình ảnh chụp tờ vé số của người dùng*/
        if(text.contains(defaultYear))
        {
            Log.d("checkGetCodeq", "\t\t\t\t\t Chứa Ngày = "+ text);
            int getYear = text.lastIndexOf(defaultYear); // năm hiện tại mà người dùng đang sống
            String getBeforeYear = text.substring(0,(getYear+4));

            String dateTamp;
            if(getBeforeYear.contains("-") && (!getDateHCM(getBeforeYear).isEmpty()))
            {
                dateTamp = getDateHCM(getBeforeYear);
            }else {
                dateTamp = text.substring(0,(getYear));
            }
            Log.d("checkGetCodeq", "\t\t\t\t\t Cắt ngày phía trước = "+ getBeforeYear);

            String getBeforeDayMonth = dateTamp
                    .replace("-","")
                    .replace("/","")
                    .replace(" ","")
                    .replace(":","")
                    .replace("*","");
            int viTriNumberBefore = 0;
            boolean checkDayMonth = true;
            String dayAndMonth ="";

            for (int i = (getBeforeDayMonth.length()-1); i>=0; i--)
            {
                        /*
                            Nếu đã có giá trị năm mà không đúng format thì ta tiếp tục xét xem có phải lấy được ngày tháng năm ở dòng này không.
                            Kiểm tra từ sau về trước để lấy ngày và tháng
                            -24-05
                            -2405
                            -24-5
                            -245
                            24 05
                            24 5
                            24:05
                            24:5
                            24*5
                            24*05
                            xo L07-03-2021
                         */
                if(getBeforeDayMonth.length() > 2)
                {
                    if(isNumeric(String.valueOf(getBeforeDayMonth.charAt(i))))
                    {
                        viTriNumberBefore += 1;
                        if(dayAndMonth.length()<=3)
                        {
                            dayAndMonth += getBeforeDayMonth.charAt(i);
                        }else {
                            // quá giới hạn ký tự ngày
                        }
                        // đây là ký tự số
                        Log.d("KYTUSO", "2405Ký tự số là: "+ getBeforeDayMonth.charAt(i));
                    }
                    else {
                        if(viTriNumberBefore < 3)
                        {
                            viTriNumberBefore = 0;
                            checkDayMonth = false;
                        }
                        Log.d("KYTUSO", "Ký tự khác: \t\t"+ getBeforeDayMonth.charAt(i));
                    }
                }
                else {
                    checkDayMonth = false;
                    // không phải date time
                    Log.d("KYTUSO", "không phải date time");
                }
            }

            if(checkDayMonth)
            {
                Log.d("checkDateT", "dayAndMonth = " + dayAndMonth);
                if(dayAndMonth.length() == 3)
                {
                    // 073 370 0 1 2
                    boolean checkDate = (Integer.parseInt(dayAndMonth.charAt(2) + ""+ dayAndMonth.charAt(1)) <= MAXDATE);
                    boolean checkMonth = false;
                    if((Integer.parseInt(dayAndMonth.charAt(0) + "") <= MAXMONTH) && (dayAndMonth.charAt(0) != 0))
                        checkMonth = true;

                    Log.d("checkDateT", "Đây là ngày " + dayAndMonth + " size ="+ dayAndMonth.length() + " Text = "+ getBeforeDayMonth);
                    Log.d("checkDateT", "Đây là tháng " + dayAndMonth.charAt(2) + ""+ dayAndMonth.charAt(1) +"-" + dayAndMonth.charAt(0) +"-"+ defaultYear);
                    Log.d("checkDateT", "Đây là boolean " + checkDate + "   "+ checkMonth + " dayAndMonth.charAt(0) = "+ dayAndMonth.charAt(0));
                    if(checkDate && checkMonth)
                    {
                        if(dateHere.isEmpty())
                        {
                            dateHere =  dayAndMonth.charAt(2) + ""+ dayAndMonth.charAt(1) +"-" + dayAndMonth.charAt(0) +"-"+ defaultYear;
                            Log.d("checkDateT", "Đây là ngày khi dayAndMonth.length() == 3 : " + dateHere  + " Text = "+ getBeforeDayMonth);
                            try {
                                txt_DatePicker.setText(formatDateStart(dateHere, DEFAULT_DATE_M, true));
                            } catch (ParseException e) {
                                Log.d("checkDateT", "Lỗi convert ngày: "+ e.getMessage());
                                e.printStackTrace();
                            }
                        }else {
                            Log.d("checkDateT", "Đã có ngày ở các dữ liệu phía trên!");
                        }
                        Log.d("checkDateT", "Đây là ngày " + dayAndMonth.charAt(2) + ""+ dayAndMonth.charAt(1) +"-" + dayAndMonth.charAt(0) +"-"+ defaultYear);
                    }else {
                        checkNotDate = true;
                    }
                }
                else if( dayAndMonth.length() == 4)
                {
                    // 0703 3070 0 1 2 4
                    String date = dayAndMonth.charAt(3) +""+ dayAndMonth.charAt(2);
                    boolean checkDate = (Integer.parseInt(date) <= MAXDATE);
                    String month = dayAndMonth.charAt(1) + "" + dayAndMonth.charAt(0);
                    boolean checkMonth = (Integer.parseInt(month) <= MAXMONTH);
                    boolean checkMonth00 = (month.equals("00") || date.equals("00"));
                    Log.d("checkDateT", "Đây là tháng " +  dayAndMonth.charAt(3) + dayAndMonth.charAt(2) + "-"  + dayAndMonth.charAt(1) + dayAndMonth.charAt(0) +"-"+ defaultYear);
                    Log.d("checkDateT", "Đây là boolean " + checkDate + "   "+ checkMonth);
                    if(checkDate && checkMonth && (!checkMonth00))
                    {
                        Log.d("checkDateT", "Đây là ngày " +  dayAndMonth.charAt(3) + dayAndMonth.charAt(2) + "-"  + dayAndMonth.charAt(1) + dayAndMonth.charAt(0) +"-"+ defaultYear);
                        if(dateHere.isEmpty())
                        {
                            dateHere = dayAndMonth.charAt(3) +""+ dayAndMonth.charAt(2) + "-"  + dayAndMonth.charAt(1) +""+ dayAndMonth.charAt(0) +"-"+ defaultYear;
                            txt_DatePicker.setText(dateHere);
                        }
                    }else {
                        checkNotDate = true;
                    }
                }
                else if(dayAndMonth.length() > 4)
                {
                    checkNotDate = true;
                    Log.d("checkDateT", "có nhiều số hơn dữ liệu ngày " + dayAndMonth + " size ="+ dayAndMonth.length() + " Text = "+ getBeforeDayMonth);
                }else {
                    checkNotDate = true;
                    Log.d("checkDateT", "Bé hơn 3 ký tự số" + " Text = "+ getBeforeDayMonth);
                }
            }
        }
        else {
            checkNotDate = true;
        }
    }

    /**
     * getCodeLottery đây là phương thức tìm kiếm và lấy ra mã vé số từ đoạn text được đọc ra từ hình ảnh tờ vé số.
     * @param text Đoạn text được đọc ra từ hình ảnh tờ vé số.
     * @param checkNotDate
     */
    private void getCodeLottery(String text, boolean checkNotDate) {

        // kiểm tra lấy ra mã số vé số từ hình ảnh người dùng chập
        String textCode = "";
        if(checkNotDate)
        {
            Log.d("lotteryResultFromImage", "checkNotDate = " + countNumeric(text));
            //lotteryResultFromImage
            if(!countNumeric(text).isEmpty())
            {
                textCode = countNumeric(text);
                Log.d("lotteryResultFromImage", "countNumeric = " + countNumeric(text));
            }
        }
        else
        {
            //7B2003 ngay 21-5-2021 782290 trong trường hợp ngày tháng đứng trước mã vé số thì tôi cắt bỏ phía trước
            // và check text phía sau xem có phải mã vé số hay không
            String dateBehind []= text.split(defaultYear);
            if(dateBehind.length >1)
            {
                if(dateBehind[1].length()>=6)
                    textCode = dateBehind[1];
                Log.d("lotteryResultFromImage", "dateBehind[1] = " + dateBehind[1]);
            }
        }
        // xử lý lấy ra mã vé số
        handleGetCodeLottery(textCode);
    }

    /**
     * handleGetCodeLottery xử lý lấy ra mã vé số
     * @param textCode đây là đoạn text đang có khả năng chứa mã vé số trong đó
     */
    private void handleGetCodeLottery(String textCode) {
        if(!textCode.isEmpty())
        {
            int demSo = 0;
            String maVeSo = "";
            String textNumber = textCode.trim().replace(" ","").replace("'","");
            if((!textNumber.contains("/")) && (!textNumber.contains(".00")))
            {
                for (int i = 0; i < textNumber.length(); i++)
                {

                    boolean equals = String.valueOf(textNumber.charAt(i)).equals(".");
                    if(isNumeric(String.valueOf(textNumber.charAt(i)))
                            || (String.valueOf(textNumber.charAt(i)).equals("O") && (demSo == 0))
                            || equals
                            || (String.valueOf(textNumber.charAt(i)).equals("I")))
                    {

                        if((!equals))
                        {
                            if(demSo < 6)
                            {
                                maVeSo += String.valueOf(textNumber.charAt(i))
                                        .replace("O","0")
                                        .replace(".","")
                                        .replace("I","1");
                                Log.d("CheckMaVeSo2", "Ký tự số là: "+ textNumber.charAt(i));
                                demSo +=1;
                            }
                        }
                    }
                    else { //|| (String.valueOf(text.charAt(i)).equals("O") && (demSo == 0))
                        if(demSo < 6)
                        {
                            demSo = 0;
                            maVeSo = "";
                            Log.d("CheckMaVeSo", "Ký tự khác: \t\t"+ textNumber.charAt(i));
                        }
                    }
                }
            }
            if(demSo == 6 && maVeSo.length() > 0)
            {
                if(!checkCodeLottery)
                {
                    Log.d("checkGetCodeq", "CheckMaVeSo = "+ demSo + " Text = "+ textNumber + " Mã vé số là: "+ maVeSo);
                    edt_Code_Lottery.setText(maVeSo);
                    checkCodeLottery = true;
                }
            }
            else {
                Log.d("CheckMaVeSo1", "CheckMaVeSo = "+ demSo + " Text = "+ textNumber + " Mã vé số là: "+ maVeSo);
            }
        }
    }



    /**
     * getProvince đây là phương thức tìm kiếm và lấy ra đài xổ số từ đoạn text được đọc ra từ hình ảnh tờ vé số.
     * @param text Đoạn text được đọc ra từ hình ảnh tờ vé số.
     */
    private void getProvince(String text) {

        // kiểm tra lấy ra đài xổ số hoặc miền xổ số
        // getTextNormalizer là phương thức chuyển ký tự có dấu thành không dấu.

        String textGetMaDai =  getTextNormalizer(text);
        textGetMaDai = textGetMaDai.toLowerCase().replace(" ","");
        Log.d("TEN_DAz","textGetMaDai = " + textGetMaDai);
        if((!textGetMaDai.contains(TAI_CHINH)))
        {
            for (int i =0; i< MA_DAI.length; i++)
            {
                if(!textGetMaDai.contains(TAI_CHINH))
                {
                    if(textGetMaDai.contains(MA_DAI[i]) || textGetMaDai.contains(TEN_DAI[i]))
                    {
                        Log.d("checkGetCodeq", " TEN_DAI_FULL = " + TEN_DAI_FULL[i] + " MA_DAI = " + MA_DAI[i] + " \nTEN_DAI = " + TEN_DAI[i] + " Text = " + textGetMaDai);
                        txt_Province_Lottery.setText(TEN_DAI_FULL[i]);
                        checkDaiXoSo = true;
                    }
                }
            }
        }
    }

    /**
     * getDateHCM Đây là phương thức kiểm tra phải ngày có phải của vé số HCM không và cắt gọn ra ngày tháng của vé số
     * @param getBeforeYear
     * @return
     */
    private String getDateHCM(String getBeforeYear) {
        String [] getHCM = getBeforeYear.split("-");
        Log.d("getHCM", getHCM[0]);
        if(getHCM.length == 3)
        {
            if(getHCM[0].length()>2)
            {
                int size = getHCM[0].length();
                String dateHCM = getHCM[0].charAt(size-2) + "" +getHCM[0].charAt(size-1);
                String monthHCM = getHCM[1];
                String yearHCM = getHCM[2];
                Log.d("getHCM", dateHCM + monthHCM + yearHCM);
                Log.d("getHCM", " yearHCM = "+ yearHCM);
                return dateHCM + monthHCM;
            }else {
                return "";
            }
        }else {
            return "";
        }
    }

    /**
     * isNumeric kiểm tra xem strNum có phải là numeric không.
     * @param strNum
     * @return
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    /**
     * countNumeric là phương thức để kiểm tra mã số có chứa năm hiện tại không
     * và nó có phải là ngày tháng chứ không phải là mã số của tờ vé số hay không.
     * @param text
     * @return
     */
    private String countNumeric(String text)
    {
        try {
            if(text.contains(defaultYear)) {
                int lastIndext = text.lastIndexOf(defaultYear);
                String textRemove = text.substring(lastIndext + 4);
                int tamp = 0;
                for (int i = 0; i < textRemove.length(); i++) {
                    if (isNumeric(String.valueOf(textRemove.charAt(i)))) {
                        tamp += 1;
                    }
                }

                Log.d("lotteryResultFromImage", "tamp = " + tamp + " textRemove = "+ textRemove);
                if (tamp < 6) {
                    return "";
                }
                else {
                    return textRemove;
                }
            }
            else {
                return text;
            }

        }catch (Exception e)
        {
            e.getMessage();
            return text;
        }

    }

}

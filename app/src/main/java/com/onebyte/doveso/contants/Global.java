package com.onebyte.doveso.contants;

import com.onebyte.doveso.controller.UIMonitorApplication;

public class Global {

    /* Don't Change */
    public final static String PATH_CORE_APP = UIMonitorApplication.getContext().getFilesDir().getParentFile().getPath() + "/";
    /* temporary file */
    // temp folder for temporary database on target device
    public final static String TEMP_FOLDER_DB =  PATH_CORE_APP + "databases/";
    // temp file name
    public final static String TEMPORARY_DB_FILE = "doveso.db";
    // temporary db version không thể thay đổi version được mà phải thay đổi tên db
    // vì db có sẳn dữ liệu ở trong rồi nếu thay đổi version và xóa thì sẽ không còn dữ liệu có sẳn.
    public final static int DB_FILE_VERSION = 1;

    public final static String DB_MIEN = "miền";
    public final static String DB_DAI = "đài";

    public static final String DEFAULT_DATE_DMY = "dd-MM-yyyy";
    public static final String DEFAULT_DATE_M = "dd-M-yyyy";

    public static final String DEFAULT_DATE = "dd/MM/yyyy";
    // DEFAULT_DATE_SELECT_FORMAT format chuẩn dùng để dò kết quả xổ số
    public static final String DEFAULT_DATE_SELECT_FORMAT = "dd-MM-yyyy";
    public static final String DEFAULT_DATE_SELECT_YYYY = "yyyy-MM-dd";
    // DATE_SELECT_FORMAT_DM format này dùng để chuyển đổi ngày từ giọng nói sang text
    public static final String DATE_SELECT_FORMAT_DM = "d-M-yyyy";
    public static final String DATE_SELECT_FORMAT_D = "d-MM-yyyy";
    public static final String DATE_SELECT_FORMAT_M = "dd-M-yyyy";
    public static final String DEFAULT_HH = "HH";
    public static final String DEFAULT_HHmm = "HHmm";
    // DEFAULT_mm dùng để kiểm tra xổ xố đài miền nam lúc 16h40
    public static final String DEFAULT_mm = "mm";
    // DEFAULT_MM dùng để lấy ra tháng của đài xổ số
    public static final String DEFAULT_MM = "MM";
    // DEFAULT_MM = 40 là thời gian xổ số của đài miền nam lúc 16h40
    public static final int DEFAULT_MINUTES = 40;
    // DEFAULT_MM = 40 là thời gian xổ số của đài miền nam lúc 16h40
    public static final int DEFAULT_HOUR_MINUTES = 1640;
    public static final String DEFAULT_YEAR = "yyyy";
    public static final int MAXDATE = 31;
    public static final int MAXMONTH = 12;

    public static final long ONEDAY = 24*60*60*1000;

    // phân biệt thể loại vé số cần dò 1: vé số miền Nam, miền Trung; 2: vé số miền Bắc; 3: vé số Vietlott 6/45; 4: vé số Vietlott 6/55

    // KET_QUA_MIEN_BAC vé số miền Bắc;
    public static final int KET_QUA_MIEN_BAC = 0;
    // KET_QUA_MIEN_BAC vé số miền Bắc;
    public static final int KET_QUA_MIEN_TRUNG = 1;
    // KET_QUA_MIEN_BAC vé số miền Bắc;
    public static final int KET_QUA_MIEN_NAM = 2;
    // KET_QUA_VIETLOTT_6_45 vé số Vietlott 6/45;
    public static final int KET_QUA_VIETLOTT_6_45 = 3;
    // KET_QUA_VIETLOTT_6_45 vé số Vietlott 6/55;
    public static final int KET_QUA_VIETLOTT_6_55 = 4;

    // KET_QUA_3_MIEN vé số miền Nam, miền Trung;
    public static final int KET_QUA_MIENNAM_MIENTRUNG = 5;
    // KET_QUA_3_MIEN vé số miền Nam, miền Trung, miền Bắc;
    public static final int KET_QUA_3MIEN = 6;

    // KET_QUA_MIEN_BAC vé số miền Bắc;
    public static final String MIEN_BAC = "Miền Bắc";
    // KET_QUA_MIEN_BAC vé số miền Bắc;
    public static final String MIEN_TRUNG = "Miền Trung";
    // KET_QUA_MIEN_BAC vé số miền Bắc;
    public static final String MIEN_NAM = "Miền Nam";
    // KET_QUA_VIETLOTT_6_45 vé số Vietlott 6/45;
    public static final String VIETLOTT_6_45 = "Vietlott 6/45";
    // KET_QUA_VIETLOTT_6_45 vé số Vietlott 6/55;
    public static final String VIETLOTT_6_55 = "Vietlott 6/55";

    public static final String TAI_CHINH = "taichinh";
    public static final String COLOR_BLACK = "#800000";

    public static String SELECT_PROVINCE = "Chọn tỉnh";

    // THANG là chữ tháng trong tiếng việt, dùng nó để xác định xem trong giọng nói có ngày tháng hay không.
    public final static String THANG = "thang";


    // Số Tiền thưởng của các giải thưởng Miền Nam và Miền Trung
    public static final String KK_MN = "6.000.000Đ";
    public static final String PDB = "50.000.000Đ";
    public static final String DB = "2.000.000.000Đ";
    public static final String G1 = "30.000.000Đ";
    public static final String G2 = "15.000.000Đ";
    public static final String G3 = "10.000.000Đ";
    public static final String G4 = "3.000.000Đ";
    public static final String G5 = "1.000.000Đ";
    public static final String G6 = "400.000Đ";
    public static final String G7 = "200.000Đ";
    public static final String G8 = "100.000Đ";

    // Số Tiền thưởng của các giải Miền Bắc
    public static final String KK_MB = "40.000Đ";
    public static final String PDB_MB = "20.000.00Đ";
    public static final String DB_MB = "1.000.000.000Đ";
    public static final String G1_MB = "10.000.000Đ";
    public static final String G2_MB = "5.000.000Đ";
    public static final String G3_MB = "1.000.000Đ";
    public static final String G4_MB = "400.000Đ";
    public static final String G5_MB = "200.000Đ";
    public static final String G6_MB = "100.000Đ";
    public static final String G7_MB = "40.000Đ";

    // đây là tên của các giả thưởng
    public static final String KHUYEN_KHICH_MB = "Giải Khuyến Khích MB";
    public static final String KHUYEN_KHICH_MN = "Giải Khuyến Khích MN";
    public static final String KHUYEN_KHICH = "Giải Khuyến Khích";
    public static final String PHU_DAC_BIET = "Giải Phụ Đặc Biệt";
    public static final String DAC_BIET = "Giải Đặc Biệt";
    public static final String GIAI_1 = "Giải Nhất";
    public static final String GIAI_2 = "Giải Nhì";
    public static final String GIAI_3 = "Giải Ba";
    public static final String GIAI_4 = "Giải Tư";
    public static final String GIAI_5 = "Giải Năm";
    public static final String GIAI_6 = "Giải Sáu";
    public static final String GIAI_7 = "Giải Bảy";
    public static final String GIAI_8 = "Giải Tám";

    // Đây là tên viết tắt của các giải
    public static final String G_8 = "G.8";
    public static final String G_7 = "G.7";
    public static final String G_6 = "G.6";
    public static final String G_5 = "G.5";
    public static final String G_4 = "G.4";
    public static final String G_3 = "G.3";
    public static final String G_2 = "G.2";
    public static final String G_1 = "G.1";
    public static final String G_DB = "ĐB";


    /**
     * SharedPreferences
     */
    public static final String SETTINGS = "Setting";
    public static final String LOTTERY_SELECT_MIEN = "mien_nguoi_dung_chon";
    public static final String KIEN_THIET = "KienThiet";
    public static final String VIETLOTT = "Vietlott";
    public static final String DATE_DELETE = "DateDeleted";

    //"In Tai Chinh"

    // tên viết tắt của các đài
    public static String[] MA_DAI = {"xsag", "xsbd", "xsbdi", "xsbl", "xsbp",
            "xsbth", "xsbt", "xscm", "xsct", "xsdl",
            "xsdlk", "xsdng", "xsdn", "xsdno", "xsdt",
            "xsgl", "xshcm", "xshg", "xskg", "xskh",
            "xskt", "xsla", "xsmb", "xsnt", "xspy",
            "xsqb", "xsqng", "xsqnm", "xsqt", "xsst",
            "xstg", "xstn", "xstth", "xstv", "xsvl",
            "xsvt", "xsbri"};

    public static String[] TEN_DAI = {"angiang", "binhduong", "binhdinh", "baclieu", "binhphuoc",
            "binhthuan", "bentre", "camau", "cantho", "dalat",
            "daklak", "danang", "dongnai", "daknong", "dongthap",
            "gialai", "hcm", "haugiang", "kiengiang", "khanhhoa",
            "kontum", "longan", "mienbac", "ninhthuan", "phuyen",
            "quangbinh", "quangNgai", "quangnam", "quangtri", "soctrang",
            "tiengiang", "tayninh", "thuat.hue", "travinh", "vinhlong",
            "vungtau","baria"};

    public static String[] TEN_DAI_FULL = {"An Giang", "Bình Dương", "Bình Định", "Bạc Liêu", "Bình Phước",
            "Bình Thuận", "Bến Tre", "Cà Mau", "Cần Thơ", "Đà Lạt",
            "Đắk Lắk", "Đà Nẵng", "Đồng Nai", "Đắk Nông", "Đồng Tháp",
            "Gia Lai", "TP. HCM", "Hậu Giang", "Kiên Giang", "Khánh Hòa",
            "Kon Tum", "Long An", "Miền bắc", "Ninh Thuận", "Phú Yên",
            "Quảng Bình", "Quảng Ngãi", "Quảng Nam", "Quảng Trị", "Sóc Trăng",
            "Tiền Giang", "Tây Ninh", "Thừa T.Huế", "Trà Vinh", "Vĩnh Long",
            "Vũng Tàu", "Vũng Tàu"};
}


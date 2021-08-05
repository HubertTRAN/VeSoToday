package com.onebyte.doveso.model;

import android.util.Log;

public class TraditionalLottery {

    private int ROW_ID;
    private String MIEN_XO_SO;
    private String TEN_DAI;
    private String NGAY_XO_SO;
    private String KET_QUA_G8;
    private String KET_QUA_G7;
    private String KET_QUA_G6;
    private String KET_QUA_G5;
    private String KET_QUA_G4;
    private String KET_QUA_G3;
    private String KET_QUA_G2;
    private String KET_QUA_G1;
    private String KET_QUA_DB;

    public TraditionalLottery() {
    }

    public int getROW_ID() {
        return ROW_ID;
    }

    public void setROW_ID(int ROW_ID) {
        this.ROW_ID = ROW_ID;
    }

    public String getMIEN_XO_SO() {
        return MIEN_XO_SO;
    }

    public void setMIEN_XO_SO(String MIEN_XO_SO) {
        this.MIEN_XO_SO = MIEN_XO_SO;
    }

    public String getTEN_DAI() {
        return TEN_DAI;
    }

    public void setTEN_DAI(String TEN_DAI) {
        this.TEN_DAI = TEN_DAI;
    }

    public String getNGAY_XO_SO() {
        return NGAY_XO_SO;
    }

    public void setNGAY_XO_SO(String NGAY_XO_SO) {
        this.NGAY_XO_SO = NGAY_XO_SO;
    }

    public String getKET_QUA_G8() {
        return KET_QUA_G8;
    }

    public void setKET_QUA_G8(String KET_QUA_G8) {
        this.KET_QUA_G8 = KET_QUA_G8;
    }

    public String getKET_QUA_G7() {
        return KET_QUA_G7;
    }

    public void setKET_QUA_G7(String KET_QUA_G7) {
        this.KET_QUA_G7 = KET_QUA_G7;
    }

    public String getKET_QUA_G6() {
        return KET_QUA_G6;
    }

    public void setKET_QUA_G6(String KET_QUA_G6) {
        this.KET_QUA_G6 = KET_QUA_G6;
    }

    public String getKET_QUA_G5() {
        return KET_QUA_G5;
    }

    public void setKET_QUA_G5(String KET_QUA_G5) {
        this.KET_QUA_G5 = KET_QUA_G5;
    }

    public String getKET_QUA_G4() {
        return KET_QUA_G4;
    }

    public void setKET_QUA_G4(String KET_QUA_G4) {
        this.KET_QUA_G4 = KET_QUA_G4;
    }

    public String getKET_QUA_G3() {
        return KET_QUA_G3;
    }

    public void setKET_QUA_G3(String KET_QUA_G3) {
        this.KET_QUA_G3 = KET_QUA_G3;
    }

    public String getKET_QUA_G2() {
        return KET_QUA_G2;
    }

    public void setKET_QUA_G2(String KET_QUA_G2) {
        this.KET_QUA_G2 = KET_QUA_G2;
    }

    public String getKET_QUA_G1() {
        return KET_QUA_G1;
    }

    public void setKET_QUA_G1(String KET_QUA_G1) {
        this.KET_QUA_G1 = KET_QUA_G1;
    }

    public String getKET_QUA_DB() {
        return KET_QUA_DB;
    }

    public void setKET_QUA_DB(String KET_QUA_DB) {
        this.KET_QUA_DB = KET_QUA_DB;
    }

    public static void TraditionalLotteryToString (TraditionalLottery traditionalLottery)
    {
        Log.d("TAG", "TraditionalLotteryToString: \n" +
                "Ten Dai Xo So: "+ traditionalLottery.getTEN_DAI()+
                "\nTen Mien Xo So: "+ traditionalLottery.getMIEN_XO_SO()+
                "\nNgay Xo So: "+ traditionalLottery.getNGAY_XO_SO()+
                "\nGiaiDB: "+ traditionalLottery.getKET_QUA_DB()+
                "\nG1: "+ traditionalLottery.getKET_QUA_G1()+
                "\nG2: "+ traditionalLottery.getKET_QUA_G2()+
                "\nG3: "+ traditionalLottery.getKET_QUA_G3()+
                "\nG4: "+ traditionalLottery.getKET_QUA_G4()+
                "\nG5: "+ traditionalLottery.getKET_QUA_G5()+
                "\nG6: "+ traditionalLottery.getKET_QUA_G6()+
                "\nG7: "+ traditionalLottery.getKET_QUA_G7()+
                "\nG8: "+ traditionalLottery.getKET_QUA_G8());
    }
}

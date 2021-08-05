package com.onebyte.doveso.model;

public class LotteryVietlott {

    private int ROW_ID;
    private int LOAI_XO_SO;
    private String NGAY_XO_SO;
    private String KET_QUA;
    private int SO_JP2;

    public LotteryVietlott() {
    }

    public int getROW_ID() {
        return ROW_ID;
    }

    public void setROW_ID(int ROW_ID) {
        this.ROW_ID = ROW_ID;
    }

    public int getLOAI_XO_SO() {
        return LOAI_XO_SO;
    }

    public void setLOAI_XO_SO(int LOAI_XO_SO) {
        this.LOAI_XO_SO = LOAI_XO_SO;
    }

    public String getNGAY_XO_SO() {
        return NGAY_XO_SO;
    }

    public void setNGAY_XO_SO(String NGAY_XO_SO) {
        this.NGAY_XO_SO = NGAY_XO_SO;
    }

    public String getKET_QUA() {
        return KET_QUA;
    }

    public void setKET_QUA(String KET_QUA) {
        this.KET_QUA = KET_QUA;
    }

    public int getSO_JP2() {
        return SO_JP2;
    }

    public void setSO_JP2(int SO_JP2) {
        this.SO_JP2 = SO_JP2;
    }
}

package com.onebyte.doveso.model;

public class LotteryHistory {

   private int ROW_ID;
   private int LOAI_VE_SO;
   private String DAI_XO_SO;
   private String NGAY_XO_SO;
   private String MA_VE_SO;
   private String KET_QUA;

    public LotteryHistory(int ROW_ID, int LOAI_VE_SO, String DAI_XO_SO, String NGAY_XO_SO, String MA_VE_SO, String KET_QUA) {
        this.ROW_ID = ROW_ID;
        this.LOAI_VE_SO = LOAI_VE_SO;
        this.DAI_XO_SO = DAI_XO_SO;
        this.NGAY_XO_SO = NGAY_XO_SO;
        this.MA_VE_SO = MA_VE_SO;
        this.KET_QUA = KET_QUA;
    }

    public LotteryHistory() {
    }

    public int getROW_ID() {
        return ROW_ID;
    }

    public void setROW_ID(int ROW_ID) {
        this.ROW_ID = ROW_ID;
    }

    public int getLOAI_VE_SO() {
        return LOAI_VE_SO;
    }

    public void setLOAI_VE_SO(int LOAI_VE_SO) {
        this.LOAI_VE_SO = LOAI_VE_SO;
    }

    public String getDAI_XO_SO() {
        return DAI_XO_SO;
    }

    public void setDAI_XO_SO(String DAI_XO_SO) {
        this.DAI_XO_SO = DAI_XO_SO;
    }

    public String getNGAY_XO_SO() {
        return NGAY_XO_SO;
    }

    public void setNGAY_XO_SO(String NGAY_XO_SO) {
        this.NGAY_XO_SO = NGAY_XO_SO;
    }

    public String getMA_VE_SO() {
        return MA_VE_SO;
    }

    public void setMA_VE_SO(String MA_VE_SO) {
        this.MA_VE_SO = MA_VE_SO;
    }

    public String getKET_QUA() {
        return KET_QUA;
    }

    public void setKET_QUA(String KET_QUA) {
        this.KET_QUA = KET_QUA;
    }
}

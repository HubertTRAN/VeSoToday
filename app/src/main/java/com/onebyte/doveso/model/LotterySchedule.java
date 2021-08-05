package com.onebyte.doveso.model;

public class LotterySchedule {
    private int Row_ID;
    private String THU;
    private String DAI_XO_SO;
    private String MIEN;
    private String LINK_RSS;


    public LotterySchedule(int rowID, String thu, String daiXoso, String mien, String linkRSS) {
        this.Row_ID = rowID;
        this.THU = thu;
        this.DAI_XO_SO = daiXoso;
        this.MIEN = mien;
        this.LINK_RSS = linkRSS;
    }

    public LotterySchedule(String DAI_XO_SO, String MIEN, String LINK_RSS) {
        this.DAI_XO_SO = DAI_XO_SO;
        this.MIEN = MIEN;
        this.LINK_RSS = LINK_RSS;
    }

    public LotterySchedule() {
    }

    public int getRow_ID() {
        return Row_ID;
    }

    public void setRow_ID(int row_ID) {
        this.Row_ID = row_ID;
    }

    public String getTHU() {
        return THU;
    }

    public void setTHU(String THU) {
        this.THU = THU;
    }

    public String getDAI_XO_SO() {
        return DAI_XO_SO;
    }

    public void setDAI_XO_SO(String DAI_XO_SO) {
        this.DAI_XO_SO = DAI_XO_SO;
    }

    public String getMIEN() {
        return MIEN;
    }

    public void setMIEN(String MIEN) {
        this.MIEN = MIEN;
    }

    public String getLINK_RSS() {
        return LINK_RSS;
    }

    public void setLINK_RSS(String LINK_RSS) {
        this.LINK_RSS = LINK_RSS;
    }

}

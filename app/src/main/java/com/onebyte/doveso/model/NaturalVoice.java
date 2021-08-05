package com.onebyte.doveso.model;

public class NaturalVoice {

     private int ROW_ID;
     private String CUM_TU;
     private String CUM_TU_CHINH_XAC;
     private String LOAI_TIM_KIEM;
     private int TU_NGU_TRONG_GIONG_NOI;

    public NaturalVoice(int ROW_ID, String CUM_TU, String CUM_TU_CHINH_XAC, String LOAI_TIM_KIEM, int TU_NGU_TRONG_GIONG_NOI) {
        this.ROW_ID = ROW_ID;
        this.CUM_TU = CUM_TU;
        this.CUM_TU_CHINH_XAC = CUM_TU_CHINH_XAC;
        this.LOAI_TIM_KIEM = LOAI_TIM_KIEM;
        this.TU_NGU_TRONG_GIONG_NOI = TU_NGU_TRONG_GIONG_NOI;
    }

    public NaturalVoice() {
    }

    public int getROW_ID() {
        return ROW_ID;
    }

    public void setROW_ID(int ROW_ID) {
        this.ROW_ID = ROW_ID;
    }

    public String getCUM_TU() {
        return CUM_TU;
    }

    public void setCUM_TU(String CUM_TU) {
        this.CUM_TU = CUM_TU;
    }

    public String getCUM_TU_CHINH_XAC() {
        return CUM_TU_CHINH_XAC;
    }

    public void setCUM_TU_CHINH_XAC(String CUM_TU_CHINH_XAC) {
        this.CUM_TU_CHINH_XAC = CUM_TU_CHINH_XAC;
    }

    public String getLOAI_TIM_KIEM() {
        return LOAI_TIM_KIEM;
    }

    public void setLOAI_TIM_KIEM(String LOAI_TIM_KIEM) {
        this.LOAI_TIM_KIEM = LOAI_TIM_KIEM;
    }

    public int getTU_NGU_TRONG_GIONG_NOI() {
        return TU_NGU_TRONG_GIONG_NOI;
    }

    public void setTU_NGU_TRONG_GIONG_NOI(int TU_NGU_TRONG_GIONG_NOI) {
        this.TU_NGU_TRONG_GIONG_NOI = TU_NGU_TRONG_GIONG_NOI;
    }
}

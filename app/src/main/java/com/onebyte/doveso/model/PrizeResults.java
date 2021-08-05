package com.onebyte.doveso.model;

public class PrizeResults {
    /*
        Đối tượng này được tạo ra dành để set dữ liệu cho item_rcv_detail_mien_nam_trung
     */
    private String prize, // đây là tên thứ hoặc tên giải thưởng
            resultsProvince1, // đây là kết quả của giải của tỉnh thứ nhất vd: giải 8; 234; 354; 231; 367
            resultsProvince2, // đây là kết quả của giải của tỉnh thứ hai
            resultsProvince3, // đây là kết quả của giải của tỉnh thứ ba
            resultsProvince4; // đây là kết quả của giải của tỉnh thứ bốn
    private int sizeProvince;  // đây là số lượng tỉnh có kết quả xổ số trong ngày

    public PrizeResults(String prize, int sizeProvince, String resultsProvince1, String resultsProvince2, String resultsProvince3, String resultsProvince4) {
        this.prize = prize;
        this.sizeProvince = sizeProvince;
        this.resultsProvince1 = resultsProvince1;
        this.resultsProvince2 = resultsProvince2;
        this.resultsProvince3 = resultsProvince3;
        this.resultsProvince4 = resultsProvince4;
    }

    public PrizeResults(String prize, int sizeProvince, String resultsProvince1) {
        this.prize = prize;
        this.resultsProvince1 = resultsProvince1;
        this.sizeProvince = sizeProvince;
    }

    public PrizeResults(String prize, int sizeProvince, String resultsProvince1, String resultsProvince2) {
        this.prize = prize;
        this.resultsProvince1 = resultsProvince1;
        this.resultsProvince2 = resultsProvince2;
        this.sizeProvince = sizeProvince;
    }

    public PrizeResults(String prize, int sizeProvince, String resultsProvince1, String resultsProvince2, String resultsProvince3) {
        this.prize = prize;
        this.resultsProvince1 = resultsProvince1;
        this.resultsProvince2 = resultsProvince2;
        this.resultsProvince3 = resultsProvince3;
        this.sizeProvince = sizeProvince;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getResultsProvince1() {
        return resultsProvince1;
    }

    public void setResultsProvince1(String resultsProvince1) {
        this.resultsProvince1 = resultsProvince1;
    }

    public String getResultsProvince2() {
        return resultsProvince2;
    }

    public void setResultsProvince2(String resultsProvince2) {
        this.resultsProvince2 = resultsProvince2;
    }

    public String getResultsProvince3() {
        return resultsProvince3;
    }

    public void setResultsProvince3(String resultsProvince3) {
        this.resultsProvince3 = resultsProvince3;
    }

    public String getResultsProvince4() {
        return resultsProvince4;
    }

    public void setResultsProvince4(String resultsProvince4) {
        this.resultsProvince4 = resultsProvince4;
    }

    public int getSizeProvince() {
        return sizeProvince;
    }

    public void setSizeProvince(int sizeProvince) {
        this.sizeProvince = sizeProvince;
    }
}

package com.onebyte.doveso.model;

/*
    WinningLottery là class đối tượng lưu lại những thông số để hiển thị cho người dùng khi họ trúng giải thưởng nào đó.
 */
public class WinningLottery {

    private int rowID; // ID của kết quả trúng thưởng
    private String prize; // giải trúng thưởng
    private String codePrizeLottery; // mã trúng thưởng của giải đó
    private String nameLottery; // tên đài mà người dùng dò vé số vd: An Giang
    private String domainLottery; // tên miền của đài mà người dùng dò vé số
    private String dateLottery; // ngày dò vé số
    private String codeLottery; // mã vé số 6 số mà người dùng nhập vào
    private int priority; // độ ưu tiên của kết quả trúng, sắp xếp từ giải đặc biệt tới giải 8

    public WinningLottery(int rowID, String prize, String codePrizeLottery, String nameLottery, String domainLottery, String dateLottery, String codeLottery, int priority) {
        this.rowID = rowID;
        this.prize = prize;
        this.codePrizeLottery = codePrizeLottery;
        this.nameLottery = nameLottery;
        this.domainLottery = domainLottery;
        this.dateLottery = dateLottery;
        this.codeLottery = codeLottery;
        this.priority = priority;
    }

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getCodePrizeLottery() {
        return codePrizeLottery;
    }

    public void setCodePrizeLottery(String codePrizeLottery) {
        this.codePrizeLottery = codePrizeLottery;
    }

    public String getNameLottery() {
        return nameLottery;
    }

    public void setNameLottery(String nameLottery) {
        this.nameLottery = nameLottery;
    }

    public String getDateLottery() {
        return dateLottery;
    }

    public void setDateLottery(String dateLottery) {
        this.dateLottery = dateLottery;
    }

    public String getCodeLottery() {
        return codeLottery;
    }

    public void setCodeLottery(String codeLottery) {
        this.codeLottery = codeLottery;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDomainLottery() {
        return domainLottery;
    }

    public void setDomainLottery(String domainLottery) {
        this.domainLottery = domainLottery;
    }
}

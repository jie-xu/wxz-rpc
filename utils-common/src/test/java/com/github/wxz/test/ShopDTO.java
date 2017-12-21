package com.github.wxz.test;

import com.github.wxz.util.excel.annotation.ExcelField;
import com.github.wxz.util.excel.annotation.ExcelSheet;
import org.apache.poi.hssf.util.HSSFColor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -9:53
 */
@ExcelSheet(name = "商户列表", headColor = HSSFColor.HSSFColorPredefined.LIGHT_GREEN)
public class ShopDTO {

    @ExcelField(name = "是否VIP商户")
    private boolean vip;

    @ExcelField(name = "商户名称")
    private String shopName;

    @ExcelField(name = "分店数量")
    private short branchNum;

    @ExcelField(name = "商户ID")
    private int shopId;

    @ExcelField(name = "浏览人数")
    private long visitNum;

    @ExcelField(name = "当月营业额")
    private float turnover;

    @ExcelField(name = "历史营业额")
    private double totalTurnover;

    @ExcelField(name = "开店时间", dateformat = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date addTime;

    @ExcelField(name = "开店时间1", dateformat = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime localDateTime;

    public ShopDTO() {
    }

    public ShopDTO(boolean vip, String shopName, short branchNum, int shopId, long visitNum, float turnover, double totalTurnover, Date addTime, LocalDateTime localDateTime) {
        this.vip = vip;
        this.shopName = shopName;
        this.branchNum = branchNum;
        this.shopId = shopId;
        this.visitNum = visitNum;
        this.turnover = turnover;
        this.totalTurnover = totalTurnover;
        this.addTime = addTime;
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public short getBranchNum() {
        return branchNum;
    }

    public void setBranchNum(short branchNum) {
        this.branchNum = branchNum;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public long getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(long visitNum) {
        this.visitNum = visitNum;
    }

    public float getTurnover() {
        return turnover;
    }

    public void setTurnover(float turnover) {
        this.turnover = turnover;
    }

    public double getTotalTurnover() {
        return totalTurnover;
    }

    public void setTotalTurnover(double totalTurnover) {
        this.totalTurnover = totalTurnover;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "ShopDTO{" +
                "vip=" + vip +
                ", shopName='" + shopName + '\'' +
                ", branchNum=" + branchNum +
                ", shopId=" + shopId +
                ", visitNum=" + visitNum +
                ", turnover=" + turnover +
                ", totalTurnover=" + totalTurnover +
                ", addTime=" + addTime +
                '}';
    }
}



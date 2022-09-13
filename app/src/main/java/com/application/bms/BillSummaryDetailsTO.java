package com.application.bms;

public class BillSummaryDetailsTO {

    private String itemName;
    private String itemDiscount;
    private String itemRate;
    private String orderedQty;
    private String total;

    public BillSummaryDetailsTO(String itemName, String itemDiscount, String itemRate, String orderedQty,String total) {
        this.itemName = itemName;
        this.itemDiscount = itemDiscount;
        this.itemRate = itemRate;
        this.orderedQty = orderedQty;
        this.total = total;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(String itemDiscount) {
        this.itemDiscount = itemDiscount;
    }



    public String getItemRate() {
        return itemRate;
    }

    public void setItemRate(String itemRate) {
        this.itemRate = itemRate;
    }

    public String getOrderedQty() {
        return orderedQty;
    }

    public void setOrderedQty(String orderedQty) {
        this.orderedQty = orderedQty;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

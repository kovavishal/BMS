package com.application.bms.model;

public class SelectedStockDetails {

    private String itemCode;
    private String mrp;

    public SelectedStockDetails(String itemCode, String mrp) {
        this.itemCode = itemCode;
        this.mrp = mrp;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }
}

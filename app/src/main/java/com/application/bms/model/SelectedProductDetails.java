package com.application.bms.model;

public class SelectedProductDetails {

    private String productCode;
    private String productName;
    private String itemNos;
    private String itemTaxPer;
    private String itemTaxCode;
    private String TotalItemQty;
    private String itemRate;
    private String itemMRP;
    private String brandCode;
    private String brandName;
    private String itemDisc;
    private String customerOrderedQty;
    private String mFreeQuantity;
    private String mCustomerCode;
    private String mCustomerName;
    private String mDate;
    private String mTotal;


    public SelectedProductDetails(){}

    public SelectedProductDetails(String productCode, String productName, String totalQuantity, String freeQuantity, String quantity, String itemNo, String discPercent, String tax,
                                  String taxCode, String rate, String mrp, String total,  String customerCode,String customerName, String date, String groupCode, String groupName){
        this.productCode =productCode;
        this.productName =productName;
        itemNos=itemNo;
        itemTaxPer=tax;
        itemTaxCode=taxCode;
        itemDisc=discPercent;
        TotalItemQty =quantity;
        itemRate=rate;
        itemMRP=mrp;
        mTotal=total;
        customerOrderedQty =totalQuantity;
        mFreeQuantity=freeQuantity;
        brandCode =groupCode;
        brandName =groupName;
        mCustomerName=customerName;
        mCustomerCode=customerCode;
        mDate=date;

    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getItemNos() {
        return itemNos;
    }

    public void setItemNos(String itemNos) {
        this.itemNos = itemNos;
    }

    public String getItemTaxPer() {
        return itemTaxPer;
    }

    public void setItemTaxPer(String itemTaxPer) {
        this.itemTaxPer = itemTaxPer;
    }

    public String getItemTaxCode() {
        return itemTaxCode;
    }

    public void setItemTaxCode(String itemTaxCode) {
        this.itemTaxCode = itemTaxCode;
    }

    public String getTotalItemQty() {
        return TotalItemQty;
    }

    public void setTotalItemQty(String totalItemQty) {
        this.TotalItemQty = totalItemQty;
    }

    public String getItemRate() {
        return itemRate;
    }

    public void setItemRate(String itemRate) {
        this.itemRate = itemRate;
    }

    public String getItemMRP() {
        return itemMRP;
    }

    public void setItemMRP(String itemMRP) {
        this.itemMRP = itemMRP;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getItemDisc() {
        return itemDisc;
    }

    public void setItemDisc(String itemDisc) {
        this.itemDisc = itemDisc;
    }

    public String getCustomerOrderedQty() {
        return customerOrderedQty;
    }

    public void setCustomerOrderedQty(String customerOrderedQty) {
        this.customerOrderedQty = customerOrderedQty;
    }

    public String getmFreeQuantity() {
        return mFreeQuantity;
    }

    public void setmFreeQuantity(String mFreeQuantity) {
        this.mFreeQuantity = mFreeQuantity;
    }

    public String getmCustomerCode() {
        return mCustomerCode;
    }

    public void setmCustomerCode(String mCustomerCode) {
        this.mCustomerCode = mCustomerCode;
    }

    public String getmCustomerName() {
        return mCustomerName;
    }

    public void setmCustomerName(String mCustomerName) {
        this.mCustomerName = mCustomerName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTotal() {
        return mTotal;
    }

    public void setmTotal(String mTotal) {
        this.mTotal = mTotal;
    }

}

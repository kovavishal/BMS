package com.application.bms.model;

public class Orders {

    private String cust_code;
    private String cust_name;
    private String manufacture_code;
    private String manufacture_name;
    private String product_code;
    private String product_name;
    private String date_time;
    private String tax_code;
    private String tax_percentage;
    private String quantity;
    private String des;
    private String freeqty;
    private String mrp;
    private String rate;
    private String value;
    private String discount;


    public Orders(String cust_code, String cust_name, String manufacture_code, String manufacture_name, String product_code,
                  String product_name,String date_time, String tax_code, String tax_percentage, String quantity, String des, String freeqty,
                  String mrp, String rate, String value, String discount) {
        this.cust_code = cust_code;
        this.cust_name = cust_name;
        this.manufacture_code = manufacture_code;
        this.manufacture_name = manufacture_name;
        this.product_code = product_code;
        this.product_name = product_name;
        this.date_time = date_time;
        this.tax_code = tax_code;
        this.tax_percentage = tax_percentage;
        this.quantity = quantity;
        this.des = des;
        this.freeqty = freeqty;
        this.mrp = mrp;
        this.rate = rate;
        this.value = value;
        this.discount = discount;
    }


    public String getCust_code() {
        return cust_code;
    }

    public void setCust_code(String cust_code) {
        this.cust_code = cust_code;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getManufacture_code() {
        return manufacture_code;
    }

    public void setManufacture_code(String manufacture_code) {
        this.manufacture_code = manufacture_code;
    }

    public String getManufacture_name() {
        return manufacture_name;
    }

    public void setManufacture_name(String manufacture_name) {
        this.manufacture_name = manufacture_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getTax_code() {
        return tax_code;
    }

    public void setTax_code(String tax_code) {
        this.tax_code = tax_code;
    }

    public String getTax_percentage() {
        return tax_percentage;
    }

    public void setTax_percentage(String tax_percentage) {
        this.tax_percentage = tax_percentage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getFreeqty() {
        return freeqty;
    }

    public void setFreeqty(String freeqty) {
        this.freeqty = freeqty;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}

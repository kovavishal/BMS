package com.application.bms.model;

public class Bills {
    private String cust_code;
    private String cust_name;
    private String order_value;


    public Bills(String cust_code, String cust_name, String order_value) {
        this.cust_code = cust_code;
        this.cust_name = cust_name;
        this.order_value = order_value;

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

    public String getOrder_value() {
        return order_value;
    }

    public void setOrder_value(String order_value) {
        this.order_value = order_value;
    }


}

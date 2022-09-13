package com.application.bms.model;

public class BillsSummary {

    String cust_id,custName,orderedValue;

    public BillsSummary() {
    }

    public BillsSummary(String cust_id,String custName, String orderedValue) {
        this.cust_id = cust_id;
        this.custName = custName;
        this.orderedValue = orderedValue;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getOrderedValue() {
        return orderedValue;
    }

    public void setOrderedValue(String orderedValue) {
        this.orderedValue = orderedValue;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }
}

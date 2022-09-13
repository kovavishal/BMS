package com.application.bms.model;

public class SelectedCustomer {

    private String customerCode;

    public SelectedCustomer(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}

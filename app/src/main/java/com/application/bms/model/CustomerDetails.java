package com.application.bms.model;

public class CustomerDetails {

    String customerCode;
    String customerName;
    String areaCode;
    String addressLine1;
    String addressLine2;
    String phoneNumber;
    String balance;

    public  CustomerDetails(){

    }

    public  CustomerDetails(String customerCode,String customerName, String areaCode,String addressLine1,String addressLine2,String phoneNumber,String balance){
        this.customerCode=customerCode;
        this.customerName=customerName;
        this.areaCode=areaCode;
        this.addressLine1=addressLine1;
        this.addressLine2=addressLine2;
        this.phoneNumber=phoneNumber;
        this.balance=balance;
    }
//    public  CustomerDetails(String customerCode,String customerName,String addressLine1,String addressLine2,String phoneNumber,String balance){
//        this.customerCode=customerCode;
//        this.customerName=customerName;
//        this.addressLine1=addressLine1;
//        this.addressLine2=addressLine2;
//        this.phoneNumber=phoneNumber;
//        this.balance=balance;
//    }


    public void setCustomerCode(String customerCode){
        this.customerCode=customerCode;
    }
    public String getCustomerCode(){
        return customerCode;
    }
    public void setCustomerName(String customerName){
        this.customerName=customerName;
    }
    public String getCustomerName(){
        return customerName;
    }
    public void setAreaCode(String areaCode){
        this.areaCode=areaCode;
    }
    public String getAreaCode(){
        return areaCode;
    }
    public void setAddressLine1(String addressLine1){
        this.addressLine1=addressLine1;
    }
    public String getAddressLine1(){
        return addressLine1;
    }
    public void setAddressLine2(String addressLine2){
        this.addressLine2=addressLine2;
    }
    public String getAddressLine2(){
        return addressLine2;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setBalance(String balance){
        this.balance=balance;
    }
    public String getBalance(){
        return balance;
    }
//    public static CustomerDetails fromCursor(Cursor cursor) {
//        //TODO return your MyListItem from cursor.
//        return cursor;
//    }
}

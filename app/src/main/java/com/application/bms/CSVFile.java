package com.application.bms;

import com.application.bms.model.CustomerDetails;
import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.GroupDetails;
import com.application.bms.model.StockDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {


    DatabaseHelper databaseHelper;
    InputStream inputStream;

    public CSVFile(InputStream inputStream) {
        System.out.println("iam at csv file");

        this.inputStream = inputStream;
        System.out.println("iinput stream==="+inputStream);
    }
    public List customerDataRead(){
        List resultList = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
        try {
            String csvLine;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] customerData = csvLine.split(",");
                CustomerDetails customerDetails=new CustomerDetails();
                if(customerData.length>0){
                    customerDetails.setCustomerCode(customerData[0].trim());
                }
                if(customerData.length>1){
                    customerDetails.setCustomerName(customerData[1].trim());
                }
                if(customerData.length>2){
                    customerDetails.setAreaCode(customerData[2].trim());
                }
                if(customerData.length>3){
                    customerDetails.setAddressLine1(customerData[3].trim());
                }
                if(customerData.length>4){
                    customerDetails.setAddressLine2(customerData[4].trim());
                }
                if(customerData.length>5){
                    customerDetails.setPhoneNumber(customerData[5].trim());
                }
                if(customerData.length>6){
                    customerDetails.setBalance(customerData[6].trim());
                }
                resultList.add(customerDetails);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }
    public List groupDataRead() {
        List resultList = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
        try {
            String csvLine;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] groupData = csvLine.split(",");
                GroupDetails groupDetails=new GroupDetails();
                if(groupData.length>0){
                    groupDetails.setGroupCode(groupData[0].trim());
                }
                if(groupData.length>1){
                    groupDetails.setGroupName(groupData[1].trim());
                }

                resultList.add(groupData);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }

    public List stockDataRead(){
        List resultList = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
        try {
            String csvLine;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] stockData = csvLine.split(",");
                StockDetails stockDetails= new StockDetails();
                if(stockData.length>0){
                    stockDetails.setItemCode(stockData[0].trim());
                }
                if(stockData.length>1){
                    stockDetails.setItemName(stockData[1].trim());
                }
                if(stockData.length>2){
                    stockDetails.setItemNos(stockData[2].trim());
                }
                if(stockData.length>3){
                    stockDetails.setItemTaxPer(stockData[3].trim());
                }
                if(stockData.length>4){
                    stockDetails.setItemTaxCode(stockData[4].trim());
                }
                if(stockData.length>5){
                    stockDetails.setItemMRP(stockData[5].trim());
                }
                if(stockData.length>6){
                    stockDetails.setItemRate(stockData[6].trim());
                }
                if(stockData.length>7){
                    stockDetails.setItemStock(stockData[7].trim());
                }
                if(stockData.length>8){
                    stockDetails.setItemGroupCode(stockData[8].trim());
                }
                if(stockData.length>9){
                    stockDetails.setItemSubGroup(stockData[9].trim());
                }
                if(stockData.length>10){
                    stockDetails.setItemDisc(stockData[10].trim());
                }
                if(stockData.length>11){
                    stockDetails.setBoxQty(stockData[11].trim());
                }
                if(stockData.length>12){
                    stockDetails.setCost(stockData[12].trim());
                }
                resultList.add(stockDetails);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }

}

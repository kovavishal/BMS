package com.application.bms.model;

public class StockDetails {

    private String itemCode;
    private String itemName;
    private String itemNos;
    private String itemTaxPer;
    private String itemTaxCode;
    private String itemMRP;
    private String itemRate;
    private String itemStock;
    private String itemGroupCode;
    private String itemSubGroup;
    private String itemDisc;
    private String boxQty;
    private String cost;
    public  StockDetails(){

    }
    public StockDetails(String itemCode ,String itemName,String itemNos,String itemTaxPer,String itemTaxCode,String itemMRP,
                        String itemRate,String itemStock,String itemGroupCode,String itemSubGroup,String itemDisc,String boxQty,String cost){
        this.itemCode=itemCode;
        this.itemName=itemName;
        this.itemNos=itemNos;
        this.itemTaxPer=itemTaxPer;
        this.itemTaxCode=itemTaxCode;
        this.itemStock=itemStock;
        this.itemRate=itemRate;
        this.itemMRP=itemMRP;
        this.itemGroupCode=itemGroupCode;
        this.itemSubGroup=itemSubGroup;
        this.itemDisc=itemDisc;
        this.boxQty =boxQty;
        this.cost =cost;
    }

//    public StockDetails(String item_name, String item_mrp, String item_rate, String item_stock) {
//        this.itemStock=item_stock;
//        this.itemRate=item_rate;
//        this.itemMRP=item_mrp;
//        this.itemName=item_name;
//    }

    public void setItemCode (String item_code){
        this.itemCode=item_code;
    }
    public String getItemCode(){
        return itemCode;
    }
    public void setItemName (String item_name){
        this.itemName=item_name;
    }
    public String getItemName(){
        return itemName;
    }
    public void setItemNos (String item_nos){
        this.itemNos=item_nos;
    }
    public String getItemNos(){
        return itemNos;
    }
    public void setItemTaxPer (String item_tax_per){
        this.itemTaxPer=item_tax_per;
    }
    public String getItemTaxPer(){
        return itemTaxPer;
    }
    public void setItemTaxCode (String item_tax_code){
        this.itemTaxCode=item_tax_code;
    }
    public String getItemTaxCode(){
        return itemTaxCode;
    }
    public void setItemStock(String itemStock){
        this.itemStock=itemStock;
    }
    public String getItemStock(){
        return itemStock;
    }
    public void setItemMRP (String item_MRP){
        this.itemMRP=item_MRP;
    }
    public String getItemMRP(){
        return itemMRP;
    }
    public void setItemRate(String itemRate){
        this.itemRate=itemRate;
    }
    public String getItemRate(){
        return itemRate;
    }
    public void setItemGroupCode(String groupCode){
        this.itemGroupCode=groupCode;
    }
    public String getItemGroupCode(){
        return itemGroupCode;
    }
    public void setItemSubGroup(String subGroup){
        this.itemSubGroup=subGroup;
    }
    public String getItemSubGroup(){
        return itemSubGroup;
    }
    public void setItemDisc(String itemDisc){
        this.itemDisc=itemDisc;
    }
    public String getItemDisc(){
        return itemDisc;
    }
    public String getBoxQty() {
        return boxQty;
    }

    public void setBoxQty(String boxQty) {
        this.boxQty = boxQty;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}

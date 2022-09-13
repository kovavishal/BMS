package com.application.bms.model;

public class GroupDetails {


    String groupCode,groupName;

    public  GroupDetails(){}

    public  GroupDetails(String gCode,String gName){
        this.groupCode = gCode;
        this.groupName = gName;
    }

    public void setGroupCode(String gcode){
        this.groupCode = gcode;
    }
    public String getGroupCode(){
        return groupCode;
    }
    public void setGroupName(String gName){
        this.groupName = gName;
    }
    public String getGroupName(){
        return groupName;
    }


}

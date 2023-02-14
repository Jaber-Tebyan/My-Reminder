package com.tebyan.my_reminder;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class Task {

    public int[] endDate;
    public int[] endTime;
    public String name;
    public int requestCode;
    public int second;


    public String GetEndDate(){
        String month,day;
        if((endDate[2]+"").length()==1){
            day="0"+endDate[2];
        }
        else{
            day=endDate[2]+"";
        }
        if((endDate[1]+"").length()==1){
            month="0"+endDate[1];
        }else{
            month=endDate[1]+"";
        }
        StringBuilder zeros= new StringBuilder();
        int delta=4-(endDate[0]+"").length();
        for (int i=0;i<delta;i++){
            zeros.append("0");
        }
        return zeros.toString()+endDate[0] + "/" + month + "/" + day;
    }
    public String GetEndTime(){


        String hour="",minute="";
        if((endTime[0]+"").length()==1){
            hour="0"+endTime[0];
        }else{
            hour=endTime[0]+"";
        }
        if((endTime[1]+"").length()==1){
            minute="0"+endTime[1];
        }
        else{
            minute=endTime[1]+"";
        }

        return hour+":"+minute;
    }
    public String GetFullEndDate(){
        return GetEndDate()+"|||"+GetEndTime();
    }
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setName(String name){
        this.name=name;
    }
    public boolean isDateEmpty(){
        return endDate[0]==0||endDate[1]==0||endDate[2]==0;
    }
    public boolean isNameEmpty(){
        return name.equals("");
    }
    public boolean isDescriptionEmpty(){
        return taskDescription.equals("");
    }

    public String getTaskDescription() {
        return taskDescription;
    }



    public Task setEndDate(int year,int month,int day){
        endDate=new int[]{year,month,day};
        return this;
    }
    public Task setEndTime(int hour,int minute){
        endTime=new int[]{hour,minute};
        return this;
    }

    private String taskDescription;

    public Task(String name,String taskDescription){
        this.taskDescription=taskDescription;
        this.name=name;
        setEndTime(0,0);
        setEndDate(0,0,0);



    }
    public void SetRequestCode(int requestCode){
        Calendar calendar=Calendar.getInstance();
        calendar.set(endDate[0],endDate[1],endDate[2],endTime[0],endTime[1],0);
        this.requestCode=requestCode;
    }
    public Task(){
        name="";
        taskDescription="";
        setEndTime(0,0);
        setEndDate(0,0,0);
    }

}

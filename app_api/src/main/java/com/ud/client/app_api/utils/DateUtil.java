package com.ud.client.app_api.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lc on 15-9-9.
 */
public class DateUtil {
    private String[] weekDaysName = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
    private static DateUtil instance;
    private SimpleDateFormat sdf;
    Calendar calendar;
    private DateUtil(){
        sdf = new SimpleDateFormat();
        calendar = Calendar.getInstance();;
    }

    public static DateUtil getInstance(){
        if(null==instance){
            synchronized (DateUtil.class){
                if(null==instance)instance = new DateUtil();
            }
        }
        return instance;
    }

    public String getNormalDate(){
        sdf.applyPattern("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    /**
     * 根据日期返回星期
     * @param date
     * @return
     */
    public String getDayOfWeek(String date){
        try{
            sdf.applyPattern("yyyy-MM-dd");
            calendar.setTime(sdf.parse(date));
            int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            return weekDaysName[intWeek];
        }catch (Exception e){
            e.printStackTrace();
        }
        return "周日";
    }

    public Date getRangeDate(){
        Date date = new Date();
        try{
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, -65);
            date = calendar.getTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }
    public String formatDate(long time,String pattern){
        try{
            sdf.applyPattern(pattern);
            calendar.setTime(new Date(time * 1000));
            String formatTime = sdf.format(calendar.getTime());
            return formatTime;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String formatDate(Date date,String pattern){
        try{
            sdf.applyPattern(pattern);
            calendar.setTime(date);
            String formatTime = sdf.format(calendar.getTime());
            return formatTime;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public List<String> getInterValDate(Date date){
        List<String> sts = new ArrayList<String>();
        sdf.applyPattern("yyyy-MM-dd");
        calendar.setTime(date);
        for(int i=0;i<7;i++){
            String dat = sdf.format(date);
            long mis = date.getTime()+24*60*60*1000;
            date.setTime(mis);
            Logger.e(date.getTime()+"===time==="+dat+" "+getDayOfWeek(dat));
            sts.add(dat.substring(5,dat.length())+" "+getDayOfWeek(dat));
        }
        return sts;
    }
    public List<String> getMapDate(Date date){
        List<String> sts = new ArrayList<String>();
        sdf.applyPattern("yyyy-MM-dd");
        calendar.setTime(date);
        for(int i=0;i<7;i++){
            String dat = sdf.format(date);
            long mis = date.getTime()+24*60*60*1000;
            date.setTime(mis);
            sts.add(dat.substring(5,dat.length())+" "+getDayOfWeek(dat)+"@"+dat);
        }
        return sts;
    }
    public List<String> getTenInterValDate(Date date){
        List<String> sts = new ArrayList<String>();
        sdf.applyPattern("yyyy-MM-dd");
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        for(int i=0;i<30;i++){
            String dat = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
            sts.add(dat);
        }
        return sts;
    }

    public long getDateInterVal(String date){
        long inteval = 0;
        try{
            sdf.applyPattern("yyyy.MM.dd");
            Date drcDate = sdf.parse(date);
            Date srcDate = new Date();
            long time =  srcDate.getTime()-drcDate.getTime();
            Logger.e("start=="+srcDate.getTime()+"end==="+drcDate.getTime());
            if(time<0){
                inteval = -1;
            }else{
                inteval = time/1000/60/60/24/365;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return inteval;
    }
}

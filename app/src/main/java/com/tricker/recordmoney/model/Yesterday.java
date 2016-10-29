package com.tricker.recordmoney.model;

/**
 * Created by Tricker on 2016/10/28  028.
 */

public class Yesterday {
    private String dateAndWeek;//日期和星期
    private String high;//高温
    private String low;//低温
    private String dayType;//白天
    private String nightType;//晚上

    public String getDateAndWeek() {
        return dateAndWeek;
    }

    public void setDateAndWeek(String dateAndWeek) {
        this.dateAndWeek = dateAndWeek;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getNightType() {
        return nightType;
    }

    public void setNightType(String nightType) {
        this.nightType = nightType;
    }
}

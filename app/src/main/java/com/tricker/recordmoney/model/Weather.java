package com.tricker.recordmoney.model;

import java.util.List;

/**
 * Created by Tricker on 2016/10/28  028.
 */

public class Weather {
    private String city;//城市名
    private String updateTime;//更新时间
    private int temprature;//温度
    private String windPower;//风力
    private String humidity;//湿度
    private String windDirection;//风向
    private String sunrise;//日出
    private String sunset;//日落
    private Environment environment;//环境
    /*Yesterday 归纳到Forecast第一项*/
//    private Yesterday yesterday;//昨天天气情况
    private List<Forecast> forecasts;//未来5天天气情况
    private List<IndexNumber> indexNumbers;//指数 11种
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getTemprature() {
        return temprature;
    }

    public void setTemprature(int temprature) {
        this.temprature = temprature;
    }

    public String getWindPower() {
        return windPower;
    }

    public void setWindPower(String windPower) {
        this.windPower = windPower;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

//    public Yesterday getYesterday() {
//        return yesterday;
//    }
//
//    public void setYesterday(Yesterday yesterday) {
//        this.yesterday = yesterday;
//    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public List<IndexNumber> getIndexNumbers() {
        return indexNumbers;
    }

    public void setIndexNumbers(List<IndexNumber> indexNumbers) {
        this.indexNumbers = indexNumbers;
    }


    @Override
    public String toString() {
        return this.city+this.temprature;
    }
}

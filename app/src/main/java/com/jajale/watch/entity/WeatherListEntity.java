package com.jajale.watch.entity;

/**
 * Created by athena on 2016/2/20.
 * Email: lizhiqiang@bjjajale.com
 */
public class WeatherListEntity {
    private String childName;
    private String city;
    private String weather;
    private String temperature;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "WeatherListEntity{" +
                "childName='" + childName + '\'' +
                ", city='" + city + '\'' +
                ", weather='" + weather + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }
}

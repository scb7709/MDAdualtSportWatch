package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2017/8/29.
 */
public class TemperatureeAndWeathere implements Serializable {
    public List<TemperatureeWeathere> Data;

    public static class TemperatureeWeathere implements Serializable{
        public String WeatherDetails;//	string	天气详情
        public String Temperature;//string	气温
    }
}

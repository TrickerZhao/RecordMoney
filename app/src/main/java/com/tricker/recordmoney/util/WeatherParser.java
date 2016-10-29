package com.tricker.recordmoney.util;

import com.tricker.recordmoney.model.Weather;

import java.io.InputStream;

/**
 * Created by Tricker on 2016/10/28  028.
 */

public interface WeatherParser {
    public Weather parse(InputStream is) throws Exception;

}

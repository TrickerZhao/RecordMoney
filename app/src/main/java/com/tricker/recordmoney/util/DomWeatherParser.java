package com.tricker.recordmoney.util;

import com.tricker.recordmoney.model.Environment;
import com.tricker.recordmoney.model.Forecast;
import com.tricker.recordmoney.model.IndexNumber;
import com.tricker.recordmoney.model.Weather;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.amap.api.col.c.i;
import static com.tricker.recordmoney.R.menu.weather;

/**
 * Created by Tricker on 2016/10/28  028.
 */

public class DomWeatherParser implements WeatherParser {

    @Override
    public Weather parse(InputStream is) throws Exception {
        Weather weather = new Weather();
        List<Forecast> forecasts = new ArrayList<>();
        List<IndexNumber> indexNumbers = new ArrayList<>();
        weather.setForecasts(forecasts);
        weather.setIndexNumbers(indexNumbers);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例
        DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例
        Document doc = builder.parse(is);   //解析输入流 得到Document实例
        Element rootElement = doc.getDocumentElement();
        NodeList nodeList = rootElement.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String nodeName = node.getNodeName();
            if(getIgnoreNode().contains(nodeName)){
                continue;
            }
            String nodeValue = node.getFirstChild().getNodeValue();
            if (nodeName.equals("city")) {
                weather.setCity(nodeValue);
            } else if (nodeName.equals("updatetime")) {
                weather.setUpdateTime(nodeValue);
            } else if (nodeName.equals("wendu")) {
                weather.setTemprature(Integer.parseInt(nodeValue));
            } else if (nodeName.equals("fengli")) {
                weather.setWindPower(nodeValue);
            } else if (nodeName.equals("shidu")) {
                weather.setHumidity(nodeValue);
            } else if (nodeName.equals("fengxiang")) {
                weather.setWindDirection(nodeValue);
            } else if (nodeName.equals("sunrise_1")) {
                weather.setSunrise(nodeValue);
            } else if (nodeName.equals("sunset_1")) {
                weather.setSunset(nodeValue);
            }else if(nodeName.equals("environment")){
                NodeList  environmentNodes= node.getChildNodes();
                Environment environment = new Environment();
                for (int j = 0; j < environmentNodes.getLength(); j++){
                    node = environmentNodes.item(j);
                    nodeName = node.getNodeName();
                    if(getIgnoreNode().contains(nodeName)){
                        continue;
                    }
                    nodeValue = node.getFirstChild().getNodeValue();
                    if(nodeName.equals("aqi")){
                        environment.setAqi(Integer.parseInt(nodeValue));
                    }else if(nodeName.equals("pm25")){
                        environment.setPm25(Integer.parseInt(nodeValue));
                    }else if(nodeName.equals("suggest")){
                        environment.setSuggest(nodeValue);
                    }else if(nodeName.equals("quality")){
                        environment.setQuality(nodeValue);
                    }else if(nodeName.equals("o3")){
                        environment.setO3(Integer.parseInt(nodeValue));
                    }else if(nodeName.equals("co")){
                        environment.setCo(Integer.parseInt(nodeValue));
                    }else if(nodeName.equals("pm10")){
                        environment.setPm10(Integer.parseInt(nodeValue));
                    }else if(nodeName.equals("so2")){
                        environment.setSo2(Integer.parseInt(nodeValue));
                    }else if(nodeName.equals("no2")){
                        environment.setNo2(Integer.parseInt(nodeValue));
                    }else if(nodeName.equals("time")){
                        environment.setTime(nodeValue);
                    }
                }
                weather.setEnvironment(environment);
            }else if(nodeName.equals("yesterday")){
                NodeList yesterdayNode= node.getChildNodes();
                Forecast forecast = new Forecast();
                for (int j = 0; j < yesterdayNode.getLength(); j++){
                    node = yesterdayNode.item(j);
                    nodeName = node.getNodeName();
                    nodeValue = node.getFirstChild().getNodeValue();
                    if(nodeName.equals("date_1")){
                        forecast.setDateAndWeek(nodeValue);
                    }else if(nodeName.equals("high_1")){
                        forecast.setHigh(nodeValue);
                    }else if(nodeName.equals("low_1")){
                        forecast.setLow(nodeValue);
                    }else if(nodeName.equals("day_1")){
                        NodeList day= node.getChildNodes();
                        for (int k = 0; k < day.getLength(); k++){
                            node = day.item(k);
                            nodeName = node.getNodeName();
                            nodeValue = node.getFirstChild().getNodeValue();
                            if(nodeName.equals("type_1")){
                                forecast.setDayType(nodeValue);
                            }
                        }
                    }else if(nodeName.equals("night_1")){
                        NodeList night= node.getChildNodes();
                        for (int k = 0; k < night.getLength(); k++){
                            node = night.item(k);
                            nodeName = node.getNodeName();
                            nodeValue = node.getFirstChild().getNodeValue();
                            if(nodeName.equals("type_1")){
                                forecast.setNightType(nodeValue);
                            }
                        }
                    }
                }
                forecasts.add(forecast);
            }else if(nodeName.equals("forecast")){
                NodeList forecastNode = node.getChildNodes();
                for (int j = 0; j < forecastNode.getLength(); j++){
                    node = forecastNode.item(j);//weather节点
                    NodeList weatherNode=node.getChildNodes();
                    Forecast forecast = new Forecast();
                    for (int k = 0; k < weatherNode.getLength(); k++){
                        node = weatherNode.item(k);
                        nodeName = node.getNodeName();
                        nodeValue = node.getFirstChild().getNodeValue();
                        if(nodeName.equals("date")){
                            forecast.setDateAndWeek(nodeValue);
                        }else if(nodeName.equals("high")){
                            forecast.setHigh(nodeValue);
                        }else if(nodeName.equals("low")){
                            forecast.setLow(nodeValue);
                        }else if(nodeName.equals("day")){
                            NodeList day= node.getChildNodes();
                            for (int m = 0; m < day.getLength(); m++){
                                node = day.item(m);
                                nodeName = node.getNodeName();
                                nodeValue = node.getFirstChild().getNodeValue();
                                if(nodeName.equals("type")){
                                    forecast.setDayType(nodeValue);
                                }
                            }
                        }else if(nodeName.equals("night")){
                            NodeList night= node.getChildNodes();
                            for (int m = 0; m < night.getLength(); m++){
                                node = night.item(m);
                                nodeName = node.getNodeName();
                                nodeValue = node.getFirstChild().getNodeValue();
                                if(nodeName.equals("type")){
                                    forecast.setNightType(nodeValue);
                                }
                            }
                        }
                    }
                    forecasts.add(forecast);

                }
                weather.setForecasts(forecasts);
            }else if(nodeName.equals("zhishus")){
                NodeList zhishus= node.getChildNodes();
                for (int j = 0; j < zhishus.getLength(); j++){
                    node = zhishus.item(j);//zhishu节点
                    NodeList zhishu=node.getChildNodes();
                    IndexNumber indexNumber = new IndexNumber();
                    for (int k = 0; k < zhishu.getLength(); k++){
                        node = zhishu.item(k);
                        nodeName = node.getNodeName();
                        nodeValue = node.getFirstChild().getNodeValue();
                        if(nodeName.equals("name")){
                            indexNumber.setName(nodeValue);
                        }else if(nodeName.equals("value")){
                            indexNumber.setValue(nodeValue);
                        }else if(nodeName.equals("detail")){
                            indexNumber.setDetail(nodeValue);
                        }
                    }
                    indexNumbers.add(indexNumber);
                }
                weather.setIndexNumbers(indexNumbers);
            }
        }


        return weather;
    }
    private List<String> getIgnoreNode(){
        List<String> nodes = new ArrayList<>();
        nodes.add("sunrise_2");
        nodes.add("sunset_2");
        nodes.add("MajorPollutants");
        nodes.add("sunrise_2");
        nodes.add("sunrise_2");
        return nodes;
    }
}

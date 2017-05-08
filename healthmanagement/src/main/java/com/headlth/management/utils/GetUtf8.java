package com.headlth.management.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by abc on 2016/7/12.
 */
public class GetUtf8 {
    public static String getUTF8XMLString(String xml) {
        if(xml.equals("/")){
            return "/";
        }
        if(xml.equals(":")){
            return ":";
        }
        if(xml.equals(" ")){
            return "%20";
        }
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString = "";
        String xmlUTF8="";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            System.out.println("utf-8 编码：" + xmlUTF8) ;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return xmlUTF8;
    }
}

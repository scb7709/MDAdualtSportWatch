package com.headlth.management.utils;

import android.util.Log;

import com.headlth.management.myview.MyToash;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import anet.channel.util.StringUtils;

/**
 * Created by abc on 2017/6/6.
 */
public class DataTransferUtils {

    public static String getName_16(String name) {
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        sb.append(name);
        String xmString = "";
        String xmlUTF8 = "";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            xmlUTF8 = xmlUTF8.replace("%", "");


            int size = 12 - xmlUTF8.length() / 2;
            sb2.append(xmlUTF8);
            for (int i = 0; i < size; i++) {
                sb2.append("00");
            }
            // System.out.println("utf-8 编码：" + sb2.toString()) ;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return sb2.toString().substring(0, sb2.toString().length());
    }

    public static String getInt_16(int num) {
        String temp = Integer.toHexString(num);
      /*  Log.i("asdd", temp);
        if (temp.length() == 1) {
            temp = "0x0" + temp;
        } else if (temp.length() == 2) {
            temp = "0x" + temp;
        }*/
        return temp;
    }

    //16转化10
    public static int getInt_10(String num) {
        int temp = 0;
        try {
            temp = Integer.parseInt(num, 16);

        } catch (Exception n) {
            temp = 0;
        }
        return temp;
    }

    //两个一位 把16进制转10进制
    public static String getString_10(String num) {
        String temp = "";
        try {
            for (int i = 0; i <= num.length() / 2; i = i + 2) {
                temp += Integer.parseInt(num.substring(i, i + 2), 16) + "";
            }
        } catch (Exception n) {
            Log.i("myblue", "yichang");
        }
        return temp;
    }

    // byte转十六进制字符串
    public static String bytes2HexString(byte[] bytes) {
        String ret = "";
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase(Locale.CHINA);
        }
        return ret;
    }

    /**
     * 将16进制的字符串转换为字节数组
     *
     * @param message
     * @return 字节数组
     */
    public static byte[] getHexBytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }

    private int getDtat(int month) {
        int month1 = 1;
        int month2 = 1;
        int i = 2;
        int[] sum = new int[Integer.MAX_VALUE];
        sum[0] = 1;
        sum[1] = 1;
        if (month == 1 || month == 2) {
            return 1;
        }
        while (i <= month - 1) {
            sum[i] = sum[i - 1] + sum[i - 2];
            i++;
        }

        return sum[month];
    }

    public static String format(byte[] data) {
        StringBuilder result = new StringBuilder();
        int n = 0;
        for (byte b : data) {
            // if(n%16==0)
            // result.append(String.format("%05x",n));
            result.append(String.format("%02x", b));
            n++;
            // if(n%16==0)
            // result.append('\n');
        }
        return result.toString();
    }

    static SimpleDateFormat simpleDateFormat;

    public static String getDate(int data) {
        //1505869059
        long time = System.currentTimeMillis();
        MyToash.Log(time + "");
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }

        return simpleDateFormat.format(new java.util.Date((long) (1505206425 * 1000))) + "    " + simpleDateFormat.format(new java.util.Date(time)) + "  " + simpleDateFormat.format(new java.util.Date((long)(data * 1000)));
    }

    //16进制字符串转化字节数组
    public static byte[] hexStr2ByteArray(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {
            //因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            //将hex 转换成byte   "&" 操作为了防止负数的自动扩展
            // hex转换成byte 其实只占用了4位，然后把高位进行右移四位
            // 然后“|”操作  低四位 就能得到 两个 16进制数转换成一个byte.
            //
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    public static String string2Unicode(String string) {
        if (StringUtils.isBlank(string)) return null;
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            String hex = Integer.toHexString(c);
            MyToash.Log(hex);
            if (hex.length() == 2) {//非中文补全两个00
              unicode.append(hex+"00");
            } else {//中文颠倒顺序
                unicode.append(hex.substring(2,4)+hex.substring(0,2));
            }

        }
        return unicode.toString();
    }

    /**
     * 字符串转java字节码
     *
     * @param b
     * @return
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节

            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        b = null;
        return b2;
    }

    /**
     * 将两个ASCII字符合成一个字节；
     * 如："EF"--> 0xEF
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     */
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式
     * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
     *
     * @param src String
     * @return byte[]
     */
    public static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[19];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < 19; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }


    public static byte[] calendar2Bytes() {
        Calendar calendar = new GregorianCalendar();
        int time = (int) ((calendar.getTimeInMillis() / 1000)/*+8*60*60*/);
        return get4Bytes(time,4);
    }

    //获取数字的四（2）个字节
    public static byte[] get4Bytes(int data, int flag) {

        byte[] bytes = new byte[flag];
        for (int i = bytes.length - 1; i >= 0; i--) {
            bytes[i] = (byte) (data & 0xFF);
            data >>= 8;
        }
        return bytes;
    }

}

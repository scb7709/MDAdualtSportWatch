package com.headlth.management.MyBlue;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by abc on 2017/7/21.
 */
public class MyBlueDataAnalysis {
    private static  Integer[] extractBeatToBeatInterval(
            BluetoothGattCharacteristic characteristic) {
        int flag = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
        int format = -1;
        int energy = -1;
        int offset = 1; // This depends on hear rate value format and if there is energy data
        int rr_count = 0;
        if ((flag & 0x01) != 0) {
            format = BluetoothGattCharacteristic.FORMAT_UINT16;
            offset = 3;
        } else {
            format = BluetoothGattCharacteristic.FORMAT_UINT8;
            offset = 2;
        }
        if ((flag & 0x08) != 0) {
            energy = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
            offset += 2;

        }
        if ((flag & 0x16) != 0) {
            rr_count = ((characteristic.getValue()).length - offset) / 2;
            if (rr_count > 0) {
                Integer[] mRr_values = new Integer[rr_count];
                for (int i = 0; i < rr_count; i++) {
                    mRr_values[i] = characteristic.getIntValue(
                            BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                    offset += 2;
                }
                return mRr_values;
            }
        }
        return null;
    }
    public static String getBlueData(BluetoothGattCharacteristic c) {
        double heartRate = extractHeartRate(c);
  /*      Integer[] interval = extractBeatToBeatInterval(c);
        float[] result = null;
        if (interval != null) {
            result = new float[interval.length + 1];
        } else {
            result = new float[2];
            result[1] = -1.0f;
        }
        result[0] = (float) heartRate;

        if (interval != null) {
            for (int i = 0; i < interval.length; i++) {
                result[i + 1] = interval[i].floatValue();
            }
        }*/
        return heartRate+"";
    }
    public static double extractHeartRate(BluetoothGattCharacteristic characteristic) {
        int flag = characteristic.getProperties();
        int format = -1;
        if ((flag & 0x01) != 0) {
            format = BluetoothGattCharacteristic.FORMAT_UINT16;
        } else {
            format = BluetoothGattCharacteristic.FORMAT_UINT8;
        }
        final int heartRate = characteristic.getIntValue(format, 1);

        return heartRate;
    }
}

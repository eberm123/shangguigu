package com.ebchinatech.kylincloud.need.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SerialNumberUtil {
    private static String SERIAL_Model = "XQ";
    private static int SERIAL_NUMBER = 1;
    private static SerialNumberUtil serialNumberUtil = null;

    private SerialNumberUtil(){

    }

    /**
     * 取得SerialNumberUtil的单例实现
     * 懒汉式实现
     *
     * @return
     */
    public static SerialNumberUtil getInstance() {
        if (serialNumberUtil == null) {
            synchronized (SerialNumberUtil.class) {
                if (serialNumberUtil == null) {
                    serialNumberUtil = new SerialNumberUtil();
                }
            }
        }
        return serialNumberUtil;
    }

    /**
     * 生成 XQ当前年份+自增序列的编码
     * */
    public String getNumberForPK(int number){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return SERIAL_Model + simpleDateFormat.format(new Date()) + '-' + serialNumberAdd(++number);
    }

    /**
     * 序列号自增
     * @param number
     */
    public String serialNumberAdd(int number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(3);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(number);
    }

}

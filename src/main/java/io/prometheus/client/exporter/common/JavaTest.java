package io.prometheus.client.exporter.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JavaTest {

    public static void main(String[] args) throws Exception {
        System.out.println(toTimestamp());
    }

    private static long toTimestamp() throws Exception {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time="2018-09-29 16:39:15";
        Date date = format.parse(time);

        //日期转时间戳（毫秒）
        return date.getTime();
    }
}

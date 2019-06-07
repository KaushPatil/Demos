package com.syslogyx.bluetoothdemo.app.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by kaushik on 16/3/18.
 */

public class Utils {

    /**
     * Method which get the current timestamp in Seconds
     *
     * @return
     */

    private static final String logCat = "Logg>>";

    public static String getCurrentTimeStampInSecondsInGMT() {
        String timeStamp = null;
        try {
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.add(Calendar.HOUR, 5);
            mCalendar.add(Calendar.MINUTE, 30);

            long timeMillis = mCalendar.getTimeInMillis();
            long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
            timeStamp = String.valueOf(timeSeconds);
//            Log.d(logCat, "Date>>" + mCalendar.getTime().toString() + " sec " + timeSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeStamp;
    }


    /**
     * Get the current date and time
     * <p>
     * Date : ddMMyy ex 240318
     * Where day = 24, month = 03, Year = 18.
     * <p>
     * Time : HHmmss ex 142405
     * Where Hours = 14, minuets = 24, Seconds = 05.
     *
     * @return dateTime ddMMyy HHmmss
     * Ex: 240318 142405
     */
    public static String getCurrentDataAndTime() {
//        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//        Log.d(logCat, "Date 1 >>" + currentDateTimeString);
        String formattedDate = null;

        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("ddMMyy HHmmss");
            formattedDate = df.format(c.getTime());
//            Log.d(logCat, "Date 2 >>" + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return formattedDate;
    }


    /**
     * Method which split the string of data time into array of string
     * <p>
     * 1st pos hold date
     * 2nd pos hold time
     *
     * @return
     */
    public static String[] getDateTimeArray() {
        String dataTime = getCurrentDataAndTime();
        String[] dateTimeArray = null;
        if (dataTime != null) {
            dateTimeArray = new String[2];
            dateTimeArray = dataTime.split("\\s");
        }

        return dateTimeArray;
    }

    /**
     * Generate the shift production unique id
     *
     * @param packet_id
     * @param timeStamp
     * @return
     */
    public static String generateSignalPacketUniqueId(String packet_id, String timeStamp) {

        String signalPacketId = "";
        try {
//            if (packet_id != null && timeStamp != null && MoManager.getInstance().getCurrentMoNumber() != null) {
//                String moNumber = MoManager.getInstance().getCurrentMoNumber();
//                signalPacketId = packet_id + timeStamp + moNumber;
//                Log.d(logCat, "Unique signalPacketId--" + signalPacketId);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return signalPacketId;
    }
}

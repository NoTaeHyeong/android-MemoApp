package org.techtown.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by F-06 on 2019-01-12.
 */

public class SetDate {
    static Date date;
    static SimpleDateFormat dateFormat;

    public static String setDate() {
        date = new Date();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(date);
    }

    public static String setDateHour() {
        date = new Date();
        dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");

        return dateFormat.format(date);
    }
}
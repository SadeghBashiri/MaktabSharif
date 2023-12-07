package ir.maktab.onlineexam.utility;

import org.joda.time.DateTime;
import org.kaveh.commons.farsi.utils.JalaliCalendarUtils;

import java.util.Date;

public class PersianDateTools {
    public static String gregorianDateToPersianString(Date gregorianDate){
        return JalaliCalendarUtils.convertGregorianToJalali(new DateTime(gregorianDate)).toString();
    }
}
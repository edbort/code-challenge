package bortolin.edison.husky.code.challenge.util;

import java.util.Calendar;
import java.util.Date;

public class Util {
 
    public static Date getStartDateTimeOf(Date startDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        setStartTime(c);
        return c.getTime();
    }
    
    public static Date getEndDateTimeOf(Date endDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        setEndTime(c);
        return c.getTime();
    }

    public static void setStartTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static void setEndTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
    }    
    
}

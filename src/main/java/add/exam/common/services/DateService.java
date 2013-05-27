package add.exam.common.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DateService
{

    public static final Integer SECONDS_IN_MINUTE = 60;
    private static final Integer SECONDS_IN_HOUR = 3600;
    private static final Integer MILLISECONDS_IN_MINUTE = 60000;
    private static final Integer MILLISECONDS_IN_SECOND = 1000;
    private static final String TIME_PATTERN = "%s:%s:%s";

    public static long getTimeLeftInSeconds(Date currentTime, Date startTime, Integer minutes){
        return (startTime.getTime() + minutes * MILLISECONDS_IN_MINUTE - currentTime.getTime()) / MILLISECONDS_IN_SECOND;
    }

    public static long getTimeRangeInSecond(Date startTime, Date currentTime){
        return (currentTime.getTime() - startTime.getTime()) /MILLISECONDS_IN_SECOND;
    }

    public static String getTimeString(Long seconds){
        Long hours = seconds / SECONDS_IN_HOUR;
        seconds -= hours * SECONDS_IN_HOUR;
        Long minutes = seconds / SECONDS_IN_MINUTE;
        seconds -= minutes * SECONDS_IN_MINUTE;
        return String.format(TIME_PATTERN,
                StringUtils.leftPad(hours.toString(), 2, '0'),
                StringUtils.leftPad(minutes.toString(), 2, '0'),
                StringUtils.leftPad(seconds.toString(), 2, '0'));
    }

    public static boolean isDateInRange(Date date, Date dateFrom, Date dateTo){
        if (date == null){
            return false;
        }
        if (dateFrom != null){
            if (date.getTime() < dateFrom.getTime()){
                return false;
            }
        }
        if (dateTo != null){
            if (date.getTime() > dateTo.getTime()){
                return false;
            }
        }
        return true;
    }

    public static Date getDateFrom(Date dateFrom)
    {
        if (dateFrom == null){
            dateFrom = new Date(110,0,1,0,0,0);
        }
        return dateFrom;
    }

    public static Date getDateTo(Date dateTo)
    {
        if (dateTo == null){
            dateTo = new Date(8099,0,1,0,0,0);
        }
        return dateTo;
    }
}

package pl.coderslab.cls_wms_app.app;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class TimeUtils {
    public static String timeNowShort(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        LocalDateTime now = LocalDateTime.now();
        String dateTimeString = now.format(formatter);
        return dateTimeString;
    }

    public static String timeNowLong(){
        LocalDateTime now = LocalDateTime.now();
        String dateTimeString = now.toString(); //.replace("T"," ");
        return dateTimeString;
    }

    public static List<String> dayOfWeeks(){
        List<String> dayOfWeeks = Arrays.asList(
                new String("MONDAY"),
                new String("TUESDAY"),
                new String("WEDNESDAY"),
                new String("THURSDAY"),
                new String("FRIDAY"),
                new String("SATURDAY"),
                new String("SUNDAY")
        );
        return dayOfWeeks;
    }
}

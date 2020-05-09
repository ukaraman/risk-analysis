package lv.iljakorneckis.webloans.helper;

import org.joda.time.DateTime;

import java.util.Date;

public class TestHelper {

    public static DateTime extractDate(String millisecondString) {
        Long millis = Long.parseLong(millisecondString);
        Date date = new Date(millis);

        return new DateTime(date);
    }
}

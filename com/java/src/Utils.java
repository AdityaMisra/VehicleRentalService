import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created on 2/7/21, 7:42 PM
 * Utils.java
 *
 * @author aditya.misra
 */


class Utils {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Calendar getDateTime(String time) throws ParseException {
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(format.parse(time));

        dateTime.set(Calendar.MINUTE, 0);
        dateTime.set(Calendar.SECOND, 0);
        dateTime.set(Calendar.MILLISECOND, 0);

        return dateTime;
    }

    public int hoursDifference(Calendar date1, Calendar date2) {

        final int MILLI_TO_HOUR = 1000 * 60 * 60;

        return (int) (date1.getTimeInMillis() - date2.getTimeInMillis()) / MILLI_TO_HOUR;
    }
}
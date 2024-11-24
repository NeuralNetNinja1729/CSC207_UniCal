package data_access;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Helper Function for Converting Dates to YYYY-MM-DD format.
 */
public class DateUtils {

    /**
     * Returns the String Representation of the First Date of the Month.
     *
     * @param date a LocalDate object.
     * @return YYYY-MM-DD String representation of the first of the Month.
     */
    public static String getStartOfMonth(LocalDate date) {
        return date.withDayOfMonth(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Returns the String Representation of the Last Date of the Month.
     *
     * @param date a LocalDate object.
     * @return YYYY-MM-DD String representation of the last of the Month.
     */
    public static String getEndOfMonth(LocalDate date) {
        return date.withDayOfMonth(date.lengthOfMonth()).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Returns the String Representation of the Date.
     *
     * @param date a LocalDate object.
     * @return YYYY-MM-DD String representation of the Date.
     */
    public static String getDateString(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}


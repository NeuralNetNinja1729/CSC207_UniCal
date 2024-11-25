package data_access;

import entity.Calendar;
import entity.GoogleCalendar;
import entity.NotionCalendar;
import entity.OutlookCalendar;

/**
 * Factory for creating the appropriate Calendar Data Access Object based on the Calendar type.
 */
public class CalendarDataAccessObjectFactory {

    /**
     * Returns the appropriate Data Access Object for the given Calendar entity.
     *
     * @param calendar the Calendar entity
     * @return the corresponding Calendar DAO
     * @throws IllegalArgumentException if the Calendar type is not supported
     */
    public Object getCalendarDataAccessObject(Calendar calendar) {
        switch (calendar.getCalendarApiName()) {
            case "GoogleCalendar":
                return new GoogleCalendarDataAccessObject((GoogleCalendar) calendar);
            case "NotionCalendar":
                return new NotionCalendarDataAccessObject((NotionCalendar) calendar);
            case "OutlookCalendar":
                return new OutlookCalendarDataAccessObject((OutlookCalendar) calendar);
            default:
                throw new IllegalArgumentException("Unsupported calendar type: " + calendar.getCalendarApiName());
        }
    }
}


package interface_adapter.change_calendar_day;

import entity.Calendar;
import entity.Event;
import java.time.LocalDate;
import java.util.ArrayList;

public class ChangeCalendarDayState {
    private ArrayList<Calendar> calendars = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private LocalDate selectedDate;
    private String error = null;

    // For creating copies in the view model
    public ChangeCalendarDayState(ChangeCalendarDayState copy) {
        if (copy != null) {
            this.calendars = new ArrayList<>(copy.calendars);
            this.events = new ArrayList<>(copy.events);
            this.selectedDate = copy.selectedDate;
            this.error = copy.error;
        }
    }

    // Default constructor
    public ChangeCalendarDayState() {}

    public ArrayList<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(ArrayList<Calendar> calendars) {
        this.calendars = calendars;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    // Helper method to get the current calendar (since we're only using one)
    public Calendar getCurrentCalendar() {
        return calendars.isEmpty() ? null : calendars.get(0);
    }
}
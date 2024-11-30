package interface_adapter.add_event;

import entity.Calendar;
import entity.Event;
import entity.User;

public class AddEventState {
    private User currentUser;
    private Calendar currCalendar;
    private Event event;
    private String currMonth;
    private Integer currYear;
    private Integer currDay;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Calendar getCurrCalendar() {
        return currCalendar;
    }

    public void setCurrCalendar(Calendar currCalendar) {
        this.currCalendar = currCalendar;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getCurrMonth() {
        return currMonth;
    }

    public void setCurrMonth(String currMonth) {
        this.currMonth = currMonth;
    }

    public Integer getCurrYear() {
        return currYear;
    }

    public void setCurrYear(Integer currYear) {
        this.currYear = currYear;
    }

    public Integer getCurrDay() {
        return currDay;
    }

    public void setCurrDay(Integer currDay) {
        this.currDay = currDay;
    }
}

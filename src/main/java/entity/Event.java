package entity;

import java.time.LocalDateTime;

public class Event {
    private String eventName;
    private LocalDateTime eventTime;
    private String eventLocation;
    private String eventDescription;

    public Event(String eventName, LocalDateTime eventTime, String eventLocation, String eventDescription) {
        this.setEventName(eventName);
        this.setEventTime(eventTime);
        this.setEventLocation(eventLocation);
        this.setEventDescription(eventDescription);
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}

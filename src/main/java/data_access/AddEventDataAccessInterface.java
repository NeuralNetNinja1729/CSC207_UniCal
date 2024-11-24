package data_access;

import entity.Event;

/**
 * Interface for adding events to a calendar.
 */
public interface AddEventDataAccessInterface {

    /**
     * Adds an event to the calendar.
     *
     * @param event the event to add.
     * @return true if the operation was successful, false otherwise.
     */
    boolean addEvent(Event event);
}

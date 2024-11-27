package data_access;

import entity.Event;

public interface DeleteEventDataAccessInterface {
  /**
   * Deletes an event from the calendar.
   *
   * @param event the event to delete.
   * @return true if the operation was successful, false otherwise.
   */
  boolean deleteEvent(Event event);
}

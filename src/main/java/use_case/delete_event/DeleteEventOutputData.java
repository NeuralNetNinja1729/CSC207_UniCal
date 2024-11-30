// Create the Output Data class
package use_case.delete_event;

import entity.Event;

public class DeleteEventOutputData {
  private final Event event;

  public DeleteEventOutputData(Event event) {
    this.event = event;
  }

  public Event getEvent() {
    return event;
  }
}

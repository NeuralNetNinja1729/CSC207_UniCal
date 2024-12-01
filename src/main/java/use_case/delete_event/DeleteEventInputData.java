package use_case.delete_event;

import entity.Event;

public class DeleteEventInputData {
  private final Event event;

  public DeleteEventInputData(Event event) {
    this.event = event;
  }

  public Event getEvent() {
    return event;
  }
}

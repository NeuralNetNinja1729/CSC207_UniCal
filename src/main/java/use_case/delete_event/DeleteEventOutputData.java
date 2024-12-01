package use_case.delete_event;

import entity.Event;

public class DeleteEventOutputData {
  private final Event event;
  private final boolean useCaseFailed;

  public DeleteEventOutputData(Event event, boolean useCaseFailed) {
    this.event = event;
    this.useCaseFailed = useCaseFailed;
  }

  public Event getEvent() {
    return event;
  }

  public boolean isUseCaseFailed() {
    return useCaseFailed;
  }
}
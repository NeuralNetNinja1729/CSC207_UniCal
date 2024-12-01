package interface_adapter.delete_event;

import entity.Event;

public class DeleteEventState {
  private Event selectedEvent = null;
  private String error = "";
  private boolean useCaseFailed = false;

  // Copy constructor for immutability
  public DeleteEventState(DeleteEventState copy) {
    selectedEvent = copy.selectedEvent;
    error = copy.error;
    useCaseFailed = copy.useCaseFailed;
  }

  // Default constructor
  public DeleteEventState() {}

  // Getters and setters
  public Event getSelectedEvent() {
    return selectedEvent;
  }

  public void setSelectedEvent(Event event) {
    this.selectedEvent = event;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public boolean isUseCaseFailed() {
    return useCaseFailed;
  }

  public void setUseCaseFailed(boolean useCaseFailed) {
    this.useCaseFailed = useCaseFailed;
  }
}

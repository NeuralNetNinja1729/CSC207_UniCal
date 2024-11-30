package interface_adapter.delete_event;

public class DeleteEventState {
  private String eventName = "";
  private String errorMessage = "";

  public DeleteEventState(DeleteEventState copy) {
    this.eventName = copy.eventName;
    this.errorMessage = copy.errorMessage;
  }

  public DeleteEventState() {}

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String error) {
    this.errorMessage = error;
  }
}

package interface_adapter.delete_event;

import interface_adapter.ViewModel;

public class DeleteEventViewModel extends ViewModel<DeleteEventState> {
  public static final String TITLE_LABEL = "Delete Event";
  public static final String DELETE_BUTTON_LABEL = "Delete";
  public static final String CANCEL_BUTTON_LABEL = "Cancel";
  public static final String CONFIRM_DELETE_MESSAGE = "Are you sure you want to delete this event?";

  public DeleteEventViewModel() {
    super("delete event");
    setState(new DeleteEventState());
  }
}

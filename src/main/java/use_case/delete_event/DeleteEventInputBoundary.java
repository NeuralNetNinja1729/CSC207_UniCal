// Create the Input Boundary interface
package use_case.delete_event;

public interface DeleteEventInputBoundary {
  /**
   * Executes the delete event use case.
   * @param deleteEventInputData the input data
   */
  void execute(DeleteEventInputData deleteEventInputData);
}

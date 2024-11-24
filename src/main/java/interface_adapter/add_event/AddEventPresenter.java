package interface_adapter.add_event;

import use_case.add_event.AddEventOutputBoundary;
import use_case.add_event.AddEventOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class AddEventPresenter implements AddEventOutputBoundary {

    public AddEventPresenter() {
        // TODO
    }

    @Override
    public void prepareSuccessView(AddEventOutputData outputData) {
        System.out.println("Successfully added event");
        // TODO
    }

    @Override
    public void prepareFailView(String error) {
        // TODO
    }
}

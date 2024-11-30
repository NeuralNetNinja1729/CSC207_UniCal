package interface_adapter.add_event;

import entity.Calendar;
import use_case.add_event.AddEventInputBoundary;
import use_case.add_event.AddEventInputData;

public class AddEventController {
    private final AddEventInputBoundary addEventUseCaseInteractor;

    public AddEventController(AddEventInputBoundary addEventUseCaseInteractor) {
        this.addEventUseCaseInteractor = addEventUseCaseInteractor;
    }

    public void execute(String eventName, String month, Integer day, Integer year, Calendar calendar) {
        String date = year + "-" + month + "-" + day;
        AddEventInputData inputData = new AddEventInputData(eventName, date, calendar);
        addEventUseCaseInteractor.execute(inputData);
    }
}

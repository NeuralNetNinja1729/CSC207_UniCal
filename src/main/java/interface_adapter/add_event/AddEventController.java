package interface_adapter.add_event;

import entity.Calendar;
import use_case.add_event.AddEventInputBoundary;
import use_case.add_event.AddEventInputData;

import java.util.HashMap;
import java.util.Map;

public class AddEventController {
    private final AddEventInputBoundary addEventUseCaseInteractor;

    public AddEventController(AddEventInputBoundary addEventUseCaseInteractor) {
        this.addEventUseCaseInteractor = addEventUseCaseInteractor;
    }

    public void execute(String eventName, String month, Integer day, Integer year, Calendar calendar) {
        Map<String, String> monthNumeric = new HashMap<>();
        monthNumeric.put("January", "01");
        monthNumeric.put("February", "02");
        monthNumeric.put("March", "03");
        monthNumeric.put("April", "04");
        monthNumeric.put("May", "05");
        monthNumeric.put("June", "06");
        monthNumeric.put("July", "07");
        monthNumeric.put("August", "08");
        monthNumeric.put("September", "09");
        monthNumeric.put("October", "10");
        monthNumeric.put("November", "11");
        monthNumeric.put("December", "12");
        String monthNum = monthNumeric.get(month);
        String dayString;
        if (day >= 10){
            dayString = day.toString();
        }
        else {
            dayString = "0" + day;
        }
        String date = year + "-" + monthNum + "-" + dayString;
        AddEventInputData inputData = new AddEventInputData(eventName, date, calendar);
        addEventUseCaseInteractor.execute(inputData);
    }
}

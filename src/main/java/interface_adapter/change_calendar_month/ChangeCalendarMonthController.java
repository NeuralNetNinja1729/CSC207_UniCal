package interface_adapter.change_calendar_month;

import use_case.change_calendar_month.ChangeCalendarMonthInputBoundary;
import use_case.change_calendar_month.ChangeCalendarMonthInputData;
import entity.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ChangeCalendarMonthController {
    private final ChangeCalendarMonthInputBoundary changeCalendarMonthUseCaseInteractor;
    private final Map<String, String> monthNumeric;

    public ChangeCalendarMonthController(ChangeCalendarMonthInputBoundary changeCalendarMonthUseCaseInteractor) {
        this.changeCalendarMonthUseCaseInteractor = changeCalendarMonthUseCaseInteractor;
        this.monthNumeric = initializeMonthMap();
    }

    public void execute(List<Calendar> calendarList, String month, Integer year) {
        String monthNum = monthNumeric.get(month);
        String date = String.format("%d-%s-01", year, monthNum);

        ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(
                calendarList,
                date
        );

        changeCalendarMonthUseCaseInteractor.execute(inputData);
    }

    private Map<String, String> initializeMonthMap() {
        Map<String, String> map = new HashMap<>();
        map.put("January", "01");
        map.put("February", "02");
        map.put("March", "03");
        map.put("April", "04");
        map.put("May", "05");
        map.put("June", "06");
        map.put("July", "07");
        map.put("August", "08");
        map.put("September", "09");
        map.put("October", "10");
        map.put("November", "11");
        map.put("December", "12");
        return map;
    }
}
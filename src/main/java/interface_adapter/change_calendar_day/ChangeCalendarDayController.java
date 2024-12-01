package interface_adapter.change_calendar_day;

import entity.Calendar;
import use_case.change_calendar_day.ChangeCalendarDayInputBoundary;
import use_case.change_calendar_day.ChangeCalendarDayInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeCalendarDayController {
    private final ChangeCalendarDayInputBoundary changeCalendarDayUseCaseInteractor;

    public ChangeCalendarDayController(ChangeCalendarDayInputBoundary changeCalendarDayUseCaseInteractor) {
        this.changeCalendarDayUseCaseInteractor = changeCalendarDayUseCaseInteractor;
    }


    public void execute(List<Calendar> calendarList, String month, Integer day, Integer year) {
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
        String dayString;
        if (day >= 10){
            dayString = day.toString();
        }
        else {
            dayString = "0" + day;
        }
        String monthNum = monthNumeric.get(month);
        String date = year + "-" + monthNum + "-" + dayString;

        final ChangeCalendarDayInputData changeCalDayInpData = new ChangeCalendarDayInputData(
                calendarList, date);

        changeCalendarDayUseCaseInteractor.execute(changeCalDayInpData);
    }
}
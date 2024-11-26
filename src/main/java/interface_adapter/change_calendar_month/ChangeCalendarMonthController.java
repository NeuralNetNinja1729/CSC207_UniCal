package interface_adapter.change_calendar_month;

import entity.Calendar;
import use_case.change_calendar_month.ChangeCalendarMonthInputBoundary;
import use_case.change_calendar_month.ChangeCalendarMonthInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeCalendarMonthController {
    private final ChangeCalendarMonthInputBoundary changeCalendarMonthUseCaseInteractor;
     public ChangeCalendarMonthController(ChangeCalendarMonthInputBoundary changeCalendarMonthUseCaseInteractor) {
         this.changeCalendarMonthUseCaseInteractor = changeCalendarMonthUseCaseInteractor;
     }

     public void execute(List<Calendar> calendarList, String month, Integer year) {
         // some work needs to be done to parse this date to a suitable format
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
         String date = year + "-" + monthNum + "-01";

         final ChangeCalendarMonthInputData changeCalMonthInpData = new ChangeCalendarMonthInputData(
                 calendarList, date);

         changeCalendarMonthUseCaseInteractor.execute(changeCalMonthInpData);
     }
}

package interface_adapter.change_calendar_month;

import entity.Calendar;
import use_case.change_calendar_month.ChangeCalendarMonthInputBoundary;
import use_case.change_calendar_month.ChangeCalendarMonthInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeCalendarMonthController {
    private final ChangeCalendarMonthInputBoundary changeCalendarMonthUseCaseInteractor;
     public ChangeCalendarMonthController(ChangeCalendarMonthInputBoundary changeCalendarMonthUseCaseInteractor) {
         this.changeCalendarMonthUseCaseInteractor = changeCalendarMonthUseCaseInteractor;
     }

     public void execute(ArrayList<Calendar> calendarList, String month, Integer year) {
         // some work needs to be done to parse this date to a suitable format
         Map<String, String> month_numeric = new HashMap<>();
         month_numeric.put("January", "01");
         month_numeric.put("February", "02");
         month_numeric.put("March", "03");
         month_numeric.put("April", "04");
         month_numeric.put("May", "05");
         month_numeric.put("June", "06");
         month_numeric.put("July", "07");
         month_numeric.put("August", "08");
         month_numeric.put("September", "09");
         month_numeric.put("October", "10");
         month_numeric.put("November", "11");
         month_numeric.put("December", "12");

         String month_num = month_numeric.get(month);
         String date = year + "-" + month_num + "-01";

         final ChangeCalendarMonthInputData changeCalMonthInpData = new ChangeCalendarMonthInputData(
                 calendarList, date);

         changeCalendarMonthUseCaseInteractor.execute(changeCalMonthInpData);
     }
}

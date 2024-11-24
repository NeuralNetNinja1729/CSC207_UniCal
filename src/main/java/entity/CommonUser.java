package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    private final String name;
    private final String password;
    private List<Calendar> calendars;

    public CommonUser(String name, String password) {
        this.name = name;
        this.password = password;
        this.calendars = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void addCalendar(Calendar calendar) {
        calendars.add(calendar);
    }
}

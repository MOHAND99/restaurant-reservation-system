package restaurant;

import java.time.LocalDate;

public class DayIncome {
    private final double dayEarn;
    private final LocalDate date;

    public DayIncome(double dayEarn, LocalDate date) {
        this.dayEarn = dayEarn;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getDayEarn() {
        return dayEarn;
    }
}

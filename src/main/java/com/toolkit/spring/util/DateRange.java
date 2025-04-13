package com.toolkit.spring.util;

import java.time.LocalDate;
import java.util.Objects;

public class DateRange
{
    private DateRange() {}

    public static LocalDate[] daysAsc(LocalDate initialDate, int ndays)
    {
        Objects.requireNonNull(initialDate);
        LocalDate[] dates = new LocalDate[ndays];

        initialDate = initialDate.plusDays(1);

        for(int i = 0; i < ndays; i++)
        { dates[ndays - i - 1] = initialDate.plusDays(-i); }

        return dates;
    }

    public static LocalDate[] monthsAsc(LocalDate initialDate, int nmonths)
    {
        Objects.requireNonNull(initialDate);
        LocalDate[] dates = new LocalDate[nmonths];

        // make date be the first of next month to cover
        // the entire current month
        initialDate = LocalDate.of(
            initialDate.getYear(),
            initialDate.getMonthValue() + 1,
            1
        );

        for(int i = 0; i < nmonths; i++)
        { dates[nmonths - i - 1] = initialDate.plusMonths(-i); }

        return dates;
    }

    public static LocalDate[] yearsAsc(LocalDate initialDate, int nyears)
    {
        Objects.requireNonNull(initialDate);
        LocalDate[] dates = new LocalDate[nyears];

        // make date be the first of next year to cover
        // the entire current year
        initialDate = LocalDate.of(
            initialDate.getYear() + 1,
            1,
            1
        );

        for(int i = 0; i < nyears; i++)
        { dates[nyears - i - 1] = initialDate.plusYears(-i); }

        return dates;
    }

    public static LocalDate[] weeksAsc(LocalDate initialDate, int nweeks)
    {
        Objects.requireNonNull(initialDate);
        LocalDate[] dates = new LocalDate[nweeks];

        // make date be the first day of next week to cover
        // the entire current month
        initialDate = initialDate.plusDays(7 - initialDate.getDayOfWeek().getValue());

        for(int i = 0; i < nweeks; i++)
        { dates[nweeks - i - 1] = initialDate.plusWeeks(-i); }

        return dates;
    }
}

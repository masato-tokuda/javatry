package org.docksidestage.bizfw.basic.buyticket;

import java.time.LocalDate;

/**
 * @author katashin
 * This class means Local date range.
 * This class means closed interval, not open interval.
 * o [begin, end]
 * x [begin, end)
 */
public class LocalDateRange {

    private final LocalDate begin;
    private final LocalDate end;

    /**
     * This is static factory method.
     * I choose static factory method over constructor because it is similar to LocalDate class.
     * @param begin NotNull
     * @param end NotNull
     */
    public static LocalDateRange of(LocalDate begin, LocalDate end) {
        return new LocalDateRange(begin, end);
    }

    /**
     * @param date NotNull
     * @return true if date exists within range.
     */
    public boolean hasInRange(LocalDate date) {
        // by the way, this statement is difficult to read...
        // I think if ChronoLocalDate class has isBeforeOrEqual and isAfterOrEqual, it makes people happy.
        return !date.isBefore(begin) && !date.isAfter(end);
    }

    private LocalDateRange(LocalDate begin, LocalDate end) {
        this.begin = begin;
        this.end = end;
    }
}

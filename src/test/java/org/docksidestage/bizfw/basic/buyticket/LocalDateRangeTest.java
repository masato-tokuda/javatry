package org.docksidestage.bizfw.basic.buyticket;

import java.time.LocalDate;

import org.docksidestage.unit.PlainTestCase;

public class LocalDateRangeTest extends PlainTestCase {

    public void testInRange() {
        final LocalDate begin = LocalDate.of(2019, 9, 1);
        final LocalDate end = LocalDate.of(2019, 10, 1);
        LocalDateRange range = LocalDateRange.of(begin, end);

        log(range.hasInRange(begin)); // should true
        log(range.hasInRange(end)); // should true
        log(range.hasInRange(begin.minusDays(1))); // should false
        log(range.hasInRange(end.plusDays(1))); // should false
    }
}

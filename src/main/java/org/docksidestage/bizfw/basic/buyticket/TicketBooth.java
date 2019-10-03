/*
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.bizfw.basic.buyticket;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.docksidestage.javatry.basic.TicketBuyResult;

// TODO kata unusedのimport宣言があるよ by jflute (2019/10/03)
/**
 * @author jflute
 * @author katashin
 */
public class TicketBooth {

    // TODO kata わざとかもしれないけど、一応Javaの慣習として、staticなものを上に、インスタンス変数はその次ってのがあるので... by jflute (2019/10/03)
    // quantityやsalesProceedsの近くに行ったほうがいいかなとは思った
    private final LocalDateProvider dateProvider;

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final int MAX_QUANTITY = 10;

    // Map<LocalDateRange, Integer> is not suitable...
    // if Java has Tuple, List<Tuple<LocalDateRange, Integer>> is better.
    // otherwise creating new class is good option, especially it has verification method of data ranges are not overlapped.
    // but I have no time for it...
    private static final Map<TicketType, Map<LocalDateRange, Integer>> priceMap;

    static {
        // TODO kata [tips]こういうのよくunmodifiableMapにしたりする (どこまでやるかさじ加減はあるけど) by jflute (2019/10/03)
        // https://github.com/lastaflute/lastaflute-example-harbor/blob/master/src/main/java/org/docksidestage/dbflute/allcommon/DBMetaInstanceHandler.java#L65
        //  e.g. priceMap = Collections.unmodifiableMap(workingMap);
        priceMap = new HashMap<>();
        final LocalDate beforeTaxIncrease = LocalDate.of(2019, 9, 30);
        final LocalDate afterTaxIncrease = beforeTaxIncrease.plusDays(1);
        final LocalDateRange beforeTaxIncreaseRange = LocalDateRange.of(LocalDate.MIN, beforeTaxIncrease);
        final LocalDateRange afterTaxIncreaseRange = LocalDateRange.of(afterTaxIncrease, LocalDate.MAX);

        final Map<LocalDateRange, Integer> oneDayTicketPrices = new HashMap<>();
        oneDayTicketPrices.put(beforeTaxIncreaseRange, 7400);
        oneDayTicketPrices.put(afterTaxIncreaseRange, 7500);
        final HashMap<LocalDateRange, Integer> twoDayTicketPrices = new HashMap<>();
        twoDayTicketPrices.put(beforeTaxIncreaseRange, 13200);
        twoDayTicketPrices.put(afterTaxIncreaseRange, 13400);
        final HashMap<LocalDateRange, Integer> fourDayTicketPrices = new HashMap<>();
        fourDayTicketPrices.put(beforeTaxIncreaseRange, 22400);
        fourDayTicketPrices.put(afterTaxIncreaseRange, 22800);

        priceMap.put(TicketType.OneDay, oneDayTicketPrices);
        priceMap.put(TicketType.TwoDay, twoDayTicketPrices);
        priceMap.put(TicketType.FourDay, fourDayTicketPrices);
    }

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // TODO kata [いいね]良いコメント by jflute (2019/10/03)
    // this quantity is common on all kinds of tickets.
    // because this means physical number of paper used to ticket. (maybe...)
    private int quantity = MAX_QUANTITY;
    private Integer salesProceeds;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public TicketBooth(LocalDateProvider provider) {
        this.dateProvider = provider;
    }

    // ===================================================================================
    //                                                                          Buy Ticket
    //                                                                          ==========
    public TicketBuyResult buyPassport(int handedMoney, TicketType ticketType) {
        int price = selectPrice(ticketType, dateProvider.now());
        Ticket ticket;
        switch (ticketType) {
        case OneDay:
            ticket = new OneDayTicket(price);
            break;
        case TwoDay:
            ticket = new PluralDayTicket(price, 2, TicketType.TwoDay);
            break;
        case FourDay:
            ticket = new PluralDayTicket(price, 4, TicketType.FourDay);
            break;
        default:
            throw new IllegalStateException("Invalid ticketType. ticket type:" + ticketType);
        }
        final int change = calculateChange(handedMoney, price);
        return new TicketBuyResult(ticket, change);
    }

    private int calculateChange(int handedMoney, int price) {
        if (quantity <= 0) {
            throw new TicketSoldOutException("Sold out");
        }
        if (handedMoney < price) {
            throw new TicketShortMoneyException("Short money: " + handedMoney);
        }
        // decrease quantity after confirmation handedMoney is larger than price.
        --quantity;
        if (salesProceeds != null) {
            salesProceeds = salesProceeds + price;
        } else {
            salesProceeds = price;
        }
        return handedMoney - price;
    }

    public static class TicketSoldOutException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public TicketSoldOutException(String msg) {
            super(msg);
        }
    }

    public static class TicketShortMoneyException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public TicketShortMoneyException(String msg) {
            super(msg);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public int getQuantity() {
        return quantity;
    }

    public Integer getSalesProceeds() {
        return salesProceeds;
    }

    private int selectPrice(TicketType ticketType, LocalDate date) {
        // I'd like to use IfPresentOrElse in Java 9....
        final Map<LocalDateRange, Integer> priceMap = TicketBooth.priceMap.get(ticketType);
        if (priceMap == null) {
            throw new IllegalStateException("Invalid ticket type. type:" + ticketType);
        }
        return priceMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().hasInRange(date))
                .findFirst()
                .map(entry -> entry.getValue())
                .orElseThrow(() -> new IllegalStateException("master date range data is broken..."));
        // this maybe not request problem, so IllegalStateException is not suitable, I think.
    }
}

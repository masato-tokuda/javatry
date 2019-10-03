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

import org.docksidestage.javatry.basic.TicketBuyResult;

/**
 * @author jflute
 * @author katashin
 */
public class TicketBooth {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final int MAX_QUANTITY = 10;
    private static final int ONE_DAY_PRICE = 7400; // when 2019/06/1
    // when 2019/09/30.
    // after 10/01 it is increased to 13400 because of consumption tax increased.
    // TODO katashin this price should be flexible.
    private static final int TWO_DAY_PRICE = 13200;
    private static final int FOUR_DAY_PRICE = 22400;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // this quantity is common on all kinds of tickets.
    // because this means physical number of paper used to ticket. (maybe...)
    private int quantity = MAX_QUANTITY;
    private Integer salesProceeds;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public TicketBooth() {
    }

    // ===================================================================================
    //                                                                          Buy Ticket
    //                                                                          ==========
    public TicketBuyResult buyPassport(int handedMoney, TicketType ticketType) {
        // TODO katashin should be more flexible. by the way, I'd like to use switch formula in Java 11...
        int price;
        Ticket ticket;
        switch (ticketType) {
        case OneDay:
            price = ONE_DAY_PRICE;
            ticket = new OneDayTicket(price);
            break;
        case TwoDay:
            price = TWO_DAY_PRICE;
            ticket = new PluralDayTicket(price, 2, TicketType.TwoDay);
            break;
        case FourDay:
            price = FOUR_DAY_PRICE;
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
}

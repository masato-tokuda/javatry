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

// TODO kata authorをよろしくね by jflute (2019/10/03)
/**
 * @author jflute
 */
public class TicketBooth {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final int MAX_QUANTITY = 10;
    private static final int ONE_DAY_PRICE = 7400; // when 2019/06/1
    // when 2019/09/30.
    // TODO kata [いいね]これはタイムリーでとてもおもしろい by jflute (2019/10/03)
    // after 10/01 it is increased to 13400 because of consumption tax increased.
    private static final int TWO_DAY_PRICE = 13200;

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
    public TicketBooth() {
    }

    // ===================================================================================
    //                                                                          Buy Ticket
    //                                                                          ==========
    public Ticket buyOneDayPassport(int handedMoney) {
        buyPassport(handedMoney, ONE_DAY_PRICE);
        return new OneDayTicket(ONE_DAY_PRICE);
    }

    /**
     * buy two day passport.
     * @param handedMoney money you handed.
     * @return change(rest handed money).
     */
    public TicketBuyResult buyTwoDayPassport(int handedMoney) {
        int change = buyPassport(handedMoney, TWO_DAY_PRICE);
        return new TicketBuyResult(new PluralDayTicket(TWO_DAY_PRICE, 2, TicketType.TwoDay), change);
    }

    private int buyPassport(int handedMoney, int price) {
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

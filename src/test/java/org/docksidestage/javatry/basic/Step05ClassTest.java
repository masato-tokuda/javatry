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
package org.docksidestage.javatry.basic;

import java.time.LocalDate;

import org.docksidestage.bizfw.basic.buyticket.LocalDateProvider;
import org.docksidestage.bizfw.basic.buyticket.Ticket;
import org.docksidestage.bizfw.basic.buyticket.TicketBooth;
import org.docksidestage.bizfw.basic.buyticket.TicketBooth.TicketShortMoneyException;
import org.docksidestage.bizfw.basic.buyticket.TicketType;
import org.docksidestage.unit.PlainTestCase;

/**
 * The test of class. <br>
 * Operate exercise as javadoc. If it's question style, write your answer before test execution. <br>
 * (javadocの通りにエクササイズを実施。質問形式の場合はテストを実行する前に考えて答えを書いてみましょう)
 * @author jflute
 * @author katashin
 */
public class Step05ClassTest extends PlainTestCase {

    private LocalDateProvider beforeTaxIncreaseDateProvider = new LocalDateProvider() {

        @Override
        public LocalDate now() {
            return LocalDate.of(2019, 9, 30);
        }
    };

    private LocalDateProvider afterTaxIncreaseDateProvider = new LocalDateProvider() {

        @Override
        public LocalDate now() {
            return LocalDate.of(2019, 10, 1);
        }
    };

    // ===================================================================================
    //                                                                          How to Use
    //                                                                          ==========
    /**
     * What string is sea variable at the method end? <br>
     * (メソッド終了時の変数 sea の中身は？)
     */
    public void test_class_howToUse_basic() {
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        booth.buyPassport(7400, TicketType.OneDay);
        int sea = booth.getQuantity();
        log(sea); // your answer? => 9
    }

    /** Same as the previous method question. (前のメソッドの質問と同じ) */
    public void test_class_howToUse_overpay() {
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        booth.buyPassport(10000, TicketType.OneDay);
        Integer sea = booth.getSalesProceeds();
        log(sea); // your answer? => 10000
        // -> 7400
    }

    /** Same as the previous method question. (前のメソッドの質問と同じ) */
    public void test_class_howToUse_nosales() {
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        Integer sea = booth.getSalesProceeds();
        log(sea); // your answer? => null
    }

    /** Same as the previous method question. (前のメソッドの質問と同じ) */
    public void test_class_howToUse_wrongQuantity() {
        Integer sea = doTest_class_ticket_wrongQuantity();
        log(sea); // your answer? => 9
        // -> 10
    }

    private Integer doTest_class_ticket_wrongQuantity() {
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        int handedMoney = 7399;
        try {
            booth.buyPassport(handedMoney, TicketType.OneDay);
            fail("always exception but none");
        } catch (TicketShortMoneyException continued) {
            log("Failed to buy one-day passport: money=" + handedMoney, continued);
        }
        return booth.getQuantity();
    }

    // ===================================================================================
    //                                                                           Let's fix
    //                                                                           =========
    /**
     * Fix the problem of ticket quantity reduction when short money. (Don't forget to fix also previous exercise answers) <br>
     * (お金不足でもチケットが減る問題をクラスを修正して解決しましょう (以前のエクササイズのanswerの修正を忘れずに))
     */
    public void test_class_letsFix_ticketQuantityReduction() {
        Integer sea = doTest_class_ticket_wrongQuantity();
        log(sea); // should be max quantity, visual check here
    }

    /**
     * Fix the problem of sales proceeds increased by handed money. (Don't forget to fix also previous exercise answers) <br>
     * (受け取ったお金の分だけ売上が増えていく問題をクラスを修正して解決しましょう (以前のエクササイズのanswerの修正を忘れずに))
     */
    public void test_class_letsFix_salesProceedsIncrease() {
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        booth.buyPassport(10000, TicketType.OneDay);
        Integer sea = booth.getSalesProceeds();
        log(sea); // should be same as one-day price, visual check here
    }

    /**
     * Make method for buying two-day passport (price is 13200). (which can return change as method return value)
     * (TwoDayPassport (金額は13200) も買うメソッドを作りましょう (戻り値でお釣りをちゃんと返すように))
     */
    public void test_class_letsFix_makeMethod_twoday() {
        // comment out after making the method
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        int money = 14000;
        int change = booth.buyPassport(money, TicketType.TwoDay).getChange();
        Integer sea = booth.getSalesProceeds() + change;
        log(sea); // should be same as money

        // and show two-day passport quantity here
        log(booth.getQuantity());
    }

    /**
     * Recycle duplicate logics between one-day and two-day by e.g. private method in class. (And confirm result of both before and after) <br>
     * (OneDayとTwoDayで冗長なロジックがあったら、クラス内のprivateメソッドなどで再利用しましょう (修正前と修正後の実行結果を確認))
     */
    public void test_class_letsFix_refactor_recycle() {
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        booth.buyPassport(10000, TicketType.OneDay);
        log(booth.getQuantity(), booth.getSalesProceeds()); // should be same as before-fix
    }

    // ===================================================================================
    //                                                                           Challenge
    //                                                                           =========
    /**
     * Now you cannot get ticket if you buy one-day passport, so return Ticket class and do in-park. <br>
     * (OneDayPassportを買ってもチケットをもらえませんでした。戻り値でTicketクラスを戻すようにしてインしましょう)
     */
    public void test_class_moreFix_return_ticket() {
        // comment out after modifying the method
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        Ticket oneDayPassport = booth.buyPassport(10000, TicketType.OneDay).getTicket();
        log(oneDayPassport.getDisplayPrice()); // should be same as one-day price
        log(oneDayPassport.isAlreadyIn()); // should be false
        oneDayPassport.doInPark();
        log(oneDayPassport.isAlreadyIn()); // should be true
    }

    /**
     * Now also you cannot get ticket if two-day passport, so return class that has ticket and change. <br>
     * (TwoDayPassportもチケットをもらえませんでした。チケットとお釣りを戻すクラスを作って戻すようにしましょう)
     */
    public void test_class_moreFix_return_whole() {
        // comment out after modifying the method
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        int handedMoney = 20000;
        TicketBuyResult twoDayPassportResult = booth.buyPassport(handedMoney, TicketType.TwoDay);
        Ticket twoDayPassport = twoDayPassportResult.getTicket();
        int change = twoDayPassportResult.getChange();
        log(twoDayPassport.getDisplayPrice() + change); // should be same as money
    }

    /**
     * Now you cannot judge ticket type "one-day or two-day?", so add method to judge it. <br>
     * (チケットをもらってもOneDayなのかTwoDayなのか区別が付きません。区別を付けられるメソッドを追加しましょう)
     */
    public void test_class_moreFix_type() {
        // your confirmation code here
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        int handedMoney = 20000;
        TicketBuyResult ticketBuyResult = booth.buyPassport(handedMoney, TicketType.TwoDay);
        // This should be TwoDay
        log(ticketBuyResult.getTicket().getTicketType());
    }

    // ===================================================================================
    //                                                                           Good Luck
    //                                                                           =========
    /**
     * Now only one use with two-day passport, so split ticket in one-day and two-day class and use interface. <br>
     * <pre>
     * o change Ticket class to interface, define doInPark(), getDisplayPrice() in it
     * o make class for one-day and class for plural days (called implementation class)
     * o make implementation classes implement Ticket interface
     * o doInPark() of plural days can execute defined times
     * </pre>
     * (TwoDayのチケットが一回しか利用できません。OneDayとTwoDayのクラスを分けてインターフェースを使うようにしましょう)
     * <pre>
     * o Ticket をインターフェース(interface)にして、doInPark(), getDisplayPrice() を定義
     * o OneDay用のクラスと複数日用のクラスを作成 (実装クラスと呼ぶ)
     * o 実装クラスが Ticket を implements するように
     * o 複数日用のクラスでは、決められた回数だけ doInPark() できるように
     * </pre>
     */
    public void test_class_moreFix_useInterface() {
        // your confirmation code here
        TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        int handedMoney = 20000;

        // test for one day
        Ticket ticket = booth.buyPassport(handedMoney, TicketType.OneDay).getTicket();
        log(ticket.isAlreadyIn()); // false;
        ticket.doInPark();
        log(ticket.isAlreadyIn()); // true;
        try {
            ticket.doInPark();
            fail("test failed.");
        } catch (Exception e) {
            log("should throw exception.", e);
        }

        // test for two day
        TicketBuyResult twoDayTicketResult = booth.buyPassport(handedMoney, TicketType.TwoDay);
        Ticket twoDayTicket = twoDayTicketResult.getTicket();

        log(twoDayTicket.isAlreadyIn()); // false;
        twoDayTicket.doInPark();
        log(twoDayTicket.isAlreadyIn()); // expected true, but it's a little weird.
        twoDayTicket.doInPark();
        log("success to go in park twice by using two day ticket.");
        try {
            twoDayTicket.doInPark();
            fail();
        } catch (Exception e) {
            log("should throw exception.", e);
        }
    }

    /**
     * Fix it to be able to buy four-day passport (price is 22400). <br>
     * (FourDayPassport (金額は22400) のチケットも買えるようにしましょう)
     */
    public void test_class_moreFix_wonder() {
        // your confirmation code here
        final TicketBooth booth = new TicketBooth(beforeTaxIncreaseDateProvider);
        int handedMoney = 30000;

        // test for four day.
        final TicketBuyResult result = booth.buyPassport(handedMoney, TicketType.FourDay);
        final Ticket ticket = result.getTicket();
        log(ticket.getDisplayPrice()); // should be 22400
        log(result.getChange() + ticket.getDisplayPrice()); // should be equal to handedMoney.

        log(ticket.isAlreadyIn()); // should false.
        ticket.doInPark();
        log(ticket.isAlreadyIn()); // should true.
        ticket.doInPark();
        ticket.doInPark();
        ticket.doInPark();
        log("success go in park four times.");
        try {
            ticket.doInPark();
            fail();
        } catch (Exception e) {
            log("should throw exception");
        }
    }

    /**
     * Refactor if you want to fix (e.g. is it well-balanced name of method and variable?). <br>
     * (その他、気になるところがあったらリファクタリングしてみましょう (例えば、バランスの良いメソッド名や変数名になっていますか？))
     */
    public void test_class_moreFix_yourRefactoring() {
        final TicketBooth ticketBoothBeforeTaxIncrease = new TicketBooth(beforeTaxIncreaseDateProvider);
        int handyMoney = 30000;

        // TODO these tests are parameterized test, so you can refactor test to read easily.

        // one day
        final TicketBuyResult oneDayResult = ticketBoothBeforeTaxIncrease.buyPassport(handyMoney, TicketType.OneDay);
        final Ticket oneDayTicket = oneDayResult.getTicket();
        log(oneDayTicket.getDisplayPrice()); // should one day ticket price before tax increase;
        log(oneDayResult.getChange() + oneDayTicket.getDisplayPrice()); // should be equal to handy money.

        // two day
        final TicketBuyResult twoDayResult = ticketBoothBeforeTaxIncrease.buyPassport(handyMoney, TicketType.TwoDay);
        final Ticket twoDayTicket = twoDayResult.getTicket();
        log(twoDayTicket.getDisplayPrice()); // should two day ticket price before tax increase;
        log(twoDayResult.getChange() + twoDayTicket.getDisplayPrice()); // should be equal to handy money.

        // four day
        final TicketBuyResult fourDayResult = ticketBoothBeforeTaxIncrease.buyPassport(handyMoney, TicketType.FourDay);
        final Ticket fourDayTicket = fourDayResult.getTicket();
        log(fourDayTicket.getDisplayPrice()); // should four day ticket price before tax increase;
        log(fourDayResult.getChange() + fourDayTicket.getDisplayPrice()); // should be equal to handy money.

        final TicketBooth ticketBoothAfterTaxIncrease = new TicketBooth(afterTaxIncreaseDateProvider);

        final TicketBuyResult oneDayResultAfter = ticketBoothAfterTaxIncrease.buyPassport(handyMoney, TicketType.OneDay);
        final Ticket oneDayTicketAfter = oneDayResultAfter.getTicket();
        log(oneDayTicketAfter.getDisplayPrice()); // should one day ticket price after tax increase;
        log(oneDayResultAfter.getChange() + oneDayTicketAfter.getDisplayPrice()); // should be equal to handy money.

        // two day
        final TicketBuyResult twoDayResultAfter = ticketBoothAfterTaxIncrease.buyPassport(handyMoney, TicketType.TwoDay);
        final Ticket twoDayTicketAfter = twoDayResultAfter.getTicket();
        log(twoDayTicketAfter.getDisplayPrice()); // should two day ticket price after tax increase;
        log(twoDayResultAfter.getChange() + twoDayTicketAfter.getDisplayPrice()); // should be equal to handy money.

        // four day
        final TicketBuyResult fourDayResultAfter = ticketBoothAfterTaxIncrease.buyPassport(handyMoney, TicketType.FourDay);
        final Ticket fourDayTicketAfter = fourDayResultAfter.getTicket();
        log(fourDayTicketAfter.getDisplayPrice()); // should four day ticket price after tax increase;
        log(fourDayResultAfter.getChange() + fourDayTicketAfter.getDisplayPrice()); // should be equal to handy money.
    }
}

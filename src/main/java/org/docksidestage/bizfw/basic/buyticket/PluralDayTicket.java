package org.docksidestage.bizfw.basic.buyticket;

public class PluralDayTicket implements Ticket {

    // TODO kata 属性たち、finalが付けられるものには付けてみよう。定義なのか状態なのかがすぐにわかるように by jflute (2019/10/03)
    private int displayPrice;
    // this is also increased by going in park if you have gone out and going in on the same day.
    private int usableCount;
    // this should be synchronize usableCount.
    private TicketType ticketType;
    private int inParkCount;

    public PluralDayTicket(int displayPrice, int usableCount, TicketType ticketType) {
        this.displayPrice = displayPrice;
        this.usableCount = usableCount;
        this.ticketType = ticketType;
        this.inParkCount = 0;
    }

    @Override
    public int getDisplayPrice() {
        return this.displayPrice;
    }

    @Override
    public void doInPark() {
        if (usableCount <= inParkCount) {
            // TODO kata countたちを例外メッセージに載せたいかもね by jflute (2019/10/03)
            throw new IllegalStateException("This ticket is used over usable count. Can not use to park in.");
        }
    }

    @Override
    public TicketType getTicketType() {
        return this.ticketType;
    }
    @Override
    public boolean isAlreadyIn() {
        return inParkCount > 0;
    }
}

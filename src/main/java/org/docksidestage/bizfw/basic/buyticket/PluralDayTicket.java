package org.docksidestage.bizfw.basic.buyticket;

/**
 * @author katashin
 */
public class PluralDayTicket implements Ticket {

    private final int displayPrice;
    // this is also increased by going in park if you have gone out and going in on the same day.
    private final int usableCount;
    // this should be synchronize usableCount.
    private final TicketType ticketType;
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
        ++inParkCount;
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

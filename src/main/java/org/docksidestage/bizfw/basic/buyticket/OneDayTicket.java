package org.docksidestage.bizfw.basic.buyticket;

// TODO kata javadocをよろしくね by jflute (2019/10/03)
public class OneDayTicket implements Ticket {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final int displayPrice;
    private boolean alreadyIn;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    // this is for step 6 tests.
    // this should be removed when this is unnecessary.
    public OneDayTicket(int displayPrice) {
        this.displayPrice = displayPrice;
    }

    @Override
    public int getDisplayPrice() {
        return this.displayPrice;
    }

    // ===================================================================================
    //                                                                             In Park
    //                                                                             =======
    @Override
    public void doInPark() {
        if (alreadyIn) {
            throw new IllegalStateException("Already in park by this ticket: displayedPrice=" + displayPrice);
        }
        alreadyIn = true;
    }

    @Override
    public TicketType getTicketType() {
        return TicketType.OneDay;
    }
    @Override
    public boolean isAlreadyIn() {
        return this.alreadyIn;
    }
}

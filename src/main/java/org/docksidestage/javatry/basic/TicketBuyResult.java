package org.docksidestage.javatry.basic;

import org.docksidestage.bizfw.basic.buyticket.Ticket;

public class TicketBuyResult {
    private Ticket ticket;
    private int change;

    public TicketBuyResult(Ticket ticket, int change) {
        this.ticket = ticket;
        this.change = change;
    }

    public Ticket getTicket() {
        return this.ticket;
    }
    public int getChange() {
        return this.change;
    }
}

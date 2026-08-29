package org.casestudies.dtos;

import org.casestudies.model.Ticket;

public class GenerateTicketResponseDto extends ResponseDto {
    private Ticket ticket;

    // Getter Setter for ticket
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}

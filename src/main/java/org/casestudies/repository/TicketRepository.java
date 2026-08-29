package org.casestudies.repository;

import org.casestudies.model.Ticket;

import java.util.*;

public class TicketRepository {
    private Long nextId = 0L;

    private final Map<Long, Ticket> tickets = new HashMap<>();

    public Ticket save(Ticket ticket) {
        Long id = ++nextId;

        ticket.setId(id);
        tickets.put(id, ticket);

        return ticket;
    }

    public Ticket getById(Long id) {
        return tickets.get(id);
    }
}

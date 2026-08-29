package org.casestudies.strategy;

import org.casestudies.model.Ticket;

public interface FeesCalculationStrategy {
    double calculateFees(Ticket ticket);
}

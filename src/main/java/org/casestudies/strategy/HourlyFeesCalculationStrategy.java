package org.casestudies.strategy;

import org.casestudies.model.Ticket;
import org.casestudies.exception.ParkingLotException;

import java.time.*;

public class HourlyFeesCalculationStrategy implements FeesCalculationStrategy {
    private static final double RATE_PER_HOUR = 50.0;

    @Override
    public double calculateFees(Ticket ticket) {
        if (ticket == null || ticket.getEntryTime() == null) {
            throw new ParkingLotException("Ticket entry time is required to calculate fees");
        }

        long hours = Duration.between(ticket.getEntryTime(), LocalDateTime.now()).toHours();
        hours = Math.max(1, hours);     // choose the larger value between 1 and hours.
        return hours * RATE_PER_HOUR;
    }
}

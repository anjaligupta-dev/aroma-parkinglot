package org.casestudies.strategy;

import org.casestudies.exception.ParkingLotException;
import org.casestudies.model.Ticket;

import java.time.*;

public class DailyFeesCalculationStrategy implements FeesCalculationStrategy {
    private static final double RATE_PER_DAY = 500.0;

    @Override
    public double calculateFees(Ticket ticket) {
        if (ticket == null || ticket.getEntryTime() == null) {
            throw new ParkingLotException("Ticket entry time is required to calculate fees");
        }

        LocalDateTime now = LocalDateTime.now();

        long durationHours = Duration.between(ticket.getEntryTime(), now).toHours();
        long days = Math.max(1, durationHours / 24);     // choose the larger value between 1 and days.
        return days * RATE_PER_DAY;
    }
}

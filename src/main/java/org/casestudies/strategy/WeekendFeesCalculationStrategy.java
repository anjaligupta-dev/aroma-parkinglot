package org.casestudies.strategy;

import org.casestudies.exception.ParkingLotException;
import org.casestudies.model.Ticket;

import java.time.*;

public class WeekendFeesCalculationStrategy implements FeesCalculationStrategy {
    private static final double WEEKEND_RATE_PER_HOUR = 100.0;

    @Override
    public double calculateFees(Ticket ticket) {
        if (ticket == null || ticket.getEntryTime() == null) {
            throw new ParkingLotException("Ticket entry time is required to calculate fees");
        }

        LocalDateTime entryTime = ticket.getEntryTime();
        LocalDateTime currentTime = LocalDateTime.now();

        DayOfWeek dayOfWeek = entryTime.getDayOfWeek();

        if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {

            throw new ParkingLotException("Weekend fee calculation is applicable only on weekends");
        }

        long durationHours = Duration.between(entryTime, currentTime).toHours();
        long hours = Math.max(1, durationHours);     // choose the larger value between 1 and hours.
        return hours * WEEKEND_RATE_PER_HOUR;
    }
}

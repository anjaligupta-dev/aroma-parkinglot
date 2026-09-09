package org.casestudies.test;

import org.casestudies.model.Ticket;
import org.casestudies.exception.ParkingLotException;
import org.casestudies.strategy.DailyFeesCalculationStrategy;


import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class DailyFeesCalculationStrategyTest {
    private final DailyFeesCalculationStrategy strategy = new DailyFeesCalculationStrategy();

    @Test
    public void shouldCalculateFeeForTwoDays() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setEntryTime(LocalDateTime.now().minusDays(2));

        // Act
        double amount = strategy.calculateFees(ticket);

        // Assert
        assertEquals(1000.0, amount);
    }

    @Test
    public void shouldChargeMinimumOneDay() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setEntryTime(LocalDateTime.now().minusHours(5));

        // Act
        double amount = strategy.calculateFees(ticket);

        // Assert
        assertEquals(500.0, amount);
    }

    @Test
    public void shouldRejectTicketWithoutEntryTime() {
        // Arrange
        Ticket ticket = new Ticket();

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> strategy.calculateFees(ticket));

        assertEquals("Ticket entry time is required to calculate fees", exception.getMessage());
    }

    @Test
    public void shouldRejectNullTicket() {
        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> strategy.calculateFees(null));

        assertEquals("Ticket entry time is required to calculate fees", exception.getMessage());
    }
}

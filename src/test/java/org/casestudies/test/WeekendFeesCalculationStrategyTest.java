package org.casestudies.test;

import org.casestudies.model.Ticket;
import org.casestudies.exception.ParkingLotException;
import org.casestudies.strategy.WeekendFeesCalculationStrategy;


import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class WeekendFeesCalculationStrategyTest {
    private final WeekendFeesCalculationStrategy strategy = new WeekendFeesCalculationStrategy();

    @Test
    public void shouldCalculateWeekendFee() {
        // Arrange
        Ticket ticket = new Ticket();

        LocalDateTime saturday = LocalDateTime.of(2026, 9, 5, 10, 0);

        ticket.setEntryTime(saturday);

        // Act
        double amount = strategy.calculateFees(ticket);

        // Assert
        assertTrue(amount >= 100.0);
    }

    @Test
    public void shouldRejectWeekdayTicket() {
        // Arrange
        Ticket ticket = new Ticket();

        LocalDateTime monday = LocalDateTime.of(2026, 9, 7, 10, 0);

        ticket.setEntryTime(monday);

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> strategy.calculateFees(ticket));

        assertEquals("Weekend fee calculation is applicable only on weekends", exception.getMessage());
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

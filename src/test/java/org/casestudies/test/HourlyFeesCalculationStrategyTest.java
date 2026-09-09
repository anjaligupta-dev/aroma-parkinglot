package org.casestudies.test;

import org.casestudies.model.Ticket;
import org.casestudies.exception.ParkingLotException;
import org.casestudies.strategy.HourlyFeesCalculationStrategy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class HourlyFeesCalculationStrategyTest {
    private final HourlyFeesCalculationStrategy strategy = new HourlyFeesCalculationStrategy();

    @Test
    public void shouldCalculateFeeForThreeHours() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setEntryTime(LocalDateTime.now().minusHours(3));

        // Act
        double amount = strategy.calculateFees(ticket);

        // Assert
        assertEquals(150.0, amount);
    }

    @Test
    public void shouldChargeMinimumOneHour() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setEntryTime(LocalDateTime.now().minusMinutes(30));

        // Act
        double amount = strategy.calculateFees(ticket);

        // Assert
        assertEquals(50.0, amount);
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

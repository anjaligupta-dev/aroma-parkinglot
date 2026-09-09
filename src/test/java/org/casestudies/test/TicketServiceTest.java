package org.casestudies.test;

import org.casestudies.enums.*;
import org.casestudies.model.*;
import org.casestudies.service.*;
import org.casestudies.strategy.*;
import org.casestudies.repository.*;
import org.casestudies.exception.ParkingLotException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class TicketServiceTest {
    private TicketService ticketService;
    private ParkingLotService parkingLotService;
    private ParkingLotRepository parkingLotRepository;
    private TicketRepository ticketRepository;

    @BeforeEach
    public void setUp() {
        parkingLotRepository = new ParkingLotRepository();

        parkingLotService = new ParkingLotService(parkingLotRepository);

        ticketRepository = new TicketRepository();

        ticketService = new TicketService(ticketRepository, new FirstAvailableSpotAssignmentStrategy(), parkingLotRepository, new HourlyFeesCalculationStrategy());
    }


    // Ticket Generation Tests
    @Test
    public void shouldGenerateTicket() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL-01-AB-1234");
        vehicle.setVehicleType(VehicleType.CAR);

        EntryGate entryGate = parkingLot.getEntryGates().get(0);

        // Act
        Ticket ticket = ticketService.generateTicket(vehicle, entryGate, SpotType.COMPACT, parkingLot.getId());

        // Assert
        assertNotNull(ticket);
        assertNotNull(ticket.getId());

        assertEquals(vehicle, ticket.getVehicle());
        assertEquals(parkingLot, ticket.getParkingLot());
        assertEquals(entryGate, ticket.getEntryGate());

        assertNotNull(ticket.getParkingSpot());
        assertEquals(SpotType.COMPACT, ticket.getParkingSpot().getSpotType());

        assertEquals(ParkingSpotStatus.OCCUPIED, ticket.getParkingSpot().getParkingSpotStatus());

        assertNotNull(ticket.getEntryTime());
    }

    @Test
    public void shouldRejectNonExistentParkingLot() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL-01-AB-1234");
        vehicle.setVehicleType(VehicleType.CAR);

        EntryGate entryGate = new EntryGate(1);
        entryGate.setGateStatus(GateStatus.OPEN);

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> ticketService.generateTicket(vehicle, entryGate, SpotType.COMPACT, 999L));

        assertEquals("Parking lot not found", exception.getMessage());
    }

    @Test
    public void shouldRejectInvalidEntryGate() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL-01-AB-1234");
        vehicle.setVehicleType(VehicleType.CAR);

        EntryGate invalidEntryGate = new EntryGate(2);
        invalidEntryGate.setGateStatus(GateStatus.OPEN);

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> ticketService.generateTicket(vehicle, invalidEntryGate, SpotType.COMPACT, parkingLot.getId()));

        assertEquals("Entry gate does not belong to parking lot", exception.getMessage());
    }

    @Test
    public void shouldRejectClosedEntryGate() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        EntryGate entryGate = parkingLot.getEntryGates().get(0);

        entryGate.setGateStatus(GateStatus.CLOSED);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL-01-AB-1234");
        vehicle.setVehicleType(VehicleType.CAR);

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> ticketService.generateTicket(vehicle, entryGate, SpotType.COMPACT, parkingLot.getId()));

        assertEquals("Entry gate is closed", exception.getMessage());
    }

    @Test
    public void shouldRejectWhenNoSuitableSpotIsAvailable() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 2);

        EntryGate entryGate = parkingLot.getEntryGates().get(0);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL-01-AB-1234");
        vehicle.setVehicleType(VehicleType.CAR);

        for (ParkingFloor floor : parkingLot.getParkingFloors()) {
            for (ParkingSpot spot : floor.getParkingSpots()) {
                spot.setParkingSpotStatus(ParkingSpotStatus.OCCUPIED);
            }
        }

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> ticketService.generateTicket(vehicle, entryGate, SpotType.COMPACT, parkingLot.getId()));

        assertEquals("No available parking spot found for requested type", exception.getMessage());
    }

    @Test
    public void shouldRejectNullVehicle() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        EntryGate entryGate = parkingLot.getEntryGates().get(0);

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> ticketService.generateTicket(null, entryGate, SpotType.COMPACT, parkingLot.getId()));

        assertEquals("Vehicle details are required", exception.getMessage());
    }

    @Test
    public void shouldRejectVehicleWithoutLicensePlate() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType(VehicleType.CAR);

        EntryGate entryGate = parkingLot.getEntryGates().get(0);

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> ticketService.generateTicket(vehicle, entryGate, SpotType.COMPACT, parkingLot.getId()));

        assertEquals("Vehicle license plate is required", exception.getMessage());
    }


    // Exit / Billing Tests
    @Test
    public void shouldProcessExitSuccessfully() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Vehicle vehicle = createVehicle();

        EntryGate entryGate = parkingLot.getEntryGates().get(0);

        Ticket ticket = ticketService.generateTicket(vehicle, entryGate, SpotType.COMPACT, parkingLot.getId());

        ticket.setEntryTime(LocalDateTime.now().minusHours(3));

        // Act
        Bill bill = ticketService.processExit(ticket.getId());

        // Assert
        assertNotNull(bill);
        assertEquals(ticket, bill.getTicket());
        assertEquals(150.0, bill.getAmount());

        assertEquals(ParkingSpotStatus.AVAILABLE, ticket.getParkingSpot().getParkingSpotStatus());
    }

    @Test
    public void shouldRejectNullTicketIdForExit() {
        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> ticketService.processExit(null));

        assertEquals("Ticket id is required", exception.getMessage());
    }

    @Test
    public void shouldRejectNonExistentTicketForExit() {
        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> ticketService.processExit(999L));

        assertEquals("Ticket not found", exception.getMessage());
    }

    @Test
    public void shouldReleaseParkingSpotAfterExit() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Ticket ticket = ticketService.generateTicket(createVehicle(), parkingLot.getEntryGates().get(0), SpotType.COMPACT, parkingLot.getId());

        assertEquals(ParkingSpotStatus.OCCUPIED, ticket.getParkingSpot().getParkingSpotStatus());

        // Act
        ticketService.processExit(ticket.getId());

        // Assert
        assertEquals(ParkingSpotStatus.AVAILABLE, ticket.getParkingSpot().getParkingSpotStatus());
    }

    @Test
    public void shouldCreateBillWithCorrectTicket() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Ticket ticket = ticketService.generateTicket(createVehicle(), parkingLot.getEntryGates().get(0), SpotType.COMPACT, parkingLot.getId());

        ticket.setEntryTime(LocalDateTime.now().minusHours(2));

        // Act
        Bill bill = ticketService.processExit(ticket.getId());

        // Assert
        assertNotNull(bill);
        assertEquals(ticket, bill.getTicket());
    }

    @Test
    public void shouldCalculateCorrectHourlyAmount() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Ticket ticket = ticketService.generateTicket(createVehicle(), parkingLot.getEntryGates().get(0), SpotType.COMPACT, parkingLot.getId());

        ticket.setEntryTime(LocalDateTime.now().minusHours(4));

        // Act
        Bill bill = ticketService.processExit(ticket.getId());

        // Assert
        assertEquals(200.0, bill.getAmount());
    }

    @Test
    public void shouldUseDailyFeeStrategy() {
        // Arrange
        TicketService dailyTicketService = new TicketService(ticketRepository, new FirstAvailableSpotAssignmentStrategy(), parkingLotRepository, new DailyFeesCalculationStrategy());

        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Ticket ticket = dailyTicketService.generateTicket(createVehicle(), parkingLot.getEntryGates().get(0), SpotType.COMPACT, parkingLot.getId());

        ticket.setEntryTime(LocalDateTime.now().minusDays(2));

        // Act
        Bill bill = dailyTicketService.processExit(ticket.getId());

        // Assert
        assertEquals(1000.0, bill.getAmount());
    }

    private Vehicle createVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL-01-AB-1234");
        vehicle.setVehicleType(VehicleType.CAR);
        return vehicle;
    }
}

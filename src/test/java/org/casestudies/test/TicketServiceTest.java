package org.casestudies.test;

import org.casestudies.enums.*;
import org.casestudies.exception.ParkingLotException;
import org.casestudies.model.EntryGate;
import org.casestudies.model.*;
import org.casestudies.repository.*;
import org.casestudies.service.*;
import org.casestudies.strategy.FirstAvailableSpotAssignmentStrategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;


public class TicketServiceTest {
    private TicketService ticketService;
    private ParkingLotService parkingLotService;


    @BeforeEach
    public void setUp() {
        ParkingLotRepository parkingLotRepository = new ParkingLotRepository();
        parkingLotService = new ParkingLotService(parkingLotRepository);

        TicketRepository ticketRepository = new TicketRepository();
        ticketService = new TicketService(ticketRepository, new FirstAvailableSpotAssignmentStrategy(), parkingLotRepository);
    }


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
}

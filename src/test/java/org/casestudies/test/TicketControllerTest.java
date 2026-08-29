package org.casestudies.test;

import org.casestudies.dtos.*;
import org.casestudies.enums.*;
import org.casestudies.model.*;
import org.casestudies.repository.*;
import org.casestudies.service.*;
import org.casestudies.controller.TicketController;
import org.casestudies.strategy.FirstAvailableSpotAssignmentStrategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;


public class TicketControllerTest {
    private TicketController ticketController;
    private ParkingLotService parkingLotService;


    @BeforeEach
    public void setUp() {
        ParkingLotRepository parkingLotRepository = new ParkingLotRepository();
        parkingLotService = new ParkingLotService(parkingLotRepository);

        TicketRepository ticketRepository = new TicketRepository();
        TicketService ticketService = new TicketService(ticketRepository, new FirstAvailableSpotAssignmentStrategy(), parkingLotRepository);
        ticketController = new TicketController(ticketService);
    }


    @Test
    public void generateTicketShouldSucceed() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL-01-AB-1234");
        vehicle.setVehicleType(VehicleType.CAR);

        EntryGate entryGate = parkingLot.getEntryGates().get(0);

        GenerateTicketRequestDto request = new GenerateTicketRequestDto();

        request.setParkingLotId(parkingLot.getId());
        request.setEntryGate(entryGate);
        request.setVehicle(vehicle);
        request.setSpotType(SpotType.COMPACT);


        // Act
        GenerateTicketResponseDto response = ticketController.generateTicket(request);


        // Assert
        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertNotNull(response.getTicket());
        assertEquals(vehicle, response.getTicket().getVehicle());
        assertNotNull(response.getTicket().getParkingSpot());
    }


    @Test
    public void generateTicketWithNullRequestShouldFail() {
        // Act
        GenerateTicketResponseDto response = ticketController.generateTicket(null);


        // Assert
        assertEquals(ResponseStatus.FAILURE, response.getResponseStatus());
        assertEquals("Request cannot be null", response.getErrorMessage());
        assertNull(response.getTicket());
    }


    @Test
    public void generateTicketWithInvalidParkingLotShouldFail() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL-01-AB-1234");
        vehicle.setVehicleType(VehicleType.CAR);

        EntryGate entryGate = new EntryGate(1);
        entryGate.setGateStatus(org.casestudies.enums.GateStatus.OPEN);

        GenerateTicketRequestDto request = new GenerateTicketRequestDto();

        request.setParkingLotId(999L);
        request.setEntryGate(entryGate);
        request.setVehicle(vehicle);
        request.setSpotType(SpotType.COMPACT);


        // Act
        GenerateTicketResponseDto response = ticketController.generateTicket(request);


        // Assert
        assertEquals(ResponseStatus.FAILURE, response.getResponseStatus());
        assertNotNull(response.getErrorMessage());
        assertNull(response.getTicket());
    }


    @Test
    public void generateTicketWithNullVehicleShouldFail() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        GenerateTicketRequestDto request = new GenerateTicketRequestDto();

        request.setParkingLotId(parkingLot.getId());
        request.setEntryGate(parkingLot.getEntryGates().get(0));
        request.setVehicle(null);
        request.setSpotType(SpotType.COMPACT);


        // Act
        GenerateTicketResponseDto response = ticketController.generateTicket(request);


        // Assert
        assertEquals(ResponseStatus.FAILURE, response.getResponseStatus());

        assertNotNull(response.getErrorMessage());
        assertNull(response.getTicket());
    }
}

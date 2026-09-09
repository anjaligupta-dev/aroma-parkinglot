package org.casestudies.test;

import org.casestudies.dtos.*;
import org.casestudies.enums.*;
import org.casestudies.model.*;
import org.casestudies.service.*;
import org.casestudies.strategy.*;
import org.casestudies.repository.*;
import org.casestudies.controller.*;


import java.time.LocalDateTime;

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

        TicketService ticketService = new TicketService(ticketRepository, new FirstAvailableSpotAssignmentStrategy(), parkingLotRepository, new HourlyFeesCalculationStrategy());

        ticketController = new TicketController(ticketService);
    }


    // Ticket Generation Tests
    @Test
    public void generateTicketShouldSucceed() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Vehicle vehicle = createVehicle();

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
        Vehicle vehicle = createVehicle();

        EntryGate entryGate = new EntryGate(1);
        entryGate.setGateStatus(GateStatus.OPEN);

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


    // Exit / Billing Tests
    @Test
    public void processExitShouldSucceed() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Vehicle vehicle = createVehicle();

        GenerateTicketRequestDto ticketRequest = createTicketRequest(parkingLot, vehicle);

        GenerateTicketResponseDto ticketResponse = ticketController.generateTicket(ticketRequest);

        Ticket ticket = ticketResponse.getTicket();

        ticket.setEntryTime(LocalDateTime.now().minusHours(2));

        ProcessExitRequestDto exitRequest = new ProcessExitRequestDto();

        exitRequest.setTicketId(ticket.getId());

        // Act
        ProcessExitResponseDto response = ticketController.processExit(exitRequest);

        // Assert
        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());

        assertNotNull(response.getBill());

        assertEquals(ticket, response.getBill().getTicket());

        assertEquals(100.0, response.getBill().getAmount());
    }

    @Test
    public void processExitWithNullRequestShouldFail() {
        // Act
        ProcessExitResponseDto response = ticketController.processExit(null);

        // Assert
        assertEquals(ResponseStatus.FAILURE, response.getResponseStatus());

        assertEquals("Request cannot be null", response.getErrorMessage());

        assertNull(response.getBill());
    }

    @Test
    public void processExitWithInvalidTicketShouldFail() {
        // Arrange
        ProcessExitRequestDto request = new ProcessExitRequestDto();

        request.setTicketId(999L);

        // Act
        ProcessExitResponseDto response = ticketController.processExit(request);

        // Assert
        assertEquals(ResponseStatus.FAILURE, response.getResponseStatus());

        assertEquals("Ticket not found", response.getErrorMessage());

        assertNull(response.getBill());
    }

    @Test
    public void processExitWithNullTicketIdShouldFail() {
        // Arrange
        ProcessExitRequestDto request = new ProcessExitRequestDto();

        request.setTicketId(null);

        // Act
        ProcessExitResponseDto response = ticketController.processExit(request);

        // Assert
        assertEquals(ResponseStatus.FAILURE, response.getResponseStatus());

        assertEquals("Ticket id is required", response.getErrorMessage());

        assertNull(response.getBill());
    }

    @Test
    public void processExitShouldReturnCorrectBillAmount() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        Ticket ticket = ticketController.generateTicket(createTicketRequest(parkingLot, createVehicle())).getTicket();

        ticket.setEntryTime(LocalDateTime.now().minusHours(3));

        ProcessExitRequestDto request = new ProcessExitRequestDto();

        request.setTicketId(ticket.getId());

        // Act
        ProcessExitResponseDto response = ticketController.processExit(request);

        // Assert
        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());

        assertNotNull(response.getBill());

        assertEquals(150.0, response.getBill().getAmount());
    }

    private Vehicle createVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL-01-AB-1234");
        vehicle.setVehicleType(VehicleType.CAR);
        return vehicle;
    }

    private GenerateTicketRequestDto createTicketRequest(ParkingLot parkingLot, Vehicle vehicle) {
        GenerateTicketRequestDto request = new GenerateTicketRequestDto();

        request.setParkingLotId(parkingLot.getId());
        request.setEntryGate(parkingLot.getEntryGates().get(0));
        request.setVehicle(vehicle);
        request.setSpotType(SpotType.COMPACT);

        return request;
    }
}

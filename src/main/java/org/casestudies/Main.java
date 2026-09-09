package org.casestudies;

import org.casestudies.dtos.*;
import org.casestudies.enums.*;
import org.casestudies.model.*;
import org.casestudies.service.*;
import org.casestudies.strategy.*;
import org.casestudies.repository.*;
import org.casestudies.controller.*;


public class Main {
    public static void main(String[] args) {
        // Create Parking Lot
        ParkingLotRepository repository = new ParkingLotRepository();
        ParkingLotService service = new ParkingLotService(repository);
        ParkingLotController controller = new ParkingLotController(service);

        CreateParkingLotRequestDto createRequest = new CreateParkingLotRequestDto();
        createRequest.setAddress("Delhi Airport");
        createRequest.setNumberOfFloors(3);

        CreateParkingLotResponseDto createResponse = controller.createParkingLot(createRequest);
        System.out.println("Create Response Status: " + createResponse.getResponseStatus());

        if (createResponse.getResponseStatus() != ResponseStatus.SUCCESS) {
            System.out.println("Failed to create parking lot: " + createResponse.getErrorMessage());
            return;
        }

        System.out.println("Parking lot created at: " + createResponse.getParkingLot().getAddress());
        System.out.println("Parking lot ID: " + createResponse.getParkingLot().getId());


        // Update Parking Lot
        UpdateParkingLotRequestDto updateRequest = new UpdateParkingLotRequestDto();
        updateRequest.setParkingLotId(createResponse.getParkingLot().getId());
        updateRequest.setAddress("Noida");

        UpdateParkingLotResponseDto updateResponse = controller.updateAddress(updateRequest);
        System.out.println("Update Response Status: " + updateResponse.getResponseStatus());

        if (updateResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
            System.out.println("Updated address: " + updateResponse.getParkingLot().getAddress());
        } else {
            System.out.println("Failed to update address: " + updateResponse.getErrorMessage());
        }


        // Ticket Repository
        TicketRepository ticketRepository = new TicketRepository();

        // Configure Ticket Service
        TicketService ticketService = new TicketService(ticketRepository, new FirstAvailableSpotAssignmentStrategy(), repository, new HourlyFeesCalculationStrategy());

        TicketController ticketController = new TicketController(ticketService);

        // Create Vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL01AB1234");
        vehicle.setVehicleType(VehicleType.CAR);

        ParkingLot parkingLot = createResponse.getParkingLot();

        EntryGate entryGate = parkingLot.getEntryGates().get(0);

        // Generate Ticket
        GenerateTicketRequestDto ticketRequest = new GenerateTicketRequestDto();

        ticketRequest.setParkingLotId(parkingLot.getId());
        ticketRequest.setEntryGate(entryGate);
        ticketRequest.setVehicle(vehicle);
        ticketRequest.setSpotType(SpotType.COMPACT);

        GenerateTicketResponseDto ticketResponse = ticketController.generateTicket(ticketRequest);

        if (ticketResponse.getResponseStatus() != ResponseStatus.SUCCESS) {
            System.out.println("Ticket generation failed: " + ticketResponse.getErrorMessage());
            return;
        }

        Ticket ticket = ticketResponse.getTicket();

        System.out.println();
        System.out.println("Ticket generated successfully");
        System.out.println("Ticket ID: " + ticket.getId());
        System.out.println("Parking Spot: " + ticket.getParkingSpot().getId());
        System.out.println("Entry Time: " + ticket.getEntryTime());


        // Process Exit
        ProcessExitRequestDto exitRequest = new ProcessExitRequestDto();

        exitRequest.setTicketId(ticket.getId());

        ProcessExitResponseDto exitResponse = ticketController.processExit(exitRequest);

        if (exitResponse.getResponseStatus() != ResponseStatus.SUCCESS) {
            System.out.println("Exit processing failed: " + exitResponse.getErrorMessage());
            return;
        }

        // Generate Bill
        Bill bill = exitResponse.getBill();

        System.out.println();
        System.out.println("Vehicle exit processed successfully");
        System.out.println("Ticket ID: " + ticket.getId());
        System.out.println("Bill Amount: " + bill.getAmount());
        System.out.println("Parking Spot Status: " + ticket.getParkingSpot().getParkingSpotStatus());
    }
}


/* *

    Output :
        Create Response Status: SUCCESS
        Parking lot created at: Delhi Airport
        Parking lot ID: 1
        Update Response Status: SUCCESS
        Updated address: Noida

        Ticket generated successfully
        Ticket ID: 1
        Parking Spot: null
        Entry Time: 2026-09-07T13:16:33.397898600

        Vehicle exit processed successfully
        Ticket ID: 1
        Bill Amount: 50.0
        Parking Spot Status: AVAILABLE

* */
package org.casestudies;


import org.casestudies.dtos.*;
import org.casestudies.enums.*;
import org.casestudies.service.*;
import org.casestudies.repository.*;
import org.casestudies.controller.*;
import org.casestudies.model.Vehicle;
import org.casestudies.strategy.FirstAvailableSpotAssignmentStrategy;


public class Main {
    public static void main(String[] args) {
        ParkingLotRepository repository = new ParkingLotRepository();
        ParkingLotService service = new ParkingLotService(repository);
        ParkingLotController controller = new ParkingLotController(service);


        CreateParkingLotRequestDto createRequest = new CreateParkingLotRequestDto();
        createRequest.setAddress("Delhi Airport");
        createRequest.setNumberOfFloors(3);

        CreateParkingLotResponseDto createResponse = controller.createParkingLot(createRequest);
        System.out.println("Create Response Status: " + createResponse.getResponseStatus());

        if (createResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
            System.out.println("Parking lot created at: " + createResponse.getParkingLot().getAddress());
            System.out.println("Parking lot ID: " + createResponse.getParkingLot().getId());
        } else {
            System.out.println("Failed to create parking lot: " + createResponse.getErrorMessage());
            return;
        }


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

        System.out.println();



        // Ticket dependencies
        TicketRepository ticketRepository = new TicketRepository();
        FirstAvailableSpotAssignmentStrategy spotAssignmentStrategy = new FirstAvailableSpotAssignmentStrategy();
        TicketService ticketService = new TicketService(ticketRepository, spotAssignmentStrategy, repository);
        TicketController ticketController = new TicketController(ticketService);


        // Create vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("DL-01-AB-1234");
        vehicle.setVehicleType(VehicleType.CAR);


        // Generate ticket request
        GenerateTicketRequestDto ticketRequest = new GenerateTicketRequestDto();
        ticketRequest.setParkingLotId(createResponse.getParkingLot().getId());
        ticketRequest.setEntryGate(createResponse.getParkingLot().getEntryGates().get(0));
        ticketRequest.setVehicle(vehicle);
        ticketRequest.setSpotType(SpotType.COMPACT);


        // Generate ticket
        GenerateTicketResponseDto ticketResponse = ticketController.generateTicket(ticketRequest);
        System.out.println("Ticket Response Status: " + ticketResponse.getResponseStatus());

        if (ticketResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
            System.out.println("Ticket ID: " + ticketResponse.getTicket().getId());
            System.out.println("Vehicle: " + ticketResponse.getTicket().getVehicle().getLicensePlate());
            System.out.println("Assigned Spot Type: " + ticketResponse.getTicket().getParkingSpot().getSpotType());
            System.out.println("Assigned Spot Status: " + ticketResponse.getTicket().getParkingSpot().getParkingSpotStatus());

        } else {
            System.out.println("Failed to generate ticket: " + ticketResponse.getErrorMessage());
        }
    }
}


/* *

    Output :
        Create Response Status: SUCCESS
        Parking lot created at: Delhi Airport
        Parking lot ID: 1
        Update Response Status: SUCCESS
        Updated address: Noida

        Ticket Response Status: SUCCESS
        Ticket ID: 1
        Vehicle: DL-01-AB-1234
        Assigned Spot Type: COMPACT
        Assigned Spot Status: OCCUPIED

* */
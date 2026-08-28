package org.casestudies;

import org.casestudies.dtos.*;
import org.casestudies.enums.ResponseStatus;
import org.casestudies.service.ParkingLotService;
import org.casestudies.controller.ParkingLotController;
import org.casestudies.repository.ParkingLotRepository;


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
    }
}


/* *

    Output :
        Create Response Status: SUCCESS
        Parking lot created at: Delhi Airport
        Parking lot ID: 1
        Update Response Status: SUCCESS
        Updated address: Noida

* */
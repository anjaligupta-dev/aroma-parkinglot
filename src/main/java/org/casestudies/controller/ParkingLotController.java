package org.casestudies.controller;


import org.casestudies.dtos.*;
import org.casestudies.enums.ResponseStatus;
import org.casestudies.exception.ParkingLotException;
import org.casestudies.model.ParkingLot;
import org.casestudies.service.ParkingLotService;

public class ParkingLotController {
    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }


    public CreateParkingLotResponseDto createParkingLot(CreateParkingLotRequestDto request) {
        CreateParkingLotResponseDto response = new CreateParkingLotResponseDto();

        if (request == null) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setErrorMessage("Request cannot be null");
            return response;
        }

        try {
            ParkingLot parkingLot = parkingLotService.createParkingLot(request.getAddress(), request.getNumberOfFloors());
            response.setParkingLot(parkingLot);
            response.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (ParkingLotException e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setErrorMessage(e.getMessage());
        }

        return response;
    }


    public UpdateParkingLotResponseDto updateAddress(UpdateParkingLotRequestDto request) {
        UpdateParkingLotResponseDto response = new UpdateParkingLotResponseDto();

        if (request == null) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setErrorMessage("Request cannot be null");
            return response;
        }

        try {
            ParkingLot updatedParkingLot = parkingLotService.updateAddress(request.getParkingLotId(), request.getAddress());
            response.setParkingLot(updatedParkingLot);
            response.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (ParkingLotException e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setErrorMessage(e.getMessage());
        }

        return response;
    }
}
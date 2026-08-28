package org.casestudies.test;

import org.casestudies.dtos.*;
import org.casestudies.enums.ResponseStatus;
import org.casestudies.service.ParkingLotService;
import org.casestudies.controller.ParkingLotController;
import org.casestudies.repository.ParkingLotRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;


public class ParkingLotControllerTest {
    private ParkingLotController parkingLotController;

    @BeforeEach
    public void setUp() {
        ParkingLotRepository parkingLotRepository = new ParkingLotRepository();
        ParkingLotService parkingLotService = new ParkingLotService(parkingLotRepository);
        parkingLotController = new ParkingLotController(parkingLotService);
    }


    @Test
    void createParkingLotShouldSucceed() {
        // Arrange
        CreateParkingLotRequestDto request = new CreateParkingLotRequestDto();
        String address = "New Delhi";
        request.setAddress(address);
        request.setNumberOfFloors(3);

        // Act
        CreateParkingLotResponseDto response = parkingLotController.createParkingLot(request);

        // Assert
        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertNotNull(response.getParkingLot());
        assertEquals(address, response.getParkingLot().getAddress());
    }


    @Test
    void createParkingLotWithBlankAddressShouldFail() {
        // Arrange
        CreateParkingLotRequestDto request = new CreateParkingLotRequestDto();
        request.setAddress("");
        request.setNumberOfFloors(3);

        // Act
        CreateParkingLotResponseDto response = parkingLotController.createParkingLot(request);

        // Assert
        assertEquals(ResponseStatus.FAILURE, response.getResponseStatus());
        assertNotNull(response.getErrorMessage());
        assertNull(response.getParkingLot());
    }


    @Test
    void createParkingLotWithNullRequestShouldFail() {
        // Arrange
        CreateParkingLotRequestDto request = null;

        // Act
        CreateParkingLotResponseDto response = parkingLotController.createParkingLot(request);

        // Assert
        assertEquals(ResponseStatus.FAILURE, response.getResponseStatus());
        assertEquals("Request cannot be null", response.getErrorMessage());
        assertNull(response.getParkingLot());
    }


    @Test
    void updateAddressShouldSucceed() {
        // Arrange
        CreateParkingLotRequestDto createRequest = new CreateParkingLotRequestDto();

        String address = "New Delhi";
        createRequest.setAddress(address);
        createRequest.setNumberOfFloors(3);

        CreateParkingLotResponseDto createResponse = parkingLotController.createParkingLot(createRequest);

        UpdateParkingLotRequestDto updateRequest = new UpdateParkingLotRequestDto();

        Long parkingLotId = createResponse.getParkingLot().getId();

        String updatedAddress = "Noida";

        updateRequest.setParkingLotId(parkingLotId);
        updateRequest.setAddress(updatedAddress);

        // Act
        UpdateParkingLotResponseDto response = parkingLotController.updateAddress(updateRequest);

        // Assert
        assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
        assertNotNull(response.getParkingLot());
        assertEquals(updatedAddress, response.getParkingLot().getAddress());
    }


    @Test
    void updateAddressForNonExistingParkingLotShouldFail() {
        // Arrange
        UpdateParkingLotRequestDto requestDto = new UpdateParkingLotRequestDto();

        requestDto.setParkingLotId(999L);
        requestDto.setAddress("Noida");

        // Act
        UpdateParkingLotResponseDto responseDto = parkingLotController.updateAddress(requestDto);

        // Assert
        assertEquals(ResponseStatus.FAILURE, responseDto.getResponseStatus());
        assertNotNull(responseDto.getErrorMessage());
        assertNull(responseDto.getParkingLot());
    }


    @Test
    void updateAddressWithNullRequestShouldFail() {
        // Arrange
        UpdateParkingLotRequestDto request = null;

        // Act
        UpdateParkingLotResponseDto response = parkingLotController.updateAddress(request);

        // Assert
        assertEquals(ResponseStatus.FAILURE, response.getResponseStatus());
        assertEquals("Request cannot be null", response.getErrorMessage());
        assertNull(response.getParkingLot());
    }
}

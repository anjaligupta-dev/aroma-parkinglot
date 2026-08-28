package org.casestudies.test;

import org.casestudies.enums.*;
import org.casestudies.model.*;
import org.casestudies.service.ParkingLotService;
import org.casestudies.exception.ParkingLotException;
import org.casestudies.repository.ParkingLotRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;


public class ParkingLotServiceTest {
    private ParkingLotService parkingLotService;

    @BeforeEach
    public void setUp() {
        ParkingLotRepository parkingLotRepository = new ParkingLotRepository();

        parkingLotService = new ParkingLotService(parkingLotRepository);
    }


    @Test
    public void shouldCreateParkingLot() {
        // Arrange
        String address = "New Delhi";
        int numberOfFloors = 3;

        // Act
        ParkingLot parkingLot = parkingLotService.createParkingLot(address, numberOfFloors);

        // Assert
        assertNotNull(parkingLot);
        assertNotNull(parkingLot.getId());

        assertEquals(address, parkingLot.getAddress());
        assertEquals(numberOfFloors, parkingLot.getParkingFloors().size());

        assertEquals(1, parkingLot.getEntryGates().size());
        assertEquals(1, parkingLot.getExitGates().size());

        assertEquals(GateStatus.OPEN, parkingLot.getEntryGates().get(0).getGateStatus());
        assertEquals(GateStatus.OPEN, parkingLot.getExitGates().get(0).getGateStatus());

        for (ParkingFloor parkingFloor : parkingLot.getParkingFloors()) {
            assertEquals(10, parkingFloor.getParkingSpots().size());

            for (ParkingSpot parkingSpot : parkingFloor.getParkingSpots()) {
                assertTrue(parkingSpot.isAvailable());
                assertEquals(SpotType.COMPACT, parkingSpot.getSpotType());
            }
        }
    }


    @Test
    public void shouldUpdateAddress() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        // Act
        ParkingLot updatedParkingLot = parkingLotService.updateAddress(parkingLot.getId(), "Noida");

        // Assert
        assertEquals("Noida", updatedParkingLot.getAddress());
        assertEquals(parkingLot.getId(), updatedParkingLot.getId());
    }


    @Test
    public void shouldRejectBlankAddress() {
        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> parkingLotService.createParkingLot("", 3));

        assertEquals("Parking lot address cannot be blank", exception.getMessage());
    }


    @Test
    public void shouldRejectNullAddress() {
        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> parkingLotService.createParkingLot(null, 3));

        assertEquals("Parking lot address cannot be blank", exception.getMessage());
    }


    @Test
    public void shouldRejectLessThanTwoFloors() {
        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> parkingLotService.createParkingLot("New Delhi", 1));

        assertEquals("Parking lot must have at least 2 floors", exception.getMessage());
    }


    @Test
    public void shouldRejectNullParkingLotId() {
        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> parkingLotService.updateAddress(null, "Noida"));

        assertEquals("Parking lot id is required", exception.getMessage());
    }


    @Test
    public void shouldRejectNullAddressDuringUpdate() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> parkingLotService.updateAddress(parkingLot.getId(), null));

        assertEquals("Address cannot be blank", exception.getMessage());
    }


    @Test
    public void shouldRejectNonExistentParkingLot() {
        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> parkingLotService.updateAddress(10L, "Noida"));

        assertEquals("Parking lot not found", exception.getMessage());
    }


    @Test
    public void shouldRejectBlankAddressDuringUpdate() {
        // Arrange
        ParkingLot parkingLot = parkingLotService.createParkingLot("New Delhi", 3);

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> parkingLotService.updateAddress(parkingLot.getId(), ""));

        assertEquals("Address cannot be blank", exception.getMessage());
    }
}

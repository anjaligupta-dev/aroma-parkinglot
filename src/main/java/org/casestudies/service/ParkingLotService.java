package org.casestudies.service;

import org.casestudies.enums.*;
import org.casestudies.model.*;
import org.casestudies.exception.ParkingLotException;
import org.casestudies.repository.ParkingLotRepository;

import java.util.*;

public class ParkingLotService {
    private static final int SPOTS_PER_FLOOR = 10;  // 10 Parking spots initially created on each floor
    private final ParkingLotRepository parkingLotRepository;

    public ParkingLotService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    public ParkingLot createParkingLot(String address, int numberOfFloors) {
        // Parking lot should have a valid address
        if (address == null || address.trim().isEmpty()) {
            throw new ParkingLotException("Parking lot address cannot be blank");
        }

        // Parking lot must have at least 2 floors
        if (numberOfFloors < 2) {
            throw new ParkingLotException("Parking lot must have at least 2 floors");
        }

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setAddress(address.trim());

        for (int i = 1; i <= numberOfFloors; i++) {
            ParkingFloor floor = new ParkingFloor(i);
            for (int j = 0; j < SPOTS_PER_FLOOR; j++) {
                floor.addSpot(new ParkingSpot(SpotType.COMPACT));
            }
            parkingLot.addFloor(floor);
        }

        EntryGate entryGate = new EntryGate(1);
        entryGate.setGateStatus(GateStatus.OPEN);
        parkingLot.addEntryGate(entryGate);

        ExitGate exitGate = new ExitGate(1);
        exitGate.setGateStatus(GateStatus.OPEN);
        parkingLot.addExitGate(exitGate);

        return parkingLotRepository.save(parkingLot);
    }

    public ParkingLot updateAddress(Long id, String newAddress) {
        // Validate parking lot ID
        if (id == null) {
            throw new ParkingLotException("Parking lot id is required");
        }

        // Parking lot should have a valid address
        if (newAddress == null || newAddress.trim().isEmpty()) {
            throw new ParkingLotException("Address cannot be blank");
        }

        ParkingLot parkingLot = parkingLotRepository.getById(id);

        if (parkingLot == null) {
            throw new ParkingLotException("Parking lot not found");
        }

        // Update the address of the existing parking lot
        parkingLot.setAddress(newAddress.trim());

        return parkingLotRepository.update(id, parkingLot);
    }
}

package org.casestudies.repository;

import org.casestudies.model.ParkingLot;

import java.util.*;

public class ParkingLotRepository {
    // Used to generate a unique ID for every new parking lot.
    private Long nextId = 0L;

    // In-memory storage for parking lots.
    private Map<Long, ParkingLot> parkingLotsMap = new HashMap<>();

    // Saves a new parking lot
    public ParkingLot save(ParkingLot parkingLot) {
        Long id = ++nextId;    // Generate a new ID.
        parkingLot.setId(id);
        parkingLotsMap.put(id, parkingLot);
        return parkingLot;
    }

    public ParkingLot getById(Long id) {
        return parkingLotsMap.get(id);
    }

    public ParkingLot update(Long id, ParkingLot parkingLot) {
        if (!parkingLotsMap.containsKey(id)) {
            return null;
        }
        parkingLot.setId(id);
        parkingLotsMap.put(id, parkingLot);
        return parkingLot;
    }
}

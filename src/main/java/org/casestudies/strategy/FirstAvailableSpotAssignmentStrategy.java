package org.casestudies.strategy;

import org.casestudies.model.*;
import org.casestudies.enums.SpotType;

public class FirstAvailableSpotAssignmentStrategy implements SpotAssignmentStrategy {
    @Override
    public ParkingSpot assignSpot(ParkingLot parkingLot, SpotType spotType) {
        if (parkingLot == null || parkingLot.getParkingFloors() == null || spotType == null) {
            return null;
        }

        for (ParkingFloor parkingFloor : parkingLot.getParkingFloors()) {
            if (parkingFloor.getParkingSpots() == null) {
                continue;
            }

            for (ParkingSpot parkingSpot : parkingFloor.getParkingSpots()) {
                if (parkingSpot != null && parkingSpot.isAvailable() && parkingSpot.getSpotType() == spotType) {
                    return parkingSpot;
                }
            }
        }

        return null;
    }
}
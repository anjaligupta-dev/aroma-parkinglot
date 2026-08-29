package org.casestudies.strategy;

import org.casestudies.enums.SpotType;
import org.casestudies.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSpotAssignmentStrategy implements SpotAssignmentStrategy {
    private final Random random = new Random();

    @Override
    public ParkingSpot assignSpot(ParkingLot parkingLot, SpotType spotType) {
        if (parkingLot == null || parkingLot.getParkingFloors() == null || spotType == null) {
            return null;
        }

        List<ParkingSpot> availableSpots = new ArrayList<>();

        for (ParkingFloor parkingFloor : parkingLot.getParkingFloors()) {
            if (parkingFloor.getParkingSpots() == null) {
                continue;
            }

            for (ParkingSpot parkingSpot : parkingFloor.getParkingSpots()) {
                if (parkingSpot != null && parkingSpot.isAvailable() && parkingSpot.getSpotType() == spotType) {
                    availableSpots.add(parkingSpot);
                }
            }
        }

        if (availableSpots.isEmpty()) {
            return null;
        }

        return availableSpots.get(random.nextInt(availableSpots.size()));
    }
}
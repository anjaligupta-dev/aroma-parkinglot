package org.casestudies.strategy;

import org.casestudies.model.*;
import org.casestudies.enums.SpotType;

public interface SpotAssignmentStrategy {
    ParkingSpot assignSpot(ParkingLot parkingLot, SpotType spotType);
}

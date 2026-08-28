package org.casestudies.model;

import org.casestudies.enums.SpotType;
import org.casestudies.enums.ParkingSpotStatus;

public class ParkingSpot extends BaseModel {
    private SpotType spotType;
    private ParkingSpotStatus parkingSpotStatus;

    public ParkingSpot(SpotType spotType) {
        this.spotType = spotType;
        this.parkingSpotStatus = ParkingSpotStatus.AVAILABLE;
    }

    // Getter for spot type
    public SpotType getSpotType() {
        return spotType;
    }

    // Getter Setter for parking spot status
    public ParkingSpotStatus getParkingSpotStatus() {
        return parkingSpotStatus;
    }

    public void setParkingSpotStatus(ParkingSpotStatus parkingSpotStatus) {
        this.parkingSpotStatus = parkingSpotStatus;
    }

    // To check if parking spot is available or not
    public boolean isAvailable() {
        return this.parkingSpotStatus == ParkingSpotStatus.AVAILABLE;
    }
}

package org.casestudies.model;

import java.util.*;

public class ParkingFloor extends BaseModel {
    private int floorNumber;
    private List<ParkingSpot> parkingSpots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.parkingSpots = new ArrayList<>();
    }

    // Getter Setter for floor number
    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    // Getter for parking spots
    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    // To add parking spot
    public void addSpot(ParkingSpot parkingSpot) {
        this.parkingSpots.add(parkingSpot);
    }
}

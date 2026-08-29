package org.casestudies.model;

import java.time.LocalDateTime;

public class Ticket extends BaseModel {
    private Vehicle vehicle;
    private ParkingSpot parkingSpot;
    private EntryGate entryGate;
    private ParkingLot parkingLot;
    private LocalDateTime entryTime;

    // Getter Setter for vehicle
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    // Getter Setter for parking spot
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    // Getter Setter for entry gate
    public EntryGate getEntryGate() {
        return entryGate;
    }

    public void setEntryGate(EntryGate entryGate) {
        this.entryGate = entryGate;
    }

    // Getter Setter for parking lot
    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    // Getter Setter for entry time
    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
}

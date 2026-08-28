package org.casestudies.model;

import java.util.*;

public class ParkingLot extends BaseModel {
    private String address;
    private List<ParkingFloor> parkingFloors;
    private List<EntryGate> entryGates;
    private List<ExitGate> exitGates;

    public ParkingLot() {
        this.parkingFloors = new ArrayList<>();
        this.entryGates = new ArrayList<>();
        this.exitGates = new ArrayList<>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ParkingFloor> getParkingFloors() {
        return parkingFloors;
    }

    public List<EntryGate> getEntryGates() {
        return entryGates;
    }

    public List<ExitGate> getExitGates() {
        return exitGates;
    }

    public void addFloor(ParkingFloor parkingFloor) {
        this.parkingFloors.add(parkingFloor);
    }

    public void addEntryGate(EntryGate entryGate) {
        this.entryGates.add(entryGate);
    }

    public void addExitGate(ExitGate exitGate) {
        this.exitGates.add(exitGate);
    }
}
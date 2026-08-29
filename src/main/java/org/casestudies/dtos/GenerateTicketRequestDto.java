package org.casestudies.dtos;

import org.casestudies.model.*;
import org.casestudies.enums.SpotType;

public class GenerateTicketRequestDto extends ResponseDto {
    private Vehicle vehicle;
    private EntryGate entryGate;
    private Long parkingLotId;
    private SpotType spotType;

    // Getter Setter for vehicle
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    // Getter Setter for entry gate
    public EntryGate getEntryGate() {
        return entryGate;
    }

    public void setEntryGate(EntryGate entryGate) {
        this.entryGate = entryGate;
    }

    // Getter Setter for parking lot id
    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    // Getter Setter for spot type
    public SpotType getSpotType() {
        return spotType;
    }

    public void setSpotType(SpotType spotType) {
        this.spotType = spotType;
    }
}
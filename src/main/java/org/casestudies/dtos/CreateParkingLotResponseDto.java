package org.casestudies.dtos;

import org.casestudies.model.ParkingLot;

public class CreateParkingLotResponseDto extends ResponseDto {
    private ParkingLot parkingLot;

    // Getter Setter for parking lot
    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
}

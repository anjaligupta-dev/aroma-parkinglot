package org.casestudies.dtos;

public class UpdateParkingLotRequestDto {
    private String address;
    private Long parkingLotId;

    // Getter Setter for parking lot address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter Setter for parking lot id
    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }
}

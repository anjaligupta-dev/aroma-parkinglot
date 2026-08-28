package org.casestudies.dtos;

public class CreateParkingLotRequestDto {
    private String address;
    private int numberOfFloors;

    // Getter Setter for parking lot address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter Setter for number of floors
    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }
}

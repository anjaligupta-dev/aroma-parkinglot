package org.casestudies.model;

import org.casestudies.enums.VehicleType;

public class Vehicle extends BaseModel {
    private String licensePlate;
    private VehicleType vehicleType;

    // Getter Setter for license plate
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    // Getter Setter for vehicle type
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}

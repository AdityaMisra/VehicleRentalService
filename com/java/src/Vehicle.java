/**
 * Created on 2/7/21, 7:40 PM
 * Vehicle.java
 *
 * @author aditya.misra
 */


class Vehicle {
    private String vehicleId;
    private VehicleType vehicleType;
    private String branchName;

    Vehicle(String vehicleId, VehicleType vehicleType, String branchName) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.branchName = branchName;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public VehicleType getVehicleType() {
        return this.vehicleType;
    }

    public String getBranchName() {
        return this.branchName;
    }
}

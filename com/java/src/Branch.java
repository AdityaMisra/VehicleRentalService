import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created on 2/7/21, 7:41 PM
 * Branch.java
 *
 * @author aditya.misra
 */


class Branch {
    private String name;
    private City city = new City("Delhi");

    public HashMap<VehicleType, Double> vehicleTypePrices;
    public ConcurrentHashMap<VehicleType, HashSet<Vehicle>> vehicleTypeList;

    public Branch(String name) {
        this.name = name;
        this.vehicleTypePrices = new HashMap<VehicleType, Double>();
        this.vehicleTypeList = new ConcurrentHashMap<>();
    }

    public void setName(String branchName) {
        this.name = branchName;
    }

    public void updateVehicleTypePrices(VehicleType vehicleType, double price) {
        vehicleTypePrices.put(vehicleType, price);
    }

    public Vehicle addVehicleType(String vehicleId, VehicleType vehicleType) {
        Vehicle newVehicle;

        if (vehicleTypeList.containsKey(vehicleType)) {
            HashSet<Vehicle> vehicles = vehicleTypeList.get(vehicleType);
            for (Vehicle vehicle : vehicles) {
                if (vehicle.getVehicleId().equalsIgnoreCase(vehicleId)) {
                    return vehicle;
                }
            }
            newVehicle = new Vehicle(vehicleId, vehicleType, this.name);
            vehicles.add(newVehicle);

        } else {
            // create new vehicle list
            HashSet<Vehicle> set = new HashSet<>();
            newVehicle = new Vehicle(vehicleId, vehicleType, this.name);
            set.add(newVehicle);

            vehicleTypeList.put(vehicleType, set);
        }
        return newVehicle;
    }
}

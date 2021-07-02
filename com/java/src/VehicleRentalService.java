import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created on 2/7/21, 7:40 PM
 * VehicleRentalService.java
 *
 * @author aditya.misra
 */

class VehicleRentalService {

    private Utils utils = new Utils();

    public HashMap<String, Branch> branches;

    public ConcurrentHashMap<VehicleType, ConcurrentHashMap<Vehicle, Set<Calendar>>> vehicleBooking;

    /*
     {
        "sedan" : {
            1: [23June2021-12, 23June2021-13, 23June2021-14]
        }
     }
     */

    public VehicleRentalService() {
        this.branches = new HashMap<>();
        this.vehicleBooking = new ConcurrentHashMap<>();
    }

    public void addBranch(String branchName) throws Exception {
        if (branchName == null || branchName.trim().equalsIgnoreCase("")) {
            throw new Exception("Invalid branch name");
        }

        Branch branch = new Branch(branchName);

        this.branches.put(branchName, branch);

    }

    public void allocatePrice(String branchName, VehicleType vehicleType, double price) {
        Branch branch = this.branches.get(branchName);
        branch.updateVehicleTypePrices(vehicleType, price);

    }

    public void addVehicle(String vehicleId, VehicleType vehicleType, String branchName) {
        Branch branch = this.branches.get(branchName);

        Vehicle vehicle = branch.addVehicleType(vehicleId, vehicleType);
        if (vehicle == null) {
            return;
        }
        return;
    }

    public String bookVehicle(VehicleType vehicleType, String startTime, String endTime) {

        Calendar startDateTime;
        Calendar endDateTime;

        try {
            startDateTime = utils.getDateTime(startTime);
            endDateTime = utils.getDateTime(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid booking slot";
        }

        int bookingHours = utils.hoursDifference(endDateTime, startDateTime);
        Set<Calendar> requestedHours = new HashSet<>();
        for (int i = 0; i < bookingHours; i++) {
            startDateTime.add(Calendar.HOUR_OF_DAY, 1);

            Calendar dateTime = Calendar.getInstance();
            dateTime.setTime(startDateTime.getTime());

            requestedHours.add(dateTime);
        }

        List<Vehicle> vehiclesNotAvailable = new ArrayList<>();

        ConcurrentHashMap<Vehicle, Set<Calendar>> vehicleHashSetConcurrentHashMap = this.vehicleBooking.get(vehicleType);

        if (vehicleHashSetConcurrentHashMap == null) {
            vehicleHashSetConcurrentHashMap = new ConcurrentHashMap<Vehicle, Set<Calendar>>();
        }

        for (Map.Entry<Vehicle, Set<Calendar>> vehicleCalendarMap : vehicleHashSetConcurrentHashMap.entrySet()) {
            Set<Calendar> bookingDateTimes = vehicleCalendarMap.getValue();
            Vehicle vehicle = vehicleCalendarMap.getKey();
            for (Calendar datetime : bookingDateTimes) {
                if (requestedHours.contains(datetime)) {
                    vehiclesNotAvailable.add(vehicle);
                    break;
                }
            }
        }

        return getVehicleWithLowestRentalPrice(vehicleType, requestedHours, vehiclesNotAvailable, vehicleHashSetConcurrentHashMap);
    }

    private String getVehicleWithLowestRentalPrice(VehicleType vehicleType, Set<Calendar> requestedHours,
                                                   List<Vehicle> vehiclesNotAvailable,
                                                   ConcurrentHashMap<Vehicle, Set<Calendar>> vehicleHashSetConcurrentHashMap) {

        Map<Double, HashSet<Vehicle>> priceAndAvailableVehicleMapping = new HashMap<>();

        double minPrice = Integer.MAX_VALUE;
        for (Branch branch : this.branches.values()) {
            if (branch.vehicleTypePrices.get(vehicleType) != null
                    && minPrice > branch.vehicleTypePrices.get(vehicleType)) {

                minPrice = branch.vehicleTypePrices.get(vehicleType);

                for (Vehicle vehicle : branch.vehicleTypeList.get(vehicleType)) {

                    if (vehiclesNotAvailable.contains(vehicle)) {
                        continue;
                    }

                    if (priceAndAvailableVehicleMapping.containsKey(minPrice)) {
                        priceAndAvailableVehicleMapping.get(minPrice).add(vehicle);
                    } else {
                        HashSet<Vehicle> set = new HashSet<>();
                        set.add(vehicle);
                        priceAndAvailableVehicleMapping.put(minPrice, set);
                    }
                }
            }
        }

//        {
//            100: [V1, v2]
//            80: [V3, v4]
//        }

        // sort the map based on the price of the vehicles
        priceAndAvailableVehicleMapping = priceAndAvailableVehicleMapping.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // pick the lowest price
        Optional<Map.Entry<Double, HashSet<Vehicle>>> vehiclesAvailableForBooking = priceAndAvailableVehicleMapping.entrySet()
                .stream()
                .findFirst();

        Vehicle vehicle = null;
        if (vehiclesAvailableForBooking.isPresent()) {
            Optional<Vehicle> optionalVehicle = vehiclesAvailableForBooking.get()
                    .getValue()
                    .stream()
                    .findAny();

            if (!optionalVehicle.isPresent()) {
                return "NO " + vehicleType.name().toUpperCase() + " AVAILABLE";
            }

            vehicle = optionalVehicle.get();
        }

        if (vehicle == null) {
            return "NO " + vehicleType.name().toUpperCase() + " AVAILABLE";
        }

        updateTheBookingLog(vehicleType, requestedHours, vehicleHashSetConcurrentHashMap, vehicle);

        return vehicle.getVehicleId() + " from " + vehicle.getBranchName() + " booked.";
    }

    private void updateTheBookingLog(VehicleType vehicleType, Set<Calendar> requestedHours,
                                     ConcurrentHashMap<Vehicle, Set<Calendar>> vehicleHashSetConcurrentHashMap,
                                     Vehicle vehicle) {

        if (vehicleHashSetConcurrentHashMap.containsKey(vehicle)) {
            vehicleHashSetConcurrentHashMap.get(vehicle).addAll(requestedHours);
        } else {
            vehicleHashSetConcurrentHashMap.put(vehicle, requestedHours);
        }

        this.vehicleBooking.put(vehicleType, vehicleHashSetConcurrentHashMap);
    }


}

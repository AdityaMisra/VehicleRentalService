/**
 * Created on 2/7/21, 7:42 PM
 * Main.java
 *
 * @author aditya.misra
 */


public class Main {
    public static void main(String[] args) throws Exception {
        VehicleRentalService rentalService = new VehicleRentalService();

        rentalService.addBranch("Vasanth Vihar");
        rentalService.addBranch("Cyber City");
        rentalService.allocatePrice("Vasanth Vihar", VehicleType.Sedan, 100);
        rentalService.allocatePrice("Vasanth Vihar", VehicleType.Hatchback, 80);
        rentalService.allocatePrice("Cyber City", VehicleType.Sedan, 200);
        rentalService.allocatePrice("Cyber City", VehicleType.Hatchback, 50);
        rentalService.addVehicle("DL 01 MR 9310", VehicleType.Sedan, "Vasanth Vihar");
        rentalService.addVehicle("DL 01 MR 9311", VehicleType.Sedan, "Cyber City");
        rentalService.addVehicle("DL 01 MR 9312", VehicleType.Hatchback, "Cyber City");

        System.out.println(rentalService.bookVehicle(VehicleType.Sedan, "2021-07-01 10:00:00", "2021-07-01 13:00:00"));
        System.out.println(rentalService.bookVehicle(VehicleType.Sedan, "2021-07-01 14:00:00", "2021-07-01 15:00:00"));
        System.out.println(rentalService.bookVehicle(VehicleType.Sedan, "2021-07-01 14:00:00", "2021-07-01 15:00:00"));
        System.out.println(rentalService.bookVehicle(VehicleType.Sedan, "2021-07-01 14:00:00", "2021-07-01 15:00:00"));

    }
}

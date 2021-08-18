package airline_reservation_system;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class FlightReservationSystem {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Derby derby = new Derby();
        FlightManager manager = new FlightManager(derby);
        List<Reservation> myReservations = derby.populateReservationsFromDB();

        Scanner scanner = new Scanner(System.in);
        System.out.print(">");

        while (scanner.hasNextLine()) {
            String inputLine = scanner.nextLine();
            if (inputLine == null || inputLine.equals("")) continue;
            Scanner commandLine = new Scanner(inputLine);
            String action = commandLine.next();
            if (action == null || action.equals("")) continue;
            if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
                return;
            else if (action.equals("PSNGRS")) {
                String flightNum = null;
                if (commandLine.hasNext()) {
                    flightNum = commandLine.next();
                    manager.printPsngrsForFlight(flightNum);
                }
            } else if (action.equals("help")) {
                System.out.println("List of available commands");
                System.out.println(ANSI_RED + "list, myres, res {flight-number}, cancel {flight-number}," +
                        "seats {flight-number}, sales, craft, sortcraft, sortbydep, sortbydur" + ANSI_RESET);
            } else if (action.equals("CNCLPSNGR")) {
                String flightNum = null;
                if (commandLine.hasNext()) {
                    flightNum = commandLine.next();
                    String name = commandLine.next();
                    String passportNumber = commandLine.next();
                    Reservation reservation = null;
                    for (int i = 0; i < myReservations.size(); i++) {
                        if (myReservations.get(i).getFlightNum().equals(flightNum))
                            reservation = myReservations.get(i);
                    }
                    if (reservation != null) {
                        manager.cancelReservation(reservation, name, passportNumber);
                        myReservations.remove(reservation);
                    } else {
                        // TODO: print a message as reservation is not found
                    }
                }
            }
            // List all flights
            else if (action.equalsIgnoreCase("LIST")) {
                manager.printAllFlights();
            } else if (action.equalsIgnoreCase("SALES")) {
                int amount = 0;
                int numOfStudentDiscounts = 0;
                int numOfLoyaltyDiscount = 0;
                int amountWithoutDiscount = 0;
                FileWriter fileWriter = new FileWriter("sales.txt");
                PrintWriter printWriter = new PrintWriter(fileWriter);
                for (Reservation myReservation : myReservations) {
                    if ("student".equals(myReservation.getCouponCode()))
                        numOfStudentDiscounts++;
                    else if ("loyalty".equals(myReservation.getCouponCode()))
                        numOfLoyaltyDiscount++;
                    else
                        amountWithoutDiscount += myReservation.getPrice();
                    amount += myReservation.getPrice();
                    printWriter.println(myReservation.toString());
                }
                printWriter.println();
                printWriter.println("Total Student Discount= " + numOfStudentDiscounts * 50);
                printWriter.println("Total Loyalty Discount = " + numOfLoyaltyDiscount * 50);
                printWriter.println("Total amount with Discount = " + (amount - amountWithoutDiscount));
                printWriter.println("Total amount without Discount = " + amountWithoutDiscount);
                printWriter.println("Total amount of Tickets= " + (amount));
                printWriter.close();
            } else if (action.equalsIgnoreCase("RESFCL")) {
                String flightNum = null;
                if (commandLine.hasNext()) {
                    flightNum = commandLine.next();
                    Reservation reservation = manager.reserveSeatOnFlight(flightNum,
                            LongHaulFlight.firstClass, null);
                    if (reservation == null)
                        System.out.println(manager.getErrorMessage());
                    else {
                        myReservations.add(reservation);
                        reservation.print();
                    }
                }
            } else if (action.equalsIgnoreCase("RES")) {
                String flightNum = null;
                if (commandLine.hasNext()) {
                    flightNum = commandLine.next();
                    String couponCode = null;
                    if (commandLine.hasNext())
                        couponCode = commandLine.next();
                    Reservation reservation = manager.reserveSeatOnFlight(flightNum,
                            LongHaulFlight.economy, couponCode);
                    if (reservation == null)
                        System.out.println(manager.getErrorMessage());
                    else {
                        myReservations.add(reservation);
                        reservation.print();
                    }
                }
            } else if (action.equalsIgnoreCase("SEATS")) {
                String flightNum = null;

                if (commandLine.hasNext()) {
                    flightNum = commandLine.next();

                    if (manager.seatsAvailable(flightNum)) {
                        System.out.println("Seats are available");
                    } else {
                        System.out.println(manager.getErrorMessage());
                    }
                }
            } else if (action.equalsIgnoreCase("CANCEL")) {
                String flightNum = null;

                if (commandLine.hasNext()) {
                    flightNum = commandLine.next();
                    Reservation reservation = null;
                    for (int i = 0; i < myReservations.size(); i++) {
                        if (myReservations.get(i).getFlightNum().equals(flightNum))
                            reservation = myReservations.get(i);
                    }
                    if (reservation != null) {
                        manager.cancelReservation(reservation);
                        myReservations.remove(reservation);
                        derby.cancelReservation(reservation.getFlightNum());
                        derby.removePassenger(reservation.getFlightNum());
                    } else {
                        System.out.println("Flight " + flightNum + " Not Found");
                    }
                }

            } else if (action.equalsIgnoreCase("MYRES")) {
                for (Reservation reservation : myReservations) {
                    reservation.print();
                }

            } else if (action.equalsIgnoreCase("CRAFT")) {
                manager.printAllAircraft();
            } else if (action.equalsIgnoreCase("SORTCRAFT")) {
                manager.sortAircraft();
                manager.printAllFlights();
            } else if (action.equalsIgnoreCase("SORTBYDEP")) {
                manager.sortByDeparture();
                manager.printAllFlights();
            } else if (action.equalsIgnoreCase("SORTBYDUR")) {
                manager.sortByDuration();
                manager.printAllFlights();
            }

            System.out.print("\n>");
        }
    }


}


package airline_reservation_system;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static airline_reservation_system.LongHaulFlight.economy;
import static airline_reservation_system.LongHaulFlight.firstClass;


public class FlightManager {
    List<Flight> flights;
    private Derby derby;
    String[] cities = {"Dallas", "New York", "London", "Paris", "Tokyo"};
    final static int DALLAS = 0;
    final static int NEWYORK = 1;
    final static int LONDON = 2;
    final static int PARIS = 3;
    final static int TOKYO = 4;

    // flight times in hours
    static int[] flightTimes = {3, // Dallas
            1, // New York
            7, // London
            8, // Paris
            16// Tokyo
    };

    List<Aircraft> airplanes;

    String errorMsg = null;

    static Random random = new Random();


    public FlightManager(Derby derby) throws SQLException {
        this.derby = derby;
        airplanes = derby.getAirCrafts();
        flights = derby.getFlights();
    }

    public static String generateFlightNumber(String airline) {
        String flightNumber = "";
        String words[] = airline.split("\\s");
        for (String word : words) {
            flightNumber += word.charAt(0);
        }
        int low = 101;
        int high = 301;
        int randomNumber = random.nextInt(high - low) + low;

        return flightNumber + randomNumber;
    }

    public void printAllFlights() {
        for (int i = 0; i < flights.size(); i++) {
            System.out.println(flights.get(i).toString());
        }
    }


    public boolean seatsAvailable(String flightNum) {
        Boolean isValidFlightNum = false;
        Flight flight = null;
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlightNum().equals(flightNum)) {
                isValidFlightNum = true;
                flight = flights.get(i);
            }
        }
        if (!isValidFlightNum) {
            errorMsg = "Flight " + flightNum + " Not Found";
            return false;
        }
        if (!flight.seatsAvailable()) {
            errorMsg = "Flight " + flightNum + " Full";
            return false;
        }

        return true;
    }


    public Reservation reserveSeatOnFlight(String flightNum, String seatType, String couponCode) {
        Boolean isValidFlightNum = false;
        Flight flight = null;
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlightNum().equals(flightNum)) {
                isValidFlightNum = true;
                flight = flights.get(i);
            }
        }
        if (!isValidFlightNum) {
            errorMsg = "Flight " + flightNum + " Not Found";
            return null;
        }
        Reservation reservation = null;
        if (LongHaulFlight.firstClass.equals(seatType)) {
            if (flight instanceof LongHaulFlight) {
                LongHaulFlight longHaulFlight = (LongHaulFlight) flight;
                boolean isReserved = longHaulFlight.reserveSeat(LongHaulFlight.firstClass);
                if (isReserved) {
                    //TODO: what to set in flightInfo
                    int price = flight.getPrice();
                    if ("student".equals(couponCode) || "loyalty".equals(couponCode))
                        price = price - 50;
                    reservation = new Reservation(flightNum, flight.toString(), couponCode, price);
                    reservation.setFirstClass();
                    derby.makeReservation(flightNum, firstClass, flight.toString(), price, couponCode);
                } else {
                    errorMsg = "Flight " + flightNum + "  Full";
                }
            }
        } else {
            boolean isReserved = flight.reserveSeat();
            if (isReserved) {
                //TODO: what to set in flightInfo
                int price = flight.getPrice();
                if ("student".equals(couponCode) || "loyalty".equals(couponCode))
                    price = price - 50;
                reservation = new Reservation(flightNum, flight.toString(), couponCode, price);
                derby.makeReservation(flightNum, economy, flight.toString(), price, couponCode);
            } else {
                errorMsg = "Flight " + flightNum + " Full";
            }
        }

        return reservation;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    public boolean cancelReservation(Reservation res) {
        String flightNum = res.getFlightNum();
        Flight flight = null;
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlightNum().equals(flightNum))
                flight = flights.get(i);
        }
        if (flight == null) {
            errorMsg = "Flight " + flightNum + " Not Found";
            return false;
        } else {
            if (res.isFirstClass() && flight instanceof LongHaulFlight) {
                LongHaulFlight longHaulFlight = (LongHaulFlight) flight;
                longHaulFlight.cancelSeat(LongHaulFlight.firstClass);
            } else {
                flight.cancelSeat();
            }
        }
        return true;
    }


    public void sortByDeparture() {
        Collections.sort(flights, new DepartureTimeComparator());
    }

    public void printPsngrsForFlight(String flightNum) {
        Flight flight = null;
        for (int i = 0; i < flights.size(); i++) {
            Flight flight1 = flights.get(i);
            if (flight1.getFlightNum() == flightNum)
                flight = flight1;
        }
        if (flight == null) {
            errorMsg = "Flight " + flightNum + "Not Found";
        } else {
            System.out.println(flight.getPassengerList());
        }
    }

    public boolean cancelReservation(Reservation reservation, String name, String passportNumber) {
        String flightNum = reservation.getFlightNum();
        Flight flight = null;
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).equals(flightNum))
                flight = flights.get(i);
        }
        if (flight == null) {
            errorMsg = "Flight " + flightNum + " Not Found";
            return false;
        } else {
            flight.cancelSeat(name, passportNumber);
            return true;
        }
    }

    private class DepartureTimeComparator implements Comparator<Flight> {

        @Override
        public int compare(Flight o1, Flight o2) {
            //TODO: verify if this work
            return o1.getDepartureTime().compareTo(o2.getDepartureTime());
        }
    }


    public void sortByDuration() {
        Collections.sort(flights, new DurationComparator());
    }


    private class DurationComparator implements Comparator<Flight> {

        @Override
        public int compare(Flight o1, Flight o2) {
            if (o1.getFlightDuration() < o2.flightDuration)
                return -1;
            else if (o1.getFlightDuration() < o2.flightDuration)
                return 1;
            return 0;
        }
    }


    public void printAllAircraft() {
        for (Aircraft aircraft : airplanes)
            aircraft.print();
    }


    public void sortAircraft() {
        Collections.sort(airplanes);
    }

}

package airline_reservation_system;

import java.util.ArrayList;
import java.util.List;

public class Flight {

    public enum Status {DELAYED, ONTIME, ARRIVED, INFLIGHT}

    ;

    String flightNum;
    String airline;
    String origin, dest;
    String departureTime;
    Status status;
    int flightDuration;
    Aircraft aircraft;
    int price;
    List<Passenger> passengerList;
    protected int passengers;

    public Flight() {
        this.flightNum = null;
        this.airline = null;
        this.dest = null;
        this.origin = "Toronto";
        this.departureTime = null;
        this.flightDuration = 0;
        this.aircraft = null;
        passengers = 0;
        status = null;
        this.price = 370;
        this.passengerList = new ArrayList<>();
    }

    public Flight(int price, String flightNum, String airline, String dest, String departure, int flightDuration, Aircraft aircraft) {
        this.price = price;
        this.flightNum = flightNum;
        this.airline = airline;
        this.dest = dest;
        this.origin = "Toronto";
        this.departureTime = departure;
        this.flightDuration = flightDuration;
        this.aircraft = aircraft;
        passengers = 0;
        status = Status.ONTIME;
        this.passengerList = new ArrayList<>();
    }

    public String getFlightNum() {
        return flightNum;
    }

    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getPrice() {
        return price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getFlightDuration() {
        return flightDuration;
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setFlightDuration(int dur) {
        this.flightDuration = dur;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }


    public boolean seatsAvailable() {
        if (passengers < aircraft.getNumSeats())
            return true;
        return false;
    }


    public void cancelSeat() {
        if (passengers == 0) {
            //TODO: may be throw an Exception
        } else {
            passengers--;
        }
    }

    public void cancelSeat(String name, String passportNumber) {
        if (passengers == 0) {
            //TODO: may be throw an Exception
        } else {
            passengerList.remove(new Passenger(name, passportNumber, 0));
            passengers--;
        }

    }

    public boolean reserveSeat() {
        if (seatsAvailable()) {
            passengers++;
            return true;
        }
        return false;
    }

    public String toString() {
        return airline + "\t Flight:  " + flightNum + "\t Dest: " + dest + "\t Departing: " + departureTime + "\t Duration: " + flightDuration + "\t Status: " + status;

    }

}

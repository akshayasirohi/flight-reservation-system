package airline_reservation_system;

import java.util.Objects;

public class Passenger {
    private String name;
    private String passportNumber;
    private int seatNumber;

    public Passenger(String name, String passportNumber, int seatNumber) {
        this.name = name;
        this.passportNumber = passportNumber;
        this.seatNumber = seatNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(name, passenger.name) &&
                Objects.equals(passportNumber, passenger.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, passportNumber);
    }

    @Override
    public String toString() {
        return "name= " + name;

    }
}

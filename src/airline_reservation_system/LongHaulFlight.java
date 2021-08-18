package airline_reservation_system;

public class LongHaulFlight extends Flight {
    int numFirstClassPassengers;
    String seatType;
    public static final String firstClass = "First Class Seat";
    public static final String economy = "Economy Seat";

    public LongHaulFlight(int price, String flightNum, String airline, String dest, String departure, int flightDuration, Aircraft aircraft) {
        super(price, flightNum, airline, dest, departure, flightDuration, aircraft);
    }

    public LongHaulFlight() {
        // default constructor
    }

    @Override
    public boolean reserveSeat() {
        reserveSeat(economy);
        return true;
    }

    public boolean reserveSeat(String seatType) {
        if (economy.equals(seatType)) {
            return super.reserveSeat();
        } else if (firstClass.equals(seatType)) {
            if (aircraft.getNumFirstClassSeats() > numFirstClassPassengers) {
                numFirstClassPassengers++;
                return true;
            } else
                return false;
        }

        return false;
    }

    // Cancel a seat
    @Override
    public void cancelSeat() {
        cancelSeat(economy);
    }

    public void cancelSeat(String seatType) {
        if (firstClass.equals(seatType) && numFirstClassPassengers > 0)
            numFirstClassPassengers--;
        else
            super.passengers--;
    }

    public int getPassengerCount() {
        return numFirstClassPassengers + super.getPassengers();
    }

    public String toString() {
        return super.toString() + " " + "LongHaul";
    }
}

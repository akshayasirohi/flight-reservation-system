package airline_reservation_system;

public class Aircraft implements Comparable<Aircraft> {
    int id;
    int numEconomySeats;
    int numFirstClassSeats;
    String model;

    public Aircraft(int id, int seats, String model) {
        this.id = id;
        this.numEconomySeats = seats;
        this.numFirstClassSeats = 0;
        this.model = model;
    }

    public Aircraft(int id, int economy, int firstClass, String model) {
        this.id = id;
        this.numEconomySeats = economy;
        this.numFirstClassSeats = firstClass;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public int getNumSeats() {
        return numEconomySeats;
    }

    public int getTotalSeats() {
        return numEconomySeats + numFirstClassSeats;
    }

    public int getNumFirstClassSeats() {
        return numFirstClassSeats;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void print() {
        System.out.println("Model: " + model + "\t Economy Seats: " + numEconomySeats + "\t First Class Seats: " + numFirstClassSeats);
    }

    @Override
    public int compareTo(Aircraft otherAircraft) {
        if (numEconomySeats < otherAircraft.getNumSeats())
            return -1;
        else if (numEconomySeats > otherAircraft.getNumSeats())
            return 1;
        else {
            if (numFirstClassSeats < otherAircraft.getNumFirstClassSeats())
                return -1;
            else if (numFirstClassSeats > otherAircraft.getNumFirstClassSeats())
                return 1;
            return 0;
        }
    }

}
  

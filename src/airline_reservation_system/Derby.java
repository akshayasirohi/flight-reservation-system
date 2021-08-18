package airline_reservation_system;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static airline_reservation_system.FlightManager.*;

public class Derby {
    private String databaseURL;
    private Connection conn;
    private Statement statement;
    private List<Flight> flights;
    private List<Aircraft> aircrafts;
    private List<Reservation> reservations;

    public Derby() throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        databaseURL = "jdbc:derby:flightdb;create=true";
        conn = DriverManager.getConnection(databaseURL);
        statement = conn.createStatement();
        createTables();
    }

    private void createTables() throws SQLException {
        Statement statement = conn.createStatement();
        String sql = "CREATE TABLE aircrafts (id int, economySeats int, firstClassSeats int, model varchar(200))";
        if (!doesTableExists("aircrafts", conn)) {
            statement.execute(sql);
            aircrafts = populateAircrafts();
        } else {
            aircrafts = populateAircraftsFromDB();

        }
        sql = "CREATE TABLE flights (flightNum varchar(200), airline varchar(200), origin varchar(200), dest varchar(200)," +
                " departureTime varchar(200), passengers int, status varchar(200), flightDuration int, aircraft int," +
                "price int, type varchar(200))";

        if (!doesTableExists("flights", conn)) {
            statement.execute(sql);
            flights = populateFlights();
        } else {
            flights = populateFlightsFromDB();
        }

        sql = "CREATE TABLE reservations (id int GENERATED ALWAYS AS IDENTITY, flightNum varchar(200)," +
                "class varchar(200), flightInfo varchar(1000), price int, couponCode varchar(200))";
        if (!doesTableExists("reservations", conn))
            statement.execute(sql);
        else
            reservations = populateReservationsFromDB();

    }

    private List<Aircraft> populateAircraftsFromDB() {
        String sql = "SELECT * FROM aircrafts";
        List<Aircraft> aircrafts = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery(sql);
            int id = 1;
            while (result.next()) {
                int economySeats = result.getInt("economySeats");
                int firstClassSeats = result.getInt("firstClassSeats");
                String model = result.getString("model");
                Aircraft aircraft = new Aircraft(id++, economySeats, firstClassSeats, model);
                aircrafts.add(aircraft);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return aircrafts;
    }

    private List<Flight> populateFlightsFromDB() {
        String sql = "SELECT * FROM flights";
        List<Flight> flights = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int price = result.getInt("price");
                String flightNum = result.getString("flightNum");
                String airline = result.getString("airline");
                String dest = result.getString("dest");
                String departureTime = result.getString("departureTime");
                int flightDuration = result.getInt("flightDuration");
                int aircraftId = result.getInt("aircraft");
                String type = result.getString("type");
                Aircraft aircraft = fetchAirCraftWithId(aircraftId);
                Flight flight;
                if ("long".equals(type)) {
                    flight = new LongHaulFlight(price, flightNum, airline,
                            dest, departureTime, flightDuration, aircraft);
                } else
                    flight = new Flight(price, flightNum, airline, dest,
                            departureTime, flightDuration, aircraft);
                flights.add(flight);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return flights;
    }

    private Aircraft fetchAirCraftWithId(int aircraftId) {
        for (int i = 0; i < aircrafts.size(); i++) {
            if (aircrafts.get(i).getId() == aircraftId)
                return aircrafts.get(i);
        }
        return null;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    private List<Flight> populateFlights() throws SQLException {
        List<Flight> flights = new ArrayList<>();
        String flightNum = generateFlightNumber("United Airlines");
        Flight flight = new Flight(370, flightNum, "United Airlines", "Dallas", "1400", flightTimes[DALLAS], aircrafts.get(0));
        flights.add(flight);
        flight.setStatus(Flight.Status.DELAYED);

        flightNum = generateFlightNumber("Air Canada");
        flight = new Flight(400, flightNum, "Air Canada", "London", "2300", flightTimes[LONDON], aircrafts.get(1));
        flights.add(flight);

        flightNum = generateFlightNumber("Air Canada");
        flight = new Flight(500, flightNum, "Air Canada", "Paris", "2200", flightTimes[PARIS], aircrafts.get(1));
        flights.add(flight);

        flightNum = generateFlightNumber("Porter Airlines");
        flight = new Flight(450, flightNum, "Porter Airlines", "New York", "1200", flightTimes[NEWYORK], aircrafts.get(2));
        flights.add(flight);

        flightNum = generateFlightNumber("United Airlines");
        flight = new Flight(470, flightNum, "United Airlines", "New York", "0900", flightTimes[NEWYORK], aircrafts.get(3));
        flights.add(flight);
        flight.setStatus(Flight.Status.INFLIGHT);

        flightNum = generateFlightNumber("Air Canada");
        flight = new Flight(520, flightNum, "Air Canada", "New York", "0600", flightTimes[NEWYORK], aircrafts.get(2));
        flights.add(flight);
        flight.setStatus(Flight.Status.INFLIGHT);


        flightNum = generateFlightNumber("United Airlines");
        flight = new Flight(330, flightNum, "United Airlines", "Paris", "2330", flightTimes[PARIS], aircrafts.get(0));
        flights.add(flight);

        flightNum = generateFlightNumber("Air Canada");
        flight = new LongHaulFlight(769, flightNum, "Air Canada", "Tokyo", "2200", flightTimes[TOKYO], aircrafts.get(4));
        flights.add(flight);

        flightNum = generateFlightNumber("Emirates");
        flight = new LongHaulFlight(800, flightNum, "Air Emirates", "Tokyo", "1300", flightTimes[TOKYO], aircrafts.get(5));
        flights.add(flight);

        addFlights(flights);
        return flights;
    }

    public void addReservation(String flightNum, String seatClass, String flightInfo,
                               int price, String couponCode) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("insert into reservations(flightNum," +
                "class,flightInfo,price,couponCode) values (?,?,?,?,?)");
        preparedStatement.setString(1, flightNum);
        preparedStatement.setString(2, seatClass);
        preparedStatement.setString(3, flightInfo);
        preparedStatement.setInt(4, price);
        preparedStatement.setString(5, couponCode);
        preparedStatement.executeUpdate();
    }

    public void showReservations() throws SQLException {
        String sql = "SELECT * FROM reservations";
        ResultSet result = statement.executeQuery(sql);
        ResultSetMetaData rsmd = result.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (result.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = result.getString(i);
                System.out.print(columnValue + " ");
            }
            System.out.println("");
        }

    }

    public void addFlights(List<Flight> flights) throws SQLException {
        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            PreparedStatement preparedStatement = conn.prepareStatement("insert into flights values (?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, flight.getFlightNum());
            preparedStatement.setString(2, flight.getAirline());
            preparedStatement.setString(3, flight.getOrigin());
            preparedStatement.setString(4, flight.getDest());
            preparedStatement.setString(5, flight.getDepartureTime());
            preparedStatement.setInt(6, flight.getPassengers());
            preparedStatement.setString(7, flight.getStatus().name());
            preparedStatement.setInt(8, flight.getFlightDuration());
            preparedStatement.setInt(9, flight.getAircraft().getId());
            preparedStatement.setInt(10, flight.getPrice());
            if (flight instanceof LongHaulFlight)
                preparedStatement.setString(11, "long");
            else
                preparedStatement.setString(11, "short");

            preparedStatement.executeUpdate();
        }
    }

    private void dropTable(Statement statement) throws SQLException {
//        String sql = "drop table flights";
//        statement.execute(sql);

    }

    private static boolean doesTableExists(String tableName, Connection conn)
            throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet result = meta.getTables(null, null, tableName.toUpperCase(), null);
        return result.next();
    }

    public void showFlights() {
        String sql = "SELECT * FROM flights";
        try {
            ResultSet result = statement.executeQuery(sql);
            ResultSetMetaData rsmd = result.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (result.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = result.getString(i);
                    System.out.print(columnValue + " ");
//                System.out.print(columnValue + " " + rsmd.getColumnName(i));

                }
                System.out.println("");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    public void addAirplanes(List<Aircraft> airplanes) throws SQLException {
        for (int i = 0; i < airplanes.size(); i++) {
            Aircraft aircraft = airplanes.get(i);
            PreparedStatement preparedStatement = conn.prepareStatement("insert into aircrafts values (?,?,?,?)");
            preparedStatement.setInt(1, aircraft.getId());
            preparedStatement.setInt(2, aircraft.getNumSeats());
            preparedStatement.setInt(3, aircraft.getNumFirstClassSeats());
            preparedStatement.setString(4, aircraft.getModel());
            preparedStatement.executeUpdate();
        }
    }

    private void shutdown() throws SQLException {
        DriverManager.getConnection("jdbc:derby:;shutdown=true");
    }

    public void cancelReservation(String flightNum) throws SQLException {
        int id = getFirstIdForFlightNumFromDB(flightNum);
        PreparedStatement preparedStatement = conn.prepareStatement("delete from reservations where id=?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    private int getFirstIdForFlightNumFromDB(String flightNum) {
        String sql = "SELECT * FROM reservations";
        try {
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String flightNumInDB = result.getString("flightNum");
                int id = result.getInt("id");
                if (flightNum.equals(flightNumInDB))
                    return id;

            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return -1;
    }

    public List<Aircraft> populateAircrafts() throws SQLException {
        List<Aircraft> aircraftList = new ArrayList<>();
        aircraftList.add(new Aircraft(1, 3, "Boeing 737"));
        aircraftList.add(new Aircraft(2, 180, "Airbus 320"));
        aircraftList.add(new Aircraft(3, 37, "Dash-8 100"));
        aircraftList.add(new Aircraft(4, 12, "Bombardier 5000"));
        aircraftList.add(new Aircraft(5, 592, 14, "Boeing 747"));
        aircraftList.add(new Aircraft(6, 383, 8, "Airbus A380"));

        addAirplanes(aircraftList);
        return aircraftList;
    }

    public List<Aircraft> getAirCrafts() {
        return aircrafts;
    }

    public void makeReservation(String flightNum, String seatClass, String flightInfo, int price, String couponCode) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement("update flights set passengers=passengers+1 where flightNum=?");
            preparedStatement.setString(1, flightNum);
            preparedStatement.executeUpdate();
            addReservation(flightNum, seatClass, flightInfo, price, couponCode);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    public List<Reservation> populateReservationsFromDB() {
        String sql = "SELECT * FROM reservations";
        List<Reservation> reservations = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String flightNum = result.getString("flightNum");
                String seatClass = result.getString("class");
                String flightInfo = result.getString("flightInfo");
                int price = result.getInt("price");
                String couponCode = result.getString("couponCode");
                Reservation reservation = new Reservation(flightNum, flightInfo, couponCode, price);
                if (seatClass.equals(reservation.firstClass))
                    reservation.setFirstClass();
                reservations.add(reservation);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return reservations;
    }

    public void removePassenger(String flightNum) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement("update flights set passengers=passengers-1 where flightNum=?");
            preparedStatement.setString(1, flightNum);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }
}


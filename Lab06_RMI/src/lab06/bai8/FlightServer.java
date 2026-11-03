package lab06.bai8;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import lab06.common.RmiSupport;
public class FlightServer {
    @SuppressWarnings("serial")
    private static class Service extends UnicastRemoteObject implements FlightService {
        private final Map<String, Integer> available = new HashMap<>();
        private final Map<String, Integer> capacity = new HashMap<>();
        Service() throws RemoteException { addFlight("VN101", 100); addFlight("VN202", 80); addFlight("VN303", 60); }
        private void addFlight(String number, int seats) { available.put(number, seats); capacity.put(number, seats); }
        private String key(String number) { return number == null ? "" : number.trim().toUpperCase(); }
        public synchronized boolean bookTicket(String number, int seats) { validate(seats); String key = key(number); Integer left = available.get(key); if (left == null || left < seats) return false; available.put(key, left - seats); return true; }
        public synchronized int availableSeats(String number) { return available.getOrDefault(key(number), -1); }
        public synchronized boolean cancelBooking(String number, int seats) { validate(seats); String key = key(number); Integer left = available.get(key); if (left == null || left + seats > capacity.get(key)) return false; available.put(key, left + seats); return true; }
        private void validate(int seats) { if (seats <= 0) throw new IllegalArgumentException("So ghe phai lon hon 0."); }
    }
    public static void main(String[] args) throws Exception { RmiSupport.bind("FlightService", new Service()); }
}

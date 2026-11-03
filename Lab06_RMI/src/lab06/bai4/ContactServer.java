package lab06.bai4;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import lab06.common.RmiSupport;
public class ContactServer {
    @SuppressWarnings("serial")
    private static class Service extends UnicastRemoteObject implements ContactService {
        // Theo yêu cầu của đề: danh bạ được lưu bằng HashMap<String, String>.
        private final Map<String, String> contacts = new HashMap<>();

        Service() throws RemoteException {}

        @Override
        public synchronized void addContact(String name, String phone) {
            if (name == null || name.isBlank() || phone == null || phone.isBlank()) throw new IllegalArgumentException("Ten va so dien thoai khong duoc rong.");
            contacts.put(name.trim().toLowerCase(), phone.trim());
        }

        @Override
        public synchronized String findContact(String name) {
            return name == null ? null : contacts.get(name.trim().toLowerCase());
        }

        @Override
        public synchronized boolean deleteContact(String name) {
            return name != null && contacts.remove(name.trim().toLowerCase()) != null;
        }
    }
    public static void main(String[] args) throws Exception { RmiSupport.bind("ContactService", new Service()); }
}

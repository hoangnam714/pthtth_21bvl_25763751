package lab06.bai9;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lab06.common.RmiSupport;
public class AuctionServer {
    private static class Product {
        final int id; final String name; double price; String highestBidder = "Chua co";
        Product(int id, String name, double price) { this.id = id; this.name = name; this.price = price; }
        String display() { return String.format("%d | %s | %,.0f | Nguoi giu gia: %s", id, name, price, highestBidder); }
    }
    @SuppressWarnings("serial")
    private static class Service extends UnicastRemoteObject implements AuctionService {
        private final Map<Integer, Product> products = new LinkedHashMap<>();
        private final AtomicInteger nextId = new AtomicInteger(1);
        Service() throws RemoteException {}
        public synchronized int addProduct(String name, double price) {
            if (name == null || name.isBlank() || !Double.isFinite(price) || price <= 0) throw new IllegalArgumentException("Thong tin san pham khong hop le.");
            int id = nextId.getAndIncrement(); products.put(id, new Product(id, name.trim(), price)); return id;
        }
        public synchronized List<String> listProducts() { List<String> result = new ArrayList<>(); products.values().forEach(p -> result.add(p.display())); return result; }
        public synchronized boolean placeBid(int id, String bidder, double amount) {
            Product product = products.get(id);
            if (product == null || bidder == null || bidder.isBlank() || !Double.isFinite(amount) || amount <= product.price) return false;
            product.price = amount; product.highestBidder = bidder.trim();
            System.out.printf("Gia moi: %s duoc %s dat %,.0f%n", product.name, product.highestBidder, amount);
            return true;
        }
    }
    public static void main(String[] args) throws Exception { RmiSupport.bind("AuctionService", new Service()); }
}

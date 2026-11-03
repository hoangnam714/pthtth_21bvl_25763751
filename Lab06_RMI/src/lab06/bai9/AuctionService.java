package lab06.bai9;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
public interface AuctionService extends Remote {
    int addProduct(String name, double startingPrice) throws RemoteException;
    List<String> listProducts() throws RemoteException;
    boolean placeBid(int productId, String bidder, double amount) throws RemoteException;
}

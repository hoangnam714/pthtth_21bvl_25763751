package lab06.bai3;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import lab06.common.RmiSupport;
public class PrimeServer {
    @SuppressWarnings("serial")
    private static class Service extends UnicastRemoteObject implements PrimeService {
        Service() throws RemoteException {}
        public boolean isPrime(int n) {
            if (n < 2) return false;
            if (n % 2 == 0) return n == 2;
            for (int i = 3; i <= n / i; i += 2) if (n % i == 0) return false;
            return true;
        }
    }
    public static void main(String[] args) throws Exception { RmiSupport.bind("PrimeService", new Service()); }
}

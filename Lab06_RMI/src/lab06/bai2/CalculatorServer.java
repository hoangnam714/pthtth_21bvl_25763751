package lab06.bai2;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import lab06.common.RmiSupport;
public class CalculatorServer {
    @SuppressWarnings("serial")
    private static class Service extends UnicastRemoteObject implements CalculatorService {
        Service() throws RemoteException {}
        public int add(int a, int b) { return a + b; }
    }
    public static void main(String[] args) throws Exception { RmiSupport.bind("CalculatorService", new Service()); }
}

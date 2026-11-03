package lab06.bai1;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import lab06.common.RmiSupport;
public class HelloServer {
    @SuppressWarnings("serial")
    private static class Service extends UnicastRemoteObject implements HelloService {
        Service() throws RemoteException {}
        public String sayHello() { return "Hello, World!"; }
    }
    public static void main(String[] args) throws Exception { RmiSupport.bind("HelloService", new Service()); }
}

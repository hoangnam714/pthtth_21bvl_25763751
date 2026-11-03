package lab06.bai5;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import lab06.common.RmiSupport;
public class ChatServer {
    @SuppressWarnings("serial")
    private static class Service extends UnicastRemoteObject implements ChatService {
        Service() throws RemoteException {}
        public void sendMessage(String message) { System.out.println("Client: " + message); System.out.println("Server: Da nhan tin nhan."); }
    }
    public static void main(String[] args) throws Exception { RmiSupport.bind("ChatService", new Service()); }
}

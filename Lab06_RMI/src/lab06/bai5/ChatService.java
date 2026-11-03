package lab06.bai5;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface ChatService extends Remote { void sendMessage(String message) throws RemoteException; }

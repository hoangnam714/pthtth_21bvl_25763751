package lab06.bai7;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface AreaService extends Remote {
    double rectangleArea(double width, double height) throws RemoteException;
    double circleArea(double radius) throws RemoteException;
    double triangleArea(double base, double height) throws RemoteException;
}

package lab06.bai7;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import lab06.common.RmiSupport;
public class AreaServer {
    @SuppressWarnings("serial")
    private static class Service extends UnicastRemoteObject implements AreaService {
        Service() throws RemoteException {}
        public double rectangleArea(double width, double height) { return positive(width) * positive(height); }
        public double circleArea(double radius) { double r = positive(radius); return Math.PI * r * r; }
        public double triangleArea(double base, double height) { return positive(base) * positive(height) / 2; }
        private double positive(double value) { if (!Double.isFinite(value) || value <= 0) throw new IllegalArgumentException("Kich thuoc phai lon hon 0."); return value; }
    }
    public static void main(String[] args) throws Exception { RmiSupport.bind("AreaService", new Service()); }
}

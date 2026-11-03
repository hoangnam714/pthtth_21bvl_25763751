package lab06.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class RmiSupport {
    public static final int PORT = 1099;

    private RmiSupport() {}

    public static Registry registry() throws RemoteException {
        try {
            return LocateRegistry.createRegistry(PORT);
        } catch (RemoteException alreadyRunning) {
            return LocateRegistry.getRegistry(PORT);
        }
    }

    public static void bind(String name, Remote service) throws RemoteException {
        registry().rebind(name, service);
        System.out.println("RMI service '" + name + "' is ready on port " + PORT + ".");
    }
}

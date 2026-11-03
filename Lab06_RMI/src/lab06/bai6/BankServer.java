package lab06.bai6;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import lab06.common.RmiSupport;
public class BankServer {
    @SuppressWarnings("serial")
    private static class Service extends UnicastRemoteObject implements BankService {
        private double balance = 1_000_000;
        Service() throws RemoteException {}
        public synchronized double getBalance() { return balance; }
        public synchronized void deposit(double amount) { validate(amount); balance += amount; }
        public synchronized boolean withdraw(double amount) { validate(amount); if (amount > balance) return false; balance -= amount; return true; }
        private void validate(double amount) { if (!Double.isFinite(amount) || amount <= 0) throw new IllegalArgumentException("So tien phai lon hon 0."); }
    }
    public static void main(String[] args) throws Exception { RmiSupport.bind("BankService", new Service()); }
}

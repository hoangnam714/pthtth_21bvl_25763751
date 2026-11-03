package lab06.bai3;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import lab06.common.RmiSupport;
public class PrimeClient {
    public static void main(String[] args) throws Exception {
        PrimeService service = (PrimeService) LocateRegistry.getRegistry("localhost", RmiSupport.PORT).lookup("PrimeService");
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Nhap n: "); int n = in.nextInt();
            System.out.println(n + (service.isPrime(n) ? " la" : " khong la") + " so nguyen to.");
        }
    }
}

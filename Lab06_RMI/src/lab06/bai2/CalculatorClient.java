package lab06.bai2;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import lab06.common.RmiSupport;
public class CalculatorClient {
    public static void main(String[] args) throws Exception {
        CalculatorService service = (CalculatorService) LocateRegistry.getRegistry("localhost", RmiSupport.PORT).lookup("CalculatorService");
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Nhap a: "); int a = in.nextInt();
            System.out.print("Nhap b: "); int b = in.nextInt();
            System.out.println("Tong = " + service.add(a, b));
        }
    }
}

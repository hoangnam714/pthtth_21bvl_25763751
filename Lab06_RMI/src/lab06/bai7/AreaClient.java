package lab06.bai7;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import lab06.common.RmiSupport;
public class AreaClient {
    public static void main(String[] args) throws Exception {
        AreaService service = (AreaService) LocateRegistry.getRegistry("localhost", RmiSupport.PORT).lookup("AreaService");
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("1.Hinh chu nhat  2.Hinh tron  3.Hinh tam giac\nChon: "); int choice = in.nextInt();
            double area;
            if (choice == 2) { System.out.print("Ban kinh: "); area = service.circleArea(in.nextDouble()); }
            else { System.out.print(choice == 1 ? "Chieu rong: " : "Day: "); double a = in.nextDouble(); System.out.print("Chieu cao: "); double h = in.nextDouble(); area = choice == 1 ? service.rectangleArea(a, h) : service.triangleArea(a, h); }
            System.out.printf("Dien tich = %.2f%n", area);
        }
    }
}

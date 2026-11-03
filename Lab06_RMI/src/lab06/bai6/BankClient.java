package lab06.bai6;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import lab06.common.RmiSupport;
public class BankClient {
    public static void main(String[] args) throws Exception {
        BankService service = (BankService) LocateRegistry.getRegistry("localhost", RmiSupport.PORT).lookup("BankService");
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.print("\n1.So du  2.Gui tien  3.Rut tien  0.Thoat\nChon: "); String choice = in.nextLine();
                if (choice.equals("0")) break;
                if (choice.equals("1")) System.out.printf("So du: %,.0f%n", service.getBalance());
                else if (choice.equals("2") || choice.equals("3")) {
                    System.out.print("So tien: "); double amount = Double.parseDouble(in.nextLine());
                    if (choice.equals("2")) { service.deposit(amount); System.out.println("Gui tien thanh cong."); }
                    else System.out.println(service.withdraw(amount) ? "Rut tien thanh cong." : "So du khong du.");
                } else System.out.println("Lua chon khong hop le.");
            }
        }
    }
}

package lab06.bai4;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import lab06.common.RmiSupport;
public class ContactClient {
    public static void main(String[] args) throws Exception {
        ContactService service = (ContactService) LocateRegistry.getRegistry("localhost", RmiSupport.PORT).lookup("ContactService");
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.print("\n1.Them/sua  2.Tim  3.Xoa  0.Thoat\nChon: "); String choice = in.nextLine();
                if (choice.equals("0")) break;
                System.out.print("Ten: "); String name = in.nextLine();
                switch (choice) {
                    case "1" -> { System.out.print("So dien thoai: "); service.addContact(name, in.nextLine()); System.out.println("Da luu."); }
                    case "2" -> { String phone = service.findContact(name); System.out.println(phone == null ? "Khong tim thay." : "So dien thoai: " + phone); }
                    case "3" -> System.out.println(service.deleteContact(name) ? "Da xoa." : "Khong tim thay.");
                    default -> System.out.println("Lua chon khong hop le.");
                }
            }
        }
    }
}

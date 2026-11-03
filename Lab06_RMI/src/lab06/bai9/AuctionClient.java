package lab06.bai9;
import java.rmi.registry.LocateRegistry;
import java.util.List;
import java.util.Scanner;
import lab06.common.RmiSupport;
public class AuctionClient {
    public static void main(String[] args) throws Exception {
        AuctionService service = (AuctionService) LocateRegistry.getRegistry("localhost", RmiSupport.PORT).lookup("AuctionService");
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.print("\n1.Them san pham  2.Xem danh sach  3.Dat gia  0.Thoat\nChon: "); String choice = in.nextLine(); if (choice.equals("0")) break;
                if (choice.equals("1")) { System.out.print("Ten san pham: "); String name = in.nextLine(); System.out.print("Gia khoi diem: "); int id = service.addProduct(name, Double.parseDouble(in.nextLine())); System.out.println("Da them, ma: " + id); }
                else if (choice.equals("2")) { List<String> products = service.listProducts(); if (products.isEmpty()) System.out.println("Chua co san pham."); else products.forEach(System.out::println); }
                else if (choice.equals("3")) { System.out.print("Ma san pham: "); int id = Integer.parseInt(in.nextLine()); System.out.print("Ten nguoi dat: "); String bidder = in.nextLine(); System.out.print("Muc gia: "); double price = Double.parseDouble(in.nextLine()); System.out.println(service.placeBid(id, bidder, price) ? "Dat gia thanh cong." : "Gia phai cao hon gia hien tai va thong tin phai hop le."); }
                else System.out.println("Lua chon khong hop le.");
            }
        }
    }
}

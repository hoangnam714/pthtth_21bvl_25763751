package lab06.bai8;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import lab06.common.RmiSupport;
public class FlightClient {
    public static void main(String[] args) throws Exception {
        FlightService service = (FlightService) LocateRegistry.getRegistry("localhost", RmiSupport.PORT).lookup("FlightService");
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.print("\n1.Kiem tra  2.Dat ve  3.Huy ve  0.Thoat\nChon: "); String choice = in.nextLine(); if (choice.equals("0")) break;
                System.out.print("Ma chuyen bay (VN101/VN202/VN303): "); String flight = in.nextLine();
                if (choice.equals("1")) { int seats = service.availableSeats(flight); System.out.println(seats < 0 ? "Khong co chuyen bay." : "So ghe trong: " + seats); continue; }
                System.out.print("So ghe: "); int seats = Integer.parseInt(in.nextLine());
                boolean ok = choice.equals("2") ? service.bookTicket(flight, seats) : choice.equals("3") && service.cancelBooking(flight, seats);
                System.out.println(ok ? "Thanh cong." : "Khong the thuc hien.");
            }
        }
    }
}

package lab06.bai5;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import lab06.common.RmiSupport;
public class ChatClient {
    public static void main(String[] args) throws Exception {
        ChatService service = (ChatService) LocateRegistry.getRegistry("localhost", RmiSupport.PORT).lookup("ChatService");
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Nhap tin nhan (exit de thoat):");
            while (true) { String message = in.nextLine(); if (message.equalsIgnoreCase("exit")) break; service.sendMessage(message); System.out.println("Server da nhan tin nhan."); }
        }
    }
}

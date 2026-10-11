package bai4;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalcClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(args.length > 0 ? args[0] : "localhost", 6004);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {
            System.out.println("Nhap CALC + 100 200 hoac QUIT");
            String line;
            while ((line = console.readLine()) != null) {
                out.println(line);
                System.out.println("Server: " + in.readLine());
                if (line.equalsIgnoreCase("QUIT")) break;
            }
        }
    }
}

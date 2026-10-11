package bai4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalcServer {
    private static final int PORT = 6004;

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Calc server listening on port " + PORT);
            while (true) {
                Socket socket = server.accept();
                new Thread(() -> serve(socket)).start();
            }
        }
    }

    private static void serve(Socket socket) {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(
                     socket.getOutputStream(), StandardCharsets.UTF_8), true)) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }
                out.println(calculate(line));
            }
        } catch (IOException e) {
            System.err.println("Calc client failed: " + e.getMessage());
        }
    }

    static String calculate(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 4 || !parts[0].equalsIgnoreCase("CALC")) {
            return "ERR INVALID_FORMAT";
        }
        try {
            long left = Long.parseLong(parts[2]);
            long right = Long.parseLong(parts[3]);
            return switch (parts[1]) {
                case "+" -> "OK " + Math.addExact(left, right);
                case "-" -> "OK " + Math.subtractExact(left, right);
                case "*" -> "OK " + Math.multiplyExact(left, right);
                case "/" -> right == 0 ? "ERR DIVIDE_BY_ZERO" : "OK " + (left / right);
                default -> "ERR UNSUPPORTED_OPERATOR";
            };
        } catch (NumberFormatException | ArithmeticException e) {
            return "ERR INVALID_NUMBER";
        }
    }
}

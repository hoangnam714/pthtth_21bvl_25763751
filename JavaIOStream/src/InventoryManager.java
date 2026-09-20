import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    public static List<Product> inputFromKeyboard(BufferedReader reader) {
        List<Product> list = new ArrayList<>();
        System.out.println("=== NHẬP DANH SÁCH SẢN PHẨM ===");
        while (true) {
            try {
                String code;
                while (true) {
                    System.out.print("Nhập mã sản phẩm (hoặc 'q' để dừng): ");
                    String line = reader.readLine();
                    if (line == null) {
                        return list;
                    }
                    line = line.replace("\uFEFF", "").trim();
                    if (line.equalsIgnoreCase("q")) {
                        return list;
                    }
                    if (line.isEmpty()) {
                        System.err.println("Từ chối dữ liệu: Mã sản phẩm không được rỗng.");
                        continue;
                    }
                    code = line;
                    break;
                }

                String name;
                while (true) {
                    System.out.print("Nhập tên sản phẩm: ");
                    String line = reader.readLine();
                    if (line == null) {
                        return list;
                    }
                    line = line.replace("\uFEFF", "").trim();
                    if (line.isEmpty()) {
                        System.err.println("Từ chối dữ liệu: Tên sản phẩm không được rỗng.");
                        continue;
                    }
                    name = line;
                    break;
                }

                double price;
                while (true) {
                    System.out.print("Nhập đơn giá: ");
                    String line = reader.readLine();
                    if (line == null) {
                        return list;
                    }
                    line = line.replace("\uFEFF", "").trim();
                    try {
                        price = Double.parseDouble(line);
                        if (price <= 0) {
                            System.err.println("Từ chối dữ liệu: Đơn giá phải lớn hơn 0.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.err.println("Từ chối dữ liệu: Đơn giá không phải là số hợp lệ.");
                    }
                }

                int quantity;
                while (true) {
                    System.out.print("Nhập số lượng: ");
                    String line = reader.readLine();
                    if (line == null) {
                        return list;
                    }
                    line = line.replace("\uFEFF", "").trim();
                    try {
                        quantity = Integer.parseInt(line);
                        if (quantity < 0) {
                            System.err.println("Từ chối dữ liệu: Số lượng không được âm.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.err.println("Từ chối dữ liệu: Số lượng không phải là số nguyên hợp lệ.");
                    }
                }

                Product product = new Product(code, name, price, quantity);
                list.add(product);
                System.out.println("Đã thêm sản phẩm thành công: " + product);
            } catch (IOException e) {
                System.err.println("Lỗi đọc dữ liệu: " + e.getMessage());
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Từ chối dữ liệu: " + e.getMessage());
            }
        }
        return list;
    }

    public static void saveToCsv(Path path, List<Product> products) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write("ma,ten,donGia,soLuong");
                writer.newLine();
                for (Product p : products) {
                    writer.write(p.toCsv());
                    writer.newLine();
                }
                System.out.println("Đã lưu " + products.size() + " sản phẩm vào tệp " + path);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi tệp " + path + ": " + e.getMessage());
        }
    }

    public static List<Product> readFromCsv(Path path) {
        List<Product> list = new ArrayList<>();
        if (!Files.exists(path)) {
            System.err.println("Lỗi: Tệp " + path + " không tồn tại.");
            return list;
        }
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null) {
                System.err.println("Cảnh báo: Tệp " + path + " rỗng.");
                return list;
            }
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.replace("\uFEFF", "").trim();
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length != 4) {
                    System.err.println("Tệp " + path + " - Bỏ qua dòng " + lineNumber + ": Không đủ 4 cột (chỉ có " + parts.length + " cột).");
                    continue;
                }
                String code = parts[0].trim();
                String name = parts[1].trim();
                double price;
                int quantity;
                try {
                    price = Double.parseDouble(parts[2].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Tệp " + path + " - Bỏ qua dòng " + lineNumber + ": Đơn giá '" + parts[2].trim() + "' không phải là số hợp lệ.");
                    continue;
                }
                try {
                    quantity = Integer.parseInt(parts[3].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Tệp " + path + " - Bỏ qua dòng " + lineNumber + ": Số lượng '" + parts[3].trim() + "' không phải là số nguyên hợp lệ.");
                    continue;
                }
                try {
                    list.add(new Product(code, name, price, quantity));
                } catch (IllegalArgumentException e) {
                    System.err.println("Tệp " + path + " - Bỏ qua dòng " + lineNumber + " không hợp lệ: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc tệp " + path + ": " + e.getMessage());
        }
        return list;
    }

    public static void displayInventory(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống.");
            return;
        }
        System.out.println("=== DANH SÁCH SẢN PHẨM ===");
        double total = 0;
        int index = 1;
        for (Product p : products) {
            System.out.printf("%d. %s%n", index++, p);
            total += p.inventoryValue();
        }
        System.out.printf("Tổng giá trị tồn kho: %,.0f VND%n", total);
    }

    public static Product findMaxInventoryValue(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống.");
            return null;
        }
        Product maxProduct = products.get(0);
        for (Product p : products) {
            if (p.inventoryValue() > maxProduct.inventoryValue()) {
                maxProduct = p;
            }
        }
        System.out.println("Sản phẩm có giá trị tồn kho cao nhất: " + maxProduct);
        return maxProduct;
    }

    public static void writeReport(Path path, List<Product> products) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            double totalValue = 0;
            Product maxProduct = null;
            for (Product p : products) {
                totalValue += p.inventoryValue();
                if (maxProduct == null || p.inventoryValue() > maxProduct.inventoryValue()) {
                    maxProduct = p;
                }
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write("BÁO CÁO TỔNG HỢP TỒN KHO");
                writer.newLine();
                writer.write("=".repeat(40));
                writer.newLine();
                writer.write("Số lượng sản phẩm: " + products.size());
                writer.newLine();
                writer.write("Tổng giá trị tồn kho: %,.0f VND".formatted(totalValue));
                writer.newLine();
                if (maxProduct != null) {
                    writer.write("Sản phẩm có giá trị tồn kho cao nhất: %s (%s - %,.0f VND)".formatted(
                            maxProduct.getName(), maxProduct.getCode(), maxProduct.inventoryValue()));
                } else {
                    writer.write("Sản phẩm có giá trị tồn kho cao nhất: Không có");
                }
                writer.newLine();
                writer.newLine();
                writer.write("DANH SÁCH CHI TIẾT:");
                writer.newLine();
                int index = 1;
                for (Product p : products) {
                    writer.write("%d. %s - %s | Đơn giá: %,.0f VND | Số lượng: %d | Trị giá: %,.0f VND".formatted(
                            index++, p.getCode(), p.getName(), p.getUnitPrice(), p.getQuantity(), p.inventoryValue()));
                    writer.newLine();
                }
                System.out.println("Đã ghi báo cáo tổng hợp vào tệp " + path);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi báo cáo vào tệp " + path + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Path csvPath = Path.of("data", "inventory.csv");
        Path reportPath = Path.of("data", "inventory-report.txt");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        List<Product> products = new ArrayList<>();

        while (true) {
            System.out.println();
            System.out.println("========== INVENTORY MANAGER ==========");
            System.out.println("1. Nhập danh sách sản phẩm từ bàn phím");
            System.out.println("2. Lưu danh sách vào tệp data/inventory.csv");
            System.out.println("3. Đọc dữ liệu từ tệp data/inventory.csv");
            System.out.println("4. Hiển thị danh sách và tổng giá trị tồn kho");
            System.out.println("5. Tìm sản phẩm có giá trị tồn kho cao nhất");
            System.out.println("6. Ghi báo cáo tổng hợp vào data/inventory-report.txt");
            System.out.println("7. Thực hiện toàn bộ quy trình tuần tự");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng (0-7): ");
            try {
                String choice = reader.readLine();
                if (choice == null) {
                    System.out.println("Kết thúc chương trình.");
                    break;
                }
                choice = choice.replace("\uFEFF", "").trim();
                if (choice.equals("0")) {
                    System.out.println("Kết thúc chương trình.");
                    break;
                }
                switch (choice) {
                    case "1":
                        List<Product> inputList = inputFromKeyboard(reader);
                        products.addAll(inputList);
                        break;
                    case "2":
                        saveToCsv(csvPath, products);
                        break;
                    case "3":
                        products = readFromCsv(csvPath);
                        System.out.println("Đã đọc " + products.size() + " sản phẩm từ tệp CSV.");
                        break;
                    case "4":
                        displayInventory(products);
                        break;
                    case "5":
                        findMaxInventoryValue(products);
                        break;
                    case "6":
                        writeReport(reportPath, products);
                        break;
                    case "7":
                        List<Product> sampleList = inputFromKeyboard(reader);
                        if (!sampleList.isEmpty()) {
                            products = sampleList;
                        }
                        saveToCsv(csvPath, products);
                        products = readFromCsv(csvPath);
                        displayInventory(products);
                        findMaxInventoryValue(products);
                        writeReport(reportPath, products);
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
                        break;
                }
            } catch (IOException e) {
                System.err.println("Lỗi vào/ra: " + e.getMessage());
                break;
            }
        }
    }
}

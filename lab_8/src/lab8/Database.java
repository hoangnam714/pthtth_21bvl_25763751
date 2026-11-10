package lab8;

import java.sql.*;

/** Kết nối SQLite và đóng tài nguyên theo thứ tự ngược. */
public final class Database {
    private Database() {}

    public static Connection open(String file) throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + file);
    }

    public static void close(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try { resource.close(); }
                catch (Exception e) { System.err.println("Loi dong tai nguyen: " + e.getMessage()); }
            }
        }
    }

    public static void rollback(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); }
            catch (SQLException e) { System.err.println("Loi rollback: " + e.getMessage()); }
        }
    }

    public static void prepareProducts(Connection conn) throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE SanPham (maSP INTEGER PRIMARY KEY, tenSP TEXT NOT NULL, gia REAL CHECK(gia >= 0), soluong INTEGER CHECK(soluong >= 0))");
            stmt.executeUpdate("INSERT INTO SanPham VALUES (1,'Laptop',15000000,10)");
            stmt.executeUpdate("INSERT INTO SanPham VALUES (2,'Chuot',200000,30)");
            stmt.executeUpdate("INSERT INTO SanPham VALUES (3,'Ban phim',500000,20)");
        } finally { close(stmt); }
    }

    public static void printProducts(Connection conn) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM SanPham ORDER BY maSP");
            while (rs.next()) {
                System.out.printf("%d | %s | %.0f | %d%n", rs.getInt("maSP"),
                    rs.getString("tenSP"), rs.getDouble("gia"), rs.getInt("soluong"));
            }
        } finally { close(rs, stmt); }
    }
}

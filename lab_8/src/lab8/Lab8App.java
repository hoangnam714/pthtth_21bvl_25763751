package lab8;

import java.sql.*;
import java.util.Scanner;

/** Một ứng dụng menu dùng chung nhanvien.db. */
public class Lab8App {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            initialize();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Khong the khoi tao: " + e.getMessage());
            return;
        }
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nLAB 08 JDBC - QUAN LY DU LIEU");
                System.out.println("1.Danh sach SP  2.Them SP  3.Tim theo ten  4.Cap nhat SP");
                System.out.println("5.Xoa SP  6.Giao dich nhieu SP  7.Nhan vien  8.Thong ke luong  0.Thoat");
                try {
                    int choice = integer(in,"Chon: ");
                    if(choice==0) break;
                    switch(choice) {
                        case 1 -> products(null);
                        case 2 -> add(integer(in,"Ma SP: "),text(in,"Ten SP: "),number(in,"Gia: "),integer(in,"So luong: "));
                        case 3 -> products(text(in,"Ten can tim: "));
                        case 4 -> update(integer(in,"Ma SP: "),number(in,"Gia moi: "),integer(in,"So luong moi: "));
                        case 5 -> write("DELETE FROM SanPham WHERE maSP=?",integer(in,"Ma SP: "));
                        case 6 -> batch(in);
                        case 7 -> query("SELECT id,ten,chuc_vu,luong FROM NhanVien ORDER BY id");
                        case 8 -> statistics();
                        default -> System.out.println("Lua chon khong hop le.");
                    }
                } catch(NumberFormatException e) { System.out.println("Vui long nhap dung dinh dang so."); }
                catch(IllegalArgumentException e) { System.out.println(e.getMessage()); }
                catch(SQLException e) { System.out.println("Loi SQL: "+e.getMessage()); }
            }
        }
    }

    private static Connection open() throws SQLException { return Database.open("nhanvien.db"); }

    private static void initialize() throws SQLException {
        Connection conn=null; Statement stmt=null; ResultSet rs=null;
        try {
            conn=open(); conn.setAutoCommit(false); stmt=conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS NhanVien(id INTEGER PRIMARY KEY, ten TEXT NOT NULL, chuc_vu TEXT, luong REAL CHECK(luong>=0))");
            boolean hasSalary=false;
            rs=stmt.executeQuery("PRAGMA table_info(NhanVien)");
            while(rs.next()) if("luong".equals(rs.getString("name"))) hasSalary=true;
            rs.close(); rs=null;
            if(!hasSalary) stmt.executeUpdate("ALTER TABLE NhanVien ADD COLUMN luong REAL");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SanPham(maSP INTEGER PRIMARY KEY, tenSP TEXT NOT NULL CHECK(length(trim(tenSP))>0), gia REAL NOT NULL CHECK(gia>=0), soluong INTEGER NOT NULL CHECK(soluong>=0))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CaiDat(khoa TEXT PRIMARY KEY)");
            rs=stmt.executeQuery("SELECT 1 FROM CaiDat WHERE khoa='seed-v1'");
            boolean seeded=rs.next(); rs.close(); rs=null;
            if(!seeded) {
                stmt.executeUpdate("INSERT OR IGNORE INTO NhanVien VALUES(1,'Nguyen Van An','Lap trinh vien',15000000)");
                stmt.executeUpdate("INSERT OR IGNORE INTO NhanVien VALUES(2,'Tran Thi Binh','Ke toan',12000000)");
                stmt.executeUpdate("INSERT OR IGNORE INTO NhanVien VALUES(3,'Le Van Cuong','Lap trinh vien',20000000)");
                stmt.executeUpdate("UPDATE NhanVien SET luong=CASE id WHEN 1 THEN 15000000 WHEN 2 THEN 12000000 WHEN 3 THEN 20000000 ELSE 0 END WHERE luong IS NULL");
                stmt.executeUpdate("INSERT OR IGNORE INTO SanPham VALUES(1,'Laptop',15000000,10)");
                stmt.executeUpdate("INSERT OR IGNORE INTO SanPham VALUES(2,'Chuot',200000,30)");
                stmt.executeUpdate("INSERT OR IGNORE INTO SanPham VALUES(3,'Ban phim',500000,20)");
                stmt.executeUpdate("INSERT INTO CaiDat VALUES('seed-v1')");
            }
            conn.commit();
        } catch(SQLException e) { Database.rollback(conn); throw e; }
        finally { Database.close(rs,stmt,conn); }
    }

    static void products(String name) throws SQLException {
        if(name==null) query("SELECT maSP,tenSP,gia,soluong FROM SanPham ORDER BY maSP");
        else query("SELECT maSP,tenSP,gia,soluong FROM SanPham WHERE instr(lower(tenSP),lower(?))>0 ORDER BY maSP",name);
    }

    /** ResultSet, Statement và Connection luôn đóng trong finally. */
    static void query(String sql,Object... values) throws SQLException {
        Connection conn=null; PreparedStatement stmt=null; ResultSet rs=null;
        try {
            conn=open(); stmt=conn.prepareStatement(sql); bind(stmt,values); rs=stmt.executeQuery();
            ResultSetMetaData meta=rs.getMetaData(); boolean found=false;
            while(rs.next()) {
                found=true;
                for(int i=1;i<=meta.getColumnCount();i++) {
                    if(i>1) System.out.print(" | ");
                    System.out.print(rs.getString(i));
                }
                System.out.println();
            }
            if(!found) System.out.println("Khong tim thay du lieu.");
        } finally { Database.close(rs,stmt,conn); }
    }

    static void write(String sql,Object... values) throws SQLException {
        Connection conn=null; PreparedStatement stmt=null;
        try {
            conn=open(); stmt=conn.prepareStatement(sql); bind(stmt,values);
            System.out.println("So ban ghi thay doi: "+stmt.executeUpdate());
        } finally { Database.close(stmt,conn); }
    }

    static void add(int id,String name,double price,int quantity) throws SQLException {
        valid(price,quantity);
        write("INSERT INTO SanPham VALUES(?,?,?,?)",id,name,price,quantity);
    }

    static void update(int id,double price,int quantity) throws SQLException {
        valid(price,quantity);
        write("UPDATE SanPham SET gia=?,soluong=? WHERE maSP=?",price,quantity,id);
    }

    private static void batch(Scanner in) throws SQLException {
        int count=integer(in,"So SP cap nhat: ");
        if(count<2||count>1000) throw new IllegalArgumentException("Nhap tu 2 den 1000 SP.");
        int[] ids=new int[count], quantities=new int[count]; double[] prices=new double[count];
        for(int i=0;i<count;i++) {
            ids[i]=integer(in,"Ma SP: "); prices[i]=number(in,"Gia moi: ");
            quantities[i]=integer(in,"So luong moi: "); valid(prices[i],quantities[i]);
        }
        batchUpdate(ids,prices,quantities);
    }

    static void batchUpdate(int[] ids,double[] prices,int[] quantities) throws SQLException {
        Connection conn=null; PreparedStatement stmt=null;
        try {
            conn=open(); conn.setAutoCommit(false);
            stmt=conn.prepareStatement("UPDATE SanPham SET gia=?,soluong=? WHERE maSP=?");
            for(int i=0;i<ids.length;i++) {
                valid(prices[i],quantities[i]); bind(stmt,prices[i],quantities[i],ids[i]);
                if(stmt.executeUpdate()!=1) throw new SQLException("Khong tim thay ma SP "+ids[i]);
            }
            conn.commit(); System.out.println("Da COMMIT toan bo cap nhat.");
        } catch(SQLException | IllegalArgumentException e) {
            Database.rollback(conn); System.out.println("Da ROLLBACK toan bo giao dich."); throw e;
        } finally { Database.close(stmt,conn); }
    }

    static void statistics() throws SQLException {
        System.out.println("Tong luong | Trung binh | Thap nhat | Cao nhat");
        query("SELECT SUM(luong),AVG(luong),MIN(luong),MAX(luong) FROM NhanVien");
        System.out.println("Nhan vien co luong thap nhat / cao nhat:");
        query("SELECT ten,luong FROM NhanVien WHERE luong=(SELECT MIN(luong) FROM NhanVien) OR luong=(SELECT MAX(luong) FROM NhanVien)");
        System.out.println("So nhan vien theo chuc vu:");
        query("SELECT chuc_vu,COUNT(*) FROM NhanVien GROUP BY chuc_vu ORDER BY chuc_vu");
    }

    private static void bind(PreparedStatement stmt,Object... values) throws SQLException {
        for(int i=0;i<values.length;i++) stmt.setObject(i+1,values[i]);
    }
    private static String text(Scanner in,String prompt) {
        System.out.print(prompt); String value=in.nextLine().trim();
        if(value.isEmpty()) throw new IllegalArgumentException("Du lieu khong duoc rong.");
        return value;
    }
    private static int integer(Scanner in,String prompt) { return Integer.parseInt(text(in,prompt)); }
    private static double number(Scanner in,String prompt) { return Double.parseDouble(text(in,prompt)); }
    private static void valid(double price,int quantity) {
        if(!Double.isFinite(price)||price<0||quantity<0) throw new IllegalArgumentException("Gia va so luong phai khong am.");
    }
}

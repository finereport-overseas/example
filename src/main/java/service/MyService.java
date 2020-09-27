package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyService {
    // define connection context定义数据库连接参数
    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver"; // driver
    private static final String URL = "jdbc:mysql://localhost:3306/joe"; // url
    private static final String USERNAME = "joe"; // username
    private static final String PASSWORD = "123456"; // password
    // register the jdbc driver
    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    // get connection
    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    // close connection
    private static void closeConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // this method must be public
    public List<String> get() throws SQLException {
        List<String> data = new ArrayList<>();
        Connection connection = getConn();
        Statement statement = connection.createStatement();
        String sql = "select id, name from test";
        data.add("id name");
        try {
            if (statement != null) {
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    data.add(rs.getInt(1) + "  " + rs.getString(2).trim());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                closeConn(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }
    // test code
    public static void main(String[] args) {
        List<String> list;
        MyService service = new MyService();
        Connection conn = null;
        try {
            conn = MyService.getConn();
            list = service.get();
            for (String s : list) {
                System.out.println(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                closeConn(conn);
            }
        }
    }
}
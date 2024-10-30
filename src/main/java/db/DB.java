package db;

import Exceptions.DBException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static Connection conn = null;

    public static Connection getConnection(){
        if(conn == null){
            String user = System.getenv("user");
            String password = System.getenv("password");
            try {
                conn = DriverManager.getConnection("jdbc:sqlserver://;serverName=localhost;databaseName=JDBCtest;trustServerCertificate=true;", user, password);
            }catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return conn;
    }

    public static void autoClose(AutoCloseable item) throws DBException {
        if(item != null){
            try{
                item.close();
            }catch(Exception e){
                throw new DBException(e.getMessage());
            }
        }
    }

}

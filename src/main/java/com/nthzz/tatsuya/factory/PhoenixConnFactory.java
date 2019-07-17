package com.nthzz.tatsuya.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by robert on 26/03/2017.
 */
public class PhoenixConnFactory {

    public static Connection getPhoenixConn(String url, Boolean autoCommit) throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        conn.setAutoCommit(autoCommit);
        return conn;
    }

}
package com.buxbuddy.auth.config;



import java.sql.Connection;
import java.sql.DriverManager;

public class DbConfigCheck {

    public static void main(String[] args) {

        String url =
                "jdbc:sqlserver://SQL1003.site4now.net:1433;"
                        + "databaseName=db_ab1edf_buxbuddy;"
                        + "encrypt=true;"
                        + "trustServerCertificate=true";

        String username = "db_ab1edf_buxbuddy_admin";
        String password = "Naveen@2000";

        try (Connection connection =
                     DriverManager.getConnection(url, username, password)) {

            System.out.println("Database connected successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
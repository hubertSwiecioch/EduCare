package com.hswie.educaremobile.helper;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;


/**
 * Created by hswie on 08.10.2015.
 */
public class DatabaseConnector {

    private static final String HOSTNAME = "192.168.0.110";
    private static final String PORT = "3306";
    private static final String USERNAME = "hswie";
    private static final String PASSWORD = "hswie";
    private static final String DATABASE = "educare";
    private static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE;
    ;

    private static final String TAG = "DatabaseConnector";

    public String connectToDatabase(String login, String password) {


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.d(TAG, login + " " + password);
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            /* System.out.println("Database connection success"); */

            String result = "";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select Password from nurse where Login = '" + login + "'");

            while(rs.next()) {
                result = rs.getString("Password");
                Log.d(TAG, result);
                return  result;

            }

        }
        catch(Exception e) {
            e.printStackTrace();

        }

        return "";
    }

}


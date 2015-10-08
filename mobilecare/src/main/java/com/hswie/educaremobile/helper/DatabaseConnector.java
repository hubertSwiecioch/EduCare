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

    private static final String HOSTNAME = "192.168.1.201";
    private static final String PORT = "3306";
//    private static final String USERNAME = "b6fd11d286cd1b";
//    private static final String PASSWORD = "a8f6b4a1";
    private static final String DATABASE = "educare";
    private static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE;
    ;

    private static final String TAG = "DatabaseConnector";

    public String connectToDatabase(String username, String password) {


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.d(TAG, username + " " + password);
            Connection con = DriverManager.getConnection(URL, username, password);
            /* System.out.println("Database connection success"); */

            String result = "";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select Password from nurse where Login = '" + username + "'");

            Log.d(TAG, "Result" + result);

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


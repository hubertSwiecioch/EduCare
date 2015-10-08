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

    private static final String HOSTNAME = "eu-cdbr-azure-west-c.cloudapp.net";
    private static final String PORT = "3306";
    private static final String USERNAME = "b6fd11d286cd1b";
    private static final String PASSWORD = "a8f6b4a1";
    private static final String DATABASE = "EduCare";
    private static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE;
    private static final String CONNECTION_STRING = "Database=EduCare;Data Source=eu-cdbr-azure-west-c.cloudapp.net;" +
                                                    "User Id=b6fd11d286cd1b;Password=a8f6b4a1";

    private static final String TAG = "DatabaseConnector";

    public void connectToDatabase() {


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            /* System.out.println("Database connection success"); */

            String result = "Database connection success\n";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from nurse");
            ResultSetMetaData rsmd = rs.getMetaData();

            while(rs.next()) {
                result += rsmd.getColumnName(1) + ": " + rs.getInt(1) + "\n";
                result += rsmd.getColumnName(2) + ": " + rs.getString(2) + "\n";
                result += rsmd.getColumnName(3) + ": " + rs.getString(3) + "\n";
            }

            Log.d(TAG, result);

        }
        catch(Exception e) {
            e.printStackTrace();

        }


    }

}


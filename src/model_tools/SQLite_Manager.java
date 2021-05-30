
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.SQLiteException;

/**
 *
 * @author yonij
 */
public class SQLite_Manager {

    private static Connection con;
    private static boolean hasData = false;

    public ResultSet displayTrail() throws SQLException, ClassNotFoundException {
        if (con == null) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("select trail_name, features from AllTrails");
        return res;
    }

    private void getConnection() throws ClassNotFoundException, SQLException {
        // sqlite driver
        Class.forName("org.sqlite.JDBC");
        // database path, if it's a new database, it will be created in the project folder
        con = DriverManager.getConnection("jdbc:sqlite:trails.db");
        initialise();
    }

    // adding a trail
    public void addTrail(int trail_id, String trail_name, String area_name, String city_name, String state_name, double length, double difficulty, String features, String activities) throws ClassNotFoundException, SQLException {
        if (con == null) {
            // get connection
            getConnection();
        }
        PreparedStatement prep = con.prepareStatement("INSERT INTO ALLTRAILS(trail_id, trail_name, area_name, "
                + "city_name, state_name, length, difficulty, features, activities)values(?,?,?,?,?,?,?,?,?);");
        prep.setInt(1, trail_id);
        prep.setString(2, trail_name);
        prep.setString(3, area_name);
        prep.setString(4, city_name);
        prep.setString(5, state_name);
        prep.setDouble(6, length);
        prep.setDouble(7, difficulty);
        prep.setString(8, features);
        prep.setString(9, activities);
        prep.execute();
    }

    // update trail
    public void updateTrail(int trail_id, String trail_name, String area_name, String city_name, String state_name, double length, double difficulty, String features, String activities) throws ClassNotFoundException, SQLException {
        if (con == null) {
            // get connection
        }
        getConnection();

        PreparedStatement prep = con.prepareStatement("UPDATE ALLTRAILS SET "
                + "trail_name = ?, "
                + "area_name = ?, "
                + "city_name = ?, "
                + "state_name = ?, "
                + "length = ?, "
                + "difficulty = ?, "
                + "features = ?, "
                + "activities = ?"
                + "WHERE trail_id = ?;");

        prep.setString(1, trail_name);
        prep.setString(2, area_name);
        prep.setString(3, city_name);
        prep.setString(4, state_name);
        prep.setDouble(5, length);
        prep.setDouble(6, difficulty);
        prep.setString(7, features);
        prep.setString(9, activities);
        prep.setInt(9, trail_id);
        prep.executeUpdate();

    }


    // returns only the states
    public ResultSet getStates() throws ClassNotFoundException{
        try {
            if(con == null){
                getConnection();
            }
            Statement state = con.createStatement();
            ResultSet res = state.executeQuery("SELECT DISTINCT state_name FROM 'AllTrails'");
            return res;
        } catch (SQLException ex) {
            System.out.println("getStates ---> " + ex.getMessage());
        }
        return null;
    }
    
    // handles select statements, returns a resultset
    public ResultSet executeSelectStatement(String sqlStatement) throws ClassNotFoundException{
        try {
            if(con == null){
                getConnection();
            }
            Statement state = con.createStatement();
            ResultSet res = state.executeQuery(sqlStatement);
            return res;
        } catch (SQLException ex) {
            System.out.println("getStates ---> " + ex.getMessage());
        }
        return null;
    }
    // returns true if table exists
    public boolean tableExists() throws SQLException {
        try {
            Statement state = con.createStatement();
            ResultSet res = state.executeQuery("SELECT ALL trail_name FROM 'AllTrails'");
        } catch (SQLiteException s) {
            System.out.println("checking if the table exists exception --- > " + s.getMessage());
            try {
                String sqlCreateTable = "CREATE TABLE IF NOT EXISTS AllTrails"
                        + "(trail_id INTEGER PRIMARY KEY,"
                        + " trail_name VARCHAR(60) NOT NULL, "
                        + " area_name VARCHAR(60) NOT NULL, "
                        + " city_name VARCHAR(30) NOT NULL, "
                        + " state_name VARCHAR(30) NOT NULL, "
                        + " length REAL NOT NULL, "
                        + " difficulty INTEGER NOT NULL, "
                        + " features VARCHAR(100) NOT NULL, "
                        + " activities VARCHAR(100) NOT NULL);";
                Statement state2 = con.createStatement();
                state2.executeUpdate(sqlCreateTable);
            } catch (SQLiteException b) {
                // need to build the table
                System.out.println("initialise()  -->> " + b.getMessage());
            }
            return false;
        }
        return true;
    }
    // initialize the databse
    public void initialise() throws SQLException {
        if (tableExists() && !hasData) {

            hasData = true;
            CSVtoDB();
        }
    }
    // import data from CSV to sqlite
    public void CSVtoDB() {
        int id;
        String name;
        String area_name;
        String city;
        String state;
        double length;
        int difficulty;
        String features;
        String activities;

        String line = "";
        String splitBy = ",";
        try {

            //parsing a CSV file into BufferedReader class constructor     
            BufferedReader br = new BufferedReader(new FileReader("src\\trail_data\\nationalpark.csv"));

            while ((line = br.readLine()) != null) //returns a Boolean value  
            {

                String[] trailInfo = line.split(splitBy);    // use comma as separator  
                id = parseInt(trailInfo[0]);
                name = trailInfo[1];
                area_name = trailInfo[2];
                city = trailInfo[3];
                state = trailInfo[4];
                length = parseDouble(trailInfo[8]);
                length /= 1760; // converting yards to miles
                difficulty = parseInt(trailInfo[10]);
                features = trailInfo[15];
                activities = trailInfo[16];

                PreparedStatement prep = con.prepareStatement("insert into AllTrails values(?,?,?,?,?,?,?,?,?);");
                prep.setInt(1, id);
                prep.setString(2, name);
                prep.setString(3, area_name);
                prep.setString(4, city);
                prep.setString(5, state);
                prep.setDouble(6, length);
                prep.setInt(7, difficulty);
                prep.setString(8, features); //featuresArray
                prep.setString(9, activities);// activitiesArray
                prep.executeUpdate();
            }
        } catch (IOException e) {
            System.out.println("CSVtoDB IOException e  -->> " + e.getMessage());
            
            e.printStackTrace();
        } catch (SQLiteException ex) {
            System.out.println("CSVtoDB SQLiteException  -->> " + ex.getMessage());
            System.out.println("CSVtoDB SQLiteException  -->> " + ex.getSQLState());
        } catch (SQLException ex) {
            Logger.getLogger(SQLite_Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

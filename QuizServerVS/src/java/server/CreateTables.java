/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.*;

/**
 *
 * @author Mike
 */
public class CreateTables {
    //  Database credentials
    static final String DB_URL = "jdbc:derby://localhost:1527/QuizServerDB";
    static final String USER = "test";
    static final String PASS = "test";
    String dbName = "TEST";
    
    Connection conn = null;
    
    CreateTables() {
        try{
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }//end try            
    }
    
    public void createUsers() {
        String createString = 
        "create table " + dbName +
        ".USERS " +
        "(USERS_ID INTEGER NOT NULL, " +
        "FIRSTNAME VARCHAR(25) NOT NULL, " +
        "LASTNAME VARCHAR(25) NOT NULL, " +
        "PRIMARY KEY (USERS_ID))";
        
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(createString);        
        } catch (SQLException e){
            
        }
    }
}

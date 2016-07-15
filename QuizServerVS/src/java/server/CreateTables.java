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
        }catch(SQLException e){
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }//end try            
    }
    
    public void createTables() {
        try{
            Statement stmt = conn.createStatement();
       
            // Drop all Tables
            stmt.executeUpdate(dropAnswer());
            stmt.executeUpdate(dropQuestion());
            stmt.executeUpdate(dropQuiz());
            stmt.executeUpdate(dropUsers());    


            //Create All Tables
            stmt.executeUpdate(createUsers());
            stmt.executeUpdate(createQuiz());
            stmt.executeUpdate(createQuestion());
            stmt.executeUpdate(createAnswer());
            
            conn.close();
        } catch (SQLException e){
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
    
    private String dropUsers(){
        String dropTables = 
                "DROP TABLE " + dbName + ".USERS";
        return dropTables;
    }
    
    private String dropQuiz(){
        String dropTables = 
                "DROP TABLE " + dbName + ".Quiz";
        return dropTables;
    }
    
    private String dropQuestion(){
        String dropTables = 
                "DROP TABLE " + dbName + ".Question";
        return dropTables;
    }
    
    private String dropAnswer(){
        String dropTables = 
                "DROP TABLE " + dbName + ".Answer";
        return dropTables;
    }
    
    private String createUsers() {
        String createUsers = 
            "CREATE TABLE " + dbName +
            ".USERS " +
            "(USERS_ID BIGINT NOT NULL, " +
            "FIRSTNAME VARCHAR(25) NOT NULL, " +
            "LASTNAME VARCHAR(25) NOT NULL, " +
            "PRIMARY KEY (USERS_ID))";
        
        return createUsers;
    }
    
    private String createQuiz() {
        String createQuiz =
                "CREATE TABLE " + dbName +
                ".QUIZ " +
                "(QUIZ_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                "USERS_ID_F BIGINT NOT NULL, " +
                "NAME VARCHAR(25) NOT NULL, " +
                "PRIMARY KEY (QUIZ_ID, USERS_ID_F), " +
                "FOREIGN KEY (USERS_ID_F) REFERENCES " + dbName +
                ".USERS (USERS_ID) ON DELETE CASCADE)";
        
        return createQuiz;
    }
    
    private String createQuestion(){
        String createQuestion = 
                "CREATE TABLE " + dbName +
                ".QUESTION " +
                "(QUESTION_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                "QUIZ_ID_F INTEGER NOT NULL, " +
                "USERS_ID_F BIGINT NOT NULL, " +
                "QUESTION VARCHAR(500) NOT NULL, " +
                "PRIMARY KEY (QUESTION_ID, QUIZ_ID_F, USERS_ID_F), " +
                "FOREIGN KEY (QUIZ_ID_F, USERS_ID_F) REFERENCES " + dbName + 
                ".QUIZ (QUIZ_ID, USERS_ID_F) ON DELETE CASCADE)";
        return createQuestion;
    }
    
    private String createAnswer(){
        String createAnswer = 
                "CREATE TABLE " + dbName +
                ".ANSWER " +
                "(ANSWER_ID INTEGER NOT NULL, " +
                "QUESTION_ID_F INTEGER NOT NULL, " +
                "QUIZ_ID_F INTEGER NOT NULL, "+
                "USERS_ID_F BIGINT NOT NULL, " +
                "ANSWER VARCHAR(250) NOT NULL, " +
                "CORRECT BOOLEAN NOT NULL, " +
                "PRIMARY KEY (ANSWER_ID, QUESTION_ID_F, QUIZ_ID_F, USERS_ID_F), " +
                "FOREIGN KEY (QUESTION_ID_F, QUIZ_ID_F, USERS_ID_F) REFERENCES " + dbName + 
                ".QUESTION (QUESTION_ID, QUIZ_ID_F, USERS_ID_F) ON DELETE CASCADE)";  
        return createAnswer;
    }
}

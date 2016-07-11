package server;

import java.sql.*;
import java.util.ArrayList;
import server.entities.Quiz;

public class QuizServerDB {
    
    //  Database credentials
    static final String DB_URL = "jdbc:derby://localhost:1527/QuizServerDB";
    static final String USER = "test";
    static final String PASS = "test";
    
    // Querie variables
    String dbName = "TEST";
    Connection conn = null;
    
    
    QuizServerDB () {      
        try{
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");   
        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }//end try       
    }
    
    boolean checkUser(String id, String name) {
        try{
            String checkUserSql = "SELECT USERS_ID FROM " + dbName +".USERS " +
                "WHERE USERS_ID = " + id;
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkUserSql);
            
            if(rs.next()){
                return true;
            } else {
                String[] parts = name.split(" ");
                String firstname = parts[0];
                String lastname = parts[1];
                
                String insertUserSql = "INSERT INTO "+ dbName + ".USERS " +
                          "VALUES ("+ id +", '"+ firstname +"', '"+ lastname +"')";
                
                stmt.executeUpdate(insertUserSql);
            }
        }catch(SQLException e){
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    ArrayList getAllQuizFromUser(String User_Id) { 
        try (Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery( "SELECT NAME, QUIZ_ID FROM TEST.QUIZ WHERE USERS_ID_f = "+User_Id);
            ArrayList<Quiz> quizList = new ArrayList<>();
            
            while (rs.next()) {
                Quiz quiz = new Quiz(rs.getInt("QUIZ_ID"), rs.getString("NAME"));
                quizList.add(quiz);
            }
            return quizList;
        }
        catch ( SQLException e ) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    // Hier anknüpfen
    void updateQuestionWithAnswers(String Question, String Question_Id, String Answer1, String Answer2, String Answer3, String Answer4) {
        try{
            System.out.println("Update Question with Answers...");
            Statement stmt = conn.createStatement();
            String sql = "UPDATE QUESTION " +
                         "SET question = "+Question+" WHERE Question_Id = "+Question_Id+" ";
            stmt.executeUpdate(sql);
            System.out.println("Updated records in Questions-Table...");
            stmt.close();
        } catch(SQLException se){
            //Handle errors
            se.printStackTrace();
        }

        
    } // updateQuestionWithAnswers
    
} // Class QuizServerDB
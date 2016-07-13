package server;

import java.io.StringReader;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.*;

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
        }catch(SQLException e){
           //Handle errors for JDBC
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
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
    

    String getAllQuizFromUser(String User_Id) { 

        try (Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery( "SELECT NAME, QUIZ_ID FROM TEST.QUIZ WHERE USERS_ID_f = "+User_Id);
            JsonArrayBuilder jsArrB = Json.createArrayBuilder();
            JsonArray jsonArray;
            
            while (rs.next()) {
                jsArrB.add(Json.createObjectBuilder()
                                .add("quiz_id", rs.getInt("QUIZ_ID"))
                                .add("name", rs.getString("NAME")));
                
            }
            jsonArray = jsArrB.build();
            return jsonArray.toString();
        }
        catch ( SQLException e ) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    void createQuiz(String jsonString) {
        // Credentials
        String usersId;
        String quizName;              
        
        try (Statement stmt = conn.createStatement()) {
            JsonObject jsObj = Json.createReader(new StringReader(jsonString)).readObject();
            usersId = jsObj.getString("users_id");
            quizName = jsObj.getString("quiz_name");
            
            String sql = "INSERT INTO " + dbName + ".QUIZ " +
                    "Values (" + usersId + "'quizName')";
            
            
        } catch (SQLException e) {
            System.err.println("Got an exception");
            System.err.println(e.getMessage());
        }
    }
    
    
    
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
    
    void insertQuestion(String users_id, String quiz_id, String quest_id, String quest_name, String answer1, Boolean cor1, String answer2, Boolean cor2, String answer3, Boolean cor3, String answer4, Boolean cor4) {
        try(Statement stmt = conn.createStatement()) {
            String question = "INSERT INTO " + dbName + ".QUESTION " +
                    "VALUES (" + quest_id + ", " + quiz_id + ", " + users_id + ", '" + quest_name + "')";
            String a1 = "INSERT INTO " +dbName + ".ANSWER " +
                    "VALUES (1, " + quest_id + ", " + quiz_id + ", " + users_id + ", '" + answer1 + "', " + cor1 + ")";
            String a2 = "INSERT INTO " +dbName + ".ANSWER " +
                    "VALUES (2, " + quest_id + ", " + quiz_id + ", " + users_id + ", '" + answer2 + "', " + cor2 + ")";
            String a3 = "INSERT INTO " +dbName + ".ANSWER " +
                    "VALUES (3, " + quest_id + ", " + quiz_id + ", " + users_id + ", '" + answer3 + "', " + cor3 + ")";
            String a4 = "INSERT INTO " +dbName + ".ANSWER " +
                    "VALUES (4, " + quest_id + ", " + quiz_id + ", " + users_id + ", '" + answer4 + "', " + cor4 + ")";
            
            stmt.executeUpdate(a1);
            stmt.executeUpdate(a2);
            stmt.executeUpdate(a3);
            stmt.executeUpdate(a4);            
        } catch (SQLException e) {
            System.err.println("We got an exception!");
            System.err.println(e.getMessage());
        }
            
    }
    
} // Class QuizServerDB
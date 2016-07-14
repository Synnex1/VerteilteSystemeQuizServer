package server;

import java.io.StringReader;
import java.math.BigDecimal;
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
    
    public boolean checkUser(String id, String name) {
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
    

    public String getAllQuizFromUser(String User_Id) { 

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
    
    public void createQuiz(String jsonString, String userId) {
        // Credentials
        String sql;
        int quizId = 0;
        String quizName;
        int questionId = 0;
        String question;
        String answer;
        Boolean correct;
        int answerId = 1;
        PreparedStatement stmt;
        ResultSet rs;

        JsonObject jsObjQuiz = Json.createReader(new StringReader(jsonString)).readObject();
        JsonArray jsArrQ = jsObjQuiz.getJsonArray("questions");
        JsonObject jsObjQ;
        JsonArray jsArrA;
        JsonObject jsObjA;
        
        quizName = jsObjQuiz.getString("quiz_name");
        try {
            // Insert quiz into quiz table
            sql = "INSERT INTO " + dbName + ".QUIZ (USERS_ID_F, NAME) " +
                    "Values (?,?)";

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, userId);
            stmt.setString(2, quizName);
            stmt.execute();
            rs = stmt.getGeneratedKeys();
            if(rs.next()){
                quizId = rs.getInt(1);
            } else {
                System.err.println("EIN FEHLER!");
            }
            rs.close();
            
            for (int i = 0; i < jsArrQ.size(); i++) {
                jsObjQ = jsArrQ.getJsonObject(i);
                question = jsObjQ.getString("question");
                jsArrA = jsObjQ.getJsonArray("answers");
                
                // Insert question into table question
                sql = "INSERT INTO " + dbName + ".QUESTION (QUIZ_ID_F, USERS_ID_F, QUESTION) " +
                        "VALUES (?,?,?)";
                stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, quizId);
                stmt.setString(2, userId);
                stmt.setString(3, question);
                stmt.execute();
                rs = stmt.getGeneratedKeys();
                if ( rs.next() ) {
                    questionId = rs.getInt(1);
                } else {
                    System.err.println("EIN FEHLER 2!");
                }
                rs.close();
                
                // Insert answers into table answer
                for (int j = 0; j < jsArrA.size(); j++) {
                    jsObjA = jsArrA.getJsonObject(j);
                    answer = jsObjA.getString("answer");
                    correct = jsObjA.getBoolean("correct");
                    
                    sql = "INSERT INTO " + dbName + ".ANSWER (ANSWER_ID, QUESTION_ID_F, QUIZ_ID_F, USERS_ID_F, ANSWER, CORRECT) " +
                            "VALUES (?,?,?,?,?,?)";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, answerId);
                    stmt.setInt(2, questionId);
                    stmt.setInt(3, quizId);
                    stmt.setString(4, userId);
                    stmt.setString(5, answer);
                    stmt.setBoolean(6, correct);
                    stmt.execute();
                    answerId++;
                }
                
            }
            
            
        } catch (SQLException e) {
            System.err.println("Got an exception");
            System.err.println(e.getMessage());
        }
    }
    
    public String getQuizInfo(int quizId) {
        PreparedStatement stmt;
        PreparedStatement stmt2;
        String sql;
        String sql2;
        ResultSet rs;
        ResultSet rs2;
        
        // credentials
        // quiz
        String quizName;
        // question 
        int questionId;
        String question;
        // answer
        int answerId;
        String answer;
        Boolean correct;
        
        JsonObjectBuilder jsObjQBuilder = Json.createObjectBuilder();
        JsonArrayBuilder jsArrQueBuilder = Json.createArrayBuilder();
        JsonArrayBuilder jsArrABuilder = Json.createArrayBuilder();
        
        try {
            // Quiz credentials
            sql = "Select NAME FROM " + dbName + ".Quiz " +
                    "WHERE QUIZ_ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quizId);
            rs = stmt.executeQuery();
            if( rs.next() ) {
                quizName = rs.getString("NAME");
                jsObjQBuilder.add("quiz_id", quizId);
                jsObjQBuilder.add("quiz_name", quizName);
            }
            
            // question credentials
            sql = "SELECT QUESTION_ID, QUESTION FROM " + dbName + ".Question " +
                    "WHERE QUIZ_ID_F = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quizId);
            rs = stmt.executeQuery();
            
            // answer credentials 
            sql2 = "SELECT ANSWER_ID, ANSWER, CORRECT FROM " + dbName + ".ANSWER " +
                    "WHERE QUESTION_ID_F = ?";
            stmt2 = conn.prepareStatement(sql2);
            while( rs.next() ) {
                System.out.println("Wasdalos hier");
                questionId = rs.getInt("QUESTION_ID");
                question = rs.getString("QUESTION");
                              
                stmt2.setInt(1, questionId);
                rs2 = stmt2.executeQuery();
                
                while ( rs2.next() ) {
                    System.out.println("Wasdaloshier2");
                    answerId = rs2.getInt("ANSWER_ID");
                    answer = rs2.getString("ANSWER");
                    correct = rs2.getBoolean("CORRECT");
                    jsArrABuilder.add(Json.createObjectBuilder()
                        .add("answer_id", answerId)
                        .add("answer", answer)
                        .add("correct", correct));
                }
                jsArrQueBuilder.add(Json.createObjectBuilder()
                        .add("question_id", questionId)
                        .add("question", question)
                        .add("answers", jsArrABuilder));  
                
            }
            jsObjQBuilder.add("questions", jsArrQueBuilder);
            
        } catch (SQLException e) {
            System.err.println("Got an exception");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        String json = jsObjQBuilder.build().toString();
        return json;
    }
    
    public void updateQuiz(int quizId, String quizName) {
        PreparedStatement stmt;
        String sql = "UPDATE " + dbName + ".QUIZ " +
                "SET NAME = ? " +
                "WHERE QUIZ_ID = ?";
        try{
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, quizName);
            stmt.setInt(2, quizId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
    
    public void updateQuestions(String jsonString) {
        JsonObject jsObj = Json.createReader(new StringReader(jsonString)).readObject();
        JsonArray jsArr = jsObj.getJsonArray("answers");
        JsonObject jsObjA;
        
        int questionId = jsObj.getInt("question_id");
        String question = jsObj.getString("question");
        int answerId;
        String answer;
        Boolean correct;
        
        String sql;
        PreparedStatement stmt;
        sql = "UPDATE " + dbName + ".QUESTION " +
                "SET QUESTION = ? " +
                "WHERE QUESTION_ID = ?";
        try{
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, question);
            stmt.setInt(2, questionId);
            stmt.executeQuery();
            
            sql = "UPDATE " + dbName + ".ANSWER " +
                    "SET ANSWER = ?, CORRECT = ? " +
                    "WHERE ANSWER_ID = ?";
            stmt = conn.prepareStatement(sql);
            for(int i = 0; i < jsArr.size(); i++) {
                jsObjA = jsArr.getJsonObject(i);
                answerId = jsObjA.getInt("answer_id");
                answer = jsObjA.getString("answer");
                correct = jsObjA.getBoolean("correct");
                
                stmt.setString(1, answer);
                stmt.setBoolean(2, correct);
                stmt.setInt(3, answerId);
                stmt.executeUpdate();
            }
            
        } catch (SQLException e) {
            System.err.println("Got an exception");
            System.err.println(e.getMessage());
        }
        
    }
    
} // Class QuizServerDB
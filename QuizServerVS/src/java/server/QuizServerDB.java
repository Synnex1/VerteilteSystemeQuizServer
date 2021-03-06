package server;

import java.io.StringReader;
import java.sql.*;
import javax.json.*;

/**
 * Database connection class.
 * 
 * @author Mike
 */
public class QuizServerDB {
    
    //  Database credentials
    static final String DB_URL = "jdbc:derby://localhost:1527/QuizServerDB";
    static final String USER = "test";
    static final String PASS = "test";
    
    // Querie variables
    String dbName = "TEST";
    Connection conn = null;
    
    /**
     *
     */
    public QuizServerDB () {      
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
    
    /**
     * Closes the connection to the database.
     */
    public void closeConn() {
        try {
            System.out.println("Closing connection to selected Database...");
            conn.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Checking if a user already exists in the database. Otherwise creates an entry of a new user in the database with the given paramaeters.
     *
     * @param id Facebook-id of the user.
     * @param name Facebook-name of the user.
     * @return Returns true if there is a user or a new user is created, false if a problem occured.
     */
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
    
    /**
     * Returns the full name of a user.
     *
     * @param userId Id of the user.
     * @return
     */
    public String getUserName(String userId) {
        String name = null;
        
        PreparedStatement stmt;
        ResultSet rs;
        String sql = "SELECT FIRSTNAME, LASTNAME FROM " + dbName + ".USERS " +
                "WHERE USERS_ID = ?";
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                name = rs.getString("FIRSTNAME") + " " + rs.getString("LASTNAME");
            }
            
        } catch (SQLException e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        
        return name;
    }
    
    /**
     * Returns the id and names of all quiz created by the user specifed by the given id.
     * 
     * @param User_Id 
     * @return Returns a JSON string of an array including all quiz created by the given user.
     */
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
    
    /**
     * Creates a new quiz entry in the database.
     *
     * @param jsonString JSON string containing the information of the quiz, questions and their answers.
     * @param userId 
     */
    public void createQuiz(String jsonString, String userId) {
        // Credentials
        int quizId = 0;
        String quizName;
        int questionId = 0;
        String question;
        String answer;
        Boolean correct;
        
        String sql;
        String sql2;
        PreparedStatement stmt;
        PreparedStatement stmt2;
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
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if(rs.next()){
                quizId = rs.getInt(1);
            } else {
                System.err.println("EIN FEHLER!");
            }
            rs.close();
            
            // Insert question into table question
            sql = "INSERT INTO " + dbName + ".QUESTION (QUIZ_ID_F, USERS_ID_F, QUESTION) " +
                "VALUES (?,?,?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < jsArrQ.size(); i++) {
                jsObjQ = jsArrQ.getJsonObject(i);
                question = jsObjQ.getString("question");
                jsArrA = jsObjQ.getJsonArray("answers");

                stmt.setInt(1, quizId);
                stmt.setString(2, userId);
                stmt.setString(3, question);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if ( rs.next() ) {
                    questionId = rs.getInt(1);
                } else {
                    System.err.println("EIN FEHLER 2!");
                }
                
                sql2 = "INSERT INTO " + dbName + ".ANSWER (ANSWER_ID, QUESTION_ID_F, QUIZ_ID_F, USERS_ID_F, ANSWER, CORRECT) " +
                            "VALUES (?,?,?,?,?,?)";
                stmt2 = conn.prepareStatement(sql2);
                
                // Insert answers into table answer
                for (int j = 0, answerId = 1; j < jsArrA.size(); j++) {
                    jsObjA = jsArrA.getJsonObject(j);
                    answer = jsObjA.getString("answer");
                    correct = jsObjA.getBoolean("correct");

                    stmt2.setInt(1, answerId);
                    stmt2.setInt(2, questionId);
                    stmt2.setInt(3, quizId);
                    stmt2.setString(4, userId);
                    stmt2.setString(5, answer);
                    stmt2.setBoolean(6, correct);
                    stmt2.executeUpdate();
                    answerId++;
                }
                
            }
            
            
        } catch (SQLException e) {
            System.err.println("Got an exception");
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Returns a JSON string of the quiz with all questions and answers given by the quiz-id.
     *
     * @param quizId Id that specifies a quiz.
     * @return Returns a JSON string of the quiz and its questions and answers.
     */
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
                questionId = rs.getInt("QUESTION_ID");
                question = rs.getString("QUESTION");
                              
                stmt2.setInt(1, questionId);
                rs2 = stmt2.executeQuery();
                
                while ( rs2.next() ) {
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
    
    /**
     * Returns a JsonObject of all questions and answers to a quiz specified by the given quiz-id.
     *
     * @param quizId 
     * @return Returns a JsonObject, that contains questions and answers.
     */
    public JsonObject getQuestions(int quizId) {
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
                questionId = rs.getInt("QUESTION_ID");
                question = rs.getString("QUESTION");
                              
                stmt2.setInt(1, questionId);
                rs2 = stmt2.executeQuery();
                
                while ( rs2.next() ) {
                    answerId = rs2.getInt("ANSWER_ID");
                    answer = rs2.getString("ANSWER");
                    correct = rs2.getBoolean("CORRECT");
                    jsArrABuilder.add(Json.createObjectBuilder()
                        .add("answer_id", answerId)
                        .add("answer", answer)
                        .add("correct", correct));
                }
                jsArrQueBuilder.add(Json.createObjectBuilder()
                        .add("question", question)
                        .add("answers", jsArrABuilder));  
                
            }
            jsObjQBuilder.add("questions", jsArrQueBuilder);
            
        } catch (SQLException e) {
            System.err.println("Got an exception");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        return jsObjQBuilder.build();
    }
    
    /**
     * Updates the name of the specified quiz given by the quiz-id.
     *
     * @param quizId
     * @param quizName 
     */
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
    
    /**
     * Updates a question and its answers in the database, specified by the question-id given in the JSON string.
     *
     * @param jsonString JSON string that contains the information about the question and its answers.
     */
    public void updateQuestion(String jsonString) {
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
            stmt.executeUpdate();
            
            sql = "UPDATE " + dbName + ".ANSWER " +
                    "SET ANSWER = ?, CORRECT = ? " +
                    "WHERE QUESTION_ID_F = ? AND ANSWER_ID = ?";
            stmt = conn.prepareStatement(sql);
            for(int i = 0; i < jsArr.size(); i++) {
                jsObjA = jsArr.getJsonObject(i);
                answerId = jsObjA.getInt("answer_id");
                answer = jsObjA.getString("answer");
                correct = jsObjA.getBoolean("correct");
                
                stmt.setString(1, answer);
                stmt.setBoolean(2, correct);
                stmt.setInt(3, questionId);
                stmt.setInt(4, answerId);
                stmt.executeUpdate();
            }
            
        } catch (SQLException e) {
            System.err.println("Got an exception");
            System.err.println(e.getMessage());
        }
        
    }
    
    /**
     * Creates a new question entry in the database.
     *
     * @param jsonString JSON string that contains the information about the question and its answers.
     * @param userId 
     */
    public void createQuestions(String jsonString, String userId) {
        JsonObject jsObj = Json.createReader(new StringReader(jsonString)).readObject();
        JsonArray jsArrQue = jsObj.getJsonArray("questions");
        JsonObject jsObjQue;
        JsonArray jsArrA;
        JsonObject jsObjA;
        
        int quizId = jsObj.getInt("quiz_id");
        int questionId = 0;
        String question;
        String answer;
        Boolean correct;
        
        String sqlQuestion = "INSERT INTO " + dbName + ".QUESTION (QUIZ_ID_F, USERS_ID_F, QUESTION) " + 
                "VALUES (?, ?, ?)";
        String sqlAnswer = "INSERT INTO " + dbName + ".ANSWER (ANSWER_ID, QUESTION_ID_F, QUIZ_ID_F, USERS_ID_F, ANSWER, CORRECT) " +
                            "VALUES (?,?,?,?,?,?)";
        PreparedStatement stmt;
        PreparedStatement stmt2;
        ResultSet rs;
        
        try {
            stmt = conn.prepareStatement(sqlQuestion, Statement.RETURN_GENERATED_KEYS);
            stmt2 = conn.prepareStatement(sqlAnswer);
            for (int i = 0; i < jsArrQue.size(); i++) {
                jsObjQue = jsArrQue.getJsonObject(i);
                question = jsObjQue.getString("question");
                jsArrA = jsObjQue.getJsonArray("answers");
                
                stmt.setInt(1, quizId);
                stmt.setString(2, userId);
                stmt.setString(3, question);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if(rs.next()) {
                    questionId = rs.getInt(1);
                }
                if(questionId != 0) {
                    for (int j = 0, answerId = 1 ; j < jsArrA.size(); j++) {
                    jsObjA = jsArrA.getJsonObject(j);
                    answer = jsObjA.getString("answer");
                    correct = jsObjA.getBoolean("correct");
                    
                    stmt2.setInt(1, answerId);
                    stmt2.setInt(2, questionId);
                    stmt2.setInt(3, quizId);
                    stmt2.setString(4, userId);
                    stmt2.setString(5, answer);
                    stmt2.setBoolean(6, correct);
                    stmt2.executeUpdate();
                    answerId++;
                    }
                } else {
                    System.err.println("Got an Exception at createQuestions!");
                } 
                
            }
        } catch (SQLException e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Deletes a quiz from the database specified by the given quiz-id.
     *
     * @param quizId
     */
    public void deleteQuiz(int quizId) {
        String sql = "DELETE FROM " + dbName + ".QUIZ " +
                "WHERE QUIZ_ID = ?";
        PreparedStatement stmt;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quizId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Deletes a question specified by the given question-id.
     *
     * @param questionId
     */
    public void deleteQuestion(int questionId) {
        String sql = "DELETE FROM " + dbName + ".QUESTION " +
                "WHERE QUESTION_ID = ?";
        PreparedStatement stmt;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, questionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
} // Class QuizServerDB
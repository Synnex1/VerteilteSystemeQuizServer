package server;

import java.sql.*;

public class QuizServerDB {
    
    //  Database credentials
    static final String DB_URL = "jdbc:derby://localhost:1527/QuizServerDB";
    static final String USER = "test";
    static final String PASS = "test";
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
    
    void closeConnection() {
        try{
            if(conn!=null)
                conn.close();
                System.out.println("conn != null -> conn.close()");            
        }catch(SQLException se){
            se.printStackTrace();
        }                  
    }
    
    void insertIntoUsers(String id, String firstname, String lastname) {
     
        Statement stmt = null;
        
        try{        
             System.out.println("Inserting records into Users-Table...");
             stmt = conn.createStatement();

             String sql = "INSERT INTO Users " +
                          "VALUES("+id+", '"+firstname+"', '"+lastname+"')";
             stmt.executeUpdate(sql);
             System.out.println("Inserted records into Users-Table...");
             stmt.close();
        } catch(Exception e){
        //Handle errors
        e.printStackTrace();
        }
    }
    
    void insertIntoQuiz(String Quiz_Id, String User_Id_f, String name) {
     
        Statement stmt = null;
        
        try{        
             System.out.println("Inserting records into Quiz-Table...");
             stmt = conn.createStatement();

             String sql = "INSERT INTO Quiz " +
                          "VALUES("+Quiz_Id+", "+User_Id_f+", '"+name+"')";
             stmt.executeUpdate(sql);
             System.out.println("Inserted records into Quiz-Table...");
             stmt.close();
        } catch(Exception e){
        //Handle errors
        e.printStackTrace();
        }              
    }
   
    void insertIntoQuestion(String Question_Id, String Quiz_Id_f, String question) {
     
        Statement stmt = null;
        
        try{        
             System.out.println("Inserting records into Question-Table...");
             stmt = conn.createStatement();

             String sql = "INSERT INTO Question " +
                          "VALUES("+Question_Id+", "+Quiz_Id_f+", '"+question+"')";
             stmt.executeUpdate(sql);
             System.out.println("Inserted records into Question-Table...");
             stmt.close();
        } catch(Exception e){
        //Handle errors
        e.printStackTrace();
        }              
    }
    
    void insertIntoAnswer(String Answer_Id, String Question_Id_f, String answer) {
     
        Statement stmt = null;
        
        try{        
             System.out.println("Inserting records into Answer-Table...");
             stmt = conn.createStatement();

             String sql = "INSERT INTO Answer " +
                          "VALUES("+Answer_Id+", "+Question_Id_f+", '"+answer+"')";
             stmt.executeUpdate(sql);
             System.out.println("Inserted records into Answer-Table...");
             stmt.close();
        } catch(Exception e){
        //Handle errors
        e.printStackTrace();
        }              
    }    
    
    void getAllQuizFromUser(String User_Id) {
        
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT NAME, QUIZ_ID FROM TEST.QUIZ WHERE USER_ID_f = "+User_Id+" " );

            while ( rs.next() )
              System.out.printf( "%s, %s %n", rs.getString(1), rs.getString(2) );

            rs.close();
            stmt.close();
        }
        catch ( SQLException e ) {
            e.printStackTrace();
        }
        finally {
            if ( conn != null )
                try { conn.close(); } catch ( SQLException e ) { e.printStackTrace(); }
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
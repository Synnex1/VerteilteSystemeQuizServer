import java.sql.*;

public class QuizServerDB {
    
   //  Database credentials
   static final String DB_URL = "jdbc:derby://localhost:1527/QuizServerDB";
   static final String USER = "test";
   static final String PASS = "test";
    
    Connection createConnection (String DB_URL, String USER, String PASS) {
        
        Connection conn = null;
        // Statement stmt = null;
        
        try{
        
        System.out.println("Connecting to a selected database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        System.out.println("Connected database successfully...");     

        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }catch(Exception e){
           //Handle errors for Class.forName
           e.printStackTrace();
        }//end try
        
        return conn;
    }       
    
    Statement insertIntoUsers(Connection conn, String id, String firstname, String lastname) {
     
        Statement stmt = null;
        
        try{        
             System.out.println("Inserting records into Users-Table...");
             stmt = conn.createStatement();

             String sql = "INSERT INTO Users " +
                          "VALUES("+id+", '"+firstname+"', '"+lastname+"')";
             stmt.executeUpdate(sql);
             System.out.println("Inserted records into Users-Table...");
        } catch(Exception e){
        //Handle errors for Class.forName
        e.printStackTrace();
        }
       return stmt; 
    }
    
    Statement insertIntoQuiz(Connection conn, String Quiz_Id, String User_Id_f, String name) {
     
        Statement stmt = null;
        
        try{        
             System.out.println("Inserting records into Quiz-Table...");
             stmt = conn.createStatement();

             String sql = "INSERT INTO Quiz " +
                          "VALUES("+Quiz_Id+", "+User_Id_f+", '"+name+"')";
             stmt.executeUpdate(sql);
             System.out.println("Inserted records into Quiz-Table...");
        } catch(Exception e){
        //Handle errors for Class.forName
        e.printStackTrace();
        }
       return stmt;         
        
    }
    
    void closeConnection(Connection conn, Statement stmt) {

        try{
            if(stmt!=null)
                conn.close();
                System.out.println("stmt != null -> conn.close()");
        }catch(SQLException se){
        }// do nothing
        try{
            if(conn!=null)
                conn.close();
                System.out.println("conn != null -> conn.close()");            
        }catch(SQLException se){
            se.printStackTrace();
        }                  
}
    
   
   public static void main(String[] args) {
       
       QuizServerDB db = new QuizServerDB();
       
       Connection conn = db.createConnection(DB_URL, USER, PASS);       
       Statement stmt = db.insertIntoQuiz(conn, "1", "10", "Hochschule OS");             
       db.closeConnection(conn, stmt);
       
       
   } // main


} // Class QuizServerDB
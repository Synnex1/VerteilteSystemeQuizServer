import java.sql.*;

public class SecondSqlAccess
{
  public static void main( String[] args )
  {
    Connection con = null;

    try
    {
      con = DriverManager.getConnection( "jdbc:derby://localhost:1527/QuizServerDB","test", "test" );
      Statement stmt = con.createStatement();

      ResultSet rs = stmt.executeQuery( "SELECT VORNAME FROM TEST.USERS" );

      while ( rs.next() )
        System.out.printf( "%s %n", rs.getString(1) );

      rs.close();

      stmt.close();
    }
    catch ( SQLException e )
    {
      e.printStackTrace();
    }
    finally
    {
      if ( con != null )
        try { con.close(); } catch ( SQLException e ) { e.printStackTrace(); }
    }
  }
}
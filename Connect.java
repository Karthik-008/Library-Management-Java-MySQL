import java.sql.Connection;
import java.sql.DriverManager;

public class Connect
{
    static Connection con;

    public static Connection getConnection()
    {
        try
        {
            String mysqlJDBCDriver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/library";
            String user = "root";
            String pass = "karthik@2003";
            Class.forName(mysqlJDBCDriver);
            con = DriverManager.getConnection(url, user, pass);
        }
        catch(Exception e)
        {
            System.out.println("Connection Failed!");

        }

        return con;
    }
}
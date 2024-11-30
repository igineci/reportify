package melany.database;

import melany.cli.CliArgsParser;
import melany.utils.Constants;

import java.sql.*;

/**
 * Utility class for managing database connections and executing queries.
 * Handles connection setup, query execution, and resource management.
 *
 * @author andjela.djekic
 */
public class DatabaseConn {
    private final String host;
    private final String database;
    private final int port;
    private final String username;
    private final String password;
    private Connection conn;
    private Statement statement;
    private ResultSet rs;

   public DatabaseConn(CliArgsParser parsedArgs) {
        this.port = parsedArgs.getPort();
        this.host = parsedArgs.getHost();
        this.username = parsedArgs.getUsername();
        this.password = parsedArgs.getPassword();
        this.database = parsedArgs.getDatabase();
    }

    /**
     * Establishes a connection to the database.
     */
    public void getConnection()  {
       String url = Constants.JDBC_URL + host + Constants.COLON + port + Constants.SLASH + database;
       try {
           this.conn = DriverManager.getConnection(url, username, password);
       } catch (SQLException e) {
           System.err.println(Constants.DATABASE_CONNECTING_ERROR + e.getMessage());
       }
    }

    /**
     * Method for fetching data from the database
     */
    public ResultSet fetch(String sqlQuery) throws SQLException {
       try {
           this.statement = conn.createStatement();
           this.rs = statement.executeQuery(sqlQuery);
           return rs;
       } catch (SQLException e) {
           System.err.println(Constants.DATABASE_FETCHING_ERROR + e.getMessage());
           return null;
       }
    }

    /**
     * Closes the database connection if it is open.
     */
    public void disconnect() {
       try {
           if (rs != null) rs.close();
           if (statement != null) statement.close();
           if (conn != null) conn.close();
           System.out.println(Constants.DISCONNECTED_MSG);
       } catch (SQLException e) {
           System.err.println(Constants.DATABASE_DISCONNECTING_ERROR + e.getMessage());
       }
    }


}

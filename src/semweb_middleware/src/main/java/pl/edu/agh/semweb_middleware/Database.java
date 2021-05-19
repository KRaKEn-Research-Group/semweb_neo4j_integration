package pl.edu.agh.semweb_middleware;

import org.json.JSONArray;

import java.sql.*;

public class Database {
  static String username = System.getenv("DB_USERNAME");
  static String password = System.getenv("DB_PASSWORD");
  static String port = System.getenv("DB_PORT");

  private static Connection getConnection() throws SQLException {
    try {
      Class.forName("virtuoso.jdbc4.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return DriverManager.getConnection("jdbc:virtuoso://host.docker.internal:" + port + '/', username, password);
  }

  public static JSONArray getResults(String query) throws SQLException {
    Connection conn = getConnection();
    Statement st = conn.createStatement();
    return Utils.resultSetToJson(st.executeQuery(query));
  }
}

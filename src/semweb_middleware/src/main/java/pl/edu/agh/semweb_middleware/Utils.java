package pl.edu.agh.semweb_middleware;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Utils {
  public static JSONArray resultSetToJson(ResultSet rs) throws SQLException {
    JSONArray json = new JSONArray();
    ResultSetMetaData rsmd = rs.getMetaData();
    int numColumns = rsmd.getColumnCount();
    while(rs.next()) {
      JSONObject obj = new JSONObject();
      for (int i = 1; i <= numColumns; ++i) {
        String columnName = rsmd.getColumnName(i);
        obj.put(columnName, rs.getObject(columnName));
      }
      json.put(obj);
    }
    return json;
  }
}

package pl.edu.agh.semweb_middleware;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class Controller {
  @PostMapping("/")
  public String getResults(@RequestBody String query) {
    try {
      return Database.getResults(query).toString();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }
}
package com.tecksupport;

import com.tecksupport.database.CourseQuery;
import com.tecksupport.database.FacultyQuery;
import com.tecksupport.database.MySQLDatabase;
import com.tecksupport.database.UserAuthQuery;
import com.tecksupport.glfw.controller.InputHandler;

public class Main {
  public static MySQLDatabase database = new MySQLDatabase("localhost/TeckSupportDB", "client", "CS370TeckSupport");
  public static void main(String[] args){
    System.out.println("Program Starting");

    database.connect();
    CourseQuery courseQuery = new CourseQuery(database.getConnection());
    UserAuthQuery userAuthQuery = new UserAuthQuery(database.getConnection());
    FacultyQuery facultyQuery = new FacultyQuery(database.getConnection());

    InputHandler instance = new InputHandler(courseQuery, userAuthQuery, facultyQuery);

    instance.init();
    instance.run();

    System.out.println("Program finished");
  }

}

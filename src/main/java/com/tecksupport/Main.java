package com.tecksupport;

import com.tecksupport.database.*;
import com.tecksupport.glfw.controller.InputHandler;
import com.tecksupport.schedulePlanner.Schedule;
import com.tecksupport.schedulePlanner.StudentSchedules;

import java.util.*;
import java.util.regex.Pattern;

public class Main {
  public static MySQLDatabase database = new MySQLDatabase("localhost/TeckSupportDB", "client", "TeckSupport");
  public static void main(String[] args){
    System.out.println("Program Starting");

    database.connect();
    CourseQuery courseQuery = new CourseQuery(database.getConnection());
    UserAuthQuery userAuthQuery = new UserAuthQuery(database.getConnection());
    FacultyQuery facultyQuery = new FacultyQuery(database.getConnection());
    // Uncomment this to update database
    FileReader fileReader = new FileReader(database.getConnection());
    fileReader.createTables();

//    testSQL(courseQuery);

    InputHandler instance = new InputHandler(courseQuery, userAuthQuery, facultyQuery);

    instance.init();
    instance.run();

    System.out.println("Program finished");
  }

  public static void testSQL(CourseQuery courseQuery) {
    List<Schedule> scheduleList = courseQuery.getScheduleList();
    HashSet<String> startTimes = new HashSet<>();
    HashSet<String> days = new HashSet<>();

    Pattern regex = Pattern.compile("S");

    for (Schedule schedule : scheduleList) {
//      Matcher matcher = regex.matcher(schedule.getDaysInWeek());
//      if (!matcher.find())
//        continue;

      days.add(schedule.getDaysInWeek());

      startTimes.add(String.valueOf(schedule.getCourseID()));
    }

    List<String> startTimeList = new ArrayList<>(startTimes.stream().toList());

    Collections.sort(startTimeList);

    for (String startTime : startTimeList) {
      System.out.println(startTime);
    }
    for (String day : days) {
      System.out.println(day);
    }
  }

}

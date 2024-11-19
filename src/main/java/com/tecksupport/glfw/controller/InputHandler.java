package com.tecksupport.glfw.controller;


import com.tecksupport.glfw.model.*;
import com.tecksupport.glfw.view.Camera;
import com.tecksupport.glfw.view.Renderer;
import com.tecksupport.glfw.view.Window;
import imgui.ImGui;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.Callback;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imgui.type.ImString;


import javax.swing.*;

import static org.lwjgl.glfw.GLFW.*;


public class InputHandler {
    private Window window;
    private Shader shader;
    private Mesh mesh;
    private Camera camera;
    private RawModel rawModel;
    private Renderer renderer;
    private Loader loader;
    private TexturedModel texturedModel;
    private RawModel square;
    private Entity entity;
    private ModelData modelData;
    private Callback mouseMovement;
    private Callback mouseButton;
    private Vector3f mouseRotatePos = new Vector3f();
    private DoubleBuffer yaw = BufferUtils.createDoubleBuffer(1);
    private DoubleBuffer pitch = BufferUtils.createDoubleBuffer(1);

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private boolean isLoggedIn = false; // To track login status
    private final HashMap<String, String> userDatabase = new HashMap<>(); // Simulated database
    private final ImString usernameBuffer = new ImString("", 128); // Using ImGui ImString
    private final ImString passwordBuffer = new ImString("", 128);
    private String loginMessage = ""; // Stores the message to display
    private String signupMessage = ""; // Message for successful signup or welcome message
    private static boolean isSidebarVisible = false;
    private Map<String, String> buildingMessages = new HashMap<>();

    float[] vertices = {
            -0.5f, 0.5f, 0,
            -0.5f, -0.5f, 0,
            0.5f, -0.5f, 0,
            0.5f, 0.5f, 0,

            -0.5f, 0.5f, 1,
            -0.5f, -0.5f, 1,
            0.5f, -0.5f, 1,
            0.5f, 0.5f, 1,

            0.5f, 0.5f, 0,
            0.5f, -0.5f, 0,
            0.5f, -0.5f, 1,
            0.5f, 0.5f, 1,

            -0.5f, 0.5f, 0,
            -0.5f, -0.5f, 0,
            -0.5f, -0.5f, 1,
            -0.5f, 0.5f, 1,

            -0.5f, 0.5f, 1,
            -0.5f, 0.5f, 0,
            0.5f, 0.5f, 0,
            0.5f, 0.5f, 1,

            -0.5f, -0.5f, 1,
            -0.5f, -0.5f, 0,
            0.5f, -0.5f, 0,
            0.5f, -0.5f, 1

    };

    float[] textureCoords = {

            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0


    };

    int[] indices = {
            0, 1, 3,
            3, 1, 2,
            4, 5, 7,
            7, 5, 6,
            8, 9, 11,
            11, 9, 10,
            12, 13, 15,
            15, 13, 14,
            16, 17, 19,
            19, 17, 18,
            20, 21, 23,
            23, 21, 22


    };

    static class CategoryNode {
        String name;
        List<CategoryNode> subcategories = new ArrayList<>();
        String description; // Optional description for leaf nodes

        // Constructor for intermediate nodes
        CategoryNode(String name) {
            this.name = name;
        }

        // Constructor for leaf nodes with a description
        CategoryNode(String name, String description) {
            this.name = name;
            this.description = description;
        }

        // Add a subcategory
        void addSubcategory(CategoryNode subcategory) {
            subcategories.add(subcategory);
        }
    }
    // The root of the navigation tree
    private CategoryNode root;
    private CategoryNode currentNode; // The currently displayed category

    // Initialize the navigation tree
    public void NavigationUI() {
        root = buildCategoryTree();
        currentNode = root; // Start at the root
    }
    private CategoryNode buildCategoryTree() {
        CategoryNode root = new CategoryNode("Main Categories");

        //Buildings category
        CategoryNode buildings = new CategoryNode("Buildings");
        CategoryNode academicHalls = new CategoryNode("Academic Halls");
        CategoryNode serviceBuildings = new CategoryNode("Service Buildings");
        //Academic Halls
        academicHalls.addSubcategory(new CategoryNode("Academic Hall", "Details about Academic Hall."));
        academicHalls.addSubcategory(new CategoryNode("Arts Building", "Details about Arts Building."));
        academicHalls.addSubcategory(new CategoryNode("Extended Learning Building", "Details about Extended Learning Building."));
        academicHalls.addSubcategory(new CategoryNode("Markstein Hall", "Details about Markstein Hall."));
        academicHalls.addSubcategory(new CategoryNode("Science Hall 1", "Details about Science Hall 1."));
        academicHalls.addSubcategory(new CategoryNode("Science Hall 2", "Details about Science Hall 2."));
        academicHalls.addSubcategory(new CategoryNode("Social & Behavioral Sciences Building", "Details about Social & Behavioral Sciences Building."));
        academicHalls.addSubcategory(new CategoryNode("University Commons", "Details about University Commons."));
        academicHalls.addSubcategory(new CategoryNode("University Hall", "Details about University Hall."));
        academicHalls.addSubcategory(new CategoryNode("Viasat Engineering Pavilion", "Details about Viasat Engineering Pavilion."));
        // Service Buildings
        serviceBuildings.addSubcategory(new CategoryNode("Administrative Building", "Details about Academic Hall."));
        serviceBuildings.addSubcategory(new CategoryNode("Center for Children & Families", "Details about Arts Building."));
        serviceBuildings.addSubcategory(new CategoryNode("Epstein Family Veterans Center", "Details about Markstein Hall."));
        serviceBuildings.addSubcategory(new CategoryNode("Kellogg Library", "Details about Science Hall 1."));
        serviceBuildings.addSubcategory(new CategoryNode("M. Gordon Clarke Fieldhouse", "Details about Science Hall 2."));
        serviceBuildings.addSubcategory(new CategoryNode("McMahan House", "Details about Social & Behavioral Sciences Building."));
        serviceBuildings.addSubcategory(new CategoryNode("Public Safety Building", "Details about University Commons."));
        serviceBuildings.addSubcategory(new CategoryNode("Sports Center", "Details about University Hall."));
        serviceBuildings.addSubcategory(new CategoryNode("Student Health & Counseling Services Building", "Details about Viasat Engineering Pavilion."));
        serviceBuildings.addSubcategory(new CategoryNode("University Student Union", "Details about Extended Learning Building."));
        // Add Academic Halls and Service Buildings to Buildings category
        buildings.addSubcategory(academicHalls);
        buildings.addSubcategory(serviceBuildings);
        //Health & Safety category
        CategoryNode healthSafety = new CategoryNode("Health & Safety");
        healthSafety.addSubcategory(new CategoryNode("Emergency Phones", "Details about emergency phones on campus."));
        healthSafety.addSubcategory(new CategoryNode("AED Locations", "Locations of Automated External Defibrillators."));
        healthSafety.addSubcategory(new CategoryNode("Lactation Rooms", "Details about lactation rooms."));
        healthSafety.addSubcategory(new CategoryNode("Public Safety Building", "Details about lactation rooms."));
        healthSafety.addSubcategory(new CategoryNode("Student Health & Counseling Services Building", "Details about lactation rooms."));
        //Parking & Transit
        CategoryNode ParkingTransit = new CategoryNode("Parking & Transit");
        ParkingTransit.addSubcategory(new CategoryNode("Emergency Phones", "Details about emergency phones on campus."));
        ParkingTransit.addSubcategory(new CategoryNode("AED Locations", "Locations of Automated External Defibrillators."));
        ParkingTransit.addSubcategory(new CategoryNode("Lactation Rooms", "Details about lactation rooms."));
        ParkingTransit.addSubcategory(new CategoryNode("Public Safety Building", "Details about lactation rooms."));
        ParkingTransit.addSubcategory(new CategoryNode("Student Health & Counseling Services Building", "Details about lactation rooms."));
        // Add main categories to root
        root.addSubcategory(buildings);
        root.addSubcategory(healthSafety);

        return root;
    }
    public void renderUI() {
        if (currentNode.subcategories.isEmpty()) {
            // If there are no subcategories, show the description (leaf node)
            renderDescription(currentNode);
        } else {
            // Render the subcategories
            renderSubcategories(currentNode);
        }
    }
    // Render subcategories as buttons
    private void renderSubcategories(CategoryNode node) {
        ImGui.text("Subcategories for " + node.name + ":");
        for (CategoryNode subcategory : node.subcategories) {
            if (ImGui.button(subcategory.name)) {
                currentNode = subcategory; // Navigate to the clicked subcategory
            }
        }

        // Add a "Back" button if not at the root
        if (currentNode != root && ImGui.button("Back")) {
            currentNode = findParentNode(root, currentNode); // Navigate back
        }
    }
    // Render the description for leaf nodes
    private void renderDescription(CategoryNode node) {
        String description = buildingMessages.get(node.name);  // Fetch description from the HashMap
        if (description != null) {
            ImGui.textWrapped("Description: " + description);
        }

        // Add a "Back" button to navigate back to the parent
        if (ImGui.button("Back")) {
            currentNode = findParentNode(root, currentNode); // Navigate back
        }
    }
    // Find the parent node of a given node (recursive search)
    private CategoryNode findParentNode(CategoryNode parent, CategoryNode child) {
        if (parent.subcategories.contains(child)) {
            return parent;
        }
        for (CategoryNode subcategory : parent.subcategories) {
            CategoryNode result = findParentNode(subcategory, child);
            if (result != null) {
                return result;
            }
        }
        return null; // If not found (shouldn't happen in a well-formed tree)
    }
    public void init() {
        NavigationUI();
        // Existing building messages
        buildingMessages.put("Academic Hall", "<html>Academic Hall\n" +
                "Acronym: ACD, Building No. 14\n" +
                "One of the three original buildings on campus, Academic Hall was the first to provide lecture classroom and computer lab space, " +
                "including a 145-seat, stadium-style lecture hall. Academic Hall features the campus’ landmark clock tower, which is also present on CSUSM’s campus logo. " +
                "First-year students are frequent visitors to the building, which is home to many general education classes.</html>");

        buildingMessages.put("Arts Building", "<html>Arts Building\n" +
                "Acronym: ARTS, Building No. 26-27\n" +
                "Built as part of a master-planned second phase of the original campus buildings, the Arts Building opened in 2003 and serves as the home of the university’s School of Arts. " +
                "The building includes music and recording studios, a lobby art gallery, a 150-seat performance hall, a dance studio and much more.</html>");

        buildingMessages.put("Extended Learning Building", "<html>Extended Learning Building\n" +
                "The largest academic building on campus, the 135,000-square-foot Extended Learning building brought all Extended Learning operations under one roof for the first time when it opened in 2019. " +
                "It was the first academic building in California established through a unique public-private partnership, requiring no state funds for its design, planning or construction. " +
                "It is also home to student support centers, community outreach centers, lab and research facilities, the STEM Education Center and the Innovation Hub.</html>");

        buildingMessages.put("Markstein Hall", "<html>Markstein Hall\n" +
                "Acronym: MARK, Building No. 13\n" +
                "The name of the building, home to the College of Business Administration, honors the owners of the San Marcos-based Markstein Beverage Co., who recognized the importance of helping build a strong program for business students. " +
                "Today, Markstein Hall is the central hub of business leadership in the region, offering signature programs such as the Center for Leadership Innovation and Mentorship Building, In the Executive’s Chair and Senior Experience, among others.</html>");

        buildingMessages.put("Science Hall 1", "<html>Science Hall 1\n" +
                "Acronym: SCI1, Building No. 3\n" +
                "One of the three original buildings on campus, Science Hall 1 houses numerous labs, offices and classrooms. " +
                "The entrance to the building is a popular destination for students, faculty, and staff, who have been visiting the Campus Coffee cart for more than 15 years.</html>");

        buildingMessages.put("Science Hall 2", "<html>Science Hall 2\n" +
                "Acronym: SCI2, Building No. 37\n" +
                "Though first-time visitors sometimes confuse it with Science Hall 1, CSUSM's second science building is located on the east side of campus between the University Student Union and the parking structure. " +
                "The building’s opening in 2003 provided much-needed additional lab and classroom space.</html>");

        buildingMessages.put("Social & Behavioral Sciences Building", "<html>Social & Behavioral Sciences Building\n" +
                "Acronym: SBSB, Building No. 31\n" +
                "Referred to as SBSB for short, the Social & Behavioral Sciences Building is home to the College of Humanities, Arts, Behavioral, and Social Sciences. " +
                "The university’s largest college has two dozen departments that offer an exciting variety of degrees and programs that make up the core of a liberal arts education at CSUSM.</html>");

        buildingMessages.put("University Commons", "<html>University Commons\n" +
                "Acronym: COM, Building No. 2\n" +
                "Built as part of CSUSM’s initial core buildings, Commons is best known as the location of the University Bookstore, where students, faculty, staff, and community members can find everything from branded merchandise, souvenirs, school supplies, and spirit apparel to computers, technological accessories, and textbooks.</html>");

        buildingMessages.put("University Hall", "<html>University Hall\n" +
                "Acronym: UNIV, Building No. 15\n" +
                "Opened in 1998, University Hall served as a model for future buildings on campus as its array of classroom configurations helped to determine how students respond to different methods of learning. " +
                "Today, University Hall is home to the College of Education, Health, and Human Services, which includes the School of Education and the School of Nursing as well as departments in human development, kinesiology, public health, social work, and speech-language pathology.</html>");

        buildingMessages.put("Viasat Engineering Pavilion", "<html>Viasat Engineering Pavilion\n" +
                "An extensive renovation was completed in spring 2020, turning the former Foundation Classroom Building into a state-of-the-art home for the university’s electrical and software engineering programs. " +
                "The pavilion is named in honor of the Carlsbad-based global communications company that provided a $1.5 million gift to become the founding partner of CSUSM’s engineering programs.</html>");

        // Add service building messages here
        buildingMessages.put("Administrative Building", "<html>Administrative Building\n" +
                "Acronym: ADM, Building No. 1\n" +
                "The Administrative Building, easily recognized by its iconic rotunda, is home to the university's administrative offices, " +
                "including the Office of the President. Students are frequent visitors to the third floor and Cougar Central, a one-stop location for Admissions & Student Outreach, " +
                "Financial Aid and Scholarships, Office of the Registrar, and Student Financial Services.</html>");

        buildingMessages.put("Center for Children & Families", "<html>Center for Children & Families\n" +
                "Acronym: CCF, Building No. 22\n" +
                "Since opening in 2007, CSUSM’s on-campus child-care center has provided convenient, quality care to the families of students, faculty, staff, and the greater community. " +
                "CCF offers child care and preschool education to children from 6 weeks to 5 years old in a state-of-the-art facility that includes 12 spacious classrooms, three age-specific playgrounds, " +
                "a full-service kitchen, a kid's kitchen, a children's garden, and internet-accessible cameras.</html>");

        buildingMessages.put("Epstein Family Veterans Center", "<html>Epstein Family Veterans Center\n" +
                "Acronym: VET, Building No. 4\n" +
                "CSUSM has the highest percentage per capita of student veterans of any California State University campus and proudly serves more than 1,700 military-connected students. " +
                "The original Veterans Center building was donated to CSUSM in 2014 by students at Stevens Institute of Technology in New Jersey. " +
                "An extensive renovation and expansion was completed in 2019, providing all of the resources our student veterans and their families need to achieve their academic and career goals.</html>");

        buildingMessages.put("Kellogg Library", "<html>Kellogg Library\n" +
                "Acronym: KEL, Building No. 17\n" +
                "Named after Keith Kellogg II, grandson of the famed cereal company magnate, Kellogg Library is an essential partner in teaching and learning, research, and community engagement at CSUSM. " +
                "The five-story, 200,000-square-foot building, which opened in 2004, includes a 24/5 Zone, available to students, faculty, and staff around the clock, five days a week. " +
                "The Starbucks located near the main entrance is a popular spot throughout the day.</html>");

        buildingMessages.put("M. Gordon Clarke Fieldhouse", "<html>M. Gordon Clarke Fieldhouse\n" +
                "Acronym: CFH, Building No. 23\n" +
                "Affectionately known as “The Clarke,” the M. Gordon Clarke Field House provided the first on-campus recreation and meeting space for students. " +
                "Completed in 2003, the building includes a fitness center, gymnasium, locker rooms, a catering kitchen, conference rooms, and office space for Campus Recreation and Cougar Athletics.</html>");

        buildingMessages.put("McMahan House", "<html>McMahan House\n" +
                "Acronym: MCM, Building No. 50\n" +
                "Built in 2009, the McMahan House is a picturesque on-campus gathering place for conferences, receptions, and even weddings. " +
                "Once considered as a location for a permanent residence for the university’s president, the McMahan House is composed of four buildings – a great room, a library, " +
                "a retreat room, and a tower room – connected by pedestrian walkways and a central courtyard that can accommodate up to 175 people.</html>");
        buildingMessages.put("Sports Center", "<html>Sports Center\n" +
                "Acronym: SC, Building No. 24A\n" +
                "With seating for 1,400, the Sports Center has been the home of men’s and women’s basketball and women’s volleyball since 2016. " +
                "The 25,000-square-foot facility also includes a ticket office, offices for coaches, a student-athlete lounge, and student-athlete locker rooms for all sports. " +
                "The Department of Athletics sponsors 13 NCAA Division II teams that compete in the California Collegiate Athletic Association. " +
                "Students can obtain free tickets for athletics events online.</html>");

        buildingMessages.put("University Student Union", "<html>University Student Union\n" +
                "Acronym: USU, Building No. 25\n" +
                "Known as “the heartbeat of campus,” the USU is the hub of student life at CSUSM. " +
                "From half a dozen dining options to a convenience store, game rooms, lounges, a ballroom, and an outdoor amphitheater, there is no shortage of spaces and events for students. " +
                "The USU is also where you’ll find Associated Students, Inc., five student life centers, the Office of the Dean of Students, and Student Life and Leadership.</html>");
        buildingMessages.put("Student Health & Counseling Services Building", "<html>Student Health & Counseling Services Building\n" +
                "Acronym: SHCSB, Building No. 21\n" +
                "Located adjacent to the parking structure on Campus Way Circle, Student Health & Counseling Services (SHCS) offers vital health services to students, " +
                "including clinical exams, counseling, pharmacy, and health education classes. SHCS is fully accredited through the Accreditation Association for Ambulatory Health Care – achieving the highest ratings in all areas of this comprehensive review – " +
                "and has a staff of professionals dedicated to serving students in a warm, caring, and professional environment.</html>");
        buildingMessages.put("Public Safety Building", "<html>Public Safety Building\n" +
                "Acronym: PSB, Building No. 63\n" +
                "Home to Parking and Commuter Services and the University Police Department, the Public Safety Building is located at La Moree Road and Barham Drive on the northeast corner of campus. " +
                "The building is staffed 24 hours a day, 365 days a year, and CSUSM was ranked first in California in the National Council for Home Safety and Security’s 2020 ranking of the safest college campuses in America.</html>");
        buildingMessages.put("Emergency Phones",
                "Emergency Phones Locations\n"+
                        "1. Parking Lot X\n" +
                        "2. Parking Lot Y\n" +
                        "3. Parking Lot B Back Right\n" +
                        "4. Parking Lot B Middle\n" +
                        "5. Parking Lot B 2nd entrance\n" +
                        "6. Parking Lot C Middle Back\n" +
                        "7. Parking Lot C Front Right\n" +
                        "8. Parking Lot C Front Left\n" +
                        "9. Parking Lot F Main Plant Outside Back\n" +
                        "10. Parking Lot F Front Left\n" +
                        "11. Parking Lot F Back Left\n" +
                        "12. Parking Lot F Middle\n" +
                        "13. Parking Lot F Back Right\n" +
                        "14. Parking Lot F Front Right\n" +
                        "15. Parking Lot E Front Middle\n" +
                        "16. Parking Lot G in front of 15\n" +
                        "17. Parking Lot H Back Left\n" +
                        "18. Parking Lot H Back Right\n" +
                        "19. Parking Lot H Front Middle\n" +
                        "20. Markstein Hall Outside right\n" +
                        "21. Markstein Hall Outside Left\n" +
                        "22. Forum Plaza Upstairs\n" +
                        "23. Forum Plaza Downstairs\n" +
                        "24. PS1 6th Floor at Elevators\n" +
                        "25. PS1 6th Floor Middle\n" +
                        "26. PS1 Entrance\n" +
                        "27. Parking Lot N Entrance\n" +
                        "28. Parking Lot N Walkway\n" +
                        "29. Parking Lot O Front Right\n" +
                        "30. Parking Lot O Back Right\n" +
                        "31. Parking Lot O Back Left\n" +
                        "32. Parking Lot K\n" +
                        "33. Parking Lot M"
        );

        buildingMessages.put("AED",
                "AED \n"+
                        "In any campus emergency, immediately call University Police at 911 or 760-750-4567.\n\n" +
                        "How to use an AED\n" +
                        "1. Open the lid and follow the verbal instructions\n" +
                        "2. Open the electrode pad package\n" +
                        "3. Place one pad on the person’s upper chest as shown on the diagram\n" +
                        "4. Place one pad on the person’s lower chest/side as shown on the diagram\n" +
                        "*It doesn’t matter which pad goes on first or on which side*\n" +
                        "Do not touch the person while the AED is analyzing\n" +
                        "5. Follow the AED's verbal instructions to apply a shock or give CPR\n\n" +
                        "Locations:\n" +
                        "1. Student Health & Counseling Services Building - 1st Floor: Stairwell\n" +
                        "2. Student Health & Counseling Services Building - 2nd Floor: At Restrooms\n" +
                        "3.Academic Hall - 2nd Floor: Breezeway Stairs\n" +
                        "4.Administrative Building - 1st Floor: Main Entrance Lobby\n" +
                        "5.Administrative Building - 3rd Floor: On Wall at Stairs\n" +
                        "6.Arts Building - 1st Floor: Main Entrance/Exit\n" +
                        "7.Arts Building - 3rd Floor: Near Elevator\n" +
                        "8.Central Plant - 2nd Floor: At Restrooms\n" +
                        "9.Clarke Field House - 1st Floor: At Front Desk\n" +
                        "10.Commons/University Store - Back Exit Doors\n" +
                        "11.Extended Learning Building - 1st Floor: Innovation Hub\n" +
                        "12.Extended Learning Building - 2nd Floor: Math/Writing Lab\n" +
                        "13.Extended Learning Building - 4th Floor: Next to Elevator\n" +
                        "14.Extended Learning Building - 6th Floor: Next to Elevator\n" +
                        "15.Kellogg Library - 2nd Floor: At Elevators.\n" +
                        "16.Kellogg Library - 3rd Floor: At Circulation Desk\n" +
                        "17.Markstein Hall - 1st Floor: At Elevators\n" +
                        "18.Markstein Hall - 3rd Floor: Near Elevators\n" +
                        "19.McMahan House - Great Room: Near Exit\n" +
                        "20.Parking Structure 1 - 1st Level: At Elevators\n" +
                        "21.Parking Structure 1 - 6th Level: At Elevators\n" +
                        "22.Public Safety Building - 1st Floor: Near Front Desk\n" +
                        "23.Science Hall I - 2nd Floor: At Entrance\n" +
                        "24.Science Hall II - 1st Floor: At Entrance\n" +
                        "25.Science Hall II - 2nd Floor: At Main Entry\n" +
                        "26.Social & Behavioral Science Building - 1st Floor: At Elevators\n" +
                        "27.Social & Behavioral Science Building - 2nd Floor: Near Restrooms\n" +
                        "28.Social & Behavioral Science Building - 4th Floor: Near Restroom\n" +
                        "29.Sports Center - 1st Floor: Back Wall Near Restroom\n" +
                        "30.Sports Center - 1st Floor: Front Wall Near Restroom\n" +
                        "31.University Hall - 2nd Floor: Main Entry\n" +
                        "32.University Hall - 3rd Floor: Nurse Lobby\n" +
                        "33.University Services Building - At Loading Dock\n" +
                        "34.University Services Building - Front Desk Lobby\n" +
                        "35.University Student Union - 2nd Floor: Ballroom Hallway\n" +
                        "36.University Student Union - 4th Floor: At Market  \n" +
                        "37.University Student Union - 4th Floor: Near Restrooms\n" +
                        "38.University Village Apartments - 3rd Floor: C Block - Cycling Room\n" +
                        "39.University Village Apartments - Main Entrance At Front Desk\n" +
                        "40. Viasat Engineering Pavilion - Breezeway"
        );

        buildingMessages.put("Lactation Rooms",
                "Lactation Rooms\n"+
                        "Helpful Links\n" +
                        "<a href='https://www.csusm.edu/wgec/parenting/lactation_spaces.html'>Lactation Spaces</a> | " +
                        "<a href='https://www.csusm.edu/wgec/parenting/lactation_spaces.html'>Gender Equity Center</a> <br> |" +
                        "Locations:\n" +
                        "1. Administrative Building 6312 Lactation Space\n" +
                        "2. Extended Learning Building 544 Lactation Space\n" +
                        "3. Gender Equity Center Lactation Space (USU 3200C)\n" +
                        "4. Kellogg Library 3017 Lactation Space"
        );

        buildingMessages.put("Public Safety Building",
                "Public Safety Building\n" +
                        "Acronym: PSB, Building No. 63\n" +
                        "Home to Parking and Commuter Services and the University Police Department," +
                        " the Public Safety Building is located at La Moree Road and Barham Drive on the northeast corner of campus." +
                        " The building is staffed 24 hours a day, 365 days a year, and CSUSM was ranked first" +
                        " in California in the National Council for Home Safety and Security’s 2020 ranking of the " +
                        "safest college campuses in America."
        );

        buildingMessages.put("Student Health & Counseling Services Building",
                "Student Health & Counseling Services Building\n" +
                        "Acronym: SHCSB, Building No. 21\n" +
                        "Located adjacent to the parking structure on Campus Way Circle, Student Health" +
                        " & Counseling Services (SHCS) offers vital health services to students, including clinical exams," +
                        " counseling, pharmacy, and health education classes. " +
                        "SHCS is fully accredited through the Accreditation Association for Ambulatory Health Care" +
                        " – achieving the highest ratings in all areas of this comprehensive review" +
                        " – and has a staff of professionals dedicated to serving students in a warm" +
                        ", caring, and professional environment.");
        window = new Window(800, 600, "myPath");
        window.init();
        loader = new Loader();
        shader = new Shader(
                "src/main/java/com/tecksupport/glfw/shader/vertexShader.txt",
                "src/main/java/com/tecksupport/glfw/shader/fragmentShader.txt"
        );
        renderer = new Renderer(shader, window);

//        rawModel = loader.loadToVAO(vertices, textureCoords, indices);

        rawModel = loader.loadToVAO(OBJFileLoader.loadOBJ("School"));

        texturedModel = new TexturedModel(rawModel, new ModelTexture(loader.loadTexture("SchoolTexture")));

        entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 0, 0, 10);

        camera = new Camera();
//        camera.createMatrix(45.0f, 0.1f, 100, shader, "camera");
//
//        Matrix4f camMat = camera.getMatrix(45.0f, 0.1f, 100, shader, "camera");
//        shader.setUniform("camera", camMat);

        mouseMovement = glfwSetCursorPosCallback(window.getWindowID(), this::cursorCallback);
        mouseButton = glfwSetMouseButtonCallback(window.getWindowID(), this::mouseButtonCallback);

        System.out.println("Initializing ImGui");
        ImGui.createContext();
        imGuiGlfw.init(window.getWindowID(), true);
        imGuiGl3.init(window.getGlslVersion());
        System.out.println("Initialized ImGui");
    }

    public void run() {
        while (!window.shouldClose()) {
            startFrameImGui();
            if (!isLoggedIn) {
                renderLoginPage();
            } else {
                // Only render the main application if the user is logged in
                processInput();
                renderer.prepare();
                shader.bind();
                shader.loadViewMatrix(camera);
                renderer.render(entity, shader);
                shader.unbind();
                renderUI();
            }



            endFrameImGui();
            window.update();
        }
        cleanup();
    }

    public void processInput() {
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_W) == GLFW_PRESS) {
            camera.forward();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_A) == GLFW_PRESS) {
            camera.left();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_S) == GLFW_PRESS) {
            camera.backward();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_D) == GLFW_PRESS) {
            camera.right();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_SPACE) == GLFW_PRESS) {
            camera.up();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_LEFT_ALT) == GLFW_PRESS) {
            camera.down();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_Q) == GLFW_PRESS) {
            camera.yawLeft();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_E) == GLFW_PRESS) {
            camera.yawRight();
        }
        // Handle more inputs here
    }


    void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {

            glfwGetCursorPos(window, yaw, pitch);

            mouseRotatePos.x = (float) yaw.get(0);
            mouseRotatePos.y = (float) pitch.get(0);
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        } else {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
    }

    void cursorCallback(long window, double xPos, double yPos) {
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) != GLFW_PRESS)
            return;
        double yaw = xPos - mouseRotatePos.x;
        double pitch = yPos - mouseRotatePos.y;

        camera.addRotation((float) pitch, (float) yaw, 0);

        mouseRotatePos.x = (float) xPos;
        mouseRotatePos.y = (float) yPos;
    }

    public void cleanup() {
        shader.cleanup();
        loader.cleanUp();
        // renderer.cleanup();
        // mesh.cleanup();
        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        ImGui.destroyContext();
        window.cleanup();
    }
    public void renderLoginPage() {
        // Set the size and position of the login window
        ImGui.setNextWindowSize(400, 200, ImGuiCond.Always);
        ImGui.setNextWindowPos(window.getWindowWidth() / 2.0f - 200, window.getWindowHeight() / 2.0f - 100, ImGuiCond.Always);

        ImGui.begin("Login Page");

        ImGui.text("Please log in to access the application.");
        ImGui.spacing();

        // Input fields for username and password
        ImGui.inputText("Username", usernameBuffer, ImGuiInputTextFlags.None);
        ImGui.inputText("Password", passwordBuffer, ImGuiInputTextFlags.Password);

        // Handle the "Sign In" button
        if (ImGui.button("Sign In")) {
            String username = usernameBuffer.get().trim();
            String password = passwordBuffer.get().trim();
            if (validateCredentials(username, password)) {
                isLoggedIn = true; // User has logged in
                loginMessage = ""; // Clear message on successful login
                signupMessage = ""; // Clear any previous signup message
            } else {
                loginMessage = "Invalid credentials. Please try again."; // Set error message
            }
        }

        // Handle the "Sign Up" button
        ImGui.sameLine(); // Position "Sign Up" next to "Sign In"
        if (ImGui.button("Sign Up")) {
            String username = usernameBuffer.get().trim();
            String password = passwordBuffer.get().trim();
            boolean signUpSuccess = handleSignUp(username, password); // Assume this function returns a boolean indicating success
            if (signUpSuccess) {
                signupMessage = "Welcome, " + username + "! You have successfully signed up."; // Set welcome message
            } else {
                signupMessage = "Signup failed. Please try again."; // Set error message for signup
            }
        }

        ImGui.spacing();

        // Display login message (if any)
        if (!loginMessage.isEmpty()) {
            ImGui.textColored(1.0f, 0.0f, 0.0f, 1.0f, loginMessage); // Red text for error messages
        }
        if (!signupMessage.isEmpty()) {
            ImGui.textColored(0.0f, 1.0f, 0.0f, 1.0f, signupMessage); // Green text for success message
        }

        ImGui.end();
    }
    private boolean validateCredentials(String username, String password) {
        // Replace with real credential validation logic
        return userDatabase.containsKey(username) && userDatabase.get(username).equals(password);
    }
    private boolean handleSignUp(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Sign-Up Failed: Username and password cannot be empty.");
            return false;
        }

        if (userDatabase.containsKey(username)) {
            System.out.println("Sign-Up Failed: Username already exists.");
        } else {
            userDatabase.put(username, password);
            System.out.println("Sign-Up Successful! Welcome, " + username + "!");
        }
        userDatabase.put(username, password);
        return true;
    }

    private void handleSignIn(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Sign-In Failed: Username and password cannot be empty.");
            return;
        }

        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            System.out.println("Sign-In Successful! Welcome back, " + username + "!");
            isLoggedIn = true;
        } else {
            System.out.println("Sign-In Failed: Invalid username or password.");
        }
    }
    public class BuildingData {
        private String description;
        private String imagePath;
        private String iconPath;

        public BuildingData(String description, String imagePath, String iconPath) {
            this.description = description;
            this.imagePath = imagePath;
            this.iconPath = iconPath;
        }

        public String getDescription() {
            return description;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getIconPath() {
            return iconPath;
        }
    }
    void startFrameImGui() {
        imGuiGl3.newFrame();
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    void endFrameImGui() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }
    }
}

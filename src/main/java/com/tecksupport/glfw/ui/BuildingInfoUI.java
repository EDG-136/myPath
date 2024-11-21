package com.tecksupport.glfw.ui;

import com.tecksupport.glfw.view.Window;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingInfoUI {
    private static boolean isSidebarVisible = false;
    private final Window window;
    private final HashMap<String, String> userDatabase = new HashMap<>(); // Simulated database
    private final ImString usernameBuffer = new ImString("", 128); // Using ImGui ImString
    private final ImString passwordBuffer = new ImString("", 128);
    private String loginMessage = ""; // Stores the message to display
    private String signupMessage = ""; // Message for successful signup or welcome message
    private Map<String, String> buildingMessages = new HashMap<>();
    // The root of the navigation tree
    private CategoryNode root;
    private CategoryNode currentNode; // The currently displayed category
    private boolean isLoggedIn = false; // To track login status

    public BuildingInfoUI(Window window)
    {
        this.window = window;
        init();
    }

    public void init() {
        NavigationUI();
        // Existing building messages
        buildingMessages.put("Academic Hall", "Academic Hall\n" +
                "Acronym: ACD, Building No. 14\n" +
                "One of the three original buildings on campus, Academic Hall was the first to provide lecture" +
                " classroom and computer lab space, " +
                "including a 145-seat, stadium-style lecture hall. Academic Hall features the campus’ landmark clock tower," +
                " which is also present on CSUSM’s campus logo. " +
                "First-year students are frequent visitors to the building, which is home to many general education classes.");

        buildingMessages.put("Arts Building", "Arts Building\n" +
                "Acronym: ARTS, Building No. 26-27\n" +
                "Built as part of a master-planned second phase of the original campus buildings," +
                " the Arts Building opened in 2003 and serves as the home of the university’s School of Arts. " +
                "The building includes music and recording studios, a lobby art gallery, a 150-seat performance hall," +
                " a dance studio and much more.");

        buildingMessages.put("Extended Learning Building", "Extended Learning Building\n" +
                "The largest academic building on campus, the 135,000-square-foot Extended Learning building brought all" +
                " Extended Learning operations under one roof for the first time when it opened in 2019. " +
                "It was the first academic building in California established through a unique " +
                "public-private partnership, requiring no state funds for its design, planning or construction. " +
                "It is also home to student support centers, community outreach centers, lab and research facilities," +
                " the STEM Education Center and the Innovation Hub.");

        buildingMessages.put("Markstein Hall", "Markstein Hall\n" +
                "Acronym: MARK, Building No. 13\n" +
                "The name of the building, home to the College of Business Administration, honors the owners of the " +
                "San Marcos-based Markstein Beverage Co., who recognized the importance of helping build a strong program for" +
                " business students. " +
                "Today, Markstein Hall is the central hub of business leadership in the region, " +
                "offering signature programs such as the Center for Leadership Innovation and Mentorship " +
                "Building, In the Executive’s Chair and Senior Experience, among others.");

        buildingMessages.put("Science Hall 1", "Science Hall 1\n" +
                "Acronym: SCI1, Building No. 3\n" +
                "One of the three original buildings on campus, Science Hall 1 houses numerous labs, offices and classrooms. " +
                "The entrance to the building is a popular destination for students, faculty, and staff, " +
                "who have been visiting the Campus Coffee cart for more than 15 years.");

        buildingMessages.put("Science Hall 2", "Science Hall 2\n" +
                "Acronym: SCI2, Building No. 37\n" +
                "Though first-time visitors sometimes confuse it with Science Hall 1," +
                " CSUSM's second science building is located on the east side of campus between the University" +
                " Student Union and the parking structure. " +
                "The building’s opening in 2003 provided much-needed additional lab and classroom space.");

        buildingMessages.put("Social & Behavioral Sciences Building", "Social & Behavioral Sciences Building\n" +
                "Acronym: SBSB, Building No. 31\n" +
                "Referred to as SBSB for short, the Social & Behavioral Sciences Building" +
                " is home to the College of Humanities, Arts, Behavioral, and Social Sciences. " +
                "The university’s largest college has two dozen departments that offer an exciting" +
                " variety of degrees and programs that make up the core of a liberal arts education at CSUSM.");

        buildingMessages.put("University Commons", "University Commons\n" +
                "Acronym: COM, Building No. 2\n" +
                "Built as part of CSUSM’s initial core buildings, Commons is best known as the location " +
                "of the University Bookstore, where students, faculty, staff, and community members can" +
                " find everything from branded merchandise, souvenirs, school supplies, and spirit apparel to computers," +
                " technological accessories, and textbooks.");

        buildingMessages.put("University Hall", "University Hall\n" +
                "Acronym: UNIV, Building No. 15\n" +
                "Opened in 1998, University Hall served as a model for future buildings on campus as its array" +
                " of classroom configurations helped to determine how students respond to different methods of learning. " +
                "Today, University Hall is home to the College of Education, Health, and Human Services, which" +
                " includes the School of Education and the School of Nursing as well as departments" +
                " in human development, kinesiology, public health, social work, and speech-language pathology.");

        buildingMessages.put("Viasat Engineering Pavilion", "Viasat Engineering Pavilion\n" +
                "An extensive renovation was completed in spring 2020, turning the former Foundation Classroom" +
                " Building into a state-of-the-art home for the university’s electrical and software engineering programs. " +
                "The pavilion is named in honor of the Carlsbad-based global communications company that provided a" +
                " $1.5 million gift to become the founding partner of CSUSM’s engineering programs.");

        // Add service building messages here
        buildingMessages.put("Administrative Building", "Administrative Building\n" +
                "Acronym: ADM, Building No. 1\n" +
                "The Administrative Building, easily recognized by its iconic rotunda, is home to the university's" +
                " administrative offices, " +
                "including the Office of the President. Students are frequent visitors to the third floor and" +
                " Cougar Central, a one-stop location for Admissions & Student Outreach, " +
                "Financial Aid and Scholarships, Office of the Registrar, and Student Financial Services.");

        buildingMessages.put("Center for Children & Families", "Center for Children & Families\n" +
                "Acronym: CCF, Building No. 22\n" +
                "Since opening in 2007, CSUSM’s on-campus child-care center has provided convenient," +
                " quality care to the families of students, faculty, staff, and the greater community. " +
                "CCF offers child care and preschool education to children from 6 weeks to 5 years old " +
                "in a state-of-the-art facility that includes 12 spacious classrooms, three age-specific playgrounds, " +
                "a full-service kitchen, a kid's kitchen, a children's garden, and internet-accessible cameras.");

        buildingMessages.put("Epstein Family Veterans Center", "Epstein Family Veterans Center\n" +
                "Acronym: VET, Building No. 4\n" +
                "CSUSM has the highest percentage per capita of student veterans of any California State University campus" +
                " and proudly serves more than 1,700 military-connected students. " +
                "The original Veterans Center building was donated to CSUSM in 2014 by students at Stevens Institute" +
                " of Technology in New Jersey. " +
                "An extensive renovation and expansion was completed in 2019, providing all of the resources our" +
                " student veterans and their families need to achieve their academic and career goals.");

        buildingMessages.put("Kellogg Library", "Kellogg Library\n" +
                "Acronym: KEL, Building No. 17\n" +
                "Named after Keith Kellogg II, grandson of the famed cereal company magnate, Kellogg Library is an " +
                "essential partner in teaching and learning, research, and community engagement at CSUSM. " +
                "The five-story, 200,000-square-foot building, which opened in 2004, includes a 24/5 Zone, available" +
                " to students, faculty, and staff around the clock, five days a week. " +
                "The Starbucks located near the main entrance is a popular spot throughout the day.");

        buildingMessages.put("M. Gordon Clarke Fieldhouse", "M. Gordon Clarke Fieldhouse\n" +
                "Acronym: CFH, Building No. 23\n" +
                "Affectionately known as “The Clarke,” the M. Gordon Clarke Field House provided the first on-campus recreation" +
                " and meeting space for students. " +
                "Completed in 2003, the building includes a fitness center, gymnasium, locker rooms, " +
                "a catering kitchen, conference rooms, and office space for Campus Recreation and Cougar Athletics.");

        buildingMessages.put("McMahan House", "McMahan House\n" +
                "Acronym: MCM, Building No. 50\n" +
                "Built in 2009, the McMahan House is a picturesque on-campus gathering place for conferences, receptions," +
                " and even weddings. " +
                "Once considered as a location for a permanent residence for the university’s president, " +
                "the McMahan House is composed of four buildings – a great room, a library, " +
                "a retreat room, and a tower room – connected by pedestrian walkways and a central courtyard " +
                "that can accommodate up to 175 people.");
        buildingMessages.put("Sports Center", "Sports Center\n" +
                "Acronym: SC, Building No. 24A\n" +
                "With seating for 1,400, the Sports Center has been the home of men’s and women’s basketball" +
                " and women’s volleyball since 2016. " +
                "The 25,000-square-foot facility also includes a ticket office, offices for coaches, a student-athlete lounge, " +
                "and student-athlete locker rooms for all sports. " +
                "The Department of Athletics sponsors 13 NCAA Division II teams that compete in the California Collegiate" +
                " Athletic Association. " +
                "Students can obtain free tickets for athletics events online.");

        buildingMessages.put("University Student Union", "University Student Union\n" +
                "Acronym: USU, Building No. 25\n" +
                "Known as “the heartbeat of campus,” the USU is the hub of student life at CSUSM. " +
                "From half a dozen dining options to a convenience store, game rooms, lounges, a ballroom, and an outdoor amphitheater," +
                " there is no shortage of spaces and events for students. " +
                "The USU is also where you’ll find Associated Students, Inc., five student life centers, the Office of the Dean" +
                " of Students, and Student Life and Leadership.");
        buildingMessages.put("Student Health & Counseling Services Building", "Student Health & Counseling Services Building\n" +
                "Acronym: SHCSB, Building No. 21\n" +
                "Located adjacent to the parking structure on Campus Way Circle, Student Health & Counseling Services" +
                " (SHCS) offers vital health services to students, " +
                "including clinical exams, counseling, pharmacy, and health education classes. SHCS is fully accredited through" +
                " the Accreditation Association for Ambulatory Health Care – achieving the highest ratings in all areas of this" +
                " comprehensive review – " +
                "and has a staff of professionals dedicated to serving students in a warm, caring, and professional environment.");
        buildingMessages.put("Public Safety Building", "Public Safety Building\n" +
                "Acronym: PSB, Building No. 63\n" +
                "Home to Parking and Commuter Services and the University Police Department, the Public Safety Building" +
                " is located at La Moree Road and Barham Drive on the northeast corner of campus. " +
                "The building is staffed 24 hours a day, 365 days a year, and CSUSM was ranked first in California" +
                " in the National Council for Home Safety and Security’s 2020 ranking of the safest college campuses in America.");
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
    }
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
        academicHalls.addSubcategory(new CategoryNode("Academic Hall"));
        academicHalls.addSubcategory(new CategoryNode("Arts Building"));
        academicHalls.addSubcategory(new CategoryNode("Extended Learning Building"));
        academicHalls.addSubcategory(new CategoryNode("Markstein Hall"));
        academicHalls.addSubcategory(new CategoryNode("Science Hall 1"));
        academicHalls.addSubcategory(new CategoryNode("Science Hall 2"));
        academicHalls.addSubcategory(new CategoryNode("Social & Behavioral Sciences Building"));
        academicHalls.addSubcategory(new CategoryNode("University Commons"));
        academicHalls.addSubcategory(new CategoryNode("University Hall"));
        academicHalls.addSubcategory(new CategoryNode("Viasat Engineering Pavilion"));
        // Service Buildings
        serviceBuildings.addSubcategory(new CategoryNode("Administrative Building"));
        serviceBuildings.addSubcategory(new CategoryNode("Center for Children & Families"));
        serviceBuildings.addSubcategory(new CategoryNode("Epstein Family Veterans Center"));
        serviceBuildings.addSubcategory(new CategoryNode("Kellogg Library"));
        serviceBuildings.addSubcategory(new CategoryNode("M. Gordon Clarke Fieldhouse"));
        serviceBuildings.addSubcategory(new CategoryNode("McMahan House"));
        serviceBuildings.addSubcategory(new CategoryNode("Public Safety Building"));
        serviceBuildings.addSubcategory(new CategoryNode("Sports Center"));
        serviceBuildings.addSubcategory(new CategoryNode("Student Health & Counseling Services Building"));
        serviceBuildings.addSubcategory(new CategoryNode("University Student Union"));
        // Add Academic Halls and Service Buildings to Buildings category
        buildings.addSubcategory(academicHalls);
        buildings.addSubcategory(serviceBuildings);
        //Health & Safety category
        CategoryNode healthSafety = new CategoryNode("Health & Safety");
        healthSafety.addSubcategory(new CategoryNode("Emergency Phones"));
        healthSafety.addSubcategory(new CategoryNode("AED Locations"));
        healthSafety.addSubcategory(new CategoryNode("Lactation Rooms"));
        healthSafety.addSubcategory(new CategoryNode("Public Safety Building"));
        healthSafety.addSubcategory(new CategoryNode("Student Health & Counseling Services Building"));
        //Parking & Transit
        CategoryNode ParkingTransit = new CategoryNode("Parking & Transit");
        ParkingTransit.addSubcategory(new CategoryNode("Emergency Phones"));
        ParkingTransit.addSubcategory(new CategoryNode("AED Locations"));
        ParkingTransit.addSubcategory(new CategoryNode("Lactation Rooms"));
        ParkingTransit.addSubcategory(new CategoryNode("Public Safety Building"));
        ParkingTransit.addSubcategory(new CategoryNode("Student Health & Counseling Services Building"));
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

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

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
}

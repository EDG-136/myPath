package com.tecksupport.glfw.ui;

import com.tecksupport.glfw.view.Window;
import imgui.ImGui;
import imgui.flag.*;
import imgui.type.ImString;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BuildingInfoUI {
    private final Window window;
    private Map<String,String> buildingMessages = new HashMap<>();
    // The root of the navigation tree
    private CategoryNode root;
    private CategoryNode currentNode; // The currently displayed category
    private HashMap<String, Integer> icons = new HashMap<>();
    Map<String, Object> Images = new HashMap<>();
    private ImString searchQuery = new ImString(256); // Initialize with a maximum capacity of 256 characters
    private final Map<String, Integer> imageIndices = new HashMap<>(); // Maintain a map to track the current image index for each category
    private boolean isFullscreen = false; // Track if the image is in fullscreen

    public BuildingInfoUI(Window window)
    {
        this.window = window;
        init();
    }
    public void init() {
        NavigationUI();
        icons.put("Buildings", ImageLoader.loadTexture("src/main/resources/Icons/Buildings.png"));
        //Academic Halls
        icons.put("Academic Halls", ImageLoader.loadTexture("src/main/resources/Icons/Academic Halls.png"));
        Images.put("Academic Hall", ImageLoader.loadTexture("src/main/resources/Images/Academic Hall.png"));
        Images.put("Arts Building", ImageLoader.loadTexture("src/main/resources/Images/Arts Building.png"));
        Images.put("Extended Learning Building", ImageLoader.loadTexture("src/main/resources/Images/Extended Learning Building.png"));
        Images.put("Markstein Hall", ImageLoader.loadTexture("src/main/resources/Images/Markstein Hall.png"));
        Images.put("Science Hall 1", ImageLoader.loadTexture("src/main/resources/Images/Science Hall 1.png"));
        Images.put("Science Hall 2", ImageLoader.loadTexture("src/main/resources/Images/Scienc Hall 2.png"));
        Images.put("Social & Behavioral Sciences Building", ImageLoader.loadTexture("src/main/resources/Images/Social & Behavioral Sciences Building.png"));
        Images.put("University Commons", ImageLoader.loadTexture("src/main/resources/Images/University Commons.png"));
        Images.put("University Hall", ImageLoader.loadTexture("src/main/resources/Images/University Hall.png"));

        //Service Builings
        icons.put("Service Buildings", ImageLoader.loadTexture("src/main/resources/Icons/Service Buildings.png"));
        Images.put("Administrative Building", ImageLoader.loadTexture("src/main/resources/Images/Administrative Building.png"));
        Images.put("Center for Children & Families", ImageLoader.loadTexture("src/main/resources/Images/Center for Children & Families.png"));
        Images.put("Epstein Family Veterans Center", ImageLoader.loadTexture("src/main/resources/Images/Epstein Family Veterans Center.png"));
        Images.put("Kellogg Library", ImageLoader.loadTexture("src/main/resources/Images/Kellogg Library.png"));
        Images.put("M. Gordon Clarke Fieldhouse", ImageLoader.loadTexture("src/main/resources/Images/M. Gordon Clarke Fieldhouse.png"));
        Images.put("McMahan House", ImageLoader.loadTexture("src/main/resources/Images/McMahan House.png"));
        Images.put("Public Safety Building", ImageLoader.loadTexture("src/main/resources/Images/Public Safety Building.png"));
        Images.put("Sports Center", ImageLoader.loadTexture("src/main/resources/Images/Sports Center.png"));
        Images.put("Student Health & Counseling Services Building", ImageLoader.loadTexture("src/main/resources/Images/Student Health & Counseling Services Building.png"));
        List<Integer> USUImages = new ArrayList<>();
        USUImages.add(ImageLoader.loadTexture("src/main/resources/Images/University Student Union 1.png"));
        USUImages.add(ImageLoader.loadTexture("src/main/resources/Images/University Student Union 2.png"));
        Images.put("University Student Union", USUImages);
        // Health
        icons.put("Health & Safety", ImageLoader.loadTexture("src/main/resources/Icons/medic.png"));
        icons.put("Emergency Phones", ImageLoader.loadTexture("src/main/resources/Icons/Emergency Phones.png"));
        icons.put("AED", ImageLoader.loadTexture("src/main/resources/Icons/AED.png"));
        icons.put("Lactation Rooms", ImageLoader.loadTexture("src/main/resources/Icons/Lactation Rooms.png"));
        //Parking
        icons.put("Parking & Transit", ImageLoader.loadTexture("src/main/resources/Icons/bus-station.png"));
        icons.put("30 Minute Parking", ImageLoader.loadTexture("src/main/resources/Icons/Parking.png"));
        icons.put("Alternative Transportation", ImageLoader.loadTexture("src/main/resources/Icons/Alternative Transportation.png"));
        //Zip Car Shareing Program
        icons.put("Zip Car Sharing Program", ImageLoader.loadTexture("src/main/resources/Icons/Zip Car Sharing Program.png"));
        List<Integer> ZipImages = new ArrayList<>();
        ZipImages.add(ImageLoader.loadTexture("src/main/resources/Images/Zip Car Sharing Program 1.png"));
        ZipImages.add(ImageLoader.loadTexture("src/main/resources/Images/Zip Car Sharing Program 2.png"));
        ZipImages.add(ImageLoader.loadTexture("src/main/resources/Images/Zip Car Sharing Program 3.png"));
        Images.put("Zip Car Sharing Program", ZipImages);
        icons.put("Bike Lockers", ImageLoader.loadTexture("src/main/resources/Icons/Bike Racks.png"));
        icons.put("Carpool Parking", ImageLoader.loadTexture("src/main/resources/Icons/Parking.png"));
        icons.put("Bike Racks", ImageLoader.loadTexture("src/main/resources/Icons/Bike Racks.png"));
        icons.put("Electric Vehicle Charging Station", ImageLoader.loadTexture("src/main/resources/Icons/Electric Vehicle Charging Station.png"));
        icons.put("Disabled Parking", ImageLoader.loadTexture("src/main/resources/Icons/Disabled Parking.png"));
        icons.put("Faculty/Staff Parking", ImageLoader.loadTexture("src/main/resources/Icons/Parking.png"));
        icons.put("General Parking", ImageLoader.loadTexture("src/main/resources/Icons/Parking.png"));
        icons.put("Streets", ImageLoader.loadTexture("src/main/resources/Icons/Streets.png"));
        icons.put("Metered Parking", ImageLoader.loadTexture("src/main/resources/Icons/Parking.png"));
        icons.put("Permit Purchase Stations", ImageLoader.loadTexture("src/main/resources/Icons/Permit Purchace Stations.png"));
        icons.put("NCTD Bus Stops", ImageLoader.loadTexture("src/main/resources/Icons/NCTD Bus Stops.png"));
        icons.put("Sprinter Station", ImageLoader.loadTexture("src/main/resources/Icons/Sprinter Station.png"));
        //Services
        icons.put("Services", ImageLoader.loadTexture("src/main/resources/Icons/Services.png"));
        icons.put("Cougar Central", ImageLoader.loadTexture("src/main/resources/Icons/Services.png"));
        //IT Help Desk
        icons.put("IT Help Desk", ImageLoader.loadTexture("src/main/resources/Icons/IT Help Desk.png"));
        Images.put("IT Help Desk", ImageLoader.loadTexture("src/main/resources/Images/IT Help.png"));
        // Academic Success Center
        icons.put("Learning Centers", ImageLoader.loadTexture("src/main/resources/Icons/Learning Centers.png"));
        Images.put("Academic Success Center", ImageLoader.loadTexture("src/main/resources/Images/Academic Success Center.png"));
        // Alumni Office
        icons.put("Alumni Association", ImageLoader.loadTexture("src/main/resources/Icons/Alumni Association.png"));
        Images.put("Alumni Association Office", ImageLoader.loadTexture("src/main/resources/Images/Alumni Association Office.png"));
        //Athletics
        icons.put("Athletics", ImageLoader.loadTexture("src/main/resources/Icons/Athletics.png"));
        //Baseball
        List<Integer> BaseballImages = new ArrayList<>();
        BaseballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Baseball Field 1.png"));
        BaseballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Baseball Field 2.png"));
        BaseballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Baseball Field 3.png"));
        BaseballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Baseball Field 4.png"));
        BaseballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Baseball Field 5.png"));
        BaseballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Baseball Field 6.png"));
        Images.put("Baseball Field", BaseballImages);
        //Clarke Field House
        List<Integer> ClarkeImages = new ArrayList<>();
        ClarkeImages.add(ImageLoader.loadTexture("src/main/resources/Images/Clarke Field House 1.png"));
        ClarkeImages.add(ImageLoader.loadTexture("src/main/resources/Images/Clarke Field House 2.png"));
        ClarkeImages.add(ImageLoader.loadTexture("src/main/resources/Images/Clarke Field House 3.png"));
        ClarkeImages.add(ImageLoader.loadTexture("src/main/resources/Images/Clarke Field House 4.png"));
        ClarkeImages.add(ImageLoader.loadTexture("src/main/resources/Images/Clarke Field House 5.png"));
        ClarkeImages.add(ImageLoader.loadTexture("src/main/resources/Images/Clarke Field House 6.png"));
        Images.put("Clarke Field House", ClarkeImages);
        //Mangrum Track & Field
        List<Integer> mangImages = new ArrayList<>();
        mangImages.add(ImageLoader.loadTexture("src/main/resources/Images/Mangrum Track & Field 1.png"));
        mangImages.add(ImageLoader.loadTexture("src/main/resources/Images/Mangrum Track & Field 2.png"));
        mangImages.add(ImageLoader.loadTexture("src/main/resources/Images/Mangrum Track & Field 3.png"));
        mangImages.add(ImageLoader.loadTexture("src/main/resources/Images/Mangrum Track & Field 4.png"));
        mangImages.add(ImageLoader.loadTexture("src/main/resources/Images/Mangrum Track & Field 5.png"));
        mangImages.add(ImageLoader.loadTexture("src/main/resources/Images/Mangrum Track & Field 6.png"));
        Images.put("Mangrum Track & Field", mangImages);
        //Multi-Purpose Field
        List<Integer> MutiImages = new ArrayList<>();
        MutiImages.add(ImageLoader.loadTexture("src/main/resources/Images/Multi-Purpose Field 1.png"));
        MutiImages.add(ImageLoader.loadTexture("src/main/resources/Images/Multi-Purpose Field 2.png"));
        Images.put("Multi-Purpose Field", MutiImages);
        //Softball Field
        List<Integer> softballImages = new ArrayList<>();
        softballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Softball Field 1.png"));
        softballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Softball Field 2.png"));
        softballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Softball Field 3.png"));
        softballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Softball Field 4.png"));
        softballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Softball Field 5.png"));
        softballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Softball Field 6.png"));
        softballImages.add(ImageLoader.loadTexture("src/main/resources/Images/Softball Field 7.png"));
        Images.put("Softball Field", softballImages);
        icons.put("Office of Global Education", ImageLoader.loadTexture("src/main/resources/Icons/Office of global Education.png"));
        icons.put("Career Center", ImageLoader.loadTexture("src/main/resources/Icons/Career Center.png"));
        icons.put("Academic Advising", ImageLoader.loadTexture("src/main/resources/Icons/Academic Advising.png"));
        //Student Centers
        icons.put("Student Centers", ImageLoader.loadTexture("src/main/resources/Icons/table.png"));
        //Student Housing
        icons.put("Student Housing", ImageLoader.loadTexture("src/main/resources/Icons/home.png"));
        // North Commons
        List<Integer> northCommonsImages = new ArrayList<>();
        northCommonsImages.add(ImageLoader.loadTexture("src/main/resources/Images/North Coms 1.png"));
        northCommonsImages.add(ImageLoader.loadTexture("src/main/resources/Images/North Coms 2.png"));
        northCommonsImages.add(ImageLoader.loadTexture("src/main/resources/Images/North Coms 3.png"));
        northCommonsImages.add(ImageLoader.loadTexture("src/main/resources/Images/North Coms 4.png"));
        northCommonsImages.add(ImageLoader.loadTexture("src/main/resources/Images/North Coms 5.png"));
        northCommonsImages.add(ImageLoader.loadTexture("src/main/resources/Images/North Coms 6.png"));
        northCommonsImages.add(ImageLoader.loadTexture("src/main/resources/Images/North Coms 7.png"));
        northCommonsImages.add(ImageLoader.loadTexture("src/main/resources/Images/North Coms 8.png"));
        Images.put("North Commons", northCommonsImages);
        //The QUAD
        List<Integer> QuadImages = new ArrayList<>();
        QuadImages.add(ImageLoader.loadTexture("src/main/resources/Images/Quad 1.png"));
        QuadImages.add(ImageLoader.loadTexture("src/main/resources/Images/Quad 2.png"));
        QuadImages.add(ImageLoader.loadTexture("src/main/resources/Images/Quad 3.png"));
        QuadImages.add(ImageLoader.loadTexture("src/main/resources/Images/Quad 4.png"));
        QuadImages.add(ImageLoader.loadTexture("src/main/resources/Images/Quad 5.png"));
        QuadImages.add(ImageLoader.loadTexture("src/main/resources/Images/Quad 6.png"));
        Images.put("The QUAD", QuadImages);
        //The QUAD
        List<Integer> UavImages = new ArrayList<>();
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA 1.png"));
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA-2.png"));
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA-3.png"));
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA-4.png"));
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA-5.png"));
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA-6.png"));
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA-7.png"));
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA-8.png"));
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA-9.png"));
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA-10.png"));
        UavImages.add(ImageLoader.loadTexture("src/main/resources/Images/UVA 11.png"));
        Images.put("University Village Apartments", UavImages);
        //Food
        icons.put("Dining & Cafes", ImageLoader.loadTexture("src/main/resources/Icons/Dining & Cafes.png"));
        //Study
        icons.put("Study And Online Class Spaces", ImageLoader.loadTexture("src/main/resources/Icons/open-book.png"));

        icons.put("Open Access Computer Lab", ImageLoader.loadTexture("src/main/resources/Icons/open-book.png"));
        icons.put("Kellogg Library Group Study Rooms", ImageLoader.loadTexture("src/main/resources/Icons/open-book.png"));
        icons.put("Indoor Study Spaces", ImageLoader.loadTexture("src/main/resources/Icons/open-book.png"));
        //Outdoor
        icons.put("Outdoor Study Spaces", ImageLoader.loadTexture("src/main/resources/Icons/open-book.png"));
        //Forum Plaza
        List<Integer> ForumImages = new ArrayList<>();
        ForumImages.add(ImageLoader.loadTexture("src/main/resources/Images/Forum Plaza 1.png"));
        ForumImages.add(ImageLoader.loadTexture("src/main/resources/Images/Forum Plaza 2.png"));
        ForumImages.add(ImageLoader.loadTexture("src/main/resources/Images/Forum Plaza 3.png"));
        ForumImages.add(ImageLoader.loadTexture("src/main/resources/Images/Forum Plaza 4.png"));
        ForumImages.add(ImageLoader.loadTexture("src/main/resources/Images/Forum Plaza 5.png"));
        ForumImages.add(ImageLoader.loadTexture("src/main/resources/Images/Forum Plaza 6.png"));
        ForumImages.add(ImageLoader.loadTexture("src/main/resources/Images/Forum Plaza 7.png"));
        Images.put("Forum Plaza", ForumImages);
        //Founders Plaza
        List<Integer> FounderImages = new ArrayList<>();
        FounderImages.add(ImageLoader.loadTexture("src/main/resources/Images/Founders Plaza 1.png"));
        FounderImages.add(ImageLoader.loadTexture("src/main/resources/Images/Founders Plaza 2.png"));
        FounderImages.add(ImageLoader.loadTexture("src/main/resources/Images/Founders Plaza 3.png"));
        FounderImages.add(ImageLoader.loadTexture("src/main/resources/Images/Founders Plaza 4.png"));
        Images.put("Founders Plaza", FounderImages);
        //Gonzalez Grove
        List<Integer> GonzImages = new ArrayList<>();
        GonzImages.add(ImageLoader.loadTexture("src/main/resources/Images/Gonzalez Grove 1.png"));
        GonzImages.add(ImageLoader.loadTexture("src/main/resources/Images/Gonzalez Grove 2.png"));
        GonzImages.add(ImageLoader.loadTexture("src/main/resources/Images/Gonzalez Grove 3.png"));
        GonzImages.add(ImageLoader.loadTexture("src/main/resources/Images/Gonzalez Grove 4.png"));
        GonzImages.add(ImageLoader.loadTexture("src/main/resources/Images/Gonzalez Grove 5.png"));
        GonzImages.add(ImageLoader.loadTexture("src/main/resources/Images/Gonzalez Grove 6.png"));
        GonzImages.add(ImageLoader.loadTexture("src/main/resources/Images/Gonzalez Grove 7.png"));
        Images.put("Gonzalez Grove", GonzImages);
        //Palm Court
        List<Integer> PalmImages = new ArrayList<>();
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 1.png"));
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 2.png"));
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 3.png"));
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 4.png"));
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 5.png"));
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 6.png"));
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 7.png"));
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 8.png"));
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 9.png"));
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 10.png"));
        PalmImages.add(ImageLoader.loadTexture("src/main/resources/Images/Palm Court 11.png"));
        Images.put("Palm Court", PalmImages);
        //Tukwut Courtyard
        List<Integer> TukwutImages = new ArrayList<>();
        TukwutImages.add(ImageLoader.loadTexture("src/main/resources/Images/Tukwut Courtyard 1.png"));
        TukwutImages.add(ImageLoader.loadTexture("src/main/resources/Images/Tukwut Courtyard 2.png"));
        TukwutImages.add(ImageLoader.loadTexture("src/main/resources/Images/Tukwut Courtyard 3.png"));
        Images.put("Tukwut Courtyard", TukwutImages);
        //USU Ampitheater
        List<Integer> USUAmpImages = new ArrayList<>();
        USUAmpImages.add(ImageLoader.loadTexture("src/main/resources/Images/USU Ampitheater 1.png"));
        USUAmpImages.add(ImageLoader.loadTexture("src/main/resources/Images/USU Ampitheater 2.png"));
        Images.put("USU Ampitheater", USUAmpImages);
        icons.put("Plazas & Gardens", ImageLoader.loadTexture("src/main/resources/Icons/Tree.png"));
        icons.put("Gardens", ImageLoader.loadTexture("src/main/resources/Icons/Gardens.png"));
        //Sustainable Food Project Garden
        Images.put("Sustainable Food Project Garden", ImageLoader.loadTexture("src/main/resources/Images/Sustainable Food Project Garden.png"));
        icons.put("Plazas", ImageLoader.loadTexture("src/main/resources/Icons/Plazas.png"));
        //Kellogg Plaza
        List<Integer> KelloImages = new ArrayList<>();
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 1.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 2.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 3.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 4.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 5.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 6.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 7.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 8.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 9.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 10.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 11.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 12.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 13.png"));
        KelloImages.add(ImageLoader.loadTexture("src/main/resources/Images/Kellogg Plaza 14.png"));
        Images.put("Kellogg Plaza", KelloImages);

        // Existing building messages
        buildingMessages.put("Academic Hall",
                "Acronym: ACD, Building No. 14\n" +
                "One of the three original buildings on campus, Academic Hall was the first to provide lecture" +
                " classroom and computer lab space, " +
                "including a 145-seat, stadium-style lecture hall. Academic Hall features the campus’ landmark clock tower," +
                " which is also present on CSUSM’s campus logo. " +
                "First-year students are frequent visitors to the building, which is home to many general education classes.");

        buildingMessages.put("Arts Building",
                "Acronym: ARTS, Building No. 26-27\n" +
                "Built as part of a master-planned second phase of the original campus buildings," +
                " the Arts Building opened in 2003 and serves as the home of the university’s School of Arts. " +
                "The building includes music and recording studios, a lobby art gallery, a 150-seat performance hall," +
                " a dance studio and much more.");

        buildingMessages.put("Extended Learning Building",
                "The largest academic building on campus, the 135,000-square-foot Extended Learning building brought all" +
                " Extended Learning operations under one roof for the first time when it opened in 2019. " +
                "It was the first academic building in California established through a unique " +
                "public-private partnership, requiring no state funds for its design, planning or construction. " +
                "It is also home to student support centers, community outreach centers, lab and research facilities," +
                " the STEM Education Center and the Innovation Hub.");

        buildingMessages.put("Markstein Hall",
                "Acronym: MARK, Building No. 13\n" +
                "The name of the building, home to the College of Business Administration, honors the owners of the " +
                "San Marcos-based Markstein Beverage Co., who recognized the importance of helping build a strong program for" +
                " business students. " +
                "Today, Markstein Hall is the central hub of business leadership in the region, " +
                "offering signature programs such as the Center for Leadership Innovation and Mentorship " +
                "Building, In the Executive’s Chair and Senior Experience, among others.");

        buildingMessages.put("Science Hall 1",
                "Acronym: SCI1, Building No. 3\n" +
                "One of the three original buildings on campus, Science Hall 1 houses numerous labs, offices and classrooms. " +
                "The entrance to the building is a popular destination for students, faculty, and staff, " +
                "who have been visiting the Campus Coffee cart for more than 15 years.");

        buildingMessages.put("Science Hall 2",
                "Acronym: SCI2, Building No. 37\n" +
                "Though first-time visitors sometimes confuse it with Science Hall 1," +
                " CSUSM's second science building is located on the east side of campus between the University" +
                " Student Union and the parking structure. " +
                "The building’s opening in 2003 provided much-needed additional lab and classroom space.");

        buildingMessages.put("Social & Behavioral Sciences Building",
                "Acronym: SBSB, Building No. 31\n" +
                "Referred to as SBSB for short, the Social & Behavioral Sciences Building" +
                " is home to the College of Humanities, Arts, Behavioral, and Social Sciences. " +
                "The university’s largest college has two dozen departments that offer an exciting" +
                " variety of degrees and programs that make up the core of a liberal arts education at CSUSM.");

        buildingMessages.put("University Commons",
                "Acronym: COM, Building No. 2\n" +
                "Built as part of CSUSM’s initial core buildings, Commons is best known as the location " +
                "of the University Bookstore, where students, faculty, staff, and community members can" +
                " find everything from branded merchandise, souvenirs, school supplies, and spirit apparel to computers," +
                " technological accessories, and textbooks.");

        buildingMessages.put("University Hall",
                "Acronym: UNIV, Building No. 15\n" +
                "Opened in 1998, University Hall served as a model for future buildings on campus as its array" +
                " of classroom configurations helped to determine how students respond to different methods of learning. " +
                "Today, University Hall is home to the College of Education, Health, and Human Services, which" +
                " includes the School of Education and the School of Nursing as well as departments" +
                " in human development, kinesiology, public health, social work, and speech-language pathology.");

        buildingMessages.put("Viasat Engineering Pavilion",
                "An extensive renovation was completed in spring 2020, turning the former Foundation Classroom" +
                " Building into a state-of-the-art home for the university’s electrical and software engineering programs. " +
                "The pavilion is named in honor of the Carlsbad-based global communications company that provided a" +
                " $1.5 million gift to become the founding partner of CSUSM’s engineering programs.");

        // Add service building messages here
        buildingMessages.put("Administrative Building",
                "Acronym: ADM, Building No. 1\n" +
                "The Administrative Building, easily recognized by its iconic rotunda, is home to the university's" +
                " administrative offices, " +
                "including the Office of the President. Students are frequent visitors to the third floor and" +
                " Cougar Central, a one-stop location for Admissions & Student Outreach, " +
                "Financial Aid and Scholarships, Office of the Registrar, and Student Financial Services.");

        buildingMessages.put("Center for Children & Families",
                "Acronym: CCF, Building No. 22\n" +
                "Since opening in 2007, CSUSM’s on-campus child-care center has provided convenient," +
                " quality care to the families of students, faculty, staff, and the greater community. " +
                "CCF offers child care and preschool education to children from 6 weeks to 5 years old " +
                "in a state-of-the-art facility that includes 12 spacious classrooms, three age-specific playgrounds, " +
                "a full-service kitchen, a kid's kitchen, a children's garden, and internet-accessible cameras.");

        buildingMessages.put("Epstein Family Veterans Center",
                "Acronym: VET, Building No. 4\n" +
                "CSUSM has the highest percentage per capita of student veterans of any California State University campus" +
                " and proudly serves more than 1,700 military-connected students. " +
                "The original Veterans Center building was donated to CSUSM in 2014 by students at Stevens Institute" +
                " of Technology in New Jersey. " +
                "An extensive renovation and expansion was completed in 2019, providing all of the resources our" +
                " student veterans and their families need to achieve their academic and career goals.");

        buildingMessages.put("Kellogg Library",
                "Acronym: KEL, Building No. 17\n" +
                "Named after Keith Kellogg II, grandson of the famed cereal company magnate, Kellogg Library is an " +
                "essential partner in teaching and learning, research, and community engagement at CSUSM. " +
                "The five-story, 200,000-square-foot building, which opened in 2004, includes a 24/5 Zone, available" +
                " to students, faculty, and staff around the clock, five days a week. " +
                "The Starbucks located near the main entrance is a popular spot throughout the day.");

        buildingMessages.put("M. Gordon Clarke Fieldhouse",
                "Acronym: CFH, Building No. 23\n" +
                "Affectionately known as “The Clarke,” the M. Gordon Clarke Field House provided the first on-campus recreation" +
                " and meeting space for students. " +
                "Completed in 2003, the building includes a fitness center, gymnasium, locker rooms, " +
                "a catering kitchen, conference rooms, and office space for Campus Recreation and Cougar Athletics.");

        buildingMessages.put("McMahan House",
                "Acronym: MCM, Building No. 50\n" +
                "Built in 2009, the McMahan House is a picturesque on-campus gathering place for conferences, receptions," +
                " and even weddings. " +
                "Once considered as a location for a permanent residence for the university’s president, " +
                "the McMahan House is composed of four buildings – a great room, a library, " +
                "a retreat room, and a tower room – connected by pedestrian walkways and a central courtyard " +
                "that can accommodate up to 175 people.");
        buildingMessages.put("Sports Center",
                "Acronym: SC, Building No. 24A\n" +
                "With seating for 1,400, the Sports Center has been the home of men’s and women’s basketball" +
                " and women’s volleyball since 2016. " +
                "The 25,000-square-foot facility also includes a ticket office, offices for coaches, a student-athlete lounge, " +
                "and student-athlete locker rooms for all sports. " +
                "The Department of Athletics sponsors 13 NCAA Division II teams that compete in the California Collegiate" +
                " Athletic Association. " +
                "Students can obtain free tickets for athletics events online.");

        buildingMessages.put("University Student Union",
                "Acronym: USU, Building No. 25\n" +
                "Known as “the heartbeat of campus,” the USU is the hub of student life at CSUSM. " +
                "From half a dozen dining options to a convenience store, game rooms, lounges, a ballroom, and an outdoor amphitheater," +
                " there is no shortage of spaces and events for students. " +
                "The USU is also where you’ll find Associated Students, Inc., five student life centers, the Office of the Dean" +
                " of Students, and Student Life and Leadership.");
        buildingMessages.put("Student Health & Counseling Services Building",
                "Acronym: SHCSB, Building No. 21\n" +
                "Located adjacent to the parking structure on Campus Way Circle, Student Health & Counseling Services" +
                " (SHCS) offers vital health services to students, " +
                "including clinical exams, counseling, pharmacy, and health education classes. SHCS is fully accredited through" +
                " the Accreditation Association for Ambulatory Health Care – achieving the highest ratings in all areas of this" +
                " comprehensive review – " +
                "and has a staff of professionals dedicated to serving students in a warm, caring, and professional environment.");
        buildingMessages.put("Public Safety Building",
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
                        "Helpful Links\n" +
                        "<a href='https://www.csusm.edu/wgec/parenting/lactation_spaces.html'>Lactation Spaces</a> | " +
                        "<a href='https://www.csusm.edu/wgec/parenting/lactation_spaces.html'>Gender Equity Center</a> \n" +
                        "\n" +
                        "Locations:\n" +
                        "1. Administrative Building 6312 Lactation Space\n" +
                        "2. Extended Learning Building 544 Lactation Space\n" +
                        "3. Gender Equity Center Lactation Space (USU 3200C)\n" +
                        "4. Kellogg Library 3017 Lactation Space"
        );

        buildingMessages.put("30 Minute Parking",
                        "These are “no permit required” spaces available to the community and guests to park in close proximity, " +
                        "short-term. There is a 30 minute maximum time limit enforced in these spaces."
        );
        buildingMessages.put("Bike Lockers",
                        "1.Outside M.Gorden Clark Firehouse\n" +
                        "2.Outside SHCSB"
        );
        buildingMessages.put("Carpool Parking",
                "Location\n" +
                        "Carpool Parking Lot C\n" +
                        "Carpool Parking Lot E-Faculty/Staff only\n" +
                        "Carpool Parking Lot F\n" +
                        "Carpool Parking PS1"
        );
        buildingMessages.put("Bike Racks",
                "Location\n" +
                        "1.Sprinter Platform \n" +
                        "2.Lot O\n" +
                        "3.M. Gordon Clarke Fieldhouse\n" +
                        "4.USU on Ground Level Outside\n" +
                        "5.University Hall Back Right\n" +
                        "6.Kellogg Library Ground Level\n" +
                        "7.Lot H\n" +
                        "8.University Commons Ouside Front Door\n" +
                        "9. Lot C"
        );
        buildingMessages.put("Electric Vehicle Charging Station",
                        "Our charging stations are networked through ChargePoint and are located on the 1st level of PS1. " +
                        "A valid CSUSM parking session is required when parked at the electric vehicle charging stations." +
                        " There is a 4 hour max time limit and all vehicles must be actively charging."
        );
        buildingMessages.put("Zip Car Sharing Program",
                "Car Share\n" +
                        "Zipcar offers students, staff and faculty the freedom to have a car – even if you don’t own one.  There are 2 cars on the first floor of the parking structure for you to check out and use for quick and day trips. \n" +
                        "\n" +
                        "\n" +
                        "Follow the steps below to sign up and use Zipcar\n" +
                        "HOW IT WORKS: \n" +
                        "Apply online it only takes a few minutes. Once you’re approved, you get your very own Zipcard.\n " +
                        "<a href='https://www.zipcar.com/universities/cal-state-san-marcos'>Zipcar</a> \n" +
                        "\n" +
                        "Reserve one of our cars at a low rate – for a couple of hours or the entire day. Do it online or use a phone.\n" +
                        "\n" +
                        "Walk to the car, and hold your Zipcard to the windshield. The doors will unlock, and it’s all yours!\n" +
                        "\n" +
                        "Drive away…and return the Zipcar to the same reserved parking spot at the end of your reservation. It’s that simple. And remember, gas and insurance are always included!\n" +
                        "<a href='https://www.zipcar.com/how-it-works'>More Info On Zipcar</a> "
        );
        buildingMessages.put("Disabled Parking",
                "Locations\n" +
                        "1.Lot K \n" +
                        "2.Lot J\n" +
                        "3.Lot M \n" +
                        "4.Lot N\n" +
                        "5.PS1\n" +
                        "6.Lot O\n" +
                        "7.Lot L\n" +
                        "8.Lot W\n" +
                        "9.Lot B\n" +
                        "10.Lot C\n" +
                        "11.Lot G\n" +
                        "12.Lot E\n" +
                        "13.Lot H"
        );
        buildingMessages.put("Faculty/Staff Parking",
                "Locations\n" +
                        "Lot D - Reserved Parking\n" +
                        "Lot E - Faculty/Staff Parking\n" +
                        "Lot H - Faculty/Staff Parking\n" +
                        "Lot M - Reserved Parking \n"
        );
        buildingMessages.put("General Parking",
                "Locations\n" +
                        "Lot B\n" +
                        "Lot C\n" +
                        "Lot F\n" +
                        "Lot G - Service Vehicles Only\n" +
                        "Lot J\n" +
                        "Lot K\n" +
                        "Lot L - 2 hour max.\n" +
                        "Lot N\n" +
                        "Lot X\n" +
                        "Lot Y\n" +
                        "Lot Z\n" +
                        "Parking Structure 1\n" +
                        "Parking Structure 2\n"
        );
        buildingMessages.put("Streets",
                        "Campus View Dr.\n" +
                        "Campus Way\n" +
                        "South Twin Oaks Valley Rd.\n" +
                        "The Circle"
        );
        buildingMessages.put("Metered Parking",
                        "Parking meters accept quarters, nickels, and dimes. Parking meters cost $1.50 per half hour."
        );
        buildingMessages.put("Permit Purchase Stations",
                        "Pay stations accept 1's, 5's\n" +
                        "\n" +
                        "All Day permit: $10\n" +
                        "3 hour permit: $5\n" +
                        "24 hour permit: $15\n" +
                        "License plate for vehicle parked on campus will be required. \n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "For more info call Parking and Commuter Services at (760)750-7500."
        );
        buildingMessages.put("NCTD Bus Stops",
                        "The Circle\n" +
                        "Practice Field\n" +
                        "Sprinter Platform"
        );
        buildingMessages.put("Sprinter Station",
                        "The Sprinter Runs through CSUSM In the Upper Right Hand Side of Campus "
        );
        buildingMessages.put("Admissions",
                        "Administrative Building 3900\n" +
                        "\n" +
                        "760-750-4848 - select option 0\n" +
                        "Applicant Inquiries: apply@csusm.edu\n" +
                        "Campus Tours: campustour@csusm.edu\n" +
                        "How to Apply\n" +
                        "Everything you need to know about applying to Cal State San Marcos can be found in this section." +
                        " Please select the category that best describes you for more information on your eligibility and application" +
                        " requirements.\n" +
                        "<a href='https://www.csusm.edu/admissions/how-to-apply/freshman/index.html'>Freshman</a>\n" +
                        "<a href='https://www.csusm.edu/admissions/how-to-apply/transfer/index.html'>Transfer</a>\n" +
                        "<a href='https://www.csusm.edu/global/admissions/'>International</a>\n" +
                        "Apply\n" +
                        "Our online application must be complete through <a href='https://www.calstate.edu/apply'>CAApply</a>," +
                        " California State University's comprehensive website. Be sure to apply on time! Review" +
                        "<a href='https://www.csusm.edu/admissions/how-to-apply/freshman/important-deadlines.html'>important dates and deadlines.</a> \n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Pay\n" +
                        "A non-refundable application processing fee of $55 is required. " +
                        "(This is required for each campus to which you apply.) " +
                        "For low-income California residents, a fee waiver may be available online " +
                        "<a href='https://www.calstate.edu/apply'>CSUMentor</a>\n" +
                        "\n" +
                        "\n" +
                        "Submit\n" +
                        "Submit official scores from the ACT or SAT exams (first-time freshman) or official transcripts" +
                        " from each college or university attended.\n" +
                        "\n" +
                        "\n" +
                        "Follow-up\n" +
                        "Check on the status of your application on <a href='https://my.csusm.edu/'>MyCSUSM</a>" +
                        "(you will receive login information after you apply).\n" +
                        "\n" +
                        "\n" +
                        "What do I do after I apply? We begin admitting students on a rolling basis in mid-December. " +
                        " Check your <a href='https://my.csusm.edu/'>MyCSUSM</a>" +
                        " account regularly to ensure all your information is up to date, and that the office has received" +
                        " your required documentation."
        );
        buildingMessages.put("Cashier's Desk",
                        "The University Cashiers are located in Cougar Central, Administrative Building 3900." +
                        " Students with questions or problems concerning fees, holds, refunds, or payment deadlines" +
                        " are encouraged to call the Cashiers' Information Line at (760)750-4490."
        );
        buildingMessages.put("Financial Aid",
                        "Office Hours\n" +
                        "Monday - Friday 8:00 am - 5:00 pm\n" +
                        "CSUSM is closed on major holidays\n" +
                        "\n" +
                        "\n" +
                        "When calling our office please have the student’s student ID number ready." +
                        " If you are not the student calling, please make sure the student has granted your" +
                        " written authorization to release information.\n" +
                        "For more information on FERPA, please visit our" +
                        " <a href='https://www.csusm.edu/ferpa/index.html'>FERPA</a> website.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "<a href='https://www.csusm.edu/finaid/index.html'>Learn more</a>\n" +
                                "\n"
        );
        buildingMessages.put("Office of the Registrar",
                        "<a href='https://www.csusm.edu/enroll/index.html'>Enroll</a>\n" +
                        "The Office of the Registrar provides an important link between the academic policies of CSUSM" +
                        " and our academic departments and students.  What is a registrar? A university registrar" +
                        " maintains the academic records of all registered students by providing services including:\n" +
                        "\n" +
                        "Verifying student status\n" +
                        "Confirming graduation and issuing diplomas\n" +
                        "Providing transcripts\n" +
                        "Providing enrollment periods to students to enroll in classes\n"
        );
        buildingMessages.put("Student Financial Services",
                        "<a href='https://www.csusm.edu/sfs/index.html'>Student Financial Services</a>\n" +
                        "Cougar Central, Administrative Building 3800"
        );
        buildingMessages.put("IT Help Desk",
                        "The Student Technology Help Desk (STH) supports students with a variety of issues, such as:\n" +
                        "\n" +
                        "Connecting to the Campus Wi-Fi\n" +
                        "Assistance with Campus Username/Password\n" +
                        "Accessing CougarApps\n" +
                        "Accessing my.csusm.edu\n" +
                        "Cougar Courses Questions\n" +
                        "Connecting to Your Campus Email\n" +
                        "Borrowing Media Equipment\n" +
                        "Printing on Campus\n" +
                        "All services require a current campus photo ID (available at the Media Library, directly across from" +
                        " the Student Tech Help Desk).  \n" +
                        "\n" +
                        "The Student Tech Help Desk (STH) is located directly outside of Kellogg 2000," +
                        " which is an open computer lab with 92 computer stations (with both Macs and PCs)." +
                        "  Employees of the STH are available to assist you as you get familiarized with the campus " +
                        "technology.  Please stop by if you have any questions.\n"
        );
        buildingMessages.put("Academic Success Center",
                        "A space where students receive the academic support they need to succeed.\n" +
                        "\n" +
                        "Academic Coaching\n" +
                        "Academic Support Programming\n" +
                        "Workshops\n"
        );
        buildingMessages.put("Personalized Academic Success Services (PASS)",
                        "<a href='https://www.csusm.edu/readiness/pass/index.html'>PASS</a> \n" +
                        "\n" +
                        "Personalized Academic Success Services (PASS) works with students to assess " +
                        "each student’s situation holistically to provide appropriate support and resources" +
                        " (on-campus or in the community) to help students achieve academic and personal success.\n"
        );
        buildingMessages.put("STEM Success Center",
                        "<a href='https://www.csusm.edu/lts/stemsc/index.html'>STEM</a> \n" +
                        "\n" +
                        "The Office of Undergraduate Studies is thrilled to announce we are putting the M" +
                        " back in STEM! This fall the Math Lab and STEM SSC will unify as the STEM Success Center under" +
                        " the direction of the current math lab director, Jen Brich."
        );
        buildingMessages.put("Student Outreach and Referral (SOAR) and Cougar Care Network (CCN)",
                        "<a href='https://www.csusm.edu/ccn/index.html'>CCN</a>\n" +
                        "\n" +
                        "CCN is CSUSM’s early support program to improve student success, retention," +
                        " and persistence by providing information, resources, and support needed to ensure" +
                        " a student's personal and academic success."
        );
        buildingMessages.put("Writing Center",
                        "<a href='https://www.csusm.edu/writingcenter/information/index.html'>Writing Center</a>\n" +
                        "We work with all writers from all classes at all stages of their writing process. No matter what you are working on, if it involves writing, we want to help. That help is offered in a few, well-practiced ways:\n" +
                        "\n" +
                        "One-on-One Sessions where a tutor will sit and talk about your writing with you. " +
                        "These tutoring sessions and all services offered by the Writing Center respect your ownership " +
                        "of your writing; we will not tell you what to do, but we will help you feel confident about makin\n" +
                        "Buddy Sessions for students who want to enjoy tutoring with a friend. \n" +
                        "Workshops that help you apply a variety of topic lessons to your work and provide you" +
                        " with helpful resources to take with you. \n" +
                        "Thesis Retreats for groups of graduate students who want a quiet and tutor-supported" +
                        " space to chip away at their theses, proposals, and capstone projects.\n" +
                        "Academic English Support for students who want extra support with academic expectations" +
                        " regarding grammar, organization, source use, and more."

        );
        buildingMessages.put("Alumni Association",
                        "<a href='https://www.csusm.edu/alumni/membership/joinrenew.html'>Association Online Registration Alumni</a> \n" +
                        "\n" +
                        "For more info call (760) 750-4406 or visit our office in Commons 201.\n" +
                        "\n" +
                        "The mission of the CSUSM Alumni Association is to support and encourage the advancement of the University while fostering lifelong Cougar pride, loyalty, and involvement between alumni, local businesses, and the community at large. \n" +
                        "\n" +
                        "Our goal is to:\n" +
                        "Establish alumni traditions\n" +
                        "Promote CSU San Marcos as the university of first choice in Southern California\n" +
                        "Provide leadership and volunteerism through participation in Board of Directors and other various committees\n" +
                        "Participate in student mentoring and recruitment\n" +
                        "Legislative advocacy"
        );
        buildingMessages.put("Baseball Field", "");
        buildingMessages.put("Clarke Field House","");
        buildingMessages.put("Mangrum Track & Field","");
        buildingMessages.put("Multi-Purpose Field","");
        buildingMessages.put("Softball Field","");
        buildingMessages.put("Office of Global Education",
                        "<a href='http://www.csusm.edu/global/intstudents/index.html'>International Students Services</a>\n" +
                                "\n" +
                                "\n" +
                        "<a href='https://www.csusm.edu/global/intstudents/contact_international_advising.html'>Contact Us</a>"
        );
        buildingMessages.put("Career Center",
                        "We are here to inspire, challenge and prepare all students and alumni to navigate the path" +
                        " from college to career with clarity, competence and confidence.\n" +
                        "<a href='http://www.csusm.edu/careers/index.html'>Career Center</a>"
        );
        buildingMessages.put("CEHHS Advising",
                        "CEHHS Advising is located in university 222\n" +
                        "\n" +
                        "Academic Advising at CSUSM is a service to guide undergraduates to obtaining their degree." +
                        " It is divided by college. For information on your specific major or minor, or" +
                        " to schedule an appointment with an academic advisor, please visit:" +
                        "<a href='http://www.csusm.edu/academicadvising/'>Academic Advising</a>"
        );
        buildingMessages.put("CHABSS & CSM Advising",
                        "CHABSS & CSM Advising is located in Administrative Building 1300\n" +
                        "\n" +
                        "Academic Advising at CSUSM is a service to guide undergraduates to obtaining their degree." +
                        " It is divided by college. For information on your specific major or minor, or" +
                        " to schedule an appointment with an academic advisor, please visit:" +
                        "<a href='http://www.csusm.edu/academicadvising/'>Academic Advising</a>"
        );
        buildingMessages.put("COBA Advising",
                        "COBA Advising is located in mark 126\n" +
                        "\n" +
                        "Academic Advising at CSUSM is a service to guide undergraduates to obtaining their degree." +
                        " It is divided by college. For information on your specific major or minor, or" +
                        " to schedule an appointment with an academic advisor, please visit:" +
                        "<a href='http://www.csusm.edu/academicadvising/'>Academic Advising</a>"
        );
        buildingMessages.put("University Bookstore",
                        "Your one-stop-shop for your campus needs. Purchase textbooks, apparel, class supplies" +
                        " and technology and much more all in one convenient location. \n" +
                        "\n" +
                        "<a href='https://www.bkstr.com/csusanmarcosstore/home/'>Bookstore</a>\n" +
                        "<a href='https://www.bkstr.com/csusanmarcosstore/store-hours/'>Hours of Operation</a>"
        );
        buildingMessages.put("ASI Cougar Pantry (Commons 104)",
                        "Commons 104\n" +
                        "\n" +
                        "<a href='https://www.csusm.edu/asi/pantry/index.html/'>Cougar Pantry</a>|\n" +
                                "\n" +
                                "\n" +
                        "<a href='https://www.csusm.edu/asi/pantry/index.html/'>Associated Students, Inc</a>\n" +
                                "\n" +
                                "\n"
        );
        buildingMessages.put("Black Student Center",
                        "University Student Union 4200\n" +
                        "\n" +
                        "The mission of The Black Student Center is to promote the academic, cultural, social," +
                        " and political consciousness of students of color by actively engaging in institutional" +
                        " partnerships that will result in the overall increase of student recruitment, active member" +
                        " participation, retention, and graduation rates."
        );
        buildingMessages.put("Cross-Cultural Center",
                        "University Student Union 3400\n" +
                        "\n" +
                        "The CROSS-CULTURAL CENTER is dedicated to supporting the students of CSUSM." +
                        " We provide services and implement programs that foster an inclusive environment" +
                        " to encourage full and equal participation of all students. Our dedication to" +
                        " supporting students aligns with the university's commitment to respecting" +
                        " and modeling the diversity of our region within a context of social justice." +
                        " We seek involvement of all students and welcome suggestions, dialogue, feedback," +
                        " and opportunities for collaboration."

        );
        buildingMessages.put("Gender Equity Center",
                        "USU 3200\n" +
                        "\n" +
                        "Building on second-wave feminism’s creed of “the personal is political,” we are guided" +
                        " by principles of intersectional feminism: we recognize that while all women experience" +
                        " oppression, we also all experience oppression in varying forms based on race, age," +
                        " class, gender, sexuality, dis/ability, etc. With that in mind, we want everyone" +
                        " at the Gender Equity Center to feel supported, included, and represented. \n" +
                        "\n" +
                        "The GEC is intentionally inclusive of transgender, nonbinary, intersex, and gender non-conforming folks.\n" +
                        "\n" +
                        "HOURS\n" +
                        "Monday - Thursday 9:00 AM - 5:00 PM\n" +
                        "Friday 9:00 AM - 2:00 PM\n" +
                        "\n" +
                        "Available student resources: Desktop Computers (4), Pawprint Station (Paid Printing)," +
                        " Tables/Chairs, and Couch seats."

        );
        buildingMessages.put("LGBTQA Pride Center",
                        "USU 3100\n" +
                        "\n" +
                        "The LGBTQA Pride Center (PC) is an inclusive space which promotes positive interactions" +
                        " in a diverse environment.  The PC strives to provide a space that celebrates and" +
                        " affirms students who identify with all genders and sexualities " +
                        "in the LGBTQ community and their allies.\n" +
                        "\n" +
                        "Hours\n" +
                        "Monday & Thursday 9 AM - 8 PM\n" +
                        "Friday 9 AM - 1 PM\n" +
                        "Note: Mondays from 3 - 4 PM quiet space\n" +
                        "\n" +
                        "\n" +
                        "A quiet room is available with computers and there is a main comfortable space with outlets" +
                        " to study or take online classes."
        );
        buildingMessages.put("Latin@/x Center",
                        "USU 3300\n" +
                        "\n" +
                        "The Latin@/x Center is grounded on community building standards deriving from “Familismo”" +
                        " principals and operates from a Latinx Critical Theoretical lens. We focus on Latin@/x" +
                        " student cultural empowerment by providing affirming and validating literature, artwork," +
                        " and media within our space. We educate students and campus members about Latin@/x " +
                        "traditions, culture, and issues impacting the community. The center serves " +
                        "as a connection point to multiple internal and external networks aimed at providing" +
                        " systems of support for Latin@/x identifying students. These center standards foster" +
                        " positive transformational experiences and contribute to Latin@/x student success goals.\n" +
                        "\n" +
                        "All students are welcome to join us at the center for studying and online classes.\n" +
                        "\n" +
                        "HOURS\n" +
                        "Monday - Thursday 8:30 AM - 3:30 PM\n" +
                        "Friday 8:30 AM - 1:00 PM"

        );
        buildingMessages.put("Veterans Center",
                        "CSUSM is home to more than 500 student Veterans; the highest percentage per capita of any" +
                        " California State University campus. We pride ourselves on our high-touch holistic" +
                        " approach toward our military-connected students, and offer a wide array of services" +
                        " and resources for them."

        );
        buildingMessages.put("North Commons", "");
        buildingMessages.put("The QUAD", "");
        buildingMessages.put("University Village Apartments", "");
        buildingMessages.put("Campus Coffee",
                        "Located on the southeast side of campus on Founders Plaza, Campus Coffee has served CSUSM" +
                        " as the only standalone business on campus since 2004.  Specializing in local, sustainable " +
                        "and independent purveyors who provide the freshest, highest-quality, fair trade coffee, " +
                        "dairy, pastries, sandwiches and salads. Campus Coffee offers kosher, gluten free, vegan and organic products. " +
                        " Their commitment to the CSUSM community is evident in their efforts to support various clubs on campus" +
                        " and within the local community.\n" +
                        "\n" +
                        "<a href='https://search.csusm.edu/?t=4&q=corp%20dining%20dininghours#gsc.tab=0&gsc.q=corp%20dining%20dininghours&gsc.page=1/'>Hours of Operation</a>"

        );
        buildingMessages.put("Campus Way Cafe",
                        "Campus way Cafe is an all you care to eat community dining space where students can enjoy a nutritious" +
                        " and delicious meal with friends as part of their campus experience. Everyone will enjoy a great dining " +
                        "program that includes vegan and vegetarian options. A fresh salad station and of course a grill, pizza station," +
                        " pasta station, and to-go options!" +
                        "\n" +
                        "<a href='https://search.csusm.edu/?t=4&q=corp%20dining%20dininghours#gsc.tab=0&gsc.q=corp%20dining%20dininghours&gsc.page=1/'>Hours of Operation</a>"

        );
        buildingMessages.put("Crash's Coffee",
                        "Crash’s Cafe offers Starbucks Coffee beverages, drinks and tea. We also serve exceptional CSUSM baked goods," +
                        " gourmet salads and sandwiches. Enjoy one of our signature coffee blends, delicious coconut or chai teas," +
                        " lattes, espressos out on the patio or fireside at the University Student Union." +
                        "\n" +
                        "<a href='https://search.csusm.edu/?t=4&q=corp%20dining%20dininghours#gsc.tab=0&gsc.q=corp%20dining%20dininghours&gsc.page=1/'>Hours of Operation</a>"

        );
        buildingMessages.put("Get Fresh",
                        "Get Fresh offers a build-your-own selection of fresh and internationally inspired salad choices topped " +
                        "off with proteins. Now offering salads, rice bowls, wraps and smoothies with seasonal, local produce." +
                        " You’re going to love it at every change of the season!" +
                        "\n" +
                        "<a href='https://search.csusm.edu/?t=4&q=corp%20dining%20dininghours#gsc.tab=0&gsc.q=corp%20dining%20dininghours&gsc.page=1/'>Hours of Operation</a>"

        );
        buildingMessages.put("Hilltop Bistro Grille",
                        "Our premium grill offers a variety of choices from healthy to indulgent, familiar to intriguing. Hilltop" +
                        " Bistro Grille has many burger options, ranging from beef to chicken, turkey and vegetarian burgers, along" +
                        " with fish. The full array of sides includes traditional fries, seasoned potato wedges, slaws" +
                        " and side salads. " +
                        "\n" +
                        "<a href='https://search.csusm.edu/?t=4&q=corp%20dining%20dininghours#gsc.tab=0&gsc.q=corp%20dining%20dininghours&gsc.page=1/'>Hours of Operation</a>"

        );
        buildingMessages.put("Panda Express",
                        "Enjoy Asian-inspired dishes that are wok-tossed with fresh veggies and our gourmet sauces," +
                        " elevating fast-casual Chinese food." +
                        "\n" +
                        "<a href='https://search.csusm.edu/?t=4&q=corp%20dining%20dininghours#gsc.tab=0&gsc.q=corp%20dining%20dininghours&gsc.page=1/'>Hours of Operation</a>"

        );
        buildingMessages.put("QDOBA",
                        "Qdoba Mexican Eats offers customizable flavorful food. Plus, add queso and guacamole" +
                        " for free on any entrée! " +
                        "\n" +
                        "<a href='https://search.csusm.edu/?t=4&q=corp%20dining%20dininghours#gsc.tab=0&gsc.q=corp%20dining%20dininghours&gsc.page=1/'>Hours of Operation</a>"

        );
        buildingMessages.put("Starbucks",
                        "Starbucks roasts high-quality whole-bean coffees and sells them along with fresh, rich-brewed," +
                        " Italian-style espresso beverages, a variety of pastries and confections. Starbucks also offers" +
                        " a line of premium Teavana teas." +
                        "\n" +
                        "<a href='https://search.csusm.edu/?t=4&q=corp%20dining%20dininghours#gsc.tab=0&gsc.q=corp%20dining%20dininghours&gsc.page=1/'>Hours of Operation</a>"

        );
        buildingMessages.put("USU Market",
                        "USU Market offers all your favorite beverages, grab & go meals, sandwiches, salads," +
                        " ice cream, sushi, personal care and grocery items." +
                        "\n" +
                        "<a href='https://search.csusm.edu/?t=4&q=corp%20dining%20dininghours#gsc.tab=0&gsc.q=corp%20dining%20dininghours&gsc.page=1/'>Hours of Operation</a>"

        );
        buildingMessages.put("Open Access Computer Lab",
                "Open Access Computer Lab Locations\n" +
                        "Kellogg 2000\n" +
                        "Hours of operation:\n" +
                        "\n" +
                        "<a href='https://biblio.csusm.edu/library-hours-events/'>Open during Library open hours.</a>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Check lab and printer availability in the CSUSM mobile app."
        );
        buildingMessages.put("Kellogg Library Group Study Rooms",
                "Kellogg Library Group Study Rooms\n" +
                        "Please visit the <a href='https://biblio.csusm.edu/content/group-study-rooms-0/'> Rooms</a> " +
                        "for more information\n" +
                        "Locations\n" +
                        "Kellogg Room 2106\n" +
                        "Kellogg Room 2107\n" +
                        "Kellogg Room 2110\n" +
                        "Kellogg Room 2112\n" +
                        "Kellogg Room 2113\n" +
                        "Kellogg Room 2116\n" +
                        "Kellogg Room 4001\n" +
                        "Kellogg Room 4002\n" +
                        "Kellogg Room 4004\n" +
                        "Kellogg Room 4102\n" +
                        "Kellogg Room 4103\n" +
                        "Kellogg Room 4104\n" +
                        "Kellogg Room 4105\n" +
                        "Kellogg Room 4106\n" +
                        "Kellogg Room 4107\n" +
                        "Kellogg Room 4310\n" +
                        "Kellogg Room 4311\n" +
                        "Kellogg Room 4312\n" +
                        "Kellogg Room 4313\n" +
                        "Kellogg Room 4314\n" +
                        "Kellogg Room 4315\n" +
                        "Kellogg Room 4410\n" +
                        "Kellogg Room 4411\n" +
                        "Kellogg Room 4412\n" +
                        "Kellogg Room 4413\n" +
                        "Kellogg Room 4414\n" +
                        "Kellogg Room 5108\n" +
                        "Kellogg Room 5109\n" +
                        "Kellogg Room 5302\n" +
                        "Kellogg Room 5303\n" +
                        "Kellogg Room 5304\n" +
                        "Kellogg Room 5305\n" +
                        "Kellogg Room 5306\n" +
                        "Kellogg Room 5307\n" +
                        "Kellogg Room 5311\n" +
                        "Kellogg Room 5312\n" +
                        "Kellogg Room 5313\n" +
                        "Kellogg Room 5314\n" +
                        "Kellogg Room 5315\n" +
                        "Kellogg Room 5316\n" +
                        "Kellogg Room 5411"
        );

        buildingMessages.put("ELB 588 Global Education Center",
                "ELB 588 Global Education Center\n" +
                        "Outlets and tables/chairs available.\n" +
                        "\n" +
                        "HOURS: M-F 8:00 AM - 5:00 PM"
        );
        buildingMessages.put("Kellogg 2000",
                "Kellogg 2000\n" +
                        "Hours of operation:\n" +
                        "\n" +
                        "<a href='https://biblio.csusm.edu/library-hours-events/'>Open during Library open hours.</a>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Check lab and printer availability in the CSUSM mobile app."
        );
        buildingMessages.put("Kellogg Reading Room",
                "Kellogg Reading Room\n" +
                        "Reading Room is in the Kellogg Library \n" +
                        "Headphones are required for audio usage.\n" +
                        "Please be respectful of other students in this space."
        );
        buildingMessages.put("Forum Plaza","");
        buildingMessages.put("Founders Plaza","");
        buildingMessages.put("Gonzalez Grove","");

        buildingMessages.put("Palm Court","");
        buildingMessages.put("Parking Structure",
                "Studying in the Parking Structure must be done inside a vehicle. \n" +
                "\n" +
                "The Parking Structure has dedicated Wifi inside.");
        buildingMessages.put("Tukwut Courtyard","");
        buildingMessages.put("USU Ampitheater","");

        buildingMessages.put("Ethnobotany Garden",
                        "The garden serves as a living laboratory for students and community members who are working to understand" +
                                " botanical research activities. The garden offers students a unique opportunity to learn through hands" +
                                "-on learning and to develop a garden that features many medicinal and utilitarian plants." +
                                " Interested in getting involved, contact the Anthropology Department.");
        buildingMessages.put("Pollinator Garden",
                "The pollinator garden is overseen by the Environmental Stewards Association, student organization." +
                        "  In addition, research occurs in this garden about pollinators and the variety of species that live " +
                        "in our ecosystem.");
        buildingMessages.put("Sustainable Food Project Garden",
                "The garden allows students to understand produce justice, organic practices and growing" +
                        " practices along with theoretical classroom work.");
        buildingMessages.put("Chavez Plaza","");
        buildingMessages.put("Gonzales Grove",
                "<a href='https://www.csusm.edu/arboretum/plants/index.html?q=Gonzales%20Grove&c=6'>Webpage</a> \n" +
                "<a href='https://www.csusm.edu/arboretum/locations/gonzales-grove.pdf'>PDF</a>");
        buildingMessages.put("Kellogg Plaza", "");
        buildingMessages.put("Viasat Engineering Pavilion Plaza",
                "<a href='https://www.csusm.edu/arboretum/plants/index.html?q=Viasat%20Engineering%20Pavillion&c=6'>Webpage</a> \n" +
                "<a href='https://www.csusm.edu/arboretum/locations/viasat-engineering-pavilion.pdf'>PDF</a>");

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
        healthSafety.addSubcategory(new CategoryNode("AED"));
        healthSafety.addSubcategory(new CategoryNode("Lactation Rooms"));
        healthSafety.addSubcategory(new CategoryNode("Public Safety Building"));
        healthSafety.addSubcategory(new CategoryNode("Student Health & Counseling Services Building"));
        //Parking & Transit
        CategoryNode ParkingTransit = new CategoryNode("Parking & Transit");
        CategoryNode AltTran = new CategoryNode("Alternative Transportation");
        ParkingTransit.addSubcategory(new CategoryNode("30 Minute Parking"));
        // Add Alternative Transportation to Parking And transit
        ParkingTransit.addSubcategory(AltTran);
        AltTran.addSubcategory(new CategoryNode("Bike Lockers"));
        AltTran.addSubcategory(new CategoryNode("Carpool Parking"));
        AltTran.addSubcategory(new CategoryNode("Bike Racks"));
        AltTran.addSubcategory(new CategoryNode("Electric Vehicle Charging Station"));
        AltTran.addSubcategory(new CategoryNode("Zip Car Sharing Program"));
        //Parking/Transit Cont
        ParkingTransit.addSubcategory(new CategoryNode("Disabled Parking"));
        ParkingTransit.addSubcategory(new CategoryNode("Faculty/Staff Parking"));
        ParkingTransit.addSubcategory(new CategoryNode("General Parking"));
        ParkingTransit.addSubcategory(new CategoryNode("Streets"));
        ParkingTransit.addSubcategory(new CategoryNode("Metered Parking"));
        ParkingTransit.addSubcategory(new CategoryNode("Permit Purchase Stations"));
        ParkingTransit.addSubcategory(new CategoryNode("NCTD Bus Stops"));
        ParkingTransit.addSubcategory(new CategoryNode("Sprinter Station"));
        //Services
        CategoryNode Services = new CategoryNode("Services");
        CategoryNode CougarCentral = new CategoryNode("Cougar Central");
        CategoryNode LearningCen = new CategoryNode("Learning Centers");
        CategoryNode Athletics = new CategoryNode("Athletics");
        CategoryNode AcademicAdvising= new CategoryNode("Academic Advising");
        // Add Cougar Central to Services
        Services.addSubcategory(CougarCentral);
        CougarCentral.addSubcategory(new CategoryNode("Admissions"));
        CougarCentral.addSubcategory(new CategoryNode("Cashier's Desk"));
        CougarCentral.addSubcategory(new CategoryNode("Financial Aid"));
        CougarCentral.addSubcategory(new CategoryNode("Office of the Registrar"));
        CougarCentral.addSubcategory(new CategoryNode("Student Financial Services"));
        // IT
        Services.addSubcategory(new CategoryNode("IT Help Desk"));
        // Learning Centers
        Services.addSubcategory(LearningCen);
        LearningCen.addSubcategory(new CategoryNode("Academic Success Center"));
        LearningCen.addSubcategory(new CategoryNode("Personalized Academic Success Services (PASS)"));
        LearningCen.addSubcategory(new CategoryNode("STEM Success Center"));
        LearningCen.addSubcategory(new CategoryNode("Student Outreach and Referral (SOAR) and Cougar Care Network (CCN)"));
        LearningCen.addSubcategory(new CategoryNode("Writing Center"));
        //Alumni Association
        Services.addSubcategory(new CategoryNode("Alumni Association"));
        // Athletics
        Services.addSubcategory(Athletics);
        Athletics.addSubcategory(new CategoryNode("Baseball Field"));
        Athletics.addSubcategory(new CategoryNode("Clarke Field House"));
        Athletics.addSubcategory(new CategoryNode("Mangrum Track & Field"));
        Athletics.addSubcategory(new CategoryNode("Multi-Purpose Field"));
        Athletics.addSubcategory(new CategoryNode("Softball Field"));
        Athletics.addSubcategory(new CategoryNode("Sports Center"));
        // Office of global education
        Services.addSubcategory(new CategoryNode("Office of Global Education"));
        // Career Center
        Services.addSubcategory(new CategoryNode("Career Center"));
        // Academic Advising
        Services.addSubcategory(AcademicAdvising);
        AcademicAdvising.addSubcategory(new CategoryNode("CEHHS Advising"));
        AcademicAdvising.addSubcategory(new CategoryNode("CHABSS & CSM Advising"));
        AcademicAdvising.addSubcategory(new CategoryNode("COBA Advising"));
        // Bookstore
        Services.addSubcategory(new CategoryNode("University Bookstore"));
        //Student Centers
        CategoryNode StuCen = new CategoryNode("Student Centers");
        StuCen.addSubcategory(new CategoryNode("ASI Cougar Pantry (Commons 104)"));
        StuCen.addSubcategory(new CategoryNode("Black Student Center"));
        StuCen.addSubcategory(new CategoryNode("Cross-Cultural Center"));
        StuCen.addSubcategory(new CategoryNode("Gender Equity Center"));
        StuCen.addSubcategory(new CategoryNode("Latin@/x Center"));
        StuCen.addSubcategory(new CategoryNode("LGBTQA Pride Center"));
        StuCen.addSubcategory(new CategoryNode("Veterans Center"));
        // Student housing
        CategoryNode hou = new CategoryNode("Student Housing");
        hou.addSubcategory(new CategoryNode("North Commons"));
        hou.addSubcategory(new CategoryNode("The QUAD"));
        hou.addSubcategory(new CategoryNode("University Village Apartments"));
        // Dining and Cafés
        CategoryNode food = new CategoryNode("Dining & Cafes");
        food.addSubcategory(new CategoryNode("Campus Coffee"));
        food.addSubcategory(new CategoryNode("Campus Way Cafe"));
        food.addSubcategory(new CategoryNode("Crash's Coffee"));
        food.addSubcategory(new CategoryNode("Get Fresh"));
        food.addSubcategory(new CategoryNode("Hilltop Bistro Grille"));
        food.addSubcategory(new CategoryNode("Panda Express"));
        food.addSubcategory(new CategoryNode("QDOBA"));
        food.addSubcategory(new CategoryNode("Starbucks"));
        food.addSubcategory(new CategoryNode("USU Market"));
        //Study And Online Class Spaces
        CategoryNode Stu = new CategoryNode("Study And Online Class Spaces");
        CategoryNode Open = new CategoryNode("Open Access Computer Lab");
        CategoryNode in = new CategoryNode("Indoor Study Spaces");
        CategoryNode out = new CategoryNode("Outdoor Study Spaces");
        //add open to Stu
        Open.addSubcategory(new CategoryNode("Kellogg 2000"));
        Stu.addSubcategory(Open);
        // Kellog Rooms
        Stu.addSubcategory(new CategoryNode("Kellogg Library Group Study Rooms"));
        //indoor Study Spaces
        in.addSubcategory(new CategoryNode("Crash's Coffee"));
        in.addSubcategory(new CategoryNode("ELB 588 Global Education Center"));
        in.addSubcategory(new CategoryNode("Gender Equity Center"));
        in.addSubcategory(new CategoryNode("Kellogg 2000"));
        in.addSubcategory(new CategoryNode("Latin@/x Center"));
        in.addSubcategory(new CategoryNode("LGBTQA Pride Center"));
        Stu.addSubcategory(in);
        //Outdoor Study Spaces
        out.addSubcategory(new CategoryNode("Forum Plaza"));
        out.addSubcategory(new CategoryNode("Founders Plaza"));
        out.addSubcategory(new CategoryNode("Gonzalez Grove"));
        out.addSubcategory(new CategoryNode("Palm Court"));
        out.addSubcategory(new CategoryNode("Parking Structure"));
        out.addSubcategory(new CategoryNode("Tukwut Courtyard"));
        out.addSubcategory(new CategoryNode("USU Ampitheater"));
        Stu.addSubcategory(out);
        //ElB Again
        Stu.addSubcategory(new CategoryNode("ELB 588 Global Education Center"));
        //Plazas And Gardens
        CategoryNode PlaGar = new CategoryNode("Plazas & Gardens");
        CategoryNode Gar = new CategoryNode("Gardens");
        CategoryNode Pla = new CategoryNode("Plazas");
        //Gardens
        Gar.addSubcategory(new CategoryNode("Ethnobotany Garden"));
        Gar.addSubcategory(new CategoryNode("Pollinator Garden"));
        Gar.addSubcategory(new CategoryNode("Sustainable Food Project Garden"));
        PlaGar.addSubcategory(Gar);
        //Plazas
        Pla.addSubcategory(new CategoryNode("Chavez Plaza"));
        Pla.addSubcategory(new CategoryNode("Forum Plaza"));
        Pla.addSubcategory(new CategoryNode("Founders Plaza"));
        Pla.addSubcategory(new CategoryNode("Gonzales Grove"));
        Pla.addSubcategory(new CategoryNode("Gonzalez Grove"));
        Pla.addSubcategory(new CategoryNode("Kellogg Plaza"));
        Pla.addSubcategory(new CategoryNode("Palm Court"));
        Pla.addSubcategory(new CategoryNode("Tukwut Courtyard"));
        Pla.addSubcategory(new CategoryNode("USU Ampitheater"));
        Pla.addSubcategory(new CategoryNode("Viasat Engineering Pavilion Plaza"));
        PlaGar.addSubcategory(Pla);
        // Add main categories to root
        root.addSubcategory(buildings);
        root.addSubcategory(healthSafety);
        root.addSubcategory(ParkingTransit);
        root.addSubcategory(Services);
        root.addSubcategory(StuCen);
        root.addSubcategory(hou);
        root.addSubcategory(food);
        root.addSubcategory(Stu);
        root.addSubcategory(PlaGar);

        return root;
    }
    public void renderUI() {
        ImGui.pushStyleColor(ImGuiCol.WindowBg, ImGui.getColorU32(1.0f, 1.0f, 1.0f, 1.0f)); // White background

        // Render the search bar
        renderSearchBar();

        if (!searchQuery.isEmpty()) {
            // If there's a search query, display filtered results
            renderSearchResults();
        } else if (currentNode.subcategories.isEmpty()) {
            // If no search and no subcategories, show the description (leaf node)
            renderDescription(currentNode);
        } else {
            // Render the subcategories
            renderSubcategories(currentNode);
        }

        ImGui.popStyleColor(); // Restore the original background color
    }
    private void renderSearchBar() {
        ImGui.text("Search:");
        ImGui.sameLine();

        // Input text widget for the search bar
        ImGui.inputText("##SearchBar", searchQuery, ImGuiInputTextFlags.None);

        // Provide spacing after the search bar
        ImGui.spacing();
    }
    private void renderSearchResults() {
        String query = searchQuery.get().toLowerCase(); // Get the current search query
        List<CategoryNode> matchingNodes = searchCategories(root, query);

        if (matchingNodes.isEmpty()) {
            ImGui.text("No results found for \"" + query + "\".");
        } else {
            ImGui.text("Search results:");
            for (CategoryNode node : matchingNodes) {
                String name = node.name;
                Integer iconTextureId = icons.get(name); // Fetch the icon for the category

                // Adjust padding and item spacing to ensure enough room
                ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 8.0f, 4.0f); // More padding for both icon and text
                ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 8.0f, 4.0f); // Adjust spacing

                if (ImGui.beginTable("search_results_table", 2, ImGuiTableFlags.SizingFixedFit)) {
                    // Set up the columns and their widths
                    ImGui.tableSetupColumn("Icon", ImGuiTableColumnFlags.WidthFixed, 30.0f); // Set width for icon column
                    ImGui.tableSetupColumn("Text", ImGuiTableColumnFlags.WidthFixed, 500.0f); // Set width for text column

                    // Render the icon if available
                    ImGui.tableNextColumn();
                    if (iconTextureId != null) {
                        float iconSize = 24.0f; // Icon size, adjust as needed
                        ImGui.image(iconTextureId, iconSize, iconSize);
                    } else {
                        ImGui.text(""); // Leave the icon cell empty if no icon
                    }

                    // Render the button for the category name
                    ImGui.tableNextColumn();
                    if (ImGui.button(name)) {
                        currentNode = node; // Navigate to the selected category
                        searchQuery.set(""); // Clear the search query after navigation
                    }

                    ImGui.endTable();
                }

                ImGui.popStyleVar(2); // Pop the style vars to restore original values
            }
        }
    }
    // Recursive search method
    private List<CategoryNode> searchCategories(CategoryNode node, String query) {
        List<CategoryNode> results = new ArrayList<>();
        if (node.name.toLowerCase().contains(query)) {
            results.add(node);
        }
        for (CategoryNode subcategory : node.subcategories) {
            results.addAll(searchCategories(subcategory, query));
        }
        return results;
    }
    // Render subcategories as buttons
    private void renderSubcategories(CategoryNode node) {
        ImGui.text("Subcategories for " + node.name + ":");

        for (CategoryNode subcategory : node.subcategories) {
            String name = subcategory.name;

            // Check if there's an icon for this category
            Integer iconTextureId = icons.get(name);

            if (iconTextureId != null) {
                // Adjust padding and item spacing to ensure enough room
                ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 8.0f, 4.0f); // More padding for both icon and text
                ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 8.0f, 4.0f); // Adjust spacing

                if (ImGui.beginTable("button_table", 2, ImGuiTableFlags.SizingFixedFit)) {
                    // Set up the columns and their widths
                    ImGui.tableSetupColumn("Icon", ImGuiTableColumnFlags.WidthFixed, 30.0f); // Set width for icon column
                    ImGui.tableSetupColumn("Text", ImGuiTableColumnFlags.WidthFixed, 500.0f); // Set width for text column

                    // Render the small icon with specified size
                    ImGui.tableNextColumn();
                    float iconSize = 24.0f; // Icon size, adjust as needed
                    ImGui.image(iconTextureId, iconSize, iconSize);

                    ImGui.tableNextColumn();

                    // Render the text button next to the icon
                    if (ImGui.button(name)) {
                        currentNode = subcategory; // Navigate to the clicked subcategory
                    }

                    ImGui.endTable();
                }

                ImGui.popStyleVar(2); // Pop the style vars to restore original values
            } else {
                // Render only a text button if no icon is available
                if (ImGui.button(name)) {
                    currentNode = subcategory; // Navigate to the clicked subcategory
                }
            }
        }

        // Back button for navigation
        if (currentNode != root && ImGui.button("Back")) {
            currentNode = findParentNode(root, currentNode);
        }
    }


    // Render the description for leaf nodes
    private void renderDescription(CategoryNode node) {
        String description = buildingMessages.get(node.name); // Fetch description from the HashMap
        Object imageData = Images.get(node.name);  // Fetch the image data for the category

        // Ensure the image index map is initialized for this node
        imageIndices.putIfAbsent(node.name, 0);

        if (description != null) {
            String buttonName = node.name;
            ImGui.textWrapped("Description for " + buttonName);

            if (imageData != null) {
                // Check if the image data is a single image or a list of images
                if (imageData instanceof Integer) {
                    // Render the single image
                    int imageTexture = (Integer) imageData;
                    float imageWidth = 370;
                    float imageHeight = 300;
                    ImGui.image(imageTexture, imageWidth, imageHeight);
                } else if (imageData instanceof List<?>) {
                    // Handle multiple images (List of Integer)
                    List<Integer> imageTextures = (List<Integer>) imageData;
                    int currentImageIndex = imageIndices.get(node.name);

                    // Render the current image from the list
                    int currentImageTexture = imageTextures.get(currentImageIndex);
                    float imageWidth = 350;
                    float imageHeight = 300;
                    ImGui.image(currentImageTexture, imageWidth, imageHeight);

                    // Navigation buttons for flipping through images
                    ImGui.spacing(); // Add space before the buttons

                    // Back button
                    if (ImGui.button("<")) {
                        int newIndex = (currentImageIndex - 1 + imageTextures.size()) % imageTextures.size();
                        imageIndices.put(node.name, newIndex);

                    }
                    ImGui.sameLine();

                    // Next button
                    ImGui.setCursorPosX(ImGui.getCursorPosX() + imageWidth - 50); // Adjust right margin for button position
                    if (ImGui.button(">")) {
                        int newIndex = (currentImageIndex + 1) % imageTextures.size();
                        imageIndices.put(node.name, newIndex);
                    }

                    // Display the current image index
                    ImGui.text("Image " + (currentImageIndex + 1) + " of " + imageTextures.size());
                }
            } else {
                ImGui.text("");
            }

            // Render the description text
            renderFormattedDescription(description);
        }

        // Add a "Back" button to navigate back to the parent
        if (ImGui.button("Back")) {
            currentNode = findParentNode(root, currentNode); // Navigate back
        }
    }

    // Helper to render formatted description with links
    private void renderFormattedDescription(String description) {
        // Regular expression to match <a href='URL'>Link Text</a>
        Pattern linkPattern = Pattern.compile("<a href='(.*?)'>(.*?)</a>");
        String[] lines = description.split("\n");

        for (String line : lines) {
            Matcher matcher = linkPattern.matcher(line);
            boolean foundLink = false;

            while (matcher.find()) {
                foundLink = true;
                String url = matcher.group(1); // URL
                String linkText = matcher.group(2); // Link text

                // Render link as a button
                if (ImGui.button(linkText)) {
                    openWebpage(url); // Open the URL when clicked
                }
                ImGui.sameLine(); // Keep links in the same line if desired
            }

            // Render non-link parts of the line
            if (!foundLink) {
                ImGui.textWrapped(line);
            }
        }
    }
    // Method to open a webpage
    private void openWebpage(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace(); // Log errors if any
            }
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

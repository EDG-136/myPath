package com.tecksupport.GUI;

import com.tecksupport.glfw.ui.BuildingMessageInitializer;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.net.URI;



public class mainSidebar extends JFrame {
    private JPanel mainPanel;

    // Building messages initialized via BuildingMessageInitializer
    private final BuildingMessageInitializer buildingMessageInitializer;

    public mainSidebar() {
        // Initialize the BuildingMessageInitializer
        buildingMessageInitializer = new BuildingMessageInitializer();
        String imagePath = "C:\\Users\\curth\\IdeaProjects\\test gui\\Images\\North Coms 7.png";  // Example path, adjust as needed
        ImageIcon imageIcon = new ImageIcon(imagePath);

        JFrame newFrame = new JFrame("Main Page");
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(700, 500);
        newFrame.setLayout(new BorderLayout());

        JPanel mainButtonsPanel = new JPanel();
        mainButtonsPanel.setLayout(new BoxLayout(mainButtonsPanel, BoxLayout.Y_AXIS));

        // Main buttons
        String[] categories = {
                "Buildings", "Health & Safety", "Parking & Transit",
                "Services", "Student Centers", "Student Housing",
                "Dining & Cafes", "Study and Online Class Spaces", "Plazas & Gardens", "MyPath"
        };

        for (String category : categories) {
            JButton button = createCategoryButton(category);
            mainButtonsPanel.add(button);
        }

        mainButtonsPanel.setVisible(false);
        JButton menuButton = new JButton("☰");
        setupMenuButton(menuButton, newFrame, mainButtonsPanel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(menuButton, BorderLayout.WEST);
        newFrame.add(topPanel, BorderLayout.NORTH);
        newFrame.add(mainButtonsPanel, BorderLayout.CENTER);

        // Sidebar panel for sub-categories
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setVisible(false);
        newFrame.add(sidebar, BorderLayout.WEST);

        newFrame.setVisible(true);
    }

    private JButton createCategoryButton(String category) {
        JButton button = new JButton(category);
        button.addActionListener(e -> openFrame(createPanelForCategory(category), category));
        return button;
    }

    private JPanel createPanelForCategory(String category) {
        switch (category) {
            case "Buildings":
                return createBuildingPanel();
            case "Health & Safety":
                return createHealthSafetyPanel();
            case "Parking & Transit":
                return createParkingTransitPanel();
            case "Services":
                return createServicesPanel();
            case "Student Centers":
                return createStudentCentersPanel();
            case "Student Housing":
                return createStudentHousingPanel();
            case "Dining & Cafes":
                return createDiningCafesPanel();
            case "Study and Online Class Spaces":
                return createStudyPanel();
            case "Plazas & Gardens":
                return createPlaPanel();
            case "MyPath":
                return createMyPathPanel();
            default:
                return new JPanel(); // Return empty panel for unhandled categories
        }
    }

    private JPanel createBuildingPanel() {
        JPanel buildings = new JPanel();
        JButton academicHallsButton = createSubCategoryButton("Academic Halls", "Academic Halls");
        JButton serviceBuildingsButton = createSubCategoryButton("Service Buildings", "Service Buildings");

        buildings.add(academicHallsButton);
        buildings.add(serviceBuildingsButton);
        return buildings;
    }



    private JPanel createHealthSafetyPanel() {
        JPanel healthPanel = new JPanel();

        // Create buttons dynamically based on the HashMap
        String[] healthAndSafetyBuildings = {
                "Emergency Phones",
                "AED",
                "Lactation Rooms",
                "Public Safety Building",
                "Student Health & Counseling Services Building"
        };

        for (String healthName : healthAndSafetyBuildings) {
            JButton healthButton = new JButton(healthName);

            // Add action listener to show info panel when clicked
            /*healthButton.setHorizontalAlignment(SwingConstants.LEFT);
            healthButton.setBackground(Color.WHITE);
            healthButton.setForeground(Color.BLACK);
            healthButton.setFont(new Font("Arial", Font.PLAIN, 16));
            healthButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));*/
            healthButton.addActionListener(e -> showBuildingInfo(healthName, createBuildingInfoPanel(healthName)));

            healthPanel.add(healthButton);
        }

        return healthPanel;
    }

    private JPanel createMyPathPanel() {
        JPanel myPathPanel = new JPanel();
        myPathPanel.setLayout(new BoxLayout(myPathPanel, BoxLayout.Y_AXIS));

        // Button for adding classes to the schedule
        JButton addClassesButton = new JButton("Add Classes to Schedule");
        addClassesButton.addActionListener(e -> addClassesToSchedule());
        myPathPanel.add(addClassesButton);

        // Button for enabling/disabling wheelchair accessibility
        JToggleButton wheelchairToggle = new JToggleButton("Wheelchair Accessibility: OFF");
        wheelchairToggle.addActionListener(e -> toggleWheelchairAccessibility(wheelchairToggle));
        myPathPanel.add(wheelchairToggle);

        // Button to show the calculated path
        JButton showPathButton = new JButton("Show Path");
        showPathButton.addActionListener(e -> showPath());
        myPathPanel.add(showPathButton);

        return myPathPanel;
    }

    private JPanel createParkingTransitPanel() {
        JPanel Parking = new JPanel();
        JButton AltTraButton = createSubCategoryButton("Alternative Transportation", "Alternative Transportation");
        Parking.add(AltTraButton);
        String[] ParkingTransit = {
                "30 Minute Parking",
                "Disabled Parking",
                "Faculty/Staff Parking",
                "General Parking",
                "Streets",
                "Metered Parking",
                "Permit Purchase Stations",
                "NCTD Bus Stops",
                "Sprinter Station"
        };

        for (String ParkingName : ParkingTransit) {
            JButton parButton = new JButton(ParkingName);

            /*parButton.setHorizontalAlignment(SwingConstants.LEFT);
            parButton.setBackground(Color.WHITE);
            parButton.setForeground(Color.BLACK);
            parButton.setFont(new Font("Arial", Font.PLAIN, 16));
            parButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));*/
            parButton.addActionListener(e -> showBuildingInfo(ParkingName, createBuildingInfoPanel(ParkingName)));

            Parking.add(parButton);
        }

        return Parking;
    }
    private JPanel createServicesPanel(){
        JPanel Services = new JPanel();
        JButton CCButton = createSubCategoryButton("Cougar Central", "Cougar Central");
        JButton LearnButton = createSubCategoryButton("Learning Centers", "Learning Centers");
        JButton AcademicAdvisingButton = createSubCategoryButton("Academic Advising", "Academic Advising");
        Services.add(CCButton);
        Services.add(LearnButton);
        Services.add(AcademicAdvisingButton);
        String[] ParkingTransit = {
                "IT Help Desk",
                "Alumni Association",
                "Athletics",
                "Office of Global Education",
                "Career Center",
                "University Bookstore"
        };

        for (String ServiceName : ParkingTransit) {
            JButton SerButton = new JButton(ServiceName);

            /*SerButton.setHorizontalAlignment(SwingConstants.LEFT);
            SerButton.setBackground(Color.WHITE);
            SerButton.setForeground(Color.BLACK);
            SerButton.setFont(new Font("Arial", Font.PLAIN, 16));
            SerButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));*/
            // Add action listener to show info panel when clicked
            SerButton.addActionListener(e -> showBuildingInfo(ServiceName, createBuildingInfoPanel(ServiceName)));

            Services.add(SerButton);
        }

        return Services;
    }
    private JPanel createStudentCentersPanel() {
        JPanel Centers = new JPanel();
        String[] Center = {
                "ASI Cougar Pantry (Commons 104)",
                "Black Student Center",
                "Cross-Cultural Center",
                "Gender Equity Center",
                "LGBTQA Pride Center",
                "Latin@/x Center",
                "Veterans Center"
        };

        for (String CenterName : Center) {
            JButton CenButton = new JButton(CenterName);

            /*CenButton.setHorizontalAlignment(SwingConstants.LEFT);
            CenButton.setBackground(Color.WHITE);
            CenButton.setForeground(Color.BLACK);
            CenButton.setFont(new Font("Arial", Font.PLAIN, 16));
            CenButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));*/
            // Add action listener to show info panel when clicked
            CenButton.addActionListener(e -> showBuildingInfo(CenterName, createBuildingInfoPanel(CenterName)));

            Centers.add(CenButton);
        }

        return Centers;
    }
    private JPanel createStudentHousingPanel() {
        JPanel Housing = new JPanel();
        String[] House = {
                "North Commons",
                "The QUAD",
                "University Village Apartments"
        };

        for (String HouseName : House) {
            JButton HouButton = new JButton(HouseName);
            /*HouButton.setHorizontalAlignment(SwingConstants.LEFT);
            HouButton.setBackground(Color.WHITE);
            HouButton.setForeground(Color.BLACK);
            HouButton.setFont(new Font("Arial", Font.PLAIN, 16));
            HouButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));*/
            // Add action listener to show info panel when clicked
            HouButton.addActionListener(e -> showBuildingInfo(HouseName, createBuildingInfoPanel(HouseName)));

            Housing.add(HouButton);
        }

        return Housing;
    }
    private JPanel createDiningCafesPanel() {
        JPanel Food = new JPanel();
        String[] Cafes = {
                "Campus Coffee",
                "Campus Way Cafe",
                "Crash's Coffee",
                "Get Fresh",
                "Hilltop Bistro Grille",
                "Panda Express",
                "QDOBA",
                "Starbucks",
                "USU Market"
        };

        for (String CafeName : Cafes) {
            JButton FoodButton = new JButton(CafeName);

            /*FoodButton.setHorizontalAlignment(SwingConstants.LEFT);
            FoodButton.setBackground(Color.WHITE);
            FoodButton.setForeground(Color.BLACK);
            FoodButton.setFont(new Font("Arial", Font.PLAIN, 16));
            FoodButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));*/
            // Add action listener to show info panel when clicked
            FoodButton.addActionListener(e -> showBuildingInfo(CafeName, createBuildingInfoPanel(CafeName)));

            Food.add(FoodButton);
        }

        return Food;
    }
    private JPanel createStudyPanel() {
        JPanel Study = new JPanel();
        JButton InButton = createSubCategoryButton("Indoor Study Spaces", "Indoor Study Spaces");
        Study.add(InButton);
        String[] StudyLoc = {
                "Open Access Computer Lab",
                "Kellogg Library Group Study Rooms",
                "ELB 588 Global Education Center",
                "Outdoor Study Spaces"
        };

        for (String StuName : StudyLoc) {
            JButton StuButton = new JButton(StuName);

            /*StuButton.setHorizontalAlignment(SwingConstants.LEFT);
            StuButton.setBackground(Color.WHITE);
            StuButton.setForeground(Color.BLACK);
            StuButton.setFont(new Font("Arial", Font.PLAIN, 16));
            StuButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));*/
            // Add action listener to show info panel when clicked
            StuButton.addActionListener(e -> showBuildingInfo(StuName, createBuildingInfoPanel(StuName)));

            Study.add(StuButton);
        }

        return Study;
    }
    private JPanel createPlaPanel() {
        JPanel Pla = new JPanel();
        String[] PlaNGar = {
                "Gardens",
                "Plazas",
        };

        for (String PlaName : PlaNGar) {
            JButton PlaButton = new JButton(PlaName);

            /*PlaButton.setHorizontalAlignment(SwingConstants.LEFT);
            PlaButton.setBackground(Color.WHITE);
            PlaButton.setForeground(Color.BLACK);
            PlaButton.setFont(new Font("Arial", Font.PLAIN, 16));
            PlaButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));*/
            // Add action listener to show info panel when clicked
            PlaButton.addActionListener(e -> showBuildingInfo(PlaName, createBuildingInfoPanel(PlaName)));

            Pla.add(PlaButton);
        }

        return Pla;
    }
    private JButton createSubCategoryButton(String buttonText, String type) {
        JButton button = new JButton(buttonText);
        /*button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));*/
        button.addActionListener(e -> openFrame(createBuildingDetailPanel(type), buttonText));
        return button;
    }

    private JPanel createBuildingDetailPanel(String type) {
        JPanel panel = new JPanel();
        JButton[] buttons;

        if ("Academic Halls".equals(type)) {
            String[] halls = {
                    "Academic Hall", "Arts Building", "Extended Learning Building",
                    "Markstein Hall", "Science Hall 1", "Science Hall 2",
                    "Social & Behavioral Sciences Building", "University Commons",
                    "University Hall", "Viasat Engineering Pavilion"
            };
            buttons = new JButton[halls.length];
            for (int i = 0; i < halls.length; i++) {
                buttons[i] = createHallButton(halls[i]);
                panel.add(buttons[i]);
            }
        } else if ("Service Buildings".equals(type)) {
            String[] serviceBuildings = {
                    "Administrative Building", "Center for Children & Families",
                    "Epstein Family Veterans Center", "Kellogg Library",
                    "M. Gordon Clarke Fieldhouse", "McMahan House",
                    "Public Safety Building", "Sports Center",
                    "Student Health & Counseling Services Building", "University Student Union"
            };
            buttons = new JButton[serviceBuildings.length];
            for (int i = 0; i < serviceBuildings.length; i++) {
                buttons[i] = createServiceBuildingButton(serviceBuildings[i]);
                panel.add(buttons[i]);
            }
        } else if ("Alternative Transportation".equals(type)) {
            String[] AlternativeTransportation = {
                    "Bike Lockers", "Carpool Parking",
                    "Bike Racks", "Electric Vehicle Charging Station",
                    "Zip Car Sharing Program"
            };
            buttons = new JButton[AlternativeTransportation.length];
            for (int i = 0; i < AlternativeTransportation.length; i++) {
                buttons[i] = createAlternativeTransportationButton(AlternativeTransportation[i]);
                panel.add(buttons[i]);
            }
        } else if ("Cougar Central".equals(type)) {
            String[] CougarCentral = {
                    "Admissions", "Cashier's Desk",
                    "Financial Aid", "Office of the Registrar",
                    "Student Financial Services"
            };
            buttons = new JButton[CougarCentral.length];
            for (int i = 0; i < CougarCentral.length; i++) {
                buttons[i] = createCCButton(CougarCentral[i]);
                panel.add(buttons[i]);
            }
        } else if ("Learning Centers".equals(type)) {
            String[] Learn = {
                    "Academic Success Center", "Personalized Academic Success Services (PASS)",
                    "STEM Success Center", "Student Outreach and Referral (SOAR) and Cougar Care Network (CCN)",
                    "Writing Center"
            };
            buttons = new JButton[Learn.length];
            for (int i = 0; i < Learn.length; i++) {
                buttons[i] = createCCButton(Learn[i]);
                panel.add(buttons[i]);
            }
        } else if ("Academic Advising".equals(type)) {
            String[] AA = {
                    "CEHHS Advising", "CHABSS & CSM Advising",
                    "COBA Advising"
            };
            buttons = new JButton[AA.length];
            for (int i = 0; i < AA.length; i++) {
                buttons[i] = createAAButton(AA[i]);
                panel.add(buttons[i]);
            }
        } else if ("Indoor Study Spaces".equals(type)) {
            String[] In = {
                    "ACD 201 Classroom", "ACD 301 Classroom",
                    "Crash's Coffee", "ELB 588 Global Education Center",
                    "Extended Learning Building 250", "Gender Equity Center",
                    "Kellogg 2000","Kellogg Reading Room","LGBTQA Pride Center",
                    "Latin@/x Center","MARK 107 Tiered Classroom","MARK 201 Classroom",
                    "MARK 309 PC Lab","SBSB Lobby","UNIV 273 MAC Lab","USU Commuter Lounge",
                    "USU, 4th Floor Cafe Pavilion","USU, 4th Floor Dining","USU, 4th Floor Dining Pavilion"

            };
            buttons = new JButton[In.length];
            for (int i = 0; i < In.length; i++) {
                buttons[i] = createInButton(In[i]);
                panel.add(buttons[i]);
            }
        }

        return panel;
    }


    private void addClassesToSchedule() {
        String className = JOptionPane.showInputDialog(this,
                "Enter the class name or code:",
                "Add Class to Schedule",
                JOptionPane.PLAIN_MESSAGE);

        if (className != null && !className.trim().isEmpty()) {
            // Logic to add the class to the schedule (placeholder)
            JOptionPane.showMessageDialog(this,
                    "Class \"" + className + "\" has been added to your schedule.",
                    "Class Added",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No class was added.",
                    "Add Class",
                    JOptionPane.WARNING_MESSAGE);
        }
    }


    private void toggleWheelchairAccessibility(JToggleButton toggleButton) {
        if (toggleButton.isSelected()) {
            toggleButton.setText("Wheelchair Accessibility: ON");
            // Logic to enable wheelchair-friendly paths (placeholder)
            JOptionPane.showMessageDialog(this,
                    "Wheelchair accessibility has been enabled.",
                    "Accessibility Enabled",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            toggleButton.setText("Wheelchair Accessibility: OFF");
            // Logic to disable wheelchair-friendly paths (placeholder)
            JOptionPane.showMessageDialog(this,
                    "Wheelchair accessibility has been disabled.",
                    "Accessibility Disabled",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showPath() {
        // Placeholder logic for showing the path
        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new BorderLayout());

        JLabel pathLabel = new JLabel("<html><body style='width: 300px;'>"
                + "Showing the path based on your schedule and preferences (e.g., wheelchair accessibility). "
                + "Map visualization can be implemented here."
                + "</body></html>");
        pathPanel.add(pathLabel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, pathPanel, "Show Path", JOptionPane.PLAIN_MESSAGE);
    }




    private JButton createHallButton(String hallName) {
        return createBuildingButton(hallName);
    }

    private JButton createServiceBuildingButton(String buildingName) {
        return createBuildingButton(buildingName);
    }
    private JButton createAlternativeTransportationButton(String AltTrans) {
        return createBuildingButton(AltTrans);
    }
    private JButton createCCButton(String CC) {
        return createBuildingButton(CC);
    }
    private JButton createAAButton(String AA) {
        return createBuildingButton(AA);
    }
    private JButton createInButton(String In) {
        return createBuildingButton(In);
    }
    private JButton createBuildingButton(String buildingName) {
        JButton buildingButton = new JButton(buildingName);
        /*buildingButton.setHorizontalAlignment(SwingConstants.LEFT);
        buildingButton.setBackground(Color.WHITE);
        buildingButton.setForeground(Color.BLACK);
        buildingButton.setFont(new Font("Arial", Font.PLAIN, 16));
        buildingButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));*/
        buildingButton.addActionListener(e -> showBuildingInfo(buildingName, createBuildingInfoPanel(buildingName)));
        return buildingButton;
    }

    private JPanel createBuildingInfoPanel(String buildingName) {
        String message = buildingMessageInitializer.getBuildingMessages().getOrDefault(buildingName, buildingName + "\nNo description available.");

        message = "<html><body>" + message.replace("\n", "<br>") + "</body></html>"; // Ensure HTML tags wrap entire message
        // Create a JTextPane for better text handling
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html"); // Set content type to HTML
        textPane.setText(message); // Set the text message
        textPane.setEditable(false); // Make it read-only
        textPane.setCaretPosition(0); // Scroll to the top


        // Add a HyperlinkListener to handle link clicks
        textPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        // Open the clicked link in the default web browser
                        Desktop.getDesktop().browse(new URI(e.getURL().toString()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        // Wrap the JTextPane in a JScrollPane for scrollability
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(350, 200)); // Set a preferred size

        JPanel panel = new JPanel(new BorderLayout()); // Use BorderLayout to add the scroll pane
        panel.add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the panel

        return panel;
    }

    private void showBuildingInfo(String title, JPanel panel) {
        // Show the panel in a dialog
        JOptionPane.showMessageDialog(null, panel, title, JOptionPane.PLAIN_MESSAGE);
    }
    private void setupMenuButton(JButton menuButton, JFrame newFrame, JPanel mainButtonsPanel) {
        menuButton.setBorderPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setFocusPainted(false);
        menuButton.setForeground(Color.BLACK);

        menuButton.addActionListener(e -> {
            boolean sidebarVisible = mainButtonsPanel.isVisible();
            mainButtonsPanel.setVisible(!sidebarVisible);
            newFrame.revalidate();
            newFrame.repaint();
        });
    }

    private void setupBackButton(JButton backButton, JPanel mainButtonsPanel, JPanel sidebar, JFrame frame) {
        backButton.addActionListener(e -> {
            mainButtonsPanel.setVisible(true);
            sidebar.setVisible(false);
        });
    }


    private void openFrame(JPanel panel, String title) {
        System.out.println("Opening panel for: " + title);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(mainSidebar::new);
    }
}
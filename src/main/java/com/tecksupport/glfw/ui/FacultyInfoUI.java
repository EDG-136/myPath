package com.tecksupport.glfw.ui;

import com.tecksupport.database.FacultyQuery;
import com.tecksupport.schedulePlanner.Faculty;
import com.tecksupport.glfw.view.Window;
import imgui.*;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;

import java.util.List;

public class FacultyInfoUI {
    private static final String TITLE = "Faculties";
    private static final ImVec4 LIGHT_GRAY = new ImVec4(0.8f, 0.8f, 0.8f, 1);
    private final ImGuiTextFilter filter = new ImGuiTextFilter();
    private final Window window;
    private final List<Faculty> faculties;
    private String selectedFaculty;
    private float width;
    private float height;

    public FacultyInfoUI(Window window, FacultyQuery facultyQuery) {
        this.window = window;
        faculties = facultyQuery.getListOfAllFaculties();

        // Calculate window size
        width = window.getScreenWidth() / 3f;
        height = window.getScreenHeight() / 2.5f;
    }

    public int getWindowFlags() {
        return ImGuiWindowFlags.NoCollapse;
    }

    public int getTreeNodeFlags() {
        return ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick;
    }

    public void render() {
        // Don't open the window if no faculties were found
        if (faculties == null)
            return;

        // Set window size and location
        ImGui.setNextWindowSize(width, height, ImGuiCond.Once);
        ImGui.setNextWindowPos(15, 30, ImGuiCond.Once);

        // Open window
        if (!ImGui.begin(TITLE, getWindowFlags()))
        {
            // End early if there is an error
            ImGui.end();
            return;
        }

        // Get font
        ImFont font = ImGui.getFont();
        float defaultFontScale = font.getScale();
        font.setScale(getFacultyNameScale());
        ImGui.pushFont(font);

        // Display filter
        ImGui.text("Filter");
        ImGui.sameLine();
        filter.draw("##FacultyFilter", 200);

        // Display a node for every faculty
        for (Faculty faculty : faculties) {
            // If name is empty like VIRTUAL AS or TBD
            if (faculty.getName() == null || !isFacultyPassedFilter(faculty))
                continue;

            int nodeFlag = getTreeNodeFlags();  // Setup flags

            // Highlight the node if is selected
            if (selectedFaculty != null && selectedFaculty.equalsIgnoreCase(faculty.getAcronym())) {
                nodeFlag |= ImGuiTreeNodeFlags.Selected;
            }

            // Display the node
            boolean opened = ImGui.treeNodeEx(faculty.getName(), nodeFlag);

            // Change selected node if clicked
            if (ImGui.isItemClicked() && !ImGui.isItemToggledOpen()) {
                if (selectedFaculty == null || !selectedFaculty.equals(faculty.getAcronym())) {
                    selectedFaculty = faculty.getAcronym();
                } else {
                    selectedFaculty = null;
                }
            }

            // Display the description if selected/opened
            if (opened) {
                float spacing = 20;                     // Left space of each line
                float prevFontScale = font.getScale();
                font.setScale(prevFontScale * 0.85f);  // Scale of description
                ImGui.pushFont(font);

                ImGui.setCursorPosX(spacing);
                ImGui.textColored(LIGHT_GRAY, "Acronym: " + faculty.getAcronym());

                ImGui.dummy(new ImVec2(0.0f, 0.5f));

                // Text wrap for description;
                ImGui.setCursorPosX(spacing);
                ImGui.pushTextWrapPos(width - 15);
                ImGui.textColored(LIGHT_GRAY, faculty.getDescription());
                ImGui.popTextWrapPos();

                font.setScale(prevFontScale);
                ImGui.popFont();
                ImGui.treePop();
            }
        }
        font.setScale(defaultFontScale);
        ImGui.popFont();
        ImGui.end();
    }

    private boolean isFacultyPassedFilter(Faculty faculty) {
        return filter.passFilter(faculty.getAcronym())
                || filter.passFilter(faculty.getName())
                || filter.passFilter(faculty.getDescription());
    }

    private float getFacultyNameScale() {
        return (float) window.getWindowWidth() / 640.0f;
    }
}

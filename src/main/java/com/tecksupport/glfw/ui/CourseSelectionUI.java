package com.tecksupport.glfw.ui;

import com.tecksupport.database.CourseQuery;
import com.tecksupport.schedulePlanner.CourseSection;
import com.tecksupport.glfw.view.Window;
import imgui.*;
import imgui.flag.*;

import java.util.HashSet;
import java.util.List;

public class CourseSelectionUI {
    private final static int WINDOW_FLAG = ImGuiWindowFlags.None;
    private final Window window;
    private final CourseQuery courseQuery;
    private final ImGuiTextFilter filter = new ImGuiTextFilter();
    private final HashSet<Integer> selectedCourses = new HashSet<>();
    private float width;
    private float height;
    private ImFont font;
    private float fontSize;
    private List<CourseSection> courseSectionList;
    private int dockId;



    public CourseSelectionUI(Window window, CourseQuery courseQuery) {
        this.window = window;
        this.courseQuery = courseQuery;
        this.courseSectionList = courseQuery.getAllCourses();
        width = window.getScreenWidth() / 3f;
        height = window.getScreenHeight() / 2.5f;
    }

    public void render() {
        int dockWindowFlag = ImGuiWindowFlags.None;
//                ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar
//                | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize
//                | ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoBringToFrontOnFocus;
        int dockSpaceFlag = ImGuiDockNodeFlags.PassthruCentralNode
                | ImGuiDockNodeFlags.AutoHideTabBar
                | ImGuiDockNodeFlags.NoDockingInCentralNode
                | ImGuiDockNodeFlags.NoResize
                | ImGuiDockNodeFlags.NoSplit;

        ImGui.setNextWindowPos(window.getWindowWidth() - width + 20, -7, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowSize(width, height, ImGuiCond.FirstUseEver);

        ImGui.begin("DockSpace", dockWindowFlag);
        ImGuiViewport viewport = ImGui.getMainViewport();

        dockId = ImGui.getID("DockSpace");
        ImGui.dockSpace(dockId, width, height, dockSpaceFlag);

        ImGui.setNextWindowDockID(1, ImGuiCond.FirstUseEver);
        handlePlanWindow();
        ImGui.setNextWindowDockID(1, ImGuiCond.FirstUseEver);
        handleSearchWindow();

    }

    private void handlePlanWindow() {
        int planWindowFlag = ImGuiWindowFlags.HorizontalScrollbar | ImGuiWindowFlags.NoCollapse;
//                | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize;
        String title = "Plan Schedule";
        if (!ImGui.begin(title, planWindowFlag)) {
            ImGui.end();
            return;
        }

        ImGui.text("asdasd");
        ImGui.end();
    }

    public void handleSearchWindow() {
        int searchWindowFlag = ImGuiWindowFlags.HorizontalScrollbar | ImGuiWindowFlags.NoCollapse;
//                | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize;
        String title = "Search For Class";

        if (!ImGui.begin(title, searchWindowFlag))
        {
            ImGui.end();
            return;
        }

        // Prevent horizontal scroll
        ImGui.setScrollX(0);

        font = ImGui.getFont();
        fontSize = font.getFontSize();
        float originalScale = font.getScale();

        font.setScale(1.25f);
        ImGui.pushFont(font);

        filter.draw("##CourseFilter");

        for (CourseSection courseSection : courseSectionList) {
            if (!isCoursePassedFilter(courseSection))
                continue;

            ImGui.setCursorPosX(fontSize * 1.5f);

            // Setup selectable section
            String text = courseSection.getID() + "\n" + courseSection.getSubject() + " " + courseSection.getCatalog() + "-" + courseSection.getSection() + "\n" + courseSection.getName();
            boolean isSelected = selectedCourses.contains(courseSection.getID());
            int selectableFlags = ImGuiSelectableFlags.None;
            ImVec2 size = new ImVec2(width, fontSize * 4.0f);

            // Check if selected
            if (ImGui.selectable(text, isSelected, selectableFlags, size)) {
                if (isSelected)
                    selectedCourses.remove(courseSection.getID());
                else
                    selectedCourses.add(courseSection.getID());
            }

            // Setup tooltip
            if (ImGui.isItemHovered()) {
                ImGui.setTooltip("Click to add course schedule");
            }

            // Add a horizontal line
            ImGui.separator();
        }

        font.setScale(originalScale);

        ImGui.popFont();
        ImGui.end();
    }

    private boolean isCoursePassedFilter(CourseSection courseSection) {
        return filter.passFilter(String.valueOf(courseSection.getID()))
                || filter.passFilter(courseSection.getName())
                || filter.passFilter(courseSection.getSubject())
                || filter.passFilter(courseSection.getCatalog())
                || filter.passFilter(courseSection.getSection());
    }

    private void handleRefresh() {
        courseSectionList = courseQuery.getAllCourses();
    }
}

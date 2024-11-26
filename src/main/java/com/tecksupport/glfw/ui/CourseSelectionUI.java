package com.tecksupport.glfw.ui;

import com.tecksupport.database.CourseQuery;
import com.tecksupport.database.data.Course;
import com.tecksupport.glfw.view.Window;
import imgui.*;
import imgui.flag.*;
import imgui.internal.ImGuiDockNode;

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
    private List<Course> courseList;
    private int dockId;



    public CourseSelectionUI(Window window, CourseQuery courseQuery) {
        this.window = window;
        this.courseQuery = courseQuery;
        this.courseList = courseQuery.getAllCourses();
        width = window.getScreenWidth() / 3f;
        height = window.getScreenHeight() / 2.5f;
    }

    public void render() {
        handlePlanWindow();

        handleSearchWindow();

    }

    private void handlePlanWindow() {
        ImGui.setNextWindowPos(window.getScreenWidth() - width - 20, 10, ImGuiCond.Once);
        ImGui.setNextWindowSize(width, height, ImGuiCond.Once);
        int planWindowFlag = ImGuiWindowFlags.NoCollapse;
        String title = "Plan Schedule";
        if (!ImGui.begin(title, planWindowFlag)) {
            ImGui.end();
            return;
        }

        ImGui.text("asdasd");
        ImGui.end();
    }

    public void handleSearchWindow() {
        ImGui.setNextWindowPos(window.getScreenWidth() - width - 30, window.getScreenHeight() / 2 , ImGuiCond.Once);
        ImGui.setNextWindowSize(width, height, ImGuiCond.Once);
        int searchWindowFlag = ImGuiWindowFlags.NoCollapse;
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

        for (Course course : courseList) {
            if (!isCoursePassedFilter(course))
                continue;

            ImGui.setCursorPosX(fontSize * 1.5f);

            // Setup selectable section
            String text = course.getID() + "\n" + course.getSubject() + " " + course.getCatalog() + "-" + course.getSection() + "\n" + course.getName();
            boolean isSelected = selectedCourses.contains(course.getID());
            int selectableFlags = ImGuiSelectableFlags.None;
            ImVec2 size = new ImVec2(width, fontSize * 4.0f);

            // Check if selected
            if (ImGui.selectable(text, isSelected, selectableFlags, size)) {
                if (isSelected)
                    selectedCourses.remove(course.getID());
                else
                    selectedCourses.add(course.getID());
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

    private boolean isCoursePassedFilter(Course course) {
        return filter.passFilter(String.valueOf(course.getID()))
                || filter.passFilter(course.getName())
                || filter.passFilter(course.getSubject())
                || filter.passFilter(course.getCatalog())
                || filter.passFilter(course.getSection());
    }

    private void handleRefresh() {
        courseList = courseQuery.getAllCourses();
    }
}

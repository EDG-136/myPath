package com.tecksupport.glfw.ui;

import com.tecksupport.database.CourseQuery;
import com.tecksupport.schedulePlanner.CourseSection;
import com.tecksupport.glfw.view.Window;
import com.tecksupport.schedulePlanner.GeneralCourse;
import imgui.*;
import imgui.flag.*;

import javax.print.attribute.standard.PrinterMakeAndModel;
import java.util.*;

public class GeneralCourseSelectionUI {
    private final static int WINDOW_FLAG = ImGuiWindowFlags.None;
    private final Window window;
    private final CourseQuery courseQuery;
    private final ImGuiTextFilter filter = new ImGuiTextFilter();
    private final HashSet<Integer> selectedCourses = new HashSet<>();
    private float width;
    private float height;
    private ImFont font;
    private float fontSize;
    private List<String> subjectList = new ArrayList<>();
    private List<CourseSection> courseSectionList;
    private List<GeneralCourse> generalCourseList;

    private String selectedSubject = "";
    private String selectedCatalog = "";
    private int dockId;



    public GeneralCourseSelectionUI(Window window, CourseQuery courseQuery) {
        this.window = window;
        this.courseQuery = courseQuery;
        this.courseSectionList = courseQuery.getCourseSectionList();
        this.generalCourseList = courseQuery.getGeneralCourseList();
        for (GeneralCourse generalCourse : generalCourseList) {
            String subject = generalCourse.getSubject();
            if (subjectList.contains(subject))
                continue;
            subjectList.add(subject);
        }
        Collections.sort(subjectList);
        width = window.getScreenWidth() / 3f;
        height = window.getScreenHeight() / 2.5f;
    }

    public void render() {
        handleGeneralCourseSearchWindow();
    }

    private void handleGeneralCourseSearchWindow() {
        String title = "Add Course";
        int generalCourseSearchFlag = ImGuiWindowFlags.NoCollapse;
        ImGui.setNextWindowSize(width / 2, height / 2, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowPos(window.getScreenWidth() - width, 25, ImGuiCond.FirstUseEver);

        if (!ImGui.begin(title, generalCourseSearchFlag)) {
            ImGui.end();
            return;
        }
        handleSubjectCombo();
        handleCatalogCombo();

        ImGui.end();

    }

    private void handleSubjectCombo() {
        ImGui.text("Subject");
        if (!ImGui.beginCombo("##CourseSubject", selectedSubject)) {
            return;
        }
        for (String subject : subjectList) {
            boolean isSelected = subject.equals(selectedSubject);
            if (ImGui.selectable(subject, isSelected))
                selectedSubject = subject;

            if (isSelected) {
                ImGui.setItemDefaultFocus();
            }
        }
        ImGui.endCombo();
    }

    private void handleCatalogCombo() {
        ImGui.text("Catalog");
        if (!ImGui.beginCombo("##CourseCatalog", selectedCatalog)) {
            return;
        }
        for (GeneralCourse generalCourse : generalCourseList) {
            if (!generalCourse.getSubject().equals(selectedSubject))
                continue;
            boolean isSelected = generalCourse.getCatalog().equals(selectedCatalog);
            if (ImGui.selectable(generalCourse.getCatalog(), isSelected))
                selectedCatalog = generalCourse.getCatalog();

            if (isSelected)
                ImGui.setItemDefaultFocus();
        }
        ImGui.endCombo();
    }

    public void handleSearchWindow() {
        int searchWindowFlag = ImGuiWindowFlags.NoCollapse;
        String title = "Add Course";

        if (!ImGui.begin(title, searchWindowFlag))
        {
            ImGui.end();
            return;
        }

        font = ImGui.getFont();
        fontSize = font.getFontSize();
        float originalScale = font.getScale();

        font.setScale(getFontScale());
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

    private float getFontScale() {
        return window.getScreenWidth() / 800;
    }

    private void handleRefresh() {
        courseSectionList = courseQuery.getCourseSectionList();
        generalCourseList = courseQuery.getGeneralCourseList();
        for (GeneralCourse generalCourse : generalCourseList) {
            String subject = generalCourse.getSubject();
            if (subjectList.contains(subject))
                continue;
            subjectList.add(subject);
        }
        Collections.sort(subjectList);
    }
}

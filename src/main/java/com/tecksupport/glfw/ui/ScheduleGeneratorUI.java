package com.tecksupport.glfw.ui;

import com.tecksupport.database.CourseQuery;
import com.tecksupport.schedulePlanner.*;
import com.tecksupport.glfw.view.Window;
import imgui.*;
import imgui.flag.*;
import imgui.type.ImBoolean;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class ScheduleGeneratorUI {
    private final static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mma");
    private final static int WINDOW_FLAG = ImGuiWindowFlags.None;
    private final StudentScheduleGenerator studentScheduleGenerator = new StudentScheduleGenerator();
    private final ImBoolean isOpen = new ImBoolean();
    private final Window window;
    private final CourseQuery courseQuery;
    private final ImGuiTextFilter filter = new ImGuiTextFilter();
    private final HashSet<Integer> selectedCourseIndexes = new HashSet<>();
    private final HashSet<Integer> blackListForGenerator = new HashSet<>();
    private final HashSet<Integer> courseSectionBlackListIndexes = new HashSet<>();
    private List<StudentSchedules> generatedSchedules;
    private final List<ScheduleUI> openedSchedules = new ArrayList<>();
    private int courseIndexToViewSection = -1;
    private float width;
    private float height;
    private final ImFont defaultFont;
    private float fontSize;
    private List<String> subjectList = new ArrayList<>();
    private List<CourseSection> courseSectionList;
    private List<GeneralCourse> generalCourseList;

    private String selectedSubject = "";
    private String selectedCatalog = "";



    public ScheduleGeneratorUI(Window window, CourseQuery courseQuery) {
        this.window = window;
        this.courseQuery = courseQuery;
        this.defaultFont = ImGui.getFont();
        this.isOpen.set(true);
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
        String title = "Schedule Assistant";
        int generalCourseSearchFlag = ImGuiWindowFlags.NoCollapse;
        ImGui.setNextWindowSize(width, height, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowPos(window.getScreenWidth() - width, 35, ImGuiCond.FirstUseEver);

        if (!ImGui.begin(title, isOpen, generalCourseSearchFlag)) {
            ImGui.end();
            return;
        }

        width = ImGui.getWindowWidth();
        height = ImGui.getWindowHeight();

        handleGeneralCourseSelectionTable();
        handleScheduleGeneratorTable();
        handleOpenedSchedules();

        ImGui.end();
    }

    private void handleOpenedSchedules() {
        List<ScheduleUI> toRemove = new ArrayList<>();
        for (ScheduleUI scheduleUI : openedSchedules) {
            if (!scheduleUI.isOpen())
                toRemove.add(scheduleUI);
            scheduleUI.render();
        }
        openedSchedules.removeAll(toRemove);
    }

    private void handleScheduleGeneratorTable() {
        if (ImGui.button("Generate")) {
            studentScheduleGenerator.clearGeneralCourse();
            for (int selected : selectedCourseIndexes) {
                if (blackListForGenerator.contains(selected))
                    continue;

                studentScheduleGenerator.addGeneralCourse(generalCourseList.get(selected));
            }
            generatedSchedules = studentScheduleGenerator.generateSchedule();
        }

        int tableFLag = ImGuiTableFlags.SizingStretchProp;
        if (!ImGui.beginTable("##ScheduleGenerator", 5, tableFLag))
            return;
        ImGui.tableHeadersRow();
        ImGui.tableSetColumnIndex(1);
        ImGui.text("Total");
        ImGui.sameLine();
        helpMarker("Total Distance Between Two Class");

        ImGui.tableSetColumnIndex(2);
        ImGui.text("Shortest");
        ImGui.sameLine();
        helpMarker("Shortest Distance Between Two Class");

        ImGui.tableSetColumnIndex(3);
        ImGui.text("Average");
        ImGui.sameLine();
        helpMarker("Average Distance Between Two Class");

        ImGui.tableSetColumnIndex(4);
        ImGui.sameLine();
        ImGui.text("Longest");
        ImGui.sameLine();
        helpMarker("Longest Distance Between Two Class");

        if (generatedSchedules == null) {
            ImGui.endTable();
            return;
        }
        for (StudentSchedules studentSchedules : generatedSchedules) {
            ImGui.tableNextRow();
            ImGui.tableSetColumnIndex(0);
            if (ImGui.button("View")) {
                boolean isNew = true;
                ScheduleUI newScheduleUI = new ScheduleUI(studentScheduleGenerator, studentSchedules);
                for (ScheduleUI scheduleUI : openedSchedules) {
                    if (scheduleUI.getId().equals(newScheduleUI.getId())) {
                        isNew = false;
                        break;
                    }
                }
                if (isNew)
                    openedSchedules.add(newScheduleUI);
            }
            ImGui.tableSetColumnIndex(1);
            ImGui.text("500m");

            ImGui.tableSetColumnIndex(2);
            ImGui.text("20m");

            ImGui.tableSetColumnIndex(3);
            ImGui.text("100m");

            ImGui.tableSetColumnIndex(4);
            ImGui.text("150m");
        }

        ImGui.endTable();
    }

    private void handleGeneralCourseSelectionTable() {
        int tableFlag = ImGuiTableFlags.SizingStretchProp;
        if (!ImGui.beginTable("##GeneralCourseSelection", 3, tableFlag))
            return;
        ImGui.tableNextRow();

        ImGui.tableSetColumnIndex(0);
        handleSelectedGeneralCoursesColumn();

        ImGui.setNextItemWidth(1);
        ImGui.tableSetColumnIndex(1);

        ImGui.tableSetColumnIndex(2);
        handleGeneralCourseSearchColumn();
        ImGui.endTable();
    }

    private void handleSelectedGeneralCoursesColumn() {
        float prevFont = defaultFont.getScale();
        defaultFont.setScale(getFontScale());
        ImGui.pushFont(defaultFont);
        ImGui.text("Selected Courses");
        defaultFont.setScale(prevFont);
        ImGui.popFont();
        ImGui.separator();

        defaultFont.setScale(getFontScale() * 0.75f);
        ImGui.pushFont(defaultFont);
        List<Integer> toRemove = new LinkedList<>();
        for (int selected : selectedCourseIndexes) {
            GeneralCourse selectedCourse = generalCourseList.get(selected);
            String title = selectedCourse.getSubject() + " " + selectedCourse.getCatalog();
            boolean isChecked = !blackListForGenerator.contains(selected);
            if (ImGui.checkbox("##" + title, isChecked)) {
                if (isChecked)
                    blackListForGenerator.add(selected);
                else
                    blackListForGenerator.remove(selected);
            }
            ImGui.sameLine();
            ImGui.text(title);
            ImGui.sameLine();
            ImGui.setCursorPosX(ImGui.getWindowWidth() / 2);

            if (ImGui.button("View Sections##" + selectedCourse)) {
                if (courseIndexToViewSection == selected)
                    courseIndexToViewSection = -1;
                else {
                    courseIndexToViewSection = selected;
                }
            }
            ImGui.sameLine();
            if (ImGui.button("X"))
                toRemove.add(selected);
            ImGui.sameLine(0);
            ImGui.dummy(0, 0);

            if (courseIndexToViewSection == selected)
                handleCourseSection();


            ImGui.separator();
        }
        // Remove those selected
        for (int i : toRemove) {
            selectedCourseIndexes.remove(i);
        }

        ImGui.popFont();
    }

    private void handleCourseSection() {
        float prevFont = defaultFont.getScale();
        defaultFont.setScale(getFontScale() * 0.7f);
        ImGui.pushFont(defaultFont);

        GeneralCourse generalCourseToView = generalCourseList.get(courseIndexToViewSection);
        List<CourseSection> courseSectionToShow = generalCourseToView.getCourseSectionList();

        int tableFlag = ImGuiTableFlags.SizingStretchProp | ImGuiTableFlags.SizingFixedFit | ImGuiTableFlags.BordersV | ImGuiTableFlags.BordersOuter | ImGuiTableFlags.RowBg;
        if (!ImGui.beginTable("##CourseSectionInfo", 3, tableFlag))
            return;
        ImGui.tableHeadersRow();
        ImGui.tableSetColumnIndex(0);
        boolean isSelectedAllChecked = true;
        for (int i = 0; i < courseSectionToShow.size(); i++) {
            if (courseSectionBlackListIndexes.contains(i)) {
                isSelectedAllChecked = false;
                break;
            }
        }

        if (ImGui.checkbox("##SelectAllSection", isSelectedAllChecked)) {
            if (isSelectedAllChecked) {
                for (int i = 0; i < courseSectionToShow.size(); i++) {
                    courseSectionBlackListIndexes.add(i);
                }
            } else {
                courseSectionBlackListIndexes.clear();
            }
        }

        ImGui.tableSetColumnIndex(1);
        ImGui.tableHeader("Section");

        ImGui.tableSetColumnIndex(2);
        ImGui.tableHeader("Day(s) & Building(s)");

        ImGui.tableNextRow();
        for (int i = 0; i < courseSectionToShow.size(); i++) {
            ImGui.tableSetColumnIndex(0);
            boolean isChecked = !courseSectionBlackListIndexes.contains(i);
            CourseSection currentCourseSection = courseSectionToShow.get(i);
            if (ImGui.checkbox("##" + currentCourseSection, isChecked)) {
                if (isChecked)
                    courseSectionBlackListIndexes.add(i);
                else
                    courseSectionBlackListIndexes.remove(i);
            }

            ImGui.tableSetColumnIndex(1);
            ImGui.text(currentCourseSection.getSection());

            ImGui.tableSetColumnIndex(2);
            StringBuilder dayAndBuilding = new StringBuilder();
            for (Schedule schedule : currentCourseSection.getSchedules()) {
                dayAndBuilding
                        .append(formatDaysInWeek(schedule.getDaysInWeek()))
                        .append(" ")
                        .append(schedule.getStartTime().format(timeFormatter))
                        .append(" - ")
                        .append(schedule.getEndTime().format(timeFormatter))
                        .append(" ")
                        .append(schedule.getRoomName());
                ImGui.text(dayAndBuilding.toString());
            }
            ImGui.tableNextRow();
        }
        defaultFont.setScale(prevFont);
        ImGui.popFont();
        ImGui.endTable();
    }

    private void handleGeneralCourseSearchColumn() {
        // Title
        float prevFont = defaultFont.getScale();
        defaultFont.setScale(getFontScale());
        ImGui.pushFont(defaultFont);
        ImGui.text("Add Course");
        ImGui.popFont();
        defaultFont.setScale(prevFont);
        ImGui.separator();

        defaultFont.setScale(getFontScale() * 0.75f);
        ImGui.pushFont(defaultFont);

        handleSubjectCombo();
        handleCatalogCombo();
        ImGui.spacing();
        if (ImGui.button("Add Course")) {
            for (int i = 0; i < generalCourseList.size(); i++) {
                GeneralCourse generalCourse = generalCourseList.get(i);
                if (generalCourse.getSubject().equals(selectedSubject)
                && generalCourse.getCatalog().equals(selectedCatalog)) {
                    selectedCourseIndexes.add(i);
                    break;
                }
            }
        }
        defaultFont.setScale(prevFont);
        ImGui.popFont();
    }

    private void handleSubjectCombo() {
        ImGui.text("Subject");
        if (!ImGui.beginCombo("##CourseSubject", selectedSubject)) {
            return;
        }
        for (String subject : subjectList) {
            boolean isSelected = subject.equals(selectedSubject);
            if (ImGui.selectable(subject, isSelected)) {
                selectedSubject = subject;
                selectedCatalog = "";
            }

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

    private float getFontScale() {
        return width / 350;
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

    private String formatDaysInWeek(String daysInWeek) {
        return daysInWeek
                .replace("T", "Tu")
                .replace("R", "Th")
                .replace("S", "Sa")
                .replace("U", "Su");
    }

    static void helpMarker(String message)
    {
        ImGui.textDisabled("(?)");
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.pushTextWrapPos(ImGui.getFontSize() * 35.0f);
            ImGui.textUnformatted(message);
            ImGui.popTextWrapPos();
            ImGui.endTooltip();
        }
    }
}

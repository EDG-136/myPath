package com.tecksupport.glfw.ui;

import com.tecksupport.glfw.view.Window;
import com.tecksupport.schedulePlanner.CourseSection;
import com.tecksupport.schedulePlanner.Schedule;
import com.tecksupport.schedulePlanner.StudentScheduleGenerator;
import com.tecksupport.schedulePlanner.StudentSchedules;
import imgui.ImColor;
import imgui.ImGui;
import imgui.ImVec4;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiTableBgTarget;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImBoolean;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;

public class ScheduleUI {
    private static final String startTime = "06:00:00";
    private static final String TITLE = "Schedule";
    private static final int DAYS_IN_A_WEEK = 7;
    private static final int TOTAL_BLOCK = 64; // ( 22 (10PM) - 6 (6AM) ) * 60 (MIN PER HOUR) / BLOCK_SIZE (MIN)
    private static final int BLOCK_SIZE = 15;
    private static final int OFFSET_SIZE = 10;

    private final ColoredBlock[] mondayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] tuesdayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] wednesdayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] thursdayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] fridayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] saturdayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] sundayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final String id;

    private final Window window;
    private final StudentScheduleGenerator studentScheduleGenerator;
    private final StudentSchedules studentSchedules;
    private final ImBoolean isOpen;
    private final int offset;
    private float width;
    private float height;

    public ScheduleUI(Window window, StudentScheduleGenerator studentScheduleGenerator, StudentSchedules studentSchedules, int offset) {
        this.window = window;
        this.studentScheduleGenerator = studentScheduleGenerator;
        this.studentSchedules = studentSchedules;
        this.offset = offset;
        this.isOpen = new ImBoolean(true);

        this.id = studentSchedules.getId();

        width = window.getScreenWidth() / 3;
        height = window.getScreenHeight() / 2;

        for (CourseSection courseSection : studentSchedules.getCourseSectionList()) {

            for (Schedule schedule : courseSection.getSchedules()) {
                int startBlock = timeToBlockIndex(schedule.getStartTime());
                int endBlock = timeToBlockIndex(schedule.getEndTime());
                ColoredBlock coloredBlock = new ColoredBlock();
                coloredBlock.hue = getHueValueFor(courseSection);
                coloredBlock.courseSection = courseSection;
                coloredBlock.startBlock = startBlock;
                char[] days = schedule.getDaysInWeek().toCharArray();
                for (char dayInChar : days) {
                    ColoredBlock[] coloredBlocks = getDayInWeekBlock(dayInChar);
                    if (coloredBlocks == null)
                        break;
                    for (int i = startBlock; i < endBlock && i < TOTAL_BLOCK; i++) {
                        coloredBlocks[i] = coloredBlock;
                    }
                }
            }
        }
    }

    public void render() {
        ImGui.setNextWindowSize(width, height, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowPos((window.getScreenWidth() / 3 - width + offset * OFFSET_SIZE) % window.getScreenWidth(), (40 + offset * OFFSET_SIZE) % window.getScreenHeight(), ImGuiCond.FirstUseEver);
        if (!ImGui.begin(TITLE + "##" + id, isOpen)) {
            ImGui.end();
            return;
        }

        handleCalendarTable();

        ImGui.end();
    }

    private void handleShowPathButtons() {
        for (int i = 1; i <= DAYS_IN_A_WEEK; i++) {
            String day = getDayInWeekFromNum(i);
            ImGui.tableSetColumnIndex(i);
            if (ImGui.button("Show##" + day)) {

            }
        }
    }

    private void handleCalendarTable() {
        int tableFlag = ImGuiTableFlags.SizingStretchSame | ImGuiTableFlags.BordersOuter | ImGuiTableFlags.BordersV | ImGuiTableFlags.RowBg;
        if (!ImGui.beginTable("##Calendar", 8, tableFlag))
            return;

        ImGui.tableNextRow();
        handleShowPathButtons();

        ImGui.tableHeadersRow();

        ImGui.tableSetColumnIndex(1);
        ImGui.text(formattedDayInWeek("Monday"));
        ImGui.tableSetColumnIndex(2);
        ImGui.text(formattedDayInWeek("Tuesday"));
        ImGui.tableSetColumnIndex(3);
        ImGui.text(formattedDayInWeek("Wednesday"));
        ImGui.tableSetColumnIndex(4);
        ImGui.text(formattedDayInWeek("Thursday"));
        ImGui.tableSetColumnIndex(5);
        ImGui.text(formattedDayInWeek("Friday"));
        ImGui.tableSetColumnIndex(6);
        ImGui.text(formattedDayInWeek("Saturday"));
        ImGui.tableSetColumnIndex(7);
        ImGui.text(formattedDayInWeek("Sunday"));

        for (int row = 0; row < TOTAL_BLOCK; row++) {
            ImGui.tableNextRow();
            for (int column = 0; column <= DAYS_IN_A_WEEK; column++) {
                ImGui.tableSetColumnIndex(column);
                if (column == 0) {
                    String formattedTime = minutesFromStartTimeToString(row * BLOCK_SIZE);
                    ImGui.text(formattedTime);
                    continue;
                }
                ColoredBlock[] coloredBlocks = getDayInWeekBlock(column);
                if (coloredBlocks == null)
                    break;
                ColoredBlock coloredBlock = coloredBlocks[row];
                if (coloredBlock == null) {
                    ImGui.dummy(0, 2);
                    continue;
                }
                CourseSection courseSection = coloredBlock.courseSection;
                ImGui.tableSetBgColor(ImGuiTableBgTarget.CellBg, ImColor.hsl(coloredBlock.hue, 1, 0.3f));
                if (row == coloredBlock.startBlock) {
                    ImGui.text(courseSection.getSubject() + " " + courseSection.getCatalog());
                }
                if (ImGui.isItemHovered()) {
                    ImGui.beginTooltip();
                    ImGui.text(String.valueOf(courseSection.getID()));
                    ImGui.text(courseSection.getSubject() + " " + courseSection.getCatalog() + " - " + courseSection.getSection());
                    ImGui.text(courseSection.getName());
                    ImGui.endTooltip();
                }
            }
        }
        ImGui.endTable();
    }

    private String formattedDayInWeek(String dayInWeek) {
        return String.format("%-10s", dayInWeek);
    }

    private String minutesFromStartTimeToString(int minutes) {
        int minutesAtStart = LocalTime.parse(startTime).get(ChronoField.MINUTE_OF_DAY);
        return LocalTime.ofSecondOfDay((minutesAtStart + minutes) * 60L).format(DateTimeFormatter.ofPattern("HH:mm a"));
    }

    private int timeToBlockIndex(LocalTime time) {
        int minutesAtStart = LocalTime.parse(startTime).get(ChronoField.MINUTE_OF_DAY);
        int minutes = time.get(ChronoField.MINUTE_OF_DAY);
        return (minutes - minutesAtStart) / BLOCK_SIZE;
    }

    public boolean isOpen() {
        return isOpen.get();
    }

    private ColoredBlock[] getDayInWeekBlock(int dayInNum) {
        return switch (dayInNum) {
            case 1 -> mondayBlocks;
            case 2 -> tuesdayBlocks;
            case 3 -> wednesdayBlocks;
            case 4 -> thursdayBlocks;
            case 5 -> fridayBlocks;
            case 6 -> saturdayBlocks;
            case 7 -> sundayBlocks;
            default -> null;
        };
    }

    private ColoredBlock[] getDayInWeekBlock(char dayInChar) {
        return switch (dayInChar) {
            case 'M' -> mondayBlocks;
            case 'T' -> tuesdayBlocks;
            case 'W' -> wednesdayBlocks;
            case 'R' -> thursdayBlocks;
            case 'F' -> fridayBlocks;
            case 'S' -> saturdayBlocks;
            case 'U' -> sundayBlocks;
            default -> null;
        };
    }

    private String getDayInWeekFromNum(int dayInNum) {
        return switch (dayInNum) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> null;
        };
    }

    public float getHueValueFor(CourseSection courseSection) {
        if (studentSchedules.getCourseSectionList().size() == 1)
            return 0.5f;
        List<CourseSection> courseSectionList = studentSchedules.getCourseSectionList();
        return (float) 1 / courseSectionList.size() * courseSectionList.indexOf(courseSection);
    }

    public String getId() {
        return id;
    }

    public static class ColoredBlock {
        private float hue;
        private CourseSection courseSection;
        private int startBlock;
    }
}

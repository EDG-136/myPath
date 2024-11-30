package com.tecksupport.glfw.ui;

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
import org.lwjgl.BufferUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Formattable;
import java.util.List;

public class ScheduleUI {
    private static final String startTime = "06:00:00";
    private static final String TITLE = "Schedule";
    private static final int DAYS_IN_A_WEEK = 7;
    private static final int TOTAL_BLOCK = 60; // ( 21 (9PM) - 6 (6AM) ) * 60 (MIN PER HOUR) / BLOCK_SIZE (MIN)
    private static final int BLOCK_SIZE = 15;

    private final ColoredBlock[] mondayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] tuesdayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] wednesdayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] thursdayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] fridayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] saturdayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final ColoredBlock[] sundayBlocks = new ColoredBlock[TOTAL_BLOCK];
    private final StringBuilder id = new StringBuilder();

    private final StudentScheduleGenerator studentScheduleGenerator;
    private final StudentSchedules studentSchedules;
    private final ImBoolean isOpen;

    public ScheduleUI(StudentScheduleGenerator studentScheduleGenerator, StudentSchedules studentSchedules) {
        this.studentScheduleGenerator = studentScheduleGenerator;
        this.studentSchedules = studentSchedules;
        this.isOpen = new ImBoolean(true);
        for (CourseSection courseSection : studentSchedules.getCourseSectionList()) {
            id.append(courseSection.getID());
        }

        for (CourseSection courseSection : studentSchedules.getCourseSectionList()) {

            for (Schedule schedule : courseSection.getSchedules()) {
                int startBlock = timeToBlockIndex(schedule.getStartTime());
                int endBlock = timeToBlockIndex(schedule.getEndTime());
                ColoredBlock coloredBlock = new ColoredBlock();
                coloredBlock.color = getColorFor(courseSection);
                coloredBlock.courseSection = courseSection;
                coloredBlock.startBlock = startBlock;
                char[] days = schedule.getDaysInWeek().toCharArray();
                for (char dayInChar : days) {
                    ColoredBlock[] coloredBlocks = getDayInWeekBlock(dayInChar);
                    if (coloredBlocks == null)
                        break;
                    for (int i = startBlock; i < endBlock; i++) {
                        coloredBlocks[i] = coloredBlock;
                        System.out.println("Added " + dayInChar);
                    }
                }
            }
        }
    }

    public void render() {
        ImGui.setNextWindowSize(ImGui.getWindowSizeX(), ImGui.getWindowSizeY(), ImGuiCond.FirstUseEver);
        ImGui.begin(TITLE + "##" + id, isOpen);
        if (ImGui.button("Save")) {
            studentScheduleGenerator.addSavedSchedule(studentSchedules);
        }
        int tableFlag = ImGuiTableFlags.SizingStretchProp | ImGuiTableFlags.SizingFixedFit;
        ImGui.beginTable("##Calendar", 8);
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
                if (coloredBlocks == mondayBlocks)
                    System.out.println("Yes Monday");
                if (coloredBlock == null) {
                    ImGui.dummy(0, 2);
                    continue;
                }
                CourseSection courseSection = coloredBlock.courseSection;
                ImGui.tableSetBgColor(ImGuiTableBgTarget.CellBg, ImColor.hsl(0.5f, 1, 0.3f));
                if (row == coloredBlock.startBlock)
                    ImGui.text(courseSection.getSubject() + " " + courseSection.getCatalog());
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
        ImGui.end();
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

    public ImVec4 getColorFor(CourseSection courseSection) {
        if (studentSchedules.getCourseSectionList().size() == 1)
            return hsvToRGB(0.5f, 1, 1);
        List<CourseSection> courseSectionList = studentSchedules.getCourseSectionList();
        int hue = 1 / courseSectionList.size() * courseSectionList.indexOf(courseSection);
        return hsvToRGB(hue, 1, 1);
    }

    private ImVec4 hsvToRGB(float h, float s, float v) {
        float r;
        float g;
        float b;
        if (s == 0) {
            r = v * 255;
            g = v * 255;
            b = v * 255;
        } else {
            float hValue = h * 6;
            hValue = hValue % 6;
            int iValue = (int) Math.floor(hValue);
            float value1 = v * (1 - s);
            float value2 = v * (1 - s * (hValue - iValue));
            float value3 = v * (1 - s * (1 - (hValue - iValue)));

            switch (iValue) {
                case 0:
                    r = v;
                    g = value3;
                    b = value1;
                    break;
                case 1:
                    r = value2;
                    g = v;
                    b = value1;
                    break;
                case 2:
                    r = value1;
                    g = v;
                    b = value3;
                    break;
                case 3:
                    r = value1;
                    g = value2;
                    b = v;
                    break;
                case 4:
                    r = value3;
                    g = value1;
                    b = v;
                    break;
                default:
                    r = v;
                    g = value1;
                    b = value2;
            }
        }

        return new ImVec4(r, g, b, 1);
    }

    public String getId() {
        return id.toString();
    }

    public static class ColoredBlock {
        private ImVec4 color;
        private CourseSection courseSection;
        private int startBlock;
    }
}

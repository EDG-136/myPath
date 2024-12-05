package com.tecksupport.glfw.ui;

import com.tecksupport.database.NodeQuery;
import com.tecksupport.glfw.controller.InputHandler;
import com.tecksupport.glfw.pathfinder.DijkstraAlgorithm;
import com.tecksupport.glfw.pathfinder.node.Node;
import com.tecksupport.glfw.view.Window;
import com.tecksupport.schedulePlanner.*;
import imgui.ImColor;
import imgui.ImGui;
import imgui.ImVec4;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiTableBgTarget;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImBoolean;
import org.joml.Vector4f;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
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
    private final NodeQuery nodeQuery;
    private final InputHandler inputHandler;
    private final String id;
    private final float[] hsv = new float[3];
    private final float[] rgb = new float[3];

    private final Window window;
    private final StudentSchedules studentSchedules;
    private final ImBoolean isOpen;
    private final int offset;
    private float width;
    private float height;

    public ScheduleUI(Window window, StudentSchedules studentSchedules, NodeQuery nodeQuery, InputHandler inputHandler, int offset) {
        this.window = window;
        this.studentSchedules = studentSchedules;
        this.nodeQuery = nodeQuery;
        this.inputHandler = inputHandler;
        this.offset = offset;
        this.isOpen = new ImBoolean(true);

        this.id = StudentSchedules.calculateID(studentSchedules);

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
                coloredBlock.facultyID = schedule.getFacultyID();
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
        hsv[1] = 1;
        hsv[2] = 1;
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
            EDayInWeek currentDayInWeek = getDayInWeekFromNum(i);
            ImGui.tableSetColumnIndex(i);
            if (!ImGui.button("Show##" + currentDayInWeek))
                continue;

            ColoredBlock[] blockArray = getDayInWeekBlock(i);
            if (blockArray == null)
                continue;

            List<Node> nodes = new ArrayList<>();
            for (ColoredBlock block : blockArray) {
                if (block == null)
                    continue;
                String facultyID = block.facultyID;
                Node node = nodeQuery.getEntryNodeFromFacultyID(facultyID);
                if (node == null || nodes.contains(node))
                    continue;
                nodes.add(node);
                hsv[0] = block.hue;
                ImGui.colorConvertHSVtoRGB(hsv, rgb);
                node.getEntity().setColor(new Vector4f(rgb[0], rgb[1], rgb[2], 1));
            }
            inputHandler.clearPath();
            for (Node node : nodes) {
                inputHandler.addNodeToRender(node);
            }
            for (int j = 0; j < nodes.size() - 1; j++) {
                Node fromNode = nodes.get(j);
                Node toNode = nodes.get(j + 1);
                List<Node> nodesOnPath = DijkstraAlgorithm.getShortestPath(fromNode, toNode);
                hsv[0] = (float) j / nodes.size();
                ImGui.colorConvertHSVtoRGB(hsv, rgb);
                for (int k = 0; k < nodesOnPath.size() - 1; k++) {
                    Node from = nodesOnPath.get(k);
                    Node to = nodesOnPath.get(k + 1);

                    inputHandler.drawPath(from, to, new Vector4f(rgb[0], rgb[1], rgb[2], 1));
                }
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
                    ImGui.text(coloredBlock.facultyID);
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

    private EDayInWeek getDayInWeekFromNum(int dayInNum) {
        return switch (dayInNum) {
            case 1 -> EDayInWeek.MONDAY;
            case 2 -> EDayInWeek.TUESDAY;
            case 3 -> EDayInWeek.WEDNESDAY;
            case 4 -> EDayInWeek.THURSDAY;
            case 5 -> EDayInWeek.FRIDAY;
            case 6 -> EDayInWeek.SATURDAY;
            case 7 -> EDayInWeek.SUNDAY;
            default -> null;
        };
    }

    public float getHueValueFor(CourseSection courseSection) {
        if (studentSchedules.getCourseSectionList().size() == 1)
            return 0.5f;
        List<CourseSection> courseSectionList = studentSchedules.getCourseSectionList();
        return (float) courseSectionList.indexOf(courseSection) / courseSectionList.size();
    }

    public String getId() {
        return id;
    }

    public static class ColoredBlock {
        private CourseSection courseSection;
        private String facultyID;
        private float hue;
        private int startBlock;
    }
}

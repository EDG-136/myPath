package com.tecksupport.glfw.ui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class PathFinderUI {

    public void renderUI() {
        ImGui.begin("MyPath", ImGuiWindowFlags.AlwaysAutoResize);

        if (ImGui.button("Add Classes to Schedule")) {
            // Logic to add classes
            System.out.println("Add Classes button clicked!");
        }

        if (ImGui.button("Toggle Wheelchair Accessibility")) {
            // Logic to toggle accessibility
            System.out.println("Toggle Wheelchair Accessibility button clicked!");
        }

        if (ImGui.button("Show Path")) {
            // Logic to show path
            System.out.println("Show Path button clicked!");
        }

        ImGui.end();
    }
}

package com.tecksupport;

import com.tecksupport.glfw.controller.InputHandler;

public class  Main {

  public static void main(String[] args){

    System.out.println("Hello World!");
    InputHandler instance = new InputHandler();

    instance.init();
    instance.run();

  }

}

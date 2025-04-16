package com.battleship.main;

import com.battleship.controller.GameController;
import com.battleship.interfaces.IController;

public class Main {

    public static void main(String[] args) {
        IController controller = new GameController();

        controller.init();   // Khởi tạo game (tài nguyên, config,...)
        controller.start();  // Bắt đầu vòng lặp game
    }
}

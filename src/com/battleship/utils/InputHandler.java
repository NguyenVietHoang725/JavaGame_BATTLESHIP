package com.battleship.utils;

import java.util.Scanner;

import com.battleship.model.GameLogic;
import com.battleship.model.Move;

public class InputHandler {

    private Scanner scn;
    private GameLogic game;

    public InputHandler(GameLogic game) {
        this.scn = new Scanner(System.in);
        this.game = game;
    }

    public int getAction() {
        System.out.print("🔧 Chọn hành động (1: Bắn, 2: Undo, 3: Redo, 4: Tạm dừng, 5: Thoát): ");
        while (!scn.hasNextInt()) {
            System.out.print("❗ Vui lòng nhập số hợp lệ: ");
            scn.next(); // bỏ qua đầu vào không phải số
        }
        return scn.nextInt();
    }

    public Move getMove() {
        int row = scn.nextInt();
        int col = scn.nextInt();
        return new Move(row, col, game.getNode(row, col).getStatus(), null);
    }

    public void waitForEnter() {
        System.out.print("Nhấn Enter để tiếp tục...");
        scn.nextLine(); // đọc dòng cũ
        scn.nextLine(); // đợi Enter
    }
}

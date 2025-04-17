package com.battleship.utils;

import java.util.Scanner;

import com.battleship.model.GameLogic;
import com.battleship.model.Move;
import com.battleship.view.GameView;

public class InputHandler {

    private Scanner scn;
    private GameLogic game;
    private GameView view;

    public InputHandler(GameLogic game, GameView view) {
        this.scn = new Scanner(System.in);
        this.game = game;
        this.view = view;
    }

    public int getAction() {
        view.showMessage("🔧 Chọn hành động (1: Bắn, 2: Undo, 3: Redo, 4: Tạm dừng, 5: Thoát): ");
        while (!scn.hasNextInt()) {
            view.showMessage("Vui lòng nhập số hợp lệ!");
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
        view.showMessage("Nhấn Enter để tiếp tục...");
        scn.nextLine(); // đọc dòng cũ
        scn.nextLine(); // đợi Enter
    }
    
    public int[] getPlaceShipWithSize(int size) {
        while (true) {
            try {
                int x = scn.nextInt();
                int y = scn.nextInt();
                int dir = scn.nextInt(); // 0-ngang, 1-dọc

                if (!validateShipPlacement(x, y, size, dir)) {
                	view.showMessage("❌ Vị trí không hợp lệ hoặc trùng tàu khác. Hãy thử lại.");
                	continue;
                }
                
                return new int[] {x, y, dir};
            } catch (Exception e) {
                view.showMessage("❌ Dữ liệu không hợp lệ. Nhập lại.");
                scn.nextLine(); // clear input
            }
        }
    }

    private boolean validateShipPlacement(int x, int y, int size, int dir) {
        if (size <= 0 || size > 5) return false;
        if (dir != 0 && dir != 1) return false;

        for (int i = 0; i < size; i++) {
            int xi = dir == 0 ? x + i : x;
            int yi = dir == 1 ? y + i : y;

            if (xi < 0 || xi >= 10 || yi < 0 || yi >= 10) return false;
            if (game.getNode(xi, yi).isOccupied()) return false;
        }

        return true;
    }
}

package com.battleship.controller;

import com.battleship.interfaces.IController;
import com.battleship.model.GameLogic;
import com.battleship.model.Move;
import com.battleship.utils.InputHandler;
import com.battleship.view.GameView;

public class GameController implements IController {

    private GameLogic game;
    private GameView view;
    private InputHandler input;
    private boolean isPaused;

    @Override
    public void init() {
        this.game = new GameLogic();
        this.view = new GameView();
        this.input = new InputHandler(game);
        this.isPaused = false;

        view.showMessage("🎮 Game Battleship đã khởi tạo!");
    }

    @Override
    public void start() {
        view.showMessage("🚀 BẮT ĐẦU GAME BATTLESHIP");

        // Test ship
        game.placeShip(0, 0, 3, true);
        game.placeShip(2, 2, 2, false);

        while (!game.isGameOver()) {
            if (isPaused) continue;

            view.printBoard(game.getBoard(), false);
            view.showSelectAction(); // Hiển thị menu lựa chọn

            int choice = input.getAction();

            switch (choice) {
                case 1 -> {
                    view.showMessage("🔫 Nhập tọa độ bắn (x y):");
                    Move move = input.getMove();
                    String result = game.attack(move.getRow(), move.getCol());
                    move.setNewValue(game.getNode(move.getRow(), move.getCol()).getStatus());
                    view.showMessage("🎯 Kết quả: " + result);
                }
                case 2 -> {
                    if (game.undo()) {
                        view.showMessage("↩️ Đã hoàn tác bước đi.");
                    } else {
                        view.showMessage("❌ Không thể hoàn tác.");
                    }
                }
                case 3 -> {
                    if (game.redo()) {
                        view.showMessage("↪️ Đã làm lại bước đi.");
                    } else {
                        view.showMessage("❌ Không thể làm lại.");
                    }
                }
                case 4 -> {
                    pause();
                }
                case 5 -> {
                    view.showMessage("❌ Thoát game...");
                    return;
                }
                default -> {
                    view.showMessage("⚠️ Lựa chọn không hợp lệ!");
                }
            }
        }

        end();
    }

    @Override
    public void pause() {
        isPaused = true;
        view.showMessage("⏸ Game đã tạm dừng. Nhấn bất kỳ phím nào để tiếp tục...");
        input.waitForEnter();
        isPaused = false;
    }

    @Override
    public void end() {
        view.printBoard(game.getBoard(), true);
        view.showMessage("🏁 Game kết thúc! Bạn đã tiêu diệt toàn bộ tàu.");
    }
}

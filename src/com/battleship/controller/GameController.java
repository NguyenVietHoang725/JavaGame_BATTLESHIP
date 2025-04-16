package com.battleship.controller;

import com.battleship.interfaces.IController;
import com.battleship.model.GameLogic;
import com.battleship.model.Move;
import com.battleship.utils.InputHandler;
import com.battleship.view.GameView;

public class GameController implements IController {

    private GameLogic game;
    private GameView view;
    private InputHandler inputHandler;
    private boolean isPaused;

    @Override
    public void init() {
        this.game = new GameLogic();
        this.view = new GameView();
        this.inputHandler = new InputHandler(game);
        this.isPaused = false;

        // Có thể đọc file config, khởi tạo game mode, hiển thị menu khởi động...
        view.showMessage("🎮 Game Battleship đã khởi tạo!");
    }

    @Override
    public void start() {
        view.showMessage("🚀 BẮT ĐẦU GAME BATTLESHIP");

        // Đặt một số tàu mẫu (test)
        game.placeShip(0, 0, 3, true);
        game.placeShip(2, 2, 2, false);

        while (!game.isGameOver()) {
            if (isPaused) continue;

            view.printBoard(game.getBoard(), false);
            view.showMessage("Nhập tọa độ (row col) để bắn:");

            Move move = inputHandler.getMove();

            String result = game.attack(move.getRow(), move.getCol());
            move.setNewValue(game.getNode(move.getRow(), move.getCol()).getStatus());

            view.showMessage("🎯 Kết quả: " + result);
        }

        end();
    }

    @Override
    public void pause() {
        isPaused = true;
        view.showMessage("⏸ Game đã tạm dừng.");
    }

    @Override
    public void end() {
        view.printBoard(game.getBoard(), true);
        view.showMessage("🏁 Game kết thúc! Bạn đã tiêu diệt toàn bộ tàu.");
    }
}

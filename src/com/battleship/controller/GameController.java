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
        this.input = new InputHandler(game, view);
        this.isPaused = false;

        view.showMessage("🎮 Game Battleship đã khởi tạo!");
    }

    @Override
    public void start() {
        view.showMessage("🚀 BẮT ĐẦU GAME BATTLESHIP");
        
        placeAllShips();

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
                    
                    if (result.contains("Sunk")) {
                    	view.showMessage("💥 Một tàu đã bị đánh chìm!");
                    }
                    view.showMessage("🎯 Kết quả: " + result.replace(" & Sunk", ""));
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
    
    private void placeAllShips() {
        view.showMessage("⚓ Hãy đặt 5 tàu theo thứ tự kích thước: [2, 3, 3, 4, 5]");

        for (int size : game.getShipSizes()) {
            boolean placed = false;

            while (!placed) {
                view.showMessage("🚢 Đặt tàu kích thước " + size + ": Nhập x y chiều(0-ngang, 1-dọc):");
                int[] info = input.getPlaceShipWithSize(size); // Giả sử đã sửa InputHandler

                placed = game.placeShip(info[0], info[1], size, info[2] == 0);

                if (!placed) {
                    view.showMessage("❌ Không thể đặt tàu tại vị trí này. Hãy thử lại.");
                } else {
                    view.showMessage("✅ Đặt thành công tàu kích thước " + size);
                    view.printBoard(game.getBoard(), true); // Hiển thị bảng với tàu
                }
            }
        }
    }

}

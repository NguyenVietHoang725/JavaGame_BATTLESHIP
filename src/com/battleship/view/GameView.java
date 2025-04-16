package com.battleship.view;

import com.battleship.model.Node;

public class GameView {
	public void printBoard(Node[][] board, boolean revealShips) {
        System.out.print("   ");
        for (int i = 0; i < board.length; i++) {
            System.out.printf("%2d ", i);
        }
        System.out.println();

        for (int i = 0; i < board.length; i++) {
            System.out.printf("%2d ", i);
            for (int j = 0; j < board[i].length; j++) {
                Node node = board[i][j];
                char symbol = getSymbol(node, revealShips);
                System.out.print(" " + symbol + " ");
            }
            System.out.println();
        }
    }

    private char getSymbol(Node node, boolean revealShips) {
        switch (node.getStatus()) {
            case HIT: return 'X';
            case MISS: return 'O';
            case SHIP: return revealShips ? 'S' : '~';
            default: return '~';
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}

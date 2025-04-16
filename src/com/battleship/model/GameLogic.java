package com.battleship.model;

import java.util.ArrayList;
import java.util.List;

import com.battleship.enums.NodeStatus;

public class GameLogic {
	
	// Attributes
	private Node[][] board;
	private List<Ship> ships;
	private final int BOARD_SIZE = 10;
	
	public GameLogic() {
		board = new Node[BOARD_SIZE][BOARD_SIZE];
		ships = new ArrayList<>();
		initBoard();
	}
	
	private void initBoard() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[i][j] = new Node(i, j);
			}
		}
	}
	
	public boolean placeShip(int x, int y, int size, boolean isHorizontal) {
		List<Node> shipNodes = new ArrayList<>();
		
		for (int i = 0; i < size; i++) {
			int xi = isHorizontal ? x + i : x;
			int yi = isHorizontal ? y : y + i;
			
			if (!isValidCoordinate(xi, yi) || board[xi][yi].isOccupied()) {
				return false;
			}
			shipNodes.add(board[xi][yi]);
		}
		
		for (Node node : shipNodes) {
			node.setStatus(NodeStatus.SHIP);
		}
		
		ships.add(new Ship(shipNodes.toArray(new Node[0])));
		return true;
	}
	
	public String attack(int x, int y) {
        if (!isValidCoordinate(x, y)) return "Invalid";

        Node node = board[x][y];

        if (node.getStatus() == NodeStatus.SHIP) {
            node.setStatus(NodeStatus.HIT);
            ships.forEach(Ship::updateStatus);
            return "Hit";
        } else if (node.getStatus() == NodeStatus.EMPTY) {
            node.setStatus(NodeStatus.MISS);
            return "Miss";
        } else {
            return "Already Attacked";
        }
    }
	
	public boolean isGameOver() {
        return ships.stream().allMatch(Ship::isSunk);
    }
	
	private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }
	
	public Node[][] getBoard() {
        return board;
    }
	
	public Node getNode(int x, int y) {
		return board[x][y];
	}

    public List<Ship> getShips() {
        return ships;
    }
}

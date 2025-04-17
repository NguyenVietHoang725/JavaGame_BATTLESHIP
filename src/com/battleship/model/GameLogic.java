package com.battleship.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.battleship.enums.NodeStatus;

public class GameLogic {
	
	// Attributes
	private Node[][] board;
	private List<Ship> ships;
	private final int BOARD_SIZE = 10;
	private final int[] SHIP_SIZES = {2, 3, 3, 4, 5};
	private Stack<Move> undoStack = new Stack<>();
	private Stack<Move> redoStack = new Stack<>();
	
	// Constructor
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

        Node targetNode = board[x][y];

        String result = updateNodeStatus(targetNode);

        if (result.equals("Hit")) {
            for (Ship ship : ships) {
            	boolean wasSunkBefore = ship.isSunk();
                ship.updateStatus();
                
                if (!wasSunkBefore && ship.isSunk()) {
                	result += " & Sunk";
                }
            }
        }

        return result;
    }
	
	private String updateNodeStatus(Node node) {
		NodeStatus prev = node.getStatus();
		NodeStatus newStatus;
		
        switch (prev) {
            case SHIP:
                newStatus = NodeStatus.HIT;
                break;

            case EMPTY:
                newStatus = NodeStatus.MISS;
                break;

            case HIT:
            case MISS:
                return "Already Attacked";

            default:
                return "Unknown";
        }
        
        node.setStatus(newStatus);
        
        recordMove(node.getX(), node.getY(), prev, newStatus);

        return newStatus == NodeStatus.HIT ? "Hit" : "Miss";
    }
	
	private void recordMove(int x, int y, NodeStatus prev, NodeStatus next) {
	    undoStack.push(new Move(x, y, prev, next));
	    redoStack.clear(); // Sau mỗi hành động mới, redo phải bị xóa
	}

	
	public boolean undo() {
	    if (!canUndo()) return false;

	    Move move = undoStack.pop();
	    Node node = board[move.getRow()][move.getCol()];

	    if (node.getStatus() != move.getNewValue()) return false; // đảm bảo đúng trạng thái

	    node.setStatus(move.getPrevValue());
	    redoStack.push(move);
	    return true;
	}

	
	public boolean redo() {
	    if (!canRedo()) return false;

	    Move move = redoStack.pop();
	    Node node = board[move.getRow()][move.getCol()];

	    if (node.getStatus() != move.getPrevValue()) return false; // đảm bảo đúng trạng thái

	    node.setStatus(move.getNewValue());
	    undoStack.push(move);
	    return true;
	}

	
	public boolean canUndo() {
	    return !undoStack.isEmpty();
	}

	public boolean canRedo() {
	    return !redoStack.isEmpty();
	}
	
	public boolean isGameOver() {
		for (Ship ship : ships) {
	        if (!ship.isSunk()) {
	            return false;
	        }
	    }

	    return true;
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
    
    public int[] getShipSizes() {
        return SHIP_SIZES;
    }

}

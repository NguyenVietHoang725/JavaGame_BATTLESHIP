package com.battleship.utils;

import java.util.Scanner;

import com.battleship.enums.NodeStatus;
import com.battleship.model.GameLogic;
import com.battleship.model.Move;

public class InputHandler {
	
	// Attributes
	private Scanner scn;
	private GameLogic logic;
	
	// Constructor
	public InputHandler(GameLogic logic) {
		this.scn = new Scanner(System.in);
		this.logic = logic;
	}
	
	public Move getMove() {
		int row = scn.nextInt();
		int col = scn.nextInt();
		NodeStatus prevVal = logic.getNode(row, col).getStatus();
		
		return new Move(row, col, prevVal, NodeStatus.NULL);
	}
}

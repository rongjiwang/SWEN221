package swen221.assignment2.chessview.moves;

import swen221.assignment2.chessview.*;
import swen221.assignment2.chessview.pieces.*;

public class NonCheck implements Move {
	private MultiPieceMove move;

	public NonCheck(MultiPieceMove move) {
		this.move = move;
	}

	public MultiPieceMove move() {
		return move;
	}

	public boolean isWhite() {
		return move.isWhite();
	}

	/**
	 * non-check condition
	 * 
	 * @return true is not being checked
	 */
	public boolean isValid(Board board) {
		Board nextMove = new Board(board);
		System.out.println("13");

		nextMove.apply(move);
		boolean check = false;
		if (move instanceof PawnPromotion) {
			check = nextMove.isInCheck(!isWhite());
		} else {
			check = nextMove.isInCheck(isWhite());
		}
		System.out.println("5");

		return move.isValid(board) && !check;
	}

	public void apply(Board board) {
		move.apply(board);
	}

	public String toString() {
		return move.toString();
	}
}
